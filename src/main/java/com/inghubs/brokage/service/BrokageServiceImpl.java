package com.inghubs.brokage.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.inghubs.brokage.entity.BrokageAsset;
import com.inghubs.brokage.entity.BrokageCustomer;
import com.inghubs.brokage.entity.BrokageOrder;
import com.inghubs.brokage.entity.OrderSide;
import com.inghubs.brokage.entity.OrderStatus;
import com.inghubs.brokage.exception.AssetNotFoundException;
import com.inghubs.brokage.exception.InvalidOrderStatusException;
import com.inghubs.brokage.exception.InvalidUserException;
import com.inghubs.brokage.exception.NotAuthToDeleteException;
import com.inghubs.brokage.exception.NotEnoughAssetException;
import com.inghubs.brokage.exception.NotEnoughTRYAssetException;
import com.inghubs.brokage.exception.OrderNotFoundException;
import com.inghubs.brokage.repository.AssetRepository;
import com.inghubs.brokage.repository.CustomerRepository;
import com.inghubs.brokage.repository.OrderRepository;

@Service
public class BrokageServiceImpl implements BrokageService {

    private static final String ADMIN_USER = "admin";

    private final OrderRepository orderRepository;
    private final AssetRepository assetRepository;
    private final CustomerRepository customerRepository;

    public BrokageServiceImpl(OrderRepository orderRepository, 
        AssetRepository assetRepository, 
        CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.assetRepository = assetRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public BrokageCustomer createCustomer(BrokageCustomer customer, BrokageCustomer authCustomer) {
        if (!ADMIN_USER.equals(authCustomer.getUsername())) {
            throw new InvalidUserException();
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPwd = bCryptPasswordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPwd);
        return customerRepository.save(customer);
    }

    @Override
    public BrokageOrder createOrder(BrokageOrder order, BrokageCustomer brokageCustomer) {
        order.setCustomerId(brokageCustomer.getId());
        double totalPrice = order.getPrice() * order.getSize();

        List<BrokageAsset> tryAssets = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), "TRY");
        BrokageAsset tryAsset = tryAssets.get(0);

        if (order.getOrderSide() == OrderSide.BUY) {
            if (tryAsset.getUsableSize() < totalPrice) {
                throw new NotEnoughTRYAssetException();
            }
            List<BrokageAsset> assets = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName());
            if (assets != null && assets.size() != 0) {
                BrokageAsset asset = assets.get(0);
                asset.setUsableSize(asset.getUsableSize() + order.getSize());
                assetRepository.save(asset);
            } else {
                BrokageAsset asset = new BrokageAsset(order.getCustomerId(), 
                    order.getAssetName(), order.getSize(), order.getSize());
                assetRepository.save(asset);
            }
            tryAsset.setUsableSize(tryAsset.getUsableSize() - totalPrice);
            assetRepository.save(tryAsset);
            order.setStatus(OrderStatus.PENDING);
            order.setCreateDate(LocalDate.now());
            return orderRepository.save(order);
        } else {
            List<BrokageAsset> assets = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName());
            if (assets != null && assets.size() != 0) {
                BrokageAsset asset = assets.get(0);
                if (order.getSize() > asset.getUsableSize()) {
                    throw new NotEnoughAssetException(asset.getAssetName());
                }
                asset.setUsableSize(asset.getUsableSize() - order.getSize());
                assetRepository.save(asset);

                tryAsset.setUsableSize(tryAsset.getUsableSize() + totalPrice);
                assetRepository.save(tryAsset);
                order.setStatus(OrderStatus.PENDING);
                order.setCreateDate(LocalDate.now());
                return orderRepository.save(order);
            } else {
                throw new AssetNotFoundException(order.getAssetName());
            }
        }
    }

    @Override
    public List<BrokageOrder> listOrders(LocalDate startDate, LocalDate endDate, 
        BrokageCustomer brokageCustomer) {
        if (ADMIN_USER.equals(brokageCustomer.getUsername())) {
            return orderRepository.findAllByCreateDateBetween(startDate, endDate);
        }
        return orderRepository.findByCreateDateBetweenAndCustomerId(startDate, endDate, 
            brokageCustomer.getId());
    }

    @Override
    public void deleteOrder(long id, BrokageCustomer brokageCustomer) {
        BrokageOrder order = orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
        if (!ADMIN_USER.equals(brokageCustomer.getUsername()) && order.getCustomerId() != brokageCustomer.getId()) {
            throw new NotAuthToDeleteException();
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException();
        }

        double totalPrice = order.getPrice() * order.getSize();

        List<BrokageAsset> tryAssets = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), "TRY");
        BrokageAsset tryAsset = tryAssets.get(0);

        List<BrokageAsset> assets = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName());
        if (assets == null || assets.size() == 0) {
            throw new AssetNotFoundException(order.getAssetName());
        }
        BrokageAsset asset = assets.get(0);
        if (order.getOrderSide() == OrderSide.BUY) {
            asset.setUsableSize(asset.getUsableSize() - order.getSize());
            tryAsset.setUsableSize(tryAsset.getUsableSize() + totalPrice);
        } else {
            asset.setUsableSize(asset.getUsableSize() + order.getSize());
            tryAsset.setUsableSize(tryAsset.getUsableSize() - totalPrice);
        }
                    
        assetRepository.save(tryAsset);
        assetRepository.save(asset);
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    @Override
    public List<BrokageAsset> listAssets(BrokageCustomer brokageCustomer) {
        if (ADMIN_USER.equals(brokageCustomer.getUsername())) {
            return assetRepository.findAll();
        }
        return assetRepository.findByCustomerId(brokageCustomer.getId());
    }
}

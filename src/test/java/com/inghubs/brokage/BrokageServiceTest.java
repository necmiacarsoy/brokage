package com.inghubs.brokage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inghubs.brokage.entity.BrokageAsset;
import com.inghubs.brokage.entity.BrokageCustomer;
import com.inghubs.brokage.entity.BrokageOrder;
import com.inghubs.brokage.entity.OrderSide;
import com.inghubs.brokage.entity.OrderStatus;
import com.inghubs.brokage.exception.NotEnoughAssetException;
import com.inghubs.brokage.exception.NotEnoughTRYAssetException;
import com.inghubs.brokage.repository.AssetRepository;
import com.inghubs.brokage.repository.CustomerRepository;
import com.inghubs.brokage.repository.OrderRepository;
import com.inghubs.brokage.service.BrokageServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BrokageServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private AssetRepository assetRepository;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private BrokageServiceImpl brokageService;

    @Test
    void shouldCreateCustomerWithEncodedPassword() {
        BrokageCustomer customer = new BrokageCustomer();
        customer.setId(1);
        customer.setUsername("admin");
        customer.setPassword("abc");
        when(customerRepository.save(customer)).thenReturn(customer);

        BrokageCustomer result = brokageService.createCustomer(customer, customer);

        assertNotEquals(null, result.getPassword());
    }

    @Test
    void shouldListAllOrdersWithAdmin() {
        BrokageCustomer customer = new BrokageCustomer();
        customer.setId(1);
        customer.setUsername("admin");
        customer.setPassword("abc");

        List<BrokageOrder> orders = new ArrayList<>();
        BrokageOrder order1 = new BrokageOrder();
        order1.setId(11);
        order1.setCustomerId(customer.getId());
        order1.setAssetName("asset1");
        order1.setCreateDate(LocalDate.now());
        orders.add(order1);
        when(orderRepository.findAllByCreateDateBetween(LocalDate.now(), 
            LocalDate.now())).thenReturn(orders);

        List<BrokageOrder> results = brokageService.listOrders(LocalDate.now(), 
            LocalDate.now(), customer);

        assertEquals(1, results.size());
    }

    @Test
    void shouldListAllOrdersWithCustomUser() {
        BrokageCustomer customer = new BrokageCustomer();
        customer.setId(1);
        customer.setUsername("customUser");
        customer.setPassword("abc");

        List<BrokageOrder> orders = new ArrayList<>();
        BrokageOrder order1 = new BrokageOrder();
        order1.setId(11);
        order1.setCustomerId(customer.getId());
        order1.setAssetName("asset1");
        order1.setCreateDate(LocalDate.now());
        orders.add(order1);
        when(orderRepository.findByCreateDateBetweenAndCustomerId(LocalDate.now(), 
        LocalDate.now(), customer.getId())).thenReturn(orders);

        List<BrokageOrder> results = brokageService.listOrders(LocalDate.now(), 
            LocalDate.now(), customer);

        assertEquals(1, results.size());
    }

    @Test
    void shouldListAllAssetsWithAdmin() {
        BrokageCustomer customer = new BrokageCustomer();
        customer.setId(1);
        customer.setUsername("admin");
        customer.setPassword("abc");

        List<BrokageAsset> assets = new ArrayList<>();
        BrokageAsset brokageAsset = new BrokageAsset();
        brokageAsset.setId(11);
        brokageAsset.setCustomerId(1);
        brokageAsset.setAssetName("asset1");
        assets.add(brokageAsset);
        when(assetRepository.findAll()).thenReturn(assets);

        List<BrokageAsset> results = brokageService.listAssets(customer);

        assertEquals(1, results.size());
    }

    @Test
    void shouldListAllAssetsWithCustomUser() {
        BrokageCustomer customer = new BrokageCustomer();
        customer.setId(1);
        customer.setUsername("customUser");
        customer.setPassword("abc");

        List<BrokageAsset> assets = new ArrayList<>();
        BrokageAsset brokageAsset = new BrokageAsset();
        brokageAsset.setId(11);
        brokageAsset.setCustomerId(1);
        brokageAsset.setAssetName("asset1");
        assets.add(brokageAsset);
        when(assetRepository.findByCustomerId(customer.getId())).thenReturn(assets);

        List<BrokageAsset> results = brokageService.listAssets(customer);

        assertEquals(1, results.size());
    }

    @Test
    void shouldThrowCreateBuyOrderWithAdmin() {
        BrokageCustomer customer = new BrokageCustomer();
        customer.setId(1);
        customer.setUsername("admin");
        customer.setPassword("abc");

        BrokageOrder order = new BrokageOrder();
        order.setId(1);
        order.setAssetName("asset1");
        order.setCustomerId(1);
        order.setOrderSide(OrderSide.BUY);
        order.setStatus(OrderStatus.PENDING);
        order.setPrice(100);
        order.setSize(10);

        List<BrokageAsset> tryAssets = new ArrayList<>();
        BrokageAsset tryBrokageAsset = new BrokageAsset();
        tryBrokageAsset.setId(1);
        tryBrokageAsset.setCustomerId(1);
        tryBrokageAsset.setAssetName("TRY");
        tryBrokageAsset.setUsableSize(500);
        tryAssets.add(tryBrokageAsset);
        when(assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), "TRY")).thenReturn(tryAssets);

        assertThrows(NotEnoughTRYAssetException.class, () -> brokageService.createOrder(order, customer));
    }

    @Test
    void shouldCreateBuyOrderWithAdmin() {
        BrokageCustomer customer = new BrokageCustomer();
        customer.setId(1);
        customer.setUsername("admin");
        customer.setPassword("abc");

        BrokageOrder order = new BrokageOrder();
        order.setId(1);
        order.setAssetName("asset1");
        order.setCustomerId(1);
        order.setOrderSide(OrderSide.BUY);
        order.setPrice(100);
        order.setSize(10);

        List<BrokageAsset> tryAssets = new ArrayList<>();
        BrokageAsset tryBrokageAsset = new BrokageAsset();
        tryBrokageAsset.setId(1);
        tryBrokageAsset.setCustomerId(1);
        tryBrokageAsset.setAssetName("TRY");
        tryBrokageAsset.setUsableSize(2000);
        tryAssets.add(tryBrokageAsset);
        when(assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), "TRY")).thenReturn(tryAssets);

        List<BrokageAsset> assets = new ArrayList<>();
        BrokageAsset asset = new BrokageAsset();
        asset.setId(2);
        asset.setCustomerId(1);
        asset.setAssetName(order.getAssetName());
        asset.setUsableSize(2000);
        assets.add(asset);
        when(assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName())).thenReturn(assets);

        when(assetRepository.save(asset)).thenReturn(null);
        when(assetRepository.save(tryBrokageAsset)).thenReturn(null);
        when(orderRepository.save(order)).thenReturn(order);
        
        BrokageOrder result = brokageService.createOrder(order, customer);
        assertNotEquals(null, result);
    }

    @Test
    void shouldThrowCreateSellOrderWithAdmin() {
        BrokageCustomer customer = new BrokageCustomer();
        customer.setId(1);
        customer.setUsername("admin");
        customer.setPassword("abc");

        BrokageOrder order = new BrokageOrder();
        order.setId(1);
        order.setAssetName("asset1");
        order.setCustomerId(1);
        order.setOrderSide(OrderSide.SELL);
        order.setPrice(100);
        order.setSize(10);

        List<BrokageAsset> tryAssets = new ArrayList<>();
        BrokageAsset tryBrokageAsset = new BrokageAsset();
        tryBrokageAsset.setId(1);
        tryBrokageAsset.setCustomerId(1);
        tryBrokageAsset.setAssetName("TRY");
        tryBrokageAsset.setUsableSize(2000);
        tryAssets.add(tryBrokageAsset);
        when(assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), "TRY")).thenReturn(tryAssets);

        List<BrokageAsset> assets = new ArrayList<>();
        BrokageAsset asset = new BrokageAsset();
        asset.setId(2);
        asset.setCustomerId(1);
        asset.setAssetName(order.getAssetName());
        asset.setUsableSize(5);
        assets.add(asset);
        when(assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName())).thenReturn(assets);
        
        assertThrows(NotEnoughAssetException.class, () -> brokageService.createOrder(order, customer));
    }

    @Test
    void shouldCreateSellOrderWithAdmin() {
        BrokageCustomer customer = new BrokageCustomer();
        customer.setId(1);
        customer.setUsername("admin");
        customer.setPassword("abc");

        BrokageOrder order = new BrokageOrder();
        order.setId(1);
        order.setAssetName("asset1");
        order.setCustomerId(1);
        order.setOrderSide(OrderSide.SELL);
        order.setPrice(100);
        order.setSize(10);

        List<BrokageAsset> tryAssets = new ArrayList<>();
        BrokageAsset tryBrokageAsset = new BrokageAsset();
        tryBrokageAsset.setId(1);
        tryBrokageAsset.setCustomerId(1);
        tryBrokageAsset.setAssetName("TRY");
        tryBrokageAsset.setUsableSize(2000);
        tryAssets.add(tryBrokageAsset);
        when(assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), "TRY")).thenReturn(tryAssets);

        List<BrokageAsset> assets = new ArrayList<>();
        BrokageAsset asset = new BrokageAsset();
        asset.setId(2);
        asset.setCustomerId(1);
        asset.setAssetName(order.getAssetName());
        asset.setUsableSize(100);
        assets.add(asset);
        when(assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName())).thenReturn(assets);

        when(assetRepository.save(asset)).thenReturn(null);
        when(assetRepository.save(tryBrokageAsset)).thenReturn(null);
        when(orderRepository.save(order)).thenReturn(order);
        
        BrokageOrder result = brokageService.createOrder(order, customer);
        assertNotEquals(null, result);
    }

}

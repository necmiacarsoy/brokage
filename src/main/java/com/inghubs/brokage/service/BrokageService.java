package com.inghubs.brokage.service;

import java.time.LocalDate;
import java.util.List;

import com.inghubs.brokage.entity.BrokageAsset;
import com.inghubs.brokage.entity.BrokageCustomer;
import com.inghubs.brokage.entity.BrokageOrder;

public interface BrokageService {

    BrokageCustomer createCustomer(BrokageCustomer customer, BrokageCustomer authCustomer);

    BrokageOrder createOrder(BrokageOrder order, BrokageCustomer brokageCustomer);

    List<BrokageOrder> listOrders(LocalDate startDate, LocalDate endDate, BrokageCustomer brokageCustomer);

    void deleteOrder(long id, BrokageCustomer brokageCustomer);

    List<BrokageAsset> listAssets(BrokageCustomer customer);

}

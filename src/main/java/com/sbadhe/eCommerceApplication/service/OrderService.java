package com.sbadhe.eCommerceApplication.service;

import com.sbadhe.eCommerceApplication.Model.DAO.WebOrderDAO;
import com.sbadhe.eCommerceApplication.Model.LocalUser;
import com.sbadhe.eCommerceApplication.Model.WebOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private WebOrderDAO webOrderDAO;

    public List<WebOrder> getOrders(LocalUser user){
        return webOrderDAO.findByUser(user);
    }
}

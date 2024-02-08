package com.sbadhe.eCommerceApplication.service;

import com.sbadhe.eCommerceApplication.Model.DAO.ProductDAO;
import com.sbadhe.eCommerceApplication.Model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDAO productDAO;

    public List<Product> getProducts(){
        return productDAO.findAll();
    }
}

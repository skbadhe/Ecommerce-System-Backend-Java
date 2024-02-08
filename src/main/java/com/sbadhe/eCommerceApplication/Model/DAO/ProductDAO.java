package com.sbadhe.eCommerceApplication.Model.DAO;

import com.sbadhe.eCommerceApplication.Model.Product;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductDAO extends ListCrudRepository<Product, Long> {
}

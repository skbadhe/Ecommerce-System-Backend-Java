package com.sbadhe.eCommerceApplication.Model.DAO;

import com.sbadhe.eCommerceApplication.Model.LocalUser;
import com.sbadhe.eCommerceApplication.Model.WebOrder;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WebOrderDAO extends ListCrudRepository<WebOrder, Long>{
    List<WebOrder> findByUser(LocalUser user);

}
package com.sbadhe.eCommerceApplication.Model.DAO;

import com.sbadhe.eCommerceApplication.Model.LocalUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface LocalUserDAO extends ListCrudRepository<LocalUser, Long> {
    @Query("select l from LocalUser l where upper(l.username) = upper(?1)")
    Optional<LocalUser> findByUsernameIgnoreCase(String username);

    @Query("select l from LocalUser l where upper(l.email) = upper(?1)")
    Optional<LocalUser> findByEmailIgnoreCase(String email);

}

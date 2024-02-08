package com.sbadhe.eCommerceApplication.Model.DAO;

import com.sbadhe.eCommerceApplication.Model.LocalUser;
import com.sbadhe.eCommerceApplication.Model.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface VerificationTokenDAO extends ListCrudRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    void deleteByUser(LocalUser user);
}

package com.orchid.orchidbe.repositories;

import com.orchid.orchidbe.domain.account.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    Optional<Account> findByEmail(String email);


}

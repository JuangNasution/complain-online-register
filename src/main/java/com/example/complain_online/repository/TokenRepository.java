package com.example.complain_online.repository;

import com.example.complain_online.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Juang Nasution
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    Token findByConfirmationToken(String token);
}

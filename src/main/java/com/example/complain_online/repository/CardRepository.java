package com.example.complain_online.repository;

import com.example.complain_online.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Juang Nasution
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

}

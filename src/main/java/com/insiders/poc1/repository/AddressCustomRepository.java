package com.insiders.poc1.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class AddressCustomRepository {

    private EntityManager entityManager;

    public AddressCustomRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void updateAllMainAdressToFalse(Long customerId){
        String jpql = "UPDATE Address a SET a.mainAddress = false WHERE a.customer.id = :customerId";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("customerId", customerId);
        query.executeUpdate();
    }
}
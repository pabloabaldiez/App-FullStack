package com.api.crud.repository;

import com.api.crud.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;



@Repository
public interface ProductRepository extends JpaRepository <Product, Integer>{

    Optional<Product> findByName(String name);
    boolean existsByName(String name);



}

package com.everis.autorization.repository;

import com.everis.autorization.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repositorio asociado a la entidad Products
 */
public interface ProductsRepository extends JpaRepository<Products, String> {
}

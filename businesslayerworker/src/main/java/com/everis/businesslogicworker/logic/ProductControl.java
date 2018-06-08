package com.everis.businesslogicworker.logic;

import com.everis.autorization.model.Products;
import com.everis.autorization.model.Users;
import com.everis.autorization.repository.ProductsRepository;
import com.everis.autorization.repository.UsersRepository;
import com.everis.businesslogicworker.interfaces.IProductControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EntityScan({"com.everis.*"})
public class ProductControl implements IProductControl {

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private UsersRepository usersRepository;

    public List listProducts() {
        return productsRepository.findAll();
    }

    public String newProduct(Products products) {
        productsRepository.save(products);
        return "Producto " + products.getName() + " guardado!";
    }

    public boolean userCanUseProduct(String idProduct, Users users) {
        Users aux = usersRepository.findOne(users.getIdCompany());
        Products products = productsRepository.findOne(idProduct);
        return aux.getProducts().contains(products);
    }

    public String getClassName(String idProduct) {
        Products products = productsRepository.findOne(idProduct);
        return products.getClassName();
    }
}
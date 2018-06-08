package com.everis.businesslogicworker.interfaces;

import com.everis.autorization.model.Products;
import com.everis.autorization.model.Users;

import java.util.List;

public interface IProductControl {
    List listProducts();
    String newProduct(Products products);
    boolean userCanUseProduct(String idProduct, Users users);
    String getClassName(String idProduct);
}

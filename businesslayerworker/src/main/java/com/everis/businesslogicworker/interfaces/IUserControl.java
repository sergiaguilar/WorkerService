package com.everis.businesslogicworker.interfaces;

import com.everis.autorization.model.Users;

import java.util.List;

public interface IUserControl {

    Users findUserByIdCompany(String id);

    boolean existsUser(String id);

}
package com.everis.facturationcontrolworker.interfaces;

import com.everis.autorization.model.Users;
import com.everis.billingworker.model.License;

import java.sql.Timestamp;

public interface IFacturationControl {
    String newFacturation(Timestamp timestamp, Users users, License license, Integer numPags);
    License getLicense(String idProduct);
    void addPages(License license, Integer numPags);
    Integer getActualVolume(String idLicense);
    Integer getVolume(String idLicense);
}

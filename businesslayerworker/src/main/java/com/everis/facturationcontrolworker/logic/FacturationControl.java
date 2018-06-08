package com.everis.facturationcontrolworker.logic;

import com.everis.autorization.model.Products;
import com.everis.autorization.model.Users;
import com.everis.autorization.repository.ProductsRepository;
import com.everis.autorization.repository.UsersRepository;
import com.everis.billingworker.model.Facturation;
import com.everis.billingworker.model.FacturationComposed;
import com.everis.billingworker.model.License;
import com.everis.billingworker.repository.FacturationRepository;
import com.everis.billingworker.repository.LicenseRepository;
import com.everis.facturationcontrolworker.interfaces.IFacturationControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
@EntityScan({"com.everis.*"})
public class FacturationControl implements IFacturationControl{

    @Autowired
    private FacturationRepository facturationRepository;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private UsersRepository usersRepository;

    public String newFacturation(Timestamp timestamp, Users users, License license, Integer numPags) {
        String idCompany = users.getIdCompany();
        String idLicense = license.getIdLicense();
        FacturationComposed facturationComposed = new FacturationComposed(timestamp, idCompany, idLicense);
        Facturation facturation = new Facturation(facturationComposed,numPags, license, users);

        facturationRepository.save(facturation);

        return "Páginas añadidas a la factura.";
    }

    public License getLicense(String idProduct) {
        List<License> licenses = licenseRepository.findAll();
        for(int i = 0; i < licenses.size(); ++i) {
            if( licenses.get(i).getIdProduct().getIdProduct().equals(idProduct)) {
                return licenses.get(i);
            }
        }
        return null;
    }

    public void addPages(License license, Integer numPags) {
        String idLicense = license.getIdLicense();
        Integer aux = license.getActualVolume();
        license.setActualVolume(aux + numPags);
        licenseRepository.save(license);
    }

    public Integer getActualVolume(String idLicense) {
        return licenseRepository.getOne(idLicense).getActualVolume();
    }

    public Integer getVolume(String idLicense) {
        return licenseRepository.getOne(idLicense).getVolume();
    }


}

package com.everis.billingworker.repository;

import com.everis.billingworker.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * Repositorio asociado a la entidad License
 */
public interface LicenseRepository extends JpaRepository<License, String> {
}
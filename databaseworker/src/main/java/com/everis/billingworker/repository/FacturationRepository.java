package com.everis.billingworker.repository;

import com.everis.billingworker.model.Facturation;
import com.everis.billingworker.model.FacturationComposed;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio asociado a la entidad Facturation
 */
public interface FacturationRepository extends JpaRepository<Facturation, FacturationComposed> {
}

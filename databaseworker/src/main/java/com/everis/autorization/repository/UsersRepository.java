package com.everis.autorization.repository;


import com.everis.autorization.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio asociado a la entidad Users
 */
public interface UsersRepository extends JpaRepository<Users,String> {

}

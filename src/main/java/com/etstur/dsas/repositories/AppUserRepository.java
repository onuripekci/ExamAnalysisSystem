package com.etstur.dsas.repositories;

import com.etstur.dsas.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    AppUser findByUsername(String username);
}

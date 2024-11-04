package jeryck.dev.beans.repository;

import jeryck.dev.beans.entite.Utilisateur;
import jeryck.dev.beans.entite.Validation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidationRepository extends JpaRepository<Validation, Integer>  {

    Optional<Validation> findByCode(String code);
}

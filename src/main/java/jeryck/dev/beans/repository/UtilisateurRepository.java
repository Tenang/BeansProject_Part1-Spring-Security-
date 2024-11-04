package jeryck.dev.beans.repository;

import jeryck.dev.beans.entite.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
   Optional <Utilisateur> findByEmail(String email);
}

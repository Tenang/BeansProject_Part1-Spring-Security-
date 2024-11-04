package jeryck.dev.beans.repository;

import jeryck.dev.beans.entite.Avis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvisRepository extends JpaRepository <Avis, Integer> {
}

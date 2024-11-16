package jeryck.dev.beans.repository;

import jeryck.dev.beans.entite.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepository extends JpaRepository <Jwt, Integer>{

 //   Optional<Jwt> findByValue(String value);


   Optional<Jwt> findByValueAndDesactivateAndExpire(String valeur, boolean desactivate, boolean expire);


    @Query("FROM Jwt j WHERE j.expire=:expire and j.desactivate =:desactivate  and j.utilisateur.email = :email")
    Optional<Jwt> findByUtilisateurValidToken(String email, boolean desactivate, boolean expire);

    @Query("FROM Jwt j WHERE  j.utilisateur.email = :email")
    Stream<Jwt> findByUtilisateur(String email);

    void deleteAllByExpireAndDesactivate(boolean expire, boolean desactivate);
}

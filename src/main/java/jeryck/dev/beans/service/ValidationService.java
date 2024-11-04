package jeryck.dev.beans.service;

import jeryck.dev.beans.entite.Utilisateur;
import jeryck.dev.beans.entite.Validation;
import jeryck.dev.beans.repository.ValidationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@AllArgsConstructor
public class ValidationService {

    private ValidationRepository validationRepository;
    private  NotificationService notificationService;

    public void enregistrer(Utilisateur utilisateur){

        Validation validation =new Validation();
        validation.setUtilisateur(utilisateur);

        Instant creation = Instant.now();
        validation.setCreation(creation);

        Instant expiration = Instant.now();
        expiration = expiration.plus(10, ChronoUnit.MINUTES);
        validation.setExpiration(expiration);

        Random random = new Random();
        int randomInteger = random.nextInt(999999);


        String code = String.format("%06d",randomInteger);

        validation.setCode(code);

        this.validationRepository.save(validation);
        this.notificationService.envoyer(validation);
    }

    public Validation lireEnFonctionDuCode(String code){
       return this.validationRepository.findByCode(code).orElseThrow(()-> new RuntimeException("Votre code est valide"));

    }
}

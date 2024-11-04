package jeryck.dev.beans.service;

import jeryck.dev.beans.entite.Validation;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {

    JavaMailSender javaMailSender;
    public void envoyer(Validation validation){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@chilo.tech");
        message.setTo(validation.getUtilisateur().getEmail());
        message.setSubject("Votre code activation");

      String texte =   String.format("Bonjour %s, </br> votre code d'activation est %s, A bientôt! ",
                validation.getUtilisateur().getNom(),
                validation.getCode()
        );
        message.setText(texte);

        javaMailSender.send(message);
    }
}

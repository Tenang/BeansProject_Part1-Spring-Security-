package jeryck.dev.beans.service;

import jeryck.dev.beans.entite.Avis;
import jeryck.dev.beans.entite.Utilisateur;
import jeryck.dev.beans.repository.AvisRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AvisService {

    // final veut dire qu il es statique le contenu va pas etre modifié
    private final AvisRepository avisRepository;

    public void creer(Avis avis){

    Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    avis.setUtilisateur(utilisateur);
        this.avisRepository.save(avis);
    }
}
 
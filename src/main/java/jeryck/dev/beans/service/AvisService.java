package jeryck.dev.beans.service;

import jeryck.dev.beans.entite.Avis;
import jeryck.dev.beans.repository.AvisRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AvisService {

    // final veut dire qu il es statique le contenu va pas etre modifié
    private final AvisRepository avisRepository;

    public void creer(Avis avis){
        this.avisRepository.save(avis);
    }
}

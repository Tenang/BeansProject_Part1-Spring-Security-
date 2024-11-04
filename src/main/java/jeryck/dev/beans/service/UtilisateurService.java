package jeryck.dev.beans.service;

import jeryck.dev.beans.TypeDeRole;
import jeryck.dev.beans.entite.Role;
import jeryck.dev.beans.entite.Utilisateur;
import jeryck.dev.beans.entite.Validation;
import jeryck.dev.beans.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UtilisateurService {

    private UtilisateurRepository utilisateurRepository;
    private BCryptPasswordEncoder passwordEncoder;

    private ValidationService validationService;

    public void inscription(Utilisateur utilisateur){

        //ON verifie si le mail es valide
        if (!(utilisateur.getEmail().contains("@"))){
            throw new RuntimeException("Votre Mail n'est pas valide verifier a nouveau");
        }
        if (!(utilisateur.getEmail().contains("."))){
            throw new RuntimeException("Votre Mail n'est pas valide verifier a nouveau");
        }

        Optional<Utilisateur> utilisateurOptional = this.utilisateurRepository.findByEmail(utilisateur.getEmail());
       if (utilisateurOptional.isPresent()){
           throw new RuntimeException("cette email existe deja ");
       }
        String mdpCrypter =  this.passwordEncoder.encode(utilisateur.getMdp());

        utilisateur.setMdp(mdpCrypter);
        Role roleUtilisateur = new Role();
        roleUtilisateur.setLibele(TypeDeRole.UTILISATEUR);

        utilisateur.setRole(roleUtilisateur);
        utilisateur = this.utilisateurRepository.save(utilisateur);
        this.validationService.enregistrer(utilisateur);
    }

    public void activation(Map<String, String> activation) {
        Validation validation = this.validationService.lireEnFonctionDuCode(activation.get("code"));

        if (Instant.now().isAfter(validation.getExpiration())){
            throw new RuntimeException("Votre code est expiré");
        }
        Utilisateur utilisateurActiver = this.utilisateurRepository.findById(validation.getUtilisateur().getId()).orElseThrow(()->  new RuntimeException("utilisateur inconnu "));
                utilisateurActiver.setActif(true);
                this.utilisateurRepository.save(utilisateurActiver);
    }
}

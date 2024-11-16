package jeryck.dev.beans.controller;

import jeryck.dev.beans.dto.AuthentificationDTO;
import jeryck.dev.beans.entite.Utilisateur;
import jeryck.dev.beans.securite.JwtService;
import jeryck.dev.beans.service.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j

@AllArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class UtilisateurController {

    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private UtilisateurService utilisateurService;

    @PostMapping(path = "inscription")
    public void inscription(@RequestBody Utilisateur utilisateur){

        this.utilisateurService.inscription(utilisateur);
    }

    @PostMapping(path = "activation")
    public void inscription(@RequestBody Map <String, String> activation){

        this.utilisateurService.activation(activation);
    }
@PostMapping(path = "deconnexion")
    public void deconnexion(){

        this.jwtService.deconnexion();
    }

    @PostMapping(path = "connexion")
    public Map<String, String> connexion(@RequestBody AuthentificationDTO authentificationDTO){

     final Authentication authenticate= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authentificationDTO.username(),
                        authentificationDTO.password()));

     if (authenticate.isAuthenticated()){
         return this.jwtService.generate(authentificationDTO.username());
     }
     return null;

    }
}

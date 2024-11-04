package jeryck.dev.beans.controller;

import jeryck.dev.beans.entite.Avis;
import jeryck.dev.beans.service.AvisService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("avis")
public class AvisController {

    private final AvisService avisService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void creer(@RequestBody Avis avis){
        this.avisService.creer(avis);
    }
}

package jeryck.dev.beans.entite;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "validation")
public class Validation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Instant creation;
    private  Instant activation;
    private Instant expiration;
    private String code;

    @OneToOne(cascade = CascadeType.ALL)
    private Utilisateur utilisateur;
}

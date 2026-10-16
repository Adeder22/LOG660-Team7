package com.log660.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "USAGER")
public class Usager {
    @Id
    @Column(name = "NOUSAGER", nullable = false)
    private Long id;

    @Column(name = "NOM", nullable = false, length = 35)
    private String nom;

    @Column(name = "PRENOM", nullable = false, length = 35)
    private String prenom;

    @Column(name = "COURRIEL", nullable = false)
    private String courriel;

    @Column(name = "TEL", nullable = false, columnDefinition = "CHAR(12)")
    private String tel;

    @Temporal(TemporalType.DATE)
    @Column(name = "ANNIVERSAIRE", nullable = false)
    private Date anniversaire;

    @Column(name = "MOTDEPASSE", length = 64)
    private String motdepasse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCourriel() {
        return courriel;
    }

    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Date getAnniversaire() {
        return anniversaire;
    }

    public void setAnniversaire(Date anniversaire) {
        this.anniversaire = anniversaire;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
    }

}
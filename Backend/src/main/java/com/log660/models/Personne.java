package com.log660.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "PERSONNE")
public class Personne {
    @Id
    @Column(name = "NOPERSONNE", nullable = false)
    private Long id;

    @Column(name = "NOM", nullable = false, length = 100)
    private String nom;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATENAISSANCE")
    private Date dateNaissance;

    @Column(name = "LIEUNAISSANCE")
    private String lieunaissance;

    @Column(name = "PHOTO", length = 1000)
    private String photo;

    @Column(name = "BIOGRAPHIE", length = 4000)
    private String biographie;

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

    public Date getDateNaissance() { return dateNaissance; }

    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getLieunaissance() {
        return lieunaissance;
    }

    public void setLieunaissance(String lieunaissance) {
        this.lieunaissance = lieunaissance;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBiographie() {
        return biographie;
    }

    public void setBiographie(String biographie) {
        this.biographie = biographie;
    }

}
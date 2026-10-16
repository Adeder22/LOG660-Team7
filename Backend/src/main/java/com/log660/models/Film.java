package com.log660.models;

import javax.persistence.*;

@Entity
@Table(name = "FILM")
public class Film {
    @Id
    @Column(name = "NOFILM", nullable = false)
    private Long id;

    @Column(name = "TITRE", nullable = false)
    private String titre;

    @Column(name = "ANNEESORTIE")
    private Short anneesortie;

    @Column(name = "LANGUEORIGINALE", length = 100)
    private String langueoriginale;

    @Column(name = "DUREEMINUTES")
    private Short dureeminutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOREALISATEUR")
    private Personne norealisateur;

    @Column(name = "RESUMESCENARIO", length = 4000)
    private String resumescenario;

    @Column(name = "AFFICHE", length = 1000)
    private String affiche;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Short getAnneesortie() {
        return anneesortie;
    }

    public void setAnneesortie(Short anneesortie) {
        this.anneesortie = anneesortie;
    }

    public String getLangueoriginale() {
        return langueoriginale;
    }

    public void setLangueoriginale(String langueoriginale) {
        this.langueoriginale = langueoriginale;
    }

    public Short getDureeminutes() {
        return dureeminutes;
    }

    public void setDureeminutes(Short dureeminutes) {
        this.dureeminutes = dureeminutes;
    }

    public Personne getNorealisateur() {
        return norealisateur;
    }

    public void setNorealisateur(Personne norealisateur) {
        this.norealisateur = norealisateur;
    }

    public String getResumescenario() {
        return resumescenario;
    }

    public void setResumescenario(String resumescenario) {
        this.resumescenario = resumescenario;
    }

    public String getAffiche() {
        return affiche;
    }

    public void setAffiche(String affiche) {
        this.affiche = affiche;
    }

}
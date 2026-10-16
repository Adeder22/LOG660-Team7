package com.log660.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "FILM_ACTEURS")
public class FilmActeur {
    @EmbeddedId
    private FilmActeurId id;

    @MapsId("nofilm")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "NOFILM", nullable = false)
    private Film nofilm;

    @MapsId("nopersonne")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NOPERSONNE", nullable = false)
    private Personne nopersonne;

    public FilmActeurId getId() {
        return id;
    }

    public void setId(FilmActeurId id) {
        this.id = id;
    }

    public Film getNofilm() {
        return nofilm;
    }

    public void setNofilm(Film nofilm) {
        this.nofilm = nofilm;
    }

    public Personne getNopersonne() {
        return nopersonne;
    }

    public void setNopersonne(Personne nopersonne) {
        this.nopersonne = nopersonne;
    }

}
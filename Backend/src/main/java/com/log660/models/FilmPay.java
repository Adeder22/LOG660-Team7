package com.log660.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "FILM_PAYS")
public class FilmPay {
    @EmbeddedId
    private FilmPayId id;

    @MapsId("nofilm")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "NOFILM", nullable = false)
    private Film nofilm;

    @MapsId("nompays")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NOMPAYS", nullable = false)
    private PaysProduction nompays;

    public FilmPayId getId() {
        return id;
    }

    public void setId(FilmPayId id) {
        this.id = id;
    }

    public Film getNofilm() {
        return nofilm;
    }

    public void setNofilm(Film nofilm) {
        this.nofilm = nofilm;
    }

    public PaysProduction getNompays() {
        return nompays;
    }

    public void setNompays(PaysProduction nompays) {
        this.nompays = nompays;
    }

}
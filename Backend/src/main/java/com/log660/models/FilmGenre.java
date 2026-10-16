package com.log660.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "FILM_GENRE")
public class FilmGenre {
    @EmbeddedId
    private FilmGenreId id;

    @MapsId("nofilm")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "NOFILM", nullable = false)
    private Film nofilm;

    @MapsId("typegenre")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TYPEGENRE", nullable = false)
    private Genre typegenre;

    public FilmGenreId getId() {
        return id;
    }

    public void setId(FilmGenreId id) {
        this.id = id;
    }

    public Film getNofilm() {
        return nofilm;
    }

    public void setNofilm(Film nofilm) {
        this.nofilm = nofilm;
    }

    public Genre getTypegenre() {
        return typegenre;
    }

    public void setTypegenre(Genre typegenre) {
        this.typegenre = typegenre;
    }

}
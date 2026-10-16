package com.log660.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FilmGenreId implements Serializable {
    private static final long serialVersionUID = 4991569383016868986L;
    @Column(name = "NOFILM", nullable = false)
    private Long nofilm;

    @Column(name = "TYPEGENRE", nullable = false, length = 100)
    private String typegenre;

    public Long getNofilm() {
        return nofilm;
    }

    public void setNofilm(Long nofilm) {
        this.nofilm = nofilm;
    }

    public String getTypegenre() {
        return typegenre;
    }

    public void setTypegenre(String typegenre) {
        this.typegenre = typegenre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmGenreId entity = (FilmGenreId) o;
        return Objects.equals(this.nofilm, entity.nofilm) &&
                Objects.equals(this.typegenre, entity.typegenre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nofilm, typegenre);
    }
}
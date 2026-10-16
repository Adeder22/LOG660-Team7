package com.log660.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FilmActeurId implements Serializable {
    private static final long serialVersionUID = 2222236051325632953L;
    @Column(name = "NOFILM", nullable = false)
    private Long nofilm;

    @Column(name = "NOPERSONNE", nullable = false)
    private Long nopersonne;

    @Column(name = "PERSONNAGE", nullable = false, length = 100)
    private String personnage;

    public Long getNofilm() {
        return nofilm;
    }

    public void setNofilm(Long nofilm) {
        this.nofilm = nofilm;
    }

    public Long getNopersonne() {
        return nopersonne;
    }

    public void setNopersonne(Long nopersonne) {
        this.nopersonne = nopersonne;
    }

    public String getPersonnage() {
        return personnage;
    }

    public void setPersonnage(String personnage) {
        this.personnage = personnage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmActeurId entity = (FilmActeurId) o;
        return Objects.equals(this.nofilm, entity.nofilm) &&
                Objects.equals(this.nopersonne, entity.nopersonne) &&
                Objects.equals(this.personnage, entity.personnage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nofilm, nopersonne, personnage);
    }
}
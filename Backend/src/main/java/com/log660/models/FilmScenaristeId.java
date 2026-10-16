package com.log660.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FilmScenaristeId implements Serializable {
    private static final long serialVersionUID = 353357062143062057L;
    @Column(name = "NOFILM", nullable = false)
    private Long nofilm;

    @Column(name = "NOMSCENARISTE", nullable = false, length = 200)
    private String nomscenariste;

    public Long getNofilm() {
        return nofilm;
    }

    public void setNofilm(Long nofilm) {
        this.nofilm = nofilm;
    }

    public String getNomscenariste() {
        return nomscenariste;
    }

    public void setNomscenariste(String nomscenariste) {
        this.nomscenariste = nomscenariste;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmScenaristeId entity = (FilmScenaristeId) o;
        return Objects.equals(this.nofilm, entity.nofilm) &&
                Objects.equals(this.nomscenariste, entity.nomscenariste);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nofilm, nomscenariste);
    }
}
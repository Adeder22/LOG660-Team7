package com.log660.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FilmPayId implements Serializable {
    private static final long serialVersionUID = -6573055173625053428L;
    @Column(name = "NOFILM", nullable = false)
    private Long nofilm;

    @Column(name = "NOMPAYS", nullable = false, length = 100)
    private String nompays;

    public Long getNofilm() {
        return nofilm;
    }

    public void setNofilm(Long nofilm) {
        this.nofilm = nofilm;
    }

    public String getNompays() {
        return nompays;
    }

    public void setNompays(String nompays) {
        this.nompays = nompays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmPayId entity = (FilmPayId) o;
        return Objects.equals(this.nofilm, entity.nofilm) &&
                Objects.equals(this.nompays, entity.nompays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nofilm, nompays);
    }
}
package com.log660.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "BANDES_ANNONCES")
public class BandesAnnonce {
    @EmbeddedId
    private BandesAnnonceId id;

    @MapsId("nofilm")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "NOFILM", nullable = false)
    private Film nofilm;

    public BandesAnnonceId getId() {
        return id;
    }

    public void setId(BandesAnnonceId id) {
        this.id = id;
    }

    public Film getNofilm() {
        return nofilm;
    }

    public void setNofilm(Film nofilm) {
        this.nofilm = nofilm;
    }

}
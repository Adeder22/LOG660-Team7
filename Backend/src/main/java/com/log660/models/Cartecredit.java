package com.log660.models;

import javax.persistence.*;

@Entity
@Table(name = "CARTECREDIT")
public class Cartecredit {
    @Id
    @Column(name = "NOUSAGER", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NOUSAGER", nullable = false)
    private Client client;

    @Column(name = "TYPECARTE", nullable = false, length = 20)
    private String typecarte;

    @Column(name = "NUMCARTE", nullable = false, length = 25)
    private String numcarte;

    @Column(name = "EXPMOIS", nullable = false)
    private Short expmois;

    @Column(name = "EXPANNEE", nullable = false)
    private Short expannee;

    @Column(name = "CVV", nullable = false, length = 4)
    private String cvv;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getTypecarte() {
        return typecarte;
    }

    public void setTypecarte(String typecarte) {
        this.typecarte = typecarte;
    }

    public String getNumcarte() {
        return numcarte;
    }

    public void setNumcarte(String numcarte) {
        this.numcarte = numcarte;
    }

    public Short getExpmois() {
        return expmois;
    }

    public void setExpmois(Short expmois) {
        this.expmois = expmois;
    }

    public Short getExpannee() {
        return expannee;
    }

    public void setExpannee(Short expannee) {
        this.expannee = expannee;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

}
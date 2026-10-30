package com.log660.models;

import javax.persistence.*;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "PRET")
public class Pret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOPRET", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NOUSAGER", nullable = false)
    private Client nousager;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CODECOPIE", nullable = false)
    private Copie codecopie;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATEDEBUT", nullable = false)
    private Date datedebut;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATERETOUR")
    private Date dateretour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getNousager() {
        return nousager;
    }

    public void setNousager(Client nousager) {
        this.nousager = nousager;
    }

    public Copie getCodecopie() {
        return codecopie;
    }

    public void setCodecopie(Copie codecopie) {
        this.codecopie = codecopie;
    }

    public Date getDatedebut() {
        return datedebut;
    }

    public void setDatedebut(Date datedebut) {
        this.datedebut = datedebut;
    }

    public Date getDateretour() {
        return dateretour;
    }

    public void setDateretour(Date dateretour) {
        this.dateretour = dateretour;
    }

}
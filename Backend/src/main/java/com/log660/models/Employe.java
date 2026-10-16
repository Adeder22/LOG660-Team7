package com.log660.models;

import javax.persistence.*;

@Entity
@Table(name = "EMPLOYE")
public class Employe {
    @Id
    @Column(name = "NOUSAGER", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NOUSAGER", nullable = false)
    private Usager usager;

    @Column(name = "MATRICULE", nullable = false, length = 7)
    private String matricule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usager getUsager() {
        return usager;
    }

    public void setUsager(Usager usager) {
        this.usager = usager;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

}
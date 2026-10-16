package com.log660.models;

import javax.persistence.*;

@Entity
@Table(name = "CLIENT")
public class Client {
    @Id
    @Column(name = "NOUSAGER", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NOUSAGER", nullable = false)
    private Usager usager;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CODEFORFAIT", nullable = false)
    private Forfait codeforfait;

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

    public Forfait getCodeforfait() {
        return codeforfait;
    }

    public void setCodeforfait(Forfait codeforfait) {
        this.codeforfait = codeforfait;
    }

}
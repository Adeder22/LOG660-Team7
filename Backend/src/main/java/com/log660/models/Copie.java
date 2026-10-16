package com.log660.models;

import javax.persistence.*;

@Entity
@Table(name = "COPIE")
public class Copie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODECOPIE", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NOFILM", nullable = false)
    private Film nofilm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Film getNofilm() {
        return nofilm;
    }

    public void setNofilm(Film nofilm) {
        this.nofilm = nofilm;
    }

}
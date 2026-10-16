package com.log660.models;

import javax.persistence.*;

@Entity
@Table(name = "ADRESSE")
public class Adresse {
    @Id
    @Column(name = "NOUSAGER", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NOUSAGER", nullable = false)
    private Usager usager;

    @Column(name = "NOCIV", nullable = false)
    private Long nociv;

    @Column(name = "RUE", nullable = false, length = 50)
    private String rue;

    @Column(name = "VILLE", nullable = false, length = 50)
    private String ville;

    @Column(name = "PROVINCE", nullable = false, length = 50)
    private String province;

    @Column(name = "CODEPOSTAL", nullable = false, length = 7)
    private String codepostal;

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

    public Long getNociv() {
        return nociv;
    }

    public void setNociv(Long nociv) {
        this.nociv = nociv;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCodepostal() {
        return codepostal;
    }

    public void setCodepostal(String codepostal) {
        this.codepostal = codepostal;
    }

}
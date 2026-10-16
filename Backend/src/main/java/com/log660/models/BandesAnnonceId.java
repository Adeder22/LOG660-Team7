package com.log660.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BandesAnnonceId implements Serializable {
    private static final long serialVersionUID = -7395871783433224553L;
    @Column(name = "NOFILM", nullable = false)
    private Long nofilm;

    @Column(name = "URL", nullable = false, length = 1000)
    private String url;

    public Long getNofilm() {
        return nofilm;
    }

    public void setNofilm(Long nofilm) {
        this.nofilm = nofilm;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BandesAnnonceId entity = (BandesAnnonceId) o;
        return Objects.equals(this.nofilm, entity.nofilm) &&
                Objects.equals(this.url, entity.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nofilm, url);
    }
}
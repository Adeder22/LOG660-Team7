package com.log660.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Critères saisis par le client dans le formulaire de recherche de films.
 * Tous les critères sont facultatifs; seuls ceux qui sont renseignés filtrent la recherche.
 * Les critères multivalués (acteurs, genres, pays) peuvent être répétés : un film doit alors
 * satisfaire chacune des valeurs (ex. : deux acteurs différents dans le même film).
 */
public class SearchCriteria {
    private String titre;
    private Short anneeMin;
    private Short anneeMax;
    private String langue;
    private String realisateur;
    private final List<String> acteurs = new ArrayList<>();
    private final List<String> genres = new ArrayList<>();
    private final List<String> pays = new ArrayList<>();

    public String getTitre() { return titre; }

    public void setTitre(String titre) { this.titre = titre; }

    public Short getAnneeMin() { return anneeMin; }

    public void setAnneeMin(Short anneeMin) { this.anneeMin = anneeMin; }

    public Short getAnneeMax() { return anneeMax; }

    public void setAnneeMax(Short anneeMax) { this.anneeMax = anneeMax; }

    public String getLangue() { return langue; }

    public void setLangue(String langue) { this.langue = langue; }

    public String getRealisateur() { return realisateur; }

    public void setRealisateur(String realisateur) { this.realisateur = realisateur; }

    public List<String> getActeurs() { return acteurs; }

    public void addActor(String actor) { acteurs.add(actor); }

    public List<String> getGenres() { return genres; }

    public void addGenre(String genre) { genres.add(genre); }

    public List<String> getPays() { return pays; }

    public void addCountry(String country) { pays.add(country); }
}

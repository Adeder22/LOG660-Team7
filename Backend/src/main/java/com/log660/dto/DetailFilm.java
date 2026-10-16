package com.log660.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Fiche complète d'un film, affichée lorsque le client choisit un film dans les résultats de recherche.
 * Les champs simples sont remplis par le "select new" ; les listes sont ajoutées ensuite par le DAO.
 */
public class DetailFilm {
    private final Long noFilm;
    private final String titre;
    private final Short anneeSortie;
    private final String langueOriginale;
    private final Short dureeMinutes;
    private final Long noRealisateur;
    private final String nomRealisateur;
    private final String resumeScenario;
    private final String affiche;

    private List<String> pays = new ArrayList<>();
    private List<String> genres = new ArrayList<>();
    private List<String> scenaristes = new ArrayList<>();
    private List<ActeurInfo> acteurs = new ArrayList<>();
    private List<String> bandesAnnonces = new ArrayList<>();

    public DetailFilm(Long noFilm, String titre, Short anneeSortie, String langueOriginale, Short dureeMinutes,
                      Long noRealisateur, String nomRealisateur, String resumeScenario, String affiche) {
        this.noFilm = noFilm;
        this.titre = titre;
        this.anneeSortie = anneeSortie;
        this.langueOriginale = langueOriginale;
        this.dureeMinutes = dureeMinutes;
        this.noRealisateur = noRealisateur;
        this.nomRealisateur = nomRealisateur;
        this.resumeScenario = resumeScenario;
        this.affiche = affiche;
    }

    public Long getNoFilm() { return this.noFilm; }

    public String getTitre() { return this.titre; }

    public Short getAnneeSortie() { return this.anneeSortie; }

    public String getLangueOriginale() { return this.langueOriginale; }

    public Short getDureeMinutes() { return this.dureeMinutes; }

    /** Identifiant du réalisateur (null si inconnu), pour afficher sa fiche lorsqu'on clique sur son nom. */
    public Long getNoRealisateur() { return this.noRealisateur; }

    public String getNomRealisateur() { return this.nomRealisateur; }

    public String getResumeScenario() { return this.resumeScenario; }

    public String getAffiche() { return this.affiche; }

    public List<String> getPays() { return this.pays; }

    public void setPays(List<String> pays) { this.pays = pays; }

    public List<String> getGenres() { return this.genres; }

    public void setGenres(List<String> genres) { this.genres = genres; }

    public List<String> getScenaristes() { return this.scenaristes; }

    public void setScenaristes(List<String> scenaristes) { this.scenaristes = scenaristes; }

    public List<ActeurInfo> getActeurs() { return this.acteurs; }

    public void setActeurs(List<ActeurInfo> acteurs) { this.acteurs = acteurs; }

    public List<String> getBandesAnnonces() { return this.bandesAnnonces; }

    public void setBandesAnnonces(List<String> bandesAnnonces) { this.bandesAnnonces = bandesAnnonces; }
}

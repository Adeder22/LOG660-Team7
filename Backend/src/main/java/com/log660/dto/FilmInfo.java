package com.log660.dto;

/**
 * Informations retournées au site lors de la recherche d'un film.
 * TODO
 */
public class FilmInfo {
    private final Long noFilm;
    private final String titre;
    private final Short anneeSortie;
    private final String langueOriginale;
    private final Short dureeMinutes;
    private final String nomRealisateur;
    private final String resumeScenario;
    private final String affiche;

    public FilmInfo(Long noFilm, String titre, Short anneeSortie, String langueOriginale, Short dureeMinutes, String nomRealisateur, String resumeScenario, String affiche) {
        this.noFilm = noFilm;
        this.titre = titre;
        this.anneeSortie = anneeSortie;
        this.langueOriginale = langueOriginale;
        this.dureeMinutes = dureeMinutes;
        this.nomRealisateur = nomRealisateur;
        this.resumeScenario = resumeScenario;
        this.affiche = affiche;
    }

    public Long getNoFilm() { return this.noFilm; }

    public String getTitre() { return this.titre; }

    public Short getAnneeSortie() { return this.anneeSortie; }

    public String getLangueOriginale() { return this.langueOriginale; }

    public Short getDureeMinutes() { return this.dureeMinutes; }

    public String getNomRealisateur() { return this.nomRealisateur; }

    public String getResumeScenario() { return this.resumeScenario; }

    public String getAffiche() { return this.affiche; }
}

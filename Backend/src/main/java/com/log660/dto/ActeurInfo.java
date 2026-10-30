package com.log660.dto;

/**
 * Un rôle joué dans un film : l'acteur et le personnage qu'il interprète.
 * Un acteur qui joue plusieurs personnages dans le même film apparaît une fois par personnage.
 */
public class ActeurInfo {
    private final Long noPersonne;
    private final String nom;
    private final String personnage;

    public ActeurInfo(Long noPersonne, String nom, String personnage) {
        this.noPersonne = noPersonne;
        this.nom = nom;
        this.personnage = personnage;
    }

    public Long getNoPersonne() { return this.noPersonne; }

    public String getNom() { return this.nom; }

    public String getPersonnage() { return this.personnage; }
}

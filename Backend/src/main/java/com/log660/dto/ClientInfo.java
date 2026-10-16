package com.log660.dto;

/**
 * Informations retournées au site lors de la connexion d'un client.
 */
public class ClientInfo {
    private final Long noUsager;
    private final String nom;
    private final String prenom;

    public ClientInfo(Long noUsager, String nom, String prenom) {
        this.noUsager = noUsager;
        this.nom = nom;
        this.prenom = prenom;
    }

    public Long getNoUsager() {
        return noUsager;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }
}

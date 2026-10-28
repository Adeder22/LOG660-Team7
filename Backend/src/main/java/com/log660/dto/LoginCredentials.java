package com.log660.dto;

/**
 * Identifiants fournis par un client pour se connecter.
 */
public class LoginCredentials {
    private final String email;
    private final String motDePasse;

    public LoginCredentials(String email, String motDePasse) {
        this.email = email;
        this.motDePasse = motDePasse;
    }

    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }
}

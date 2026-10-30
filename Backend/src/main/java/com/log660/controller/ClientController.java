package com.log660.controller;

import com.log660.dto.*;
import com.log660.facade.FacadeApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Assure la liaison entre le front-end et le backend
 */
@RestController
@RequestMapping("/")
public class ClientController {
    private final FacadeApplication application = new FacadeApplication();

    /**
     * Authentifie un client avec son adresse courriel et son mot de passe.
     *
     * @param loginInfo Informations du client.
     * @return Informations du client authentifié.
     */
    @PostMapping("/login")
    public ResponseEntity<ClientInfo> login(
            @RequestBody LoginCredentials loginInfo) {
        return ResponseEntity.ok(application.login(loginInfo.getEmail(), loginInfo.getMotDePasse()));
    }

    /**
     * Recherche des films selon les critères de recherche fournis.
     *
     * @param criteria Critères de recherche du film.
     * @return Liste des films correspondant aux critères demandés.
     */
    @PostMapping("/searchFilms")
    public ResponseEntity<List<FilmInfo>> searchFilms(
            @RequestBody SearchCriteria criteria) {
        return ResponseEntity.ok(application.searchFilms(criteria));
    }

    /**
     * Récupère les détails d'un film à partir de son identifiant.
     *
     * @param id Identifiant du film à consulter.
     * @return Détails du film demandé.
     */
    @PostMapping("/detailFilm")
    public ResponseEntity<DetailFilm> detailFilm(
            @RequestParam Long id) {
        return ResponseEntity.ok(application.detailFilm(id));
    }
}
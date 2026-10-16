package com.log660.facade;

import com.log660.dao.ClientDAO;
import com.log660.dao.FilmDAO;
import com.log660.dto.ClientInfo;
import com.log660.dto.FilmInfo;
import com.log660.dto.SearchCriteria;

import java.util.List;

/**
 * Point d'entrée unique de la couche présentation vers la couche métier (patron Façade).
 * Le client mince (servlet / GUI) ne doit passer que par cette classe.
 */
public class FacadeApplication {
    private final ClientDAO clientDAO = new ClientDAO();
    private final FilmDAO filmDAO = new FilmDAO();

    /**
     * Cas d'utilisation 2 : connexion d'un client.
     * Retourne les informations du client, ou null si le courriel ou le mot de passe est invalide.
     */
    public ClientInfo login(String email, String password) {
        return clientDAO.authenticate(email, password);
    }

    /**
     * Cas d'utilisation 3 : recherche de films selon les critères saisis par le client.
     * Retourne une liste vide si aucun film ne correspond.
     */
    public List<FilmInfo> searchFilms(SearchCriteria criteria) {
        return filmDAO.search(criteria);
    }
}

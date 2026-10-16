package org.example;

import com.log660.dto.ClientInfo;
import com.log660.dto.FilmInfo;
import com.log660.dto.SearchCriteria;
import com.log660.facade.FacadeApplication;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Valeurs qui viendront éventuellement du formulaire de connexion du site
            String email = "RobertCFlores21@gmail.com";
            String password = "eishie3meiH";

            ClientInfo client = new FacadeApplication().login(email, password);

            if (client == null) {
                System.out.println("Courriel inconnu ou mot de passe incorrect. Veuillez entrer de nouvelles valeurs.");
            } else {
                System.out.println("Connexion réussie : " + client.getPrenom() + " " + client.getNom()
                        + " (no usager " + client.getNoUsager() + ")");
            }

            SearchCriteria criteria = new SearchCriteria();
            criteria.setTitre("king lion");             // words in any order
            criteria.setAnneeMin((short) 1990);
            criteria.setAnneeMax((short) 2000);
            criteria.addGenre("Animation");

            List<FilmInfo> films = new FacadeApplication().searchFilms(criteria);

            if (films == null) {
                System.out.println("Problème survenue lors de la recherche");
            } else {
                for (FilmInfo film : films) {
                    System.out.println(film.getTitre() + " (" + film.getAnneeSortie() + ")");
                }
            }
        } finally {
            HibernateUtil.getSessionFactory().close();
        }
    }
}

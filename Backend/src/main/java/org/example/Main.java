package org.example;

import com.log660.dto.ActeurInfo;
import com.log660.dto.ClientInfo;
import com.log660.dto.DetailFilm;
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
            criteria.setTitre("king lion");
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

            DetailFilm detail = new FacadeApplication().detailFilm(119190);

            if (detail == null) {
                System.out.println("Aucun film ne correspond à ce numéro.");
            } else {
                System.out.println("Titre : " + detail.getTitre());
                System.out.println("Année de sortie : " + detail.getAnneeSortie());
                System.out.println("Pays de production : " + String.join(", ", detail.getPays()));
                System.out.println("Langue originale : " + detail.getLangueOriginale());
                System.out.println("Durée : " + detail.getDureeMinutes() + " minutes");
                System.out.println("Genres : " + String.join(", ", detail.getGenres()));
                System.out.println("Réalisateur : " + detail.getNomRealisateur());
                System.out.println("Scénaristes : " + String.join(", ", detail.getScenaristes()));
                System.out.println("Acteurs :");
                for (ActeurInfo acteur : detail.getActeurs()) {
                    System.out.println("  " + acteur.getNom() + " (" + acteur.getPersonnage() + ")");
                }
                System.out.println("Résumé : " + detail.getResumeScenario());
                System.out.println("Affiche : " + detail.getAffiche());
                System.out.println("Bandes-annonces : " + String.join(", ", detail.getBandesAnnonces()));
            }

        } finally {
            HibernateUtil.getSessionFactory().close();
        }
    }
}

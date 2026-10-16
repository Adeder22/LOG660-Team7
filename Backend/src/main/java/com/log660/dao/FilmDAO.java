package com.log660.dao;

import com.log660.dto.FilmInfo;
import com.log660.dto.SearchCriteria;
import org.example.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmDAO {

    /**
     * Cas d'utilisation 3 : retourne les films qui satisfont tous les critères renseignés, triés par titre.
     * Un critère vide ou null n'a aucun effet sur la recherche.
     */
    @SuppressWarnings("unchecked")
    public List<FilmInfo> search(SearchCriteria criteria) {
        // "where 1 = 1" permet d'ajouter chaque critère avec "and ..." sans traiter le premier à part
        StringBuilder hql = new StringBuilder(
                "select new com.log660.dto.FilmInfo(f.id, f.titre, f.anneesortie, f.langueoriginale, " +
                "f.dureeminutes, r.nom, f.resumescenario, f.affiche) " +
                "from Film f left join f.norealisateur r " +
                "where 1 = 1 ");
        Map<String, Object> parameters = new HashMap<>();

        addContent(hql, parameters, "f.titre", "titre", criteria.getTitre());

        if (criteria.getAnneeMin() != null) {
            hql.append("and f.anneesortie >= :anneeMin ");
            parameters.put("anneeMin", criteria.getAnneeMin());
        }
        if (criteria.getAnneeMax() != null) {
            hql.append("and f.anneesortie <= :anneeMax ");
            parameters.put("anneeMax", criteria.getAnneeMax());
        }

        if (isNotEmpty(criteria.getLangue())) {
            hql.append("and lower(f.langueoriginale) = :langue ");
            parameters.put("langue", criteria.getLangue().trim().toLowerCase());
        }

        addContent(hql, parameters, "r.nom", "realisateur", criteria.getRealisateur());

        // Un "exists" par valeur : un film peut avoir plusieurs genres, pays et acteurs,
        // et chaque valeur répétée doit être satisfaite séparément.
        List<String> genres = criteria.getGenres();
        for (int i = 0; i < genres.size(); i++) {
            if (isNotEmpty(genres.get(i))) {
                String alias = "fg" + i;
                hql.append("and exists (select 1 from FilmGenre ").append(alias)
                   .append(" where ").append(alias).append(".nofilm = f")
                   .append(" and lower(").append(alias).append(".typegenre.typegenre) = :genre").append(i).append(") ");
                parameters.put("genre" + i, genres.get(i).trim().toLowerCase());
            }
        }

        List<String> countries = criteria.getPays();
        for (int i = 0; i < countries.size(); i++) {
            if (isNotEmpty(countries.get(i))) {
                String alias = "fp" + i;
                hql.append("and exists (select 1 from FilmPay ").append(alias)
                   .append(" where ").append(alias).append(".nofilm = f")
                   .append(" and lower(").append(alias).append(".nompays.nompays) = :pays").append(i).append(") ");
                parameters.put("pays" + i, countries.get(i).trim().toLowerCase());
            }
        }

        // Tous les mots d'un même acteur doivent se trouver dans le nom de la même personne :
        // ils vont donc tous dans le même "exists".
        List<String> actors = criteria.getActeurs();
        for (int i = 0; i < actors.size(); i++) {
            if (isNotEmpty(actors.get(i))) {
                String alias = "fa" + i;
                String personAlias = "acteur" + i;
                hql.append("and exists (select 1 from FilmActeur ").append(alias)
                   .append(" join ").append(alias).append(".nopersonne ").append(personAlias)
                   .append(" where ").append(alias).append(".nofilm = f ");
                addContent(hql, parameters, personAlias + ".nom", "acteur" + i + "Mot", actors.get(i));
                hql.append(") ");
            }
        }

        hql.append("order by f.titre");

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery(hql.toString());
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                query.setParameter(parameter.getKey(), parameter.getValue());
            }
            return query.list();
        } finally {
            session.close();
        }
    }

    /**
     * Ajoute une condition "like" par mot du texte : chaque mot doit être contenu dans le champ,
     * peu importe l'ordre (ex. : "spielberg steven" trouve "Steven Spielberg"). Insensible à la casse.
     */
    private void addContent(StringBuilder hql, Map<String, Object> parameters,
                            String field, String parameterPrefix, String text) {
        if (!isNotEmpty(text)) {
            return;
        }
        String[] words = text.trim().toLowerCase().split("\\s+");
        for (int j = 0; j < words.length; j++) {
            String parameterName = parameterPrefix + j;
            hql.append("and lower(").append(field).append(") like :").append(parameterName).append(" ");
            parameters.put(parameterName, "%" + words[j] + "%");
        }
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
}

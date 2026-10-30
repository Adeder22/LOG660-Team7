package com.log660.dao;

import com.log660.exception.RentalException;
import org.example.HibernateUtil;
import org.hibernate.JDBCException;
import org.hibernate.Session;

public class PretDAO {
    // Codes d'erreur levés par la procédure p_location (RAISE_APPLICATION_ERROR)
    private static final int FILM_OR_CLIENT_NOT_FOUND = 20005;
    private static final int RENTAL_LIMIT_REACHED = 20100;
    private static final int NO_COPY_AVAILABLE = 20101;

    /**
     * Cas d'utilisation 4 : loue une copie disponible du film au client.
     * Les règles d'affaires (limite du forfait, copie disponible) sont validées par la procédure p_location.
     *
     * @throws RentalException si la location est refusée, avec un message destiné au client
     */
    public void rent(long noUsager, long noFilm) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.createSQLQuery("call p_location(:noFilm, :noUsager)")
                    .setParameter("noFilm", noFilm)
                    .setParameter("noUsager", noUsager)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (JDBCException e) {
            session.getTransaction().rollback();
            throw toRentalException(e);
        } finally {
            session.close();
        }
    }

    private RuntimeException toRentalException(JDBCException e) {
        switch (e.getSQLException().getErrorCode()) {
            case FILM_OR_CLIENT_NOT_FOUND:
                return new RentalException("Le film ou le client demandé n'existe pas.");
            case RENTAL_LIMIT_REACHED:
                return new RentalException("Vous avez atteint le nombre maximal de locations permis par votre forfait.");
            case NO_COPY_AVAILABLE:
                return new RentalException("Aucune copie de ce film n'est disponible pour le moment.");
            default:
                return e;
        }
    }
}

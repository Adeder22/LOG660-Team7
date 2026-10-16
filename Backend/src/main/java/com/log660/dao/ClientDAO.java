package com.log660.dao;

import com.log660.dto.ClientInfo;
import org.example.HibernateUtil;
import org.hibernate.Session;

public class ClientDAO {

    /**
     * Retourne les informations du client correspondant au courriel et au mot de passe fournis,
     * ou null si aucun client ne correspond.
     */
    public ClientInfo authenticate(String email, String password) {
        if (email == null || password == null) {
            return null;
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return (ClientInfo) session.createQuery(
                            "select new com.log660.dto.ClientInfo(u.id, u.nom, u.prenom) " +
                            "from Client c join c.usager u " +
                            "where u.courriel = :courriel " +
                            "and u.motdepasse = :motdepasse")
                    .setParameter("courriel", email.trim())
                    .setParameter("motdepasse", password)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }
}

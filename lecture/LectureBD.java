import java.io.FileInputStream;
import java.io.IOException;

import java.io.InputStream;

import java.sql.*;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Lit les fichiers XML de personnes, de films et de clients afin d'insérer
 * leurs données dans la base de données.
 */
public class LectureBD {
    private Connection connection;

    /**
     * Représente le rôle joué par un acteur dans un film.
     */
    public class Role {
        /**
         * Crée un rôle à partir de l'identifiant de l'acteur, de son nom et
         * du personnage interprété.
         *
         * @param i identifiant de l'acteur
         * @param n nom de l'acteur
         * @param p nom du personnage interprété
         */
        public Role(int i, String n, String p) {
            id = i;
            nom = n;
            personnage = p;
        }

        protected int id;
        protected String nom;
        protected String personnage;
    }

    /**
     * Permet la connexion à la base de données.
     */
    public LectureBD() {
        connectionBD();
    }


    /**
     * Lit un fichier XML de personnes et ajoute les personnes à la base de
     * données.
     *
     * @param nomFichier chemin du fichier
     */
    public void lecturePersonnes(String nomFichier) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            InputStream is = new FileInputStream(nomFichier);
            parser.setInput(is, null);

            int eventType = parser.getEventType();

            String tag = null,
                    nom = null,
                    anniversaire = null,
                    lieu = null,
                    photo = null,
                    bio = null;

            int id = -1;

            try (PreparedStatement batchStatement = connection.prepareCall(
                    "BEGIN INSERT INTO Personne "
                            + "(noPersonne, nom, dateNaissance, lieuNaissance, photo, biographie) "
                            + "VALUES (?, ?, ?, ?, ?, ?); END;")) {
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tag = parser.getName();

                        if (tag.equals("personne") && parser.getAttributeCount() == 1)
                            id = Integer.parseInt(parser.getAttributeValue(0));
                    } else if (eventType == XmlPullParser.END_TAG) {
                        tag = null;

                        if (parser.getName().equals("personne") && id >= 0) {
                            insertionPersonne(batchStatement, id, nom, anniversaire, lieu, photo, bio);

                            id = -1;
                            nom = null;
                            anniversaire = null;
                            lieu = null;
                            photo = null;
                            bio = null;
                        }
                    } else if (eventType == XmlPullParser.TEXT && id >= 0) {
                        if (tag != null) {
                            if (tag.equals("nom"))
                                nom = parser.getText();
                            else if (tag.equals("anniversaire"))
                                anniversaire = parser.getText();
                            else if (tag.equals("lieu"))
                                lieu = parser.getText();
                            else if (tag.equals("photo"))
                                photo = parser.getText();
                            else if (tag.equals("bio"))
                                bio = parser.getText();
                        }
                    }

                    eventType = parser.next();
                }
                batchStatement.executeBatch();
            }
        } catch (XmlPullParserException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println("IOException while parsing " + nomFichier);
        } catch (SQLException e) {
            throw new IllegalStateException("Insertion des personnes impossible.", e);
        }
    }

    /**
     * Lit un fichier XML de films et insère les films ainsi que leurs données
     * associées dans la base de données.
     *
     * @param nomFichier chemin du fichier
     */
    public void lectureFilms(String nomFichier) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            InputStream is = new FileInputStream(nomFichier);
            parser.setInput(is, null);

            int eventType = parser.getEventType();

            String tag = null,
                    titre = null,
                    langue = null,
                    poster = null,
                    roleNom = null,
                    rolePersonnage = null,
                    realisateurNom = null,
                    resume = null;

            ArrayList<String> pays = new ArrayList<String>();
            ArrayList<String> genres = new ArrayList<String>();
            ArrayList<String> scenaristes = new ArrayList<String>();
            ArrayList<Role> roles = new ArrayList<Role>();
            ArrayList<String> annonces = new ArrayList<String>();

            int id = -1,
                    annee = -1,
                    duree = -1,
                    roleId = -1,
                    realisateurId = -1;

            try (PreparedStatement batchStatement = connection.prepareCall(
                    "{call p_ajouterFilm(?,?,?,?,?,?,?,?)}")) {
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tag = parser.getName();

                        if (tag.equals("film") && parser.getAttributeCount() == 1)
                            id = Integer.parseInt(parser.getAttributeValue(0));
                        else if (tag.equals("realisateur") && parser.getAttributeCount() == 1)
                            realisateurId = Integer.parseInt(parser.getAttributeValue(0));
                        else if (tag.equals("acteur") && parser.getAttributeCount() == 1)
                            roleId = Integer.parseInt(parser.getAttributeValue(0));
                    } else if (eventType == XmlPullParser.END_TAG) {
                        tag = null;

                        if (parser.getName().equals("film") && id >= 0) {
                            insertionFilm(batchStatement, id, titre, annee, pays, langue,
                                    duree, resume, genres, realisateurNom,
                                    realisateurId, scenaristes,
                                    roles, poster, annonces);

                            id = -1;
                            annee = -1;
                            duree = -1;
                            titre = null;
                            langue = null;
                            poster = null;
                            resume = null;
                            realisateurNom = null;
                            roleNom = null;
                            rolePersonnage = null;
                            realisateurId = -1;
                            roleId = -1;

                            genres.clear();
                            scenaristes.clear();
                            roles.clear();
                            annonces.clear();
                            pays.clear();
                        }
                        if (parser.getName().equals("role") && roleId >= 0) {
                            roles.add(new Role(roleId, roleNom, rolePersonnage));
                            roleId = -1;
                            roleNom = null;
                            rolePersonnage = null;
                        }
                    } else if (eventType == XmlPullParser.TEXT && id >= 0) {
                        if (tag != null) {
                            if (tag.equals("titre"))
                                titre = parser.getText();
                            else if (tag.equals("annee"))
                                annee = Integer.parseInt(parser.getText());
                            else if (tag.equals("pays"))
                                pays.add(parser.getText());
                            else if (tag.equals("langue"))
                                langue = parser.getText();
                            else if (tag.equals("duree"))
                                duree = Integer.parseInt(parser.getText());
                            else if (tag.equals("resume"))
                                resume = parser.getText();
                            else if (tag.equals("genre"))
                                genres.add(parser.getText());
                            else if (tag.equals("realisateur"))
                                realisateurNom = parser.getText();
                            else if (tag.equals("scenariste"))
                                scenaristes.add(parser.getText());
                            else if (tag.equals("acteur"))
                                roleNom = parser.getText();
                            else if (tag.equals("personnage"))
                                rolePersonnage = parser.getText();
                            else if (tag.equals("poster"))
                                poster = parser.getText();
                            else if (tag.equals("annonce"))
                                annonces.add(parser.getText());
                        }
                    }

                    eventType = parser.next();
                }
                batchStatement.executeBatch();
            }
        } catch (XmlPullParserException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println("IOException while parsing " + nomFichier);
        } catch (SQLException e) {
            throw new IllegalStateException("Insertion des films impossible.", e);
        }
    }

    /**
     * Lit un fichier XML de clients et ajoute les clients à la base de données.
     *
     * @param nomFichier chemin du fichier
     */
    public void lectureClients(String nomFichier) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            InputStream is = new FileInputStream(nomFichier);
            parser.setInput(is, null);

            int eventType = parser.getEventType();

            String tag = null,
                    nomFamille = null,
                    prenom = null,
                    courriel = null,
                    tel = null,
                    anniv = null,
                    adresse = null,
                    ville = null,
                    province = null,
                    codePostal = null,
                    carte = null,
                    noCarte = null,
                    motDePasse = null,
                    forfait = null;

            int id = -1,
                    expMois = -1,
                    expAnnee = -1;

            // Prepare le batch d'ajout du Client
            try (PreparedStatement batchStatement = connection.prepareCall(
                    "{call p_ajouterClient(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}")) {
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tag = parser.getName();

                        if (tag.equals("client") && parser.getAttributeCount() == 1)
                            id = Integer.parseInt(parser.getAttributeValue(0));
                    } else if (eventType == XmlPullParser.END_TAG) {
                        tag = null;

                        if (parser.getName().equals("client") && id >= 0) {
                            try {
                                insertionClient(batchStatement, id, nomFamille, prenom, courriel, tel,
                                        anniv, adresse, ville, province,
                                        codePostal, carte, noCarte,
                                        expMois, expAnnee, motDePasse, forfait);
                            } catch (SQLException e) {
                                System.out.println("Insertion du client impossible: " + id);
                            }
                            nomFamille = null;
                            prenom = null;
                            courriel = null;
                            tel = null;
                            anniv = null;
                            adresse = null;
                            ville = null;
                            province = null;
                            codePostal = null;
                            carte = null;
                            noCarte = null;
                            motDePasse = null;
                            forfait = null;

                            id = -1;
                            expMois = -1;
                            expAnnee = -1;
                        }
                    } else if (eventType == XmlPullParser.TEXT && id >= 0) {
                        if (tag != null) {
                            if (tag.equals("nom-famille"))
                                nomFamille = parser.getText();
                            else if (tag.equals("prenom"))
                                prenom = parser.getText();
                            else if (tag.equals("courriel"))
                                courriel = parser.getText();
                            else if (tag.equals("tel"))
                                tel = parser.getText();
                            else if (tag.equals("anniversaire"))
                                anniv = parser.getText();
                            else if (tag.equals("adresse"))
                                adresse = parser.getText();
                            else if (tag.equals("ville"))
                                ville = parser.getText();
                            else if (tag.equals("province"))
                                province = parser.getText();
                            else if (tag.equals("code-postal"))
                                codePostal = parser.getText();
                            else if (tag.equals("carte"))
                                carte = parser.getText();
                            else if (tag.equals("no"))
                                noCarte = parser.getText();
                            else if (tag.equals("exp-mois"))
                                expMois = Integer.parseInt(parser.getText());
                            else if (tag.equals("exp-annee"))
                                expAnnee = Integer.parseInt(parser.getText());
                            else if (tag.equals("mot-de-passe"))
                                motDePasse = parser.getText();
                            else if (tag.equals("forfait"))
                                forfait = parser.getText();
                        }
                    }
                    eventType = parser.next();
                }
            }
        } catch (XmlPullParserException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println("IOException while parsing " + nomFichier);
        } catch (SQLException e) {
            System.out.println("Insertion des clients impossible.");
        }
    }

    /**
     * Prépare l'insertion d'une personne dans la batch d'instructions.
     *
     * @param statement instruction utilisée pour l'insertion
     * @param id identifiant de la personne
     * @param nom nom de la personne
     * @param anniv date de naissance
     * @param lieu lieu de naissance
     * @param photo référence de la photo
     * @param bio biographie de la personne
     * @throws SQLException si la préparation des valeurs ou l'ajout au lot échoue
     */
    private void insertionPersonne(PreparedStatement statement, int id, String nom,
                                   String anniv, String lieu, String photo, String bio)
            throws SQLException {
        String bioRes = "";
        if (bio != null && bio.length() >= 4000) {
            bioRes = bio.trim().substring(0, 3899);
        } else if (bio != null) {
            bioRes = bio.trim();
        }
        statement.setInt(1, id);
        statement.setString(2, nom);
        if (anniv == null || anniv.trim().isEmpty())
            statement.setNull(3, Types.DATE);
        else
            statement.setDate(3, Date.valueOf(anniv));
        statement.setString(4, lieu);
        statement.setString(5, photo);
        statement.setString(6, bioRes);
        statement.addBatch();
        System.out.println("Personne ajoutée au lot: " + id);
    }

    /**
     * Insère un film et ses informations associées en appelant les procédures
     * stockées appropriées.
     *
     * @param statement instruction préparée pour l'insertion du film
     * @param id identifiant du film
     * @param titre titre du film
     * @param annee année de sortie
     * @param pays pays de production du film
     * @param langue langue originale du film
     * @param duree durée du film en minutes
     * @param resume résumé du film
     * @param genres genres associés au film
     * @param realisateurNom nom du réalisateur
     * @param realisateurId identifiant du réalisateur
     * @param scenaristes noms des scénaristes associés au film
     * @param roles rôles des acteurs dans le film
     * @param poster référence de l'affiche
     * @param annonces références des bandes-annonces
     * @throws SQLException si une insertion dans la base de données échoue
     */
    private void insertionFilm(PreparedStatement statement, int id, String titre, int annee,
                               ArrayList<String> pays, String langue, int duree, String resume,
                               ArrayList<String> genres, String realisateurNom, int realisateurId,
                               ArrayList<String> scenaristes,
                               ArrayList<Role> roles, String poster,
                               ArrayList<String> annonces) throws SQLException {
        statement.setInt(1, id);
        statement.setString(2, titre);
        if (annee < 0)
            statement.setNull(3, Types.NUMERIC);
        else
            statement.setInt(3, annee);
        statement.setString(4, langue);
        if (duree < 0)
            statement.setNull(5, Types.NUMERIC);
        else
            statement.setInt(5, duree);
        if (realisateurId < 0)
            statement.setNull(6, Types.NUMERIC);
        else
            statement.setInt(6, realisateurId);
        statement.setString(7, resume);
        statement.setString(8, poster);
        statement.executeUpdate();

        // Appelle les procédures liés aux tables associés à Film
        try (PreparedStatement genreStatement = connection.prepareCall(
                     "{call p_ajouterFilmGenre(?,?)}");
             PreparedStatement paysStatement = connection.prepareCall(
                     "{call p_ajouterFilmPays(?,?)}");
             PreparedStatement annonceStatement = connection.prepareCall(
                     "{call p_ajouterBandeAnnonce(?,?)}");
             PreparedStatement acteurStatement = connection.prepareCall(
                     "{call p_ajouterFilmActeurs(?,?,?)}");
             PreparedStatement scenaristeStatement = connection.prepareCall(
                     "{call p_ajouterFilmScenariste(?,?)}")) {
            for (String genre : genres) {
                genreStatement.setInt(1, id);
                genreStatement.setString(2, genre);
                genreStatement.executeUpdate();
            }

            for (String paysProduction : pays) {
                paysStatement.setInt(1, id);
                paysStatement.setString(2, paysProduction);
                paysStatement.executeUpdate();
            }

            for (String annonce : annonces) {
                annonceStatement.setInt(1, id);
                annonceStatement.setString(2, annonce);
                annonceStatement.executeUpdate();
            }

            for (Role role : roles) {
                acteurStatement.setInt(1, id);
                acteurStatement.setInt(2, role.id);
                acteurStatement.setString(3, role.personnage);
                acteurStatement.executeUpdate();
            }

            for (String scenariste : scenaristes) {
                scenaristeStatement.setInt(1, id);
                scenaristeStatement.setString(2, scenariste);
                scenaristeStatement.executeUpdate();
            }
        }

        System.out.println("Film ajouté: " + id);
    }

    /**
     * Insère un client dans le lot d'instructions SQL en séparant le numéro
     * civique et le nom de rue de son adresse.
     *
     * @param statement instruction préparée utilisée pour l'insertion
     * @param id identifiant du client
     * @param nomFamille nom de famille du client
     * @param prenom prénom du client
     * @param courriel adresse courriel du client
     * @param tel numéro de téléphone du client
     * @param anniv date de naissance au format attendu par {@link Date#valueOf(String)}
     * @param adresse adresse complète du client
     * @param ville ville de résidence du client
     * @param province province de résidence du client
     * @param codePostal code postal du client
     * @param carte type de carte de paiement
     * @param noCarte numéro de la carte de paiement
     * @param expMois mois d'expiration de la carte
     * @param expAnnee année d'expiration de la carte
     * @param motDePasse mot de passe du client
     * @param forfait forfait d'abonnement du client
     * @throws SQLException si l'insertion dans la base de données échoue
     */
    private void insertionClient(PreparedStatement statement, int id, String nomFamille, String prenom,
                                 String courriel, String tel, String anniv,
                                 String adresse, String ville, String province,
                                 String codePostal, String carte, String noCarte,
                                 int expMois, int expAnnee, String motDePasse,
                                 String forfait) throws SQLException {
        String[] adresseSeparee = adresse.trim().split("\\s+", 2);
        int noCiv;
        String rue;
        noCiv = Integer.parseInt(adresseSeparee[0]);
        rue = adresseSeparee.length == 2 ? adresseSeparee[1] : "";

        statement.setInt(1, id);
        statement.setString(2, nomFamille);
        statement.setString(3, prenom);
        statement.setString(4, courriel);
        statement.setString(5, tel);
        if (anniv == null || anniv.trim().isEmpty())
            statement.setNull(6, Types.DATE);
        else
            statement.setDate(6, Date.valueOf(anniv));
        statement.setString(7, motDePasse);
        statement.setInt(8, noCiv);
        statement.setString(9, rue);
        statement.setString(10, ville);
        statement.setString(11, province);
        statement.setString(12, codePostal);
        statement.setString(13, forfait);
        statement.setString(14, carte);
        statement.setString(15, noCarte);
        statement.setInt(16, expMois);
        statement.setInt(17, expAnnee);
        statement.setString(18, "000");
        statement.executeUpdate();
        System.out.println("Client ajouté au lot: " + id);
    }

    /**
     * Établit la connexion à la base de données.
     *
     * @throws IllegalStateException si le pilote Oracle est absent ou si la
     *         connexion ne peut pas être établie
     */
    private void connectionBD() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//bdlog660.ens.ad.etsmtl.ca/ORCLPDB1",
                    "EQUIPE107", "Qg1C6326");
            connection.setAutoCommit(false);
            System.out.println("Connexion a la BD reussie.");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Le pilote Oracle est absent du classpath.", e);
        } catch (SQLException e) {
            throw new IllegalStateException("Connexion a la BD impossible.", e);
        }
    }

    /**
     * Lit les fichiers XML de personnes, de films et de clients,
     * puis valide la transaction.
     *
     * @param args trois chemins de fichiers dans l'ordre suivant :
     *             personnes, films et clients
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java LectureBD <personnes.xml> <films.xml> <clients.xml>");
            System.exit(1);
        }
        LectureBD lecture = new LectureBD();
        long start = System.nanoTime();

        lecture.lecturePersonnes(args[0]);
        System.out.println("Insertion Personnes reussie.");
        lecture.lectureFilms(args[1]);
        System.out.println("Insertion Films reussie.");
        lecture.lectureClients(args[2]);
        System.out.println("Insertion Clients reussie.");
        long end = System.nanoTime();
        System.out.println("Temps : " + (end - start) / 1_000_000.0 + " ms");
        try {
            lecture.connection.commit();
            lecture.connection.close();
        } catch (SQLException e) {
            throw new IllegalStateException("Connexion a la BD impossible.", e);
        }
    }
}

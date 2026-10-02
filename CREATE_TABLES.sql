--- Remove table before creating them
DROP TABLE Pret CASCADE CONSTRAINTS;
DROP TABLE Copie CASCADE CONSTRAINTS;
DROP TABLE Film_Scenariste CASCADE CONSTRAINTS;
DROP TABLE Film_Acteurs CASCADE CONSTRAINTS;
DROP TABLE Bandes_Annonces CASCADE CONSTRAINTS;
DROP TABLE Film_Pays CASCADE CONSTRAINTS;
DROP TABLE Film_Genre CASCADE CONSTRAINTS;
DROP TABLE Film CASCADE CONSTRAINTS;
DROP TABLE Personne CASCADE CONSTRAINTS;
DROP TABLE Pays_Production CASCADE CONSTRAINTS;
DROP TABLE Genre CASCADE CONSTRAINTS;
DROP TABLE carteCredit CASCADE CONSTRAINTS;
DROP TABLE Adresse CASCADE CONSTRAINTS;
DROP TABLE Client CASCADE CONSTRAINTS;
DROP TABLE Employe CASCADE CONSTRAINTS;
DROP TABLE Usager CASCADE CONSTRAINTS;
DROP TABLE Forfait CASCADE CONSTRAINTS;

--- Creation de nos tables
--- Note: la colonne tel accepte uniquement le format suivant : 000-000-0000
CREATE TABLE Usager (
    noUsager INTEGER NOT NULL,
    nom VARCHAR2(35) NOT NULL,
    prenom VARCHAR2(35) NOT NULL,
    courriel VARCHAR2(255) NOT NULL UNIQUE,
    tel CHAR(12) NOT NULL,
    anniversaire DATE NOT NULL,
    motDePasse VARCHAR2(64),
    CONSTRAINT pk_usager PRIMARY KEY (noUsager),
    CONSTRAINT format_courriel 
        CHECK (REGEXP_LIKE(courriel, '^[^@ ]+@[^@ ]+\.[^@ ]+$')),
    CONSTRAINT format_tel
        CHECK (REGEXP_LIKE(tel, '^[0-9]{3}-[0-9]{3}-[0-9]{4}$')),
    CONSTRAINT alphaNum_5_motDePasse
        CHECK (REGEXP_LIKE(motDePasse, '^[A-Za-z0-9]{5,}$'))
);

CREATE TABLE Employe (
    noUsager INTEGER NOT NULL,
    matricule VARCHAR2(7) NOT NULL UNIQUE,
    CONSTRAINT pk_employe PRIMARY KEY (noUsager),
    CONSTRAINT fk_employe_usager FOREIGN KEY (noUsager)
        REFERENCES Usager(noUsager)
);

CREATE TABLE Forfait (
    codeForfait CHAR(1) NOT NULL,
    nomForfait VARCHAR2(20) NOT NULL UNIQUE,
    prix NUMBER(5,2) NOT NULL,
    locationMax NUMBER(2) NOT NULL,
    dureeMax NUMBER(3),
    CONSTRAINT pk_forfait PRIMARY KEY (codeForfait),
    CONSTRAINT ck_forfait_code CHECK (codeForfait IN ('D', 'I', 'A'))
);

CREATE TABLE Client (
    noUsager INTEGER NOT NULL,
    codeForfait CHAR(1) NOT NULL,
    CONSTRAINT pk_client PRIMARY KEY (noUsager),
    CONSTRAINT fk_client_usager FOREIGN KEY (noUsager)
        REFERENCES Usager(noUsager),
    CONSTRAINT fk_client_forfait FOREIGN KEY (codeForfait)
        REFERENCES Forfait(codeForfait)
);

CREATE TABLE carteCredit (
    noUsager INTEGER NOT NULL,
    typeCarte VARCHAR2(20) NOT NULL,
    numCarte VARCHAR2(25) NOT NULL,
    expMois NUMBER(2) NOT NULL,
    expAnnee NUMBER(4) NOT NULL,
    CVV VARCHAR2(4) NOT NULL,
    CONSTRAINT pk_cartecredit PRIMARY KEY (noUsager),
    CONSTRAINT fk_cartecredit_client FOREIGN KEY (noUsager)
        REFERENCES Client(noUsager),
    CONSTRAINT format_cvv
        CHECK (REGEXP_LIKE(CVV, '^[0-9]{3,4}$'))
);

CREATE TABLE Adresse (
    noUsager INTEGER NOT NULL,
    noCiv INTEGER NOT NULL,
    rue VARCHAR2(50) NOT NULL,
    ville VARCHAR2(50) NOT NULL,
    province VARCHAR2(50) NOT NULL,
    codePostal VARCHAR2(7) NOT NULL,
    CONSTRAINT pk_adresse PRIMARY KEY (noUsager),
    CONSTRAINT fk_adresse_usager FOREIGN KEY (noUsager)
        REFERENCES Usager(noUsager)
);
CREATE TABLE Genre (
    typeGenre  VARCHAR2(100),
    CONSTRAINT pk_genre PRIMARY KEY (typeGenre)
);

CREATE TABLE Pays_Production (
    nomPays  VARCHAR2(100),
    CONSTRAINT pk_pays PRIMARY KEY (nomPays)
);

CREATE TABLE Personne (
    noPersonne      NUMBER,
    nom             VARCHAR2(100) NOT NULL,
    dateNaissance   DATE,
    lieuNaissance   VARCHAR2(255),
    photo           VARCHAR2(1000),
    biographie      VARCHAR2(4000),
    CONSTRAINT pk_personne PRIMARY KEY (noPersonne)
);

CREATE TABLE Film (
    noFilm            NUMBER,
    titre             VARCHAR2(255) NOT NULL,
    anneeSortie       NUMBER(4),
    langueOriginale   VARCHAR2(100),
    dureeMinutes      NUMBER(4),
    noRealisateur     NUMBER,
    resumeScenario    VARCHAR2(4000),
    affiche           VARCHAR2(1000),
    CONSTRAINT pk_film PRIMARY KEY (noFilm),
    CONSTRAINT fk_film_realisateur FOREIGN KEY (noRealisateur)
        REFERENCES Personne(noPersonne)
);

CREATE TABLE Film_Genre (
    noFilm     NUMBER,
    typeGenre  VARCHAR2(100),
    CONSTRAINT pk_film_genre PRIMARY KEY (noFilm, typeGenre),
    CONSTRAINT fk_fg_film  FOREIGN KEY (noFilm)    REFERENCES Film(noFilm) ON DELETE CASCADE,
    CONSTRAINT fk_fg_genre FOREIGN KEY (typeGenre) REFERENCES Genre(typeGenre)
);

CREATE TABLE Film_Pays (
    noFilm   NUMBER,
    nomPays  VARCHAR2(100),
    CONSTRAINT pk_film_pays PRIMARY KEY (noFilm, nomPays),
    CONSTRAINT fk_fp_film FOREIGN KEY (noFilm)  REFERENCES Film(noFilm) ON DELETE CASCADE,
    CONSTRAINT fk_fp_pays FOREIGN KEY (nomPays) REFERENCES Pays_Production(nomPays)
);

CREATE TABLE Bandes_Annonces (
    noFilm  NUMBER,
    url     VARCHAR2(1000),
    CONSTRAINT pk_bande_annonce PRIMARY KEY (noFilm, url),
    CONSTRAINT fk_ba_film FOREIGN KEY (noFilm) REFERENCES Film(noFilm) ON DELETE CASCADE
);

CREATE TABLE Film_Acteurs (
    noFilm      NUMBER,
    noPersonne  NUMBER,
    personnage  VARCHAR2(100),
    CONSTRAINT pk_film_acteurs PRIMARY KEY (noFilm, noPersonne, personnage),
    CONSTRAINT fk_fa_film     FOREIGN KEY (noFilm)     REFERENCES Film(noFilm) ON DELETE CASCADE,
    CONSTRAINT fk_fa_personne FOREIGN KEY (noPersonne) REFERENCES Personne(noPersonne)
);

CREATE TABLE Film_Scenariste (
    noFilm         NUMBER,
    nomScenariste  VARCHAR2(200),
    CONSTRAINT pk_film_scenariste PRIMARY KEY (noFilm, nomScenariste),
    CONSTRAINT fk_fs_film     FOREIGN KEY (noFilm)     REFERENCES Film(noFilm) ON DELETE CASCADE
);

CREATE TABLE Copie (
    codeCopie NUMBER GENERATED ALWAYS AS IDENTITY,
    noFilm NUMBER NOT NULL,
    CONSTRAINT pk_copie PRIMARY KEY (codeCopie),
    CONSTRAINT fk_copie_film FOREIGN KEY (noFilm)
        REFERENCES Film(noFilm)
);

CREATE TABLE Pret (
    noPret NUMBER GENERATED ALWAYS AS IDENTITY,
    noUsager NUMBER NOT NULL,
    codeCopie NUMBER NOT NULL,
    dateDebut DATE NOT NULL,
    dateRetour DATE NOT NULL,
    CONSTRAINT pk_pret PRIMARY KEY (noPret),
    CONSTRAINT fk_pret_client FOREIGN KEY (noUsager)
        REFERENCES Client(noUsager),
    CONSTRAINT fk_pret_copie FOREIGN KEY (codeCopie)
        REFERENCES Copie(codeCopie)
);

--- Liste des triggers
CREATE OR REPLACE TRIGGER carte_credit_expiree
BEFORE INSERT OR UPDATE ON carteCredit
FOR EACH ROW
BEGIN
    IF :NEW.expAnnee * 100 + :NEW.expMois
       < TO_NUMBER(TO_CHAR(SYSDATE, 'YYYYMM')) THEN
        RAISE_APPLICATION_ERROR(-20003, 'La carte de crédit est expirée.');
    END IF;
END;
/
CREATE OR REPLACE TRIGGER age_client_18
BEFORE INSERT ON Client
FOR EACH ROW
DECLARE
    v_anniversaire DATE;
BEGIN
    SELECT anniversaire INTO v_anniversaire FROM Usager WHERE noUsager = :NEW.noUsager;
    IF MONTHS_BETWEEN(SYSDATE, v_anniversaire) < 18 * 12 THEN
        RAISE_APPLICATION_ERROR(-20003, 'Le client n''a pas plus de 18 ans');
    END IF;
END;
/
--- Liste des procédures
CREATE OR REPLACE PROCEDURE p_ajouterPersonne (
    p_noPersonne    IN Personne.noPersonne%TYPE,
    p_nom           IN Personne.nom%TYPE,
    p_dateNaissance IN Personne.dateNaissance%TYPE,
    p_lieuNaissance IN Personne.lieuNaissance%TYPE,
    p_photo         IN Personne.photo%TYPE,
    p_biographie    IN Personne.biographie%TYPE
) AS
    v_personneExiste NUMBER;

BEGIN
    SELECT COUNT(*) INTO v_personneExiste FROM Personne WHERE noPersonne = p_noPersonne;

    IF v_personneExiste > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'La personne que vous souhaitez ajouter existe déjà');
    END IF;

    INSERT INTO Personne (noPersonne, nom, dateNaissance, lieuNaissance, photo, biographie)
    VALUES (p_noPersonne,p_nom,p_dateNaissance,p_lieuNaissance,p_photo,p_biographie);
END;
/

CREATE OR REPLACE PROCEDURE p_ajouterClient (
    p_noUsager     IN Usager.noUsager%TYPE,
    p_nom          IN Usager.nom%TYPE,
    p_prenom       IN Usager.prenom%TYPE,
    p_courriel     IN Usager.courriel%TYPE,
    p_tel          IN Usager.tel%TYPE,
    p_anniversaire IN Usager.anniversaire%TYPE,
    p_motDePasse   IN Usager.motDePasse%TYPE,
    p_noCiv        IN Adresse.noCiv%TYPE,
    p_rue          IN Adresse.rue%TYPE,
    p_ville        IN Adresse.ville%TYPE,
    p_province     IN Adresse.province%TYPE,
    p_codePostal   IN Adresse.codePostal%TYPE,
    p_codeForfait  IN Client.codeForfait%TYPE,
    p_typeCarte    IN carteCredit.typeCarte%TYPE,
    p_numCarte     IN carteCredit.numCarte%TYPE,
    p_expMois      IN carteCredit.expMois%TYPE,
    p_expAnnee     IN carteCredit.expAnnee%TYPE,
    p_cvv          IN carteCredit.CVV%TYPE
) AS
    v_clientExiste NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_clientExiste FROM Client WHERE noUsager = p_noUsager;
    
    IF v_clientExiste > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Le client dont vous souhaité ajouter existe déjà');
    END IF;
    
    INSERT INTO Usager (noUsager, nom, prenom, courriel, tel, anniversaire, motDePasse)
    VALUES (p_noUsager, p_nom, p_prenom, p_courriel, p_tel, p_anniversaire, p_motDePasse);

    INSERT INTO Adresse (noUsager, noCiv, rue, ville, province, codePostal)
    VALUES (p_noUsager, p_noCiv, p_rue, p_ville, p_province, p_codePostal);

    INSERT INTO Client (noUsager, codeForfait)
    VALUES (p_noUsager, p_codeForfait);

    INSERT INTO carteCredit (noUsager, typeCarte, numCarte, expMois, expAnnee, CVV)
    VALUES (p_noUsager, p_typeCarte, p_numCarte, p_expMois, p_expAnnee, p_cvv);
END;
/
CREATE OR REPLACE PROCEDURE p_ajouterFilm (
    p_noFilm          IN Film.noFilm%TYPE,
    p_titre           IN Film.titre%TYPE,
    p_anneeSortie     IN Film.anneeSortie%TYPE,
    p_langueOriginale IN Film.langueOriginale%TYPE,
    p_dureeMinutes    IN Film.dureeMinutes%TYPE,
    p_noRealisateur   IN Film.noRealisateur%TYPE,
    p_resumeScenario  IN Film.resumeScenario%TYPE,
    p_affiche         IN Film.affiche%TYPE
) AS
    v_filmExiste        NUMBER;
    --v_realisateurExiste NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_filmExiste
    FROM Film
    WHERE noFilm = p_noFilm;

    IF v_filmExiste > 0 THEN
        RAISE_APPLICATION_ERROR(-20101, 'Un film avec ce numéro existe déjà.');
    END IF;

    --- à Vérifier, mais on accept un champ noRealisateur null dans la table Film
    --SELECT COUNT(*) INTO v_realisateurExiste
    --FROM Personne
    --WHERE noPersonne = p_noRealisateur;

    --IF v_realisateurExiste = 0 THEN
    --   RAISE_APPLICATION_ERROR(-20102, 'Le réalisateur n''existe pas.');
    --END IF;

    INSERT INTO Film (noFilm, titre, anneeSortie, langueOriginale, dureeMinutes,
                      noRealisateur, resumeScenario, affiche)
    VALUES (p_noFilm, p_titre, p_anneeSortie, p_langueOriginale, p_dureeMinutes,
            p_noRealisateur, p_resumeScenario, p_affiche);
    INSERT INTO Copie (noFilm)
    VALUES (p_noFilm);
END;
/
CREATE OR REPLACE PROCEDURE p_ajouterFilmGenre (
    p_noFilm          IN Film_Genre.noFilm%TYPE,
    p_typeGenre       IN Film_Genre.typeGenre%TYPE
) AS
    v_noFilmExist     NUMBER;
    v_typeGenreExist  NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_noFilmExist FROM FILM WHERE noFilm = p_noFilm;
    IF (v_noFilmExist < 1) THEN
        RAISE_APPLICATION_ERROR(-20001, 'Aucun film existe avec ce noFilm');
    END IF;
    SELECT COUNT(*) INTO v_typeGenreExist FROM Genre WHERE typeGenre = p_typeGenre;
    IF (v_typeGenreExist < 1) THEN
        INSERT INTO Genre (typeGenre) VALUES (p_typeGenre);
    END IF;
    INSERT INTO Film_Genre (noFilm, typeGenre)
    VALUES (p_noFilm, p_typeGenre);
END;
/
CREATE OR REPLACE PROCEDURE p_ajouterFilmPays (
    p_noFilm          IN Film_Pays.noFilm%TYPE,
    p_nomPays         IN Film_Pays.nomPays%TYPE
) AS
    v_noFilmExist     NUMBER;
    v_nomPaysExist    NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_noFilmExist FROM FILM WHERE noFilm = p_noFilm;
    IF (v_noFilmExist < 1) THEN
        RAISE_APPLICATION_ERROR(-20001, 'Aucun film existe avec ce noFilm');
    END IF;
    SELECT COUNT(*) INTO v_nomPaysExist FROM Pays_Production WHERE nomPays = p_nomPays;
    IF (v_nomPaysExist < 1) THEN
        INSERT INTO Pays_Production (nomPays) VALUES (p_nomPays);
    END IF;
    INSERT INTO Film_Pays(noFilm, nomPays)
    VALUES (p_noFilm, p_nomPays);
END;
/
CREATE OR REPLACE PROCEDURE p_ajouterBandeAnnonce (
    p_noFilm IN Bandes_Annonces.noFilm%TYPE,
    p_url    IN Bandes_Annonces.url%TYPE
) AS
    v_noFilmExist NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_noFilmExist FROM Film WHERE noFilm = p_noFilm;
    IF v_noFilmExist < 1 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Aucun film existe avec ce noFilm');
    END IF;

    INSERT INTO Bandes_Annonces (noFilm, url)
    VALUES (p_noFilm, p_url);
END;
/
CREATE OR REPLACE PROCEDURE p_ajouterFilmActeurs (
    p_noFilm          IN Film_Acteurs.noFilm%TYPE,
    p_noPersonne      IN Film_Acteurs.noPersonne%TYPE,
    p_personnage      IN Film_Acteurs.personnage%TYPE
) AS
    v_noFilmExist     NUMBER;
    v_noPersonneExist NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_noFilmExist FROM FILM WHERE noFilm = p_noFilm;
    IF (v_noFilmExist < 1) THEN
        RAISE_APPLICATION_ERROR(-20001, 'Aucun film existe avec ce noFilm');
    END IF;
    SELECT COUNT(*) INTO v_noPersonneExist FROM Personne WHERE noPersonne = p_noPersonne;
    IF (v_noPersonneExist < 1) THEN
        RAISE_APPLICATION_ERROR(-20001, 'Aucune persone existe avec ce noPersonne');
    END IF;
    INSERT INTO Film_Acteurs(noFilm, noPersonne, Personnage)
    VALUES (p_noFilm, p_noPersonne, p_personnage);
END;
/
CREATE OR REPLACE PROCEDURE p_ajouterFilmScenariste (
    p_noFilm          IN Film_Scenariste.noFilm%TYPE,
    p_nomScenariste   IN Film_Scenariste.nomScenariste%TYPE
) AS
    v_noFilmExist     NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_noFilmExist FROM FILM WHERE noFilm = p_noFilm;
    IF (v_noFilmExist < 1) THEN
        RAISE_APPLICATION_ERROR(-20001, 'Aucun film existe avec ce noFilm');
    END IF;
    INSERT INTO Film_Scenariste(noFilm, nomScenariste)
    VALUES (p_noFilm, p_nomScenariste);
END;
/
CREATE OR REPLACE PROCEDURE p_location (
    p_noFilm    IN Film.noFilm%TYPe,
   p_noUsager   IN Client.noUsager%TYPE
) AS
    v_clientExist       NUMBER;
    v_filmExist         NUMBER;
    v_codeCopie         Copie.codeCopie%TYPE;
    v_dureeMax          Forfait.dureeMax%TYPE;
    v_locationMax       Forfait.locationMax%TYPE;
    v_locationEnCours      NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_filmExist FROM Film WHERE noFilm = p_noFilm;
    IF (v_filmExist < 1) THEN
        RAISE_APPLICATION_ERROR(-20005, 'Le film à louer n''existe pas');
    END IF;
    SELECT COUNT(*) INTO v_clientExist FROM Client WHERE noUsager = p_noUsager;
    IF (v_clientExist < 1) THEN
        RAISE_APPLICATION_ERROR(-20005, 'Il n''existe pas de client');
    END IF;
    
    SELECT f.locationMax, f.dureeMax INTO v_locationMax, v_dureeMax
    FROM Client c
    JOIN Forfait f ON f.codeForfait = c.codeForfait
    WHERE c.noUsager = p_noUsager;
    
    --- Il y a une petite erreur dans le compte des location en cours, a cause du calcule des dates
    SELECT COUNT(*) INTO v_locationEnCours FROM Pret WHERE noUsager = p_noUsager;
    
    IF (v_locationEnCours >= v_locationMax) THEN
        RAISE_APPLICATION_ERROR(-20100, 'Le client ne peux pas faire plus de locaiton');
    END IF;
    
    INSERT INTO Copie (noFilm)
    VALUES (p_noFilm)
    RETURNING codeCopie INTO v_codeCopie;
    
    INSERT INTO Pret (noUsager, codeCopie, dateDebut, dateRetour)
    VALUES (p_noUsager, v_codeCopie, SYSDATE, SYSDATE + v_dureeMax);
END;
/
INSERT INTO Forfait (codeForfait, nomForfait, prix, locationMax, dureeMax)
VALUES ('D', 'Débutant', 5, 1, 10);

INSERT INTO Forfait (codeForfait, nomForfait, prix, locationMax, dureeMax)
VALUES ('I', 'Intermédiaire', 10, 5, 30);

INSERT INTO Forfait (codeForfait, nomForfait, prix, locationMax, dureeMax)
VALUES ('A', 'Avancé', 15, 10, NULL);

COMMIT;
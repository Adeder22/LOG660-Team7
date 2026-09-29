--- Remove table before creating them
DROP TABLE Pret;
DROP TABLE Copie;
DROP TABLE carteCredit;
DROP TABLE Adresse;
DROP TABLE Client;
DROP TABLE Employe;
DROP TABLE Usager;
DROP TABLE Forfait;

--- Creation de nos tables
--- Note: la colonne tel accepte uniquement le format suivant : 000-000-0000
CREATE TABLE Usager (
    idUsager INTEGER NOT NULL,
    nom VARCHAR2(35) NOT NULL,
    prenom VARCHAR2(35) NOT NULL,
    courriel VARCHAR2(255) NOT NULL UNIQUE,
    tel CHAR(12) NOT NULL,
    anniversaire DATE NOT NULL,
    motDePasse VARCHAR2(64),
    CONSTRAINT pk_usager PRIMARY KEY (idUsager),
    CONSTRAINT format_courriel 
        CHECK (REGEXP_LIKE(courriel, '^[^@ ]+@[^@ ]+\.[^@ ]+$')),
    CONSTRAINT format_tel
        CHECK (REGEXP_LIKE(tel, '^[0-9]{3}-[0-9]{3}-[0-9]{4}$')),
    CONSTRAINT alphaNum_5_motDePasse
        CHECK (REGEXP_LIKE(motDePasse, '^[A-Za-z0-9]{5,}$'))
);

CREATE TABLE Employe (
    idEmploye INTEGER NOT NULL,
    matricule VARCHAR2(7) NOT NULL UNIQUE,
    CONSTRAINT pk_employe PRIMARY KEY (idEmploye),
    CONSTRAINT fk_employe_usager FOREIGN KEY (idEmploye)
        REFERENCES Usager(idUsager)
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
    idUsager INTEGER NOT NULL,
    codeForfait CHAR(1) NOT NULL,
    CONSTRAINT pk_client PRIMARY KEY (idUsager),
    CONSTRAINT fk_client_usager FOREIGN KEY (idUsager)
        REFERENCES Usager(idUsager),
    CONSTRAINT fk_client_forfait FOREIGN KEY (codeForfait)
        REFERENCES Forfait(codeForfait)
);

CREATE TABLE carteCredit (
    idClient INTEGER NOT NULL,
    typeCarte VARCHAR2(20) NOT NULL,
    numCarte VARCHAR2(25) NOT NULL,
    expMois NUMBER(2) NOT NULL,
    expAnnee NUMBER(4) NOT NULL,
    CVV VARCHAR2(4) NOT NULL,
    CONSTRAINT pk_cartecredit PRIMARY KEY (idClient),
    CONSTRAINT fk_cartecredit_client FOREIGN KEY (idClient)
        REFERENCES Client(idUsager),
    CONSTRAINT format_cvv
        CHECK (REGEXP_LIKE(CVV, '^[0-9]{3,4}$'))
);

CREATE TABLE Adresse (
    idUsager INTEGER NOT NULL,
    noCiv INTEGER NOT NULL,
    rue VARCHAR2(50) NOT NULL,
    ville VARCHAR2(50) NOT NULL,
    province VARCHAR2(50) NOT NULL,
    codePostal VARCHAR2(7) NOT NULL,
    CONSTRAINT pk_adresse PRIMARY KEY (idUsager),
    CONSTRAINT fk_adresse_usager FOREIGN KEY (idUsager)
        REFERENCES Usager(IdUsager)
);

CREATE TABLE Copie (
    codeCopie NUMBER NOT NULL,
    noFilm NUMBER NOT NULL,
    CONSTRAINT pk_copie PRIMARY KEY (codeCopie),
    CONSTRAINT fk_copie_film FOREIGN KEY (noFilm)
        REFERENCES Film(noFilm)
);

CREATE TABLE Pret (
    idPret NUMBER NOT NULL,
    idClient NUMBER NOT NULL,
    codeCopie NUMBER NOT NULL,
    dateDebut DATE NOT NULL,
    dateRetour DATE NOT NULL,
    CONSTRAINT pk_pret PRIMARY KEY (idPret),
    CONSTRAINT fk_pret_client FOREIGN KEY (idClient)
        REFERENCES Client(idUsager),
    CONSTRAINT fk_pret_copie FOREIGN KEY (codeCopie)
        REFERENCES Copie(codeCopie)
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
    noFilm      NUMBER,
    noPersonne  NUMBER,
    CONSTRAINT pk_film_scenariste PRIMARY KEY (noFilm, noPersonne),
    CONSTRAINT fk_fs_film     FOREIGN KEY (noFilm)     REFERENCES Film(noFilm) ON DELETE CASCADE,
    CONSTRAINT fk_fs_personne FOREIGN KEY (noPersonne) REFERENCES Personne(noPersonne)
);

--- Liste des triggers
CREATE OR REPLACE TRIGGER carte_credit_expiree
BEFORE UPDATE ON carteCredit
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
    SELECT anniversaire INTO v_anniversaire FROM Usager WHERE idUsager = :NEW.idUsager;
    IF MONTHS_BETWEEN(SYSDATE, v_anniversaire) < 18 * 12 THEN
        RAISE_APPLICATION_ERROR(-20003, 'Le client n''a pas plus de 18 ans');
    END IF;
END;
/
CREATE OR REPLACE PROCEDURE p_ajouterClient (
    p_idUsager     IN Usager.idUsager%TYPE,
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
    SELECT COUNT(*) INTO v_clientExiste FROM Client WHERE idUsager = p_idUsager;
    
    IF v_clientExiste > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Le client dont vous souhaité ajouter existe déjà');
    END IF;
    
    INSERT INTO Usager (idUsager, nom, prenom, courriel, tel, anniversaire, motDePasse)
    VALUES (p_idUsager, p_nom, p_prenom, p_courriel, p_tel, p_anniversaire, p_motDePasse);

    INSERT INTO Adresse (idUsager, noCiv, rue, ville, province, codePostal)
    VALUES (p_idUsager, p_noCiv, p_rue, p_ville, p_province, p_codePostal);

    INSERT INTO Client (idUsager, codeForfait)
    VALUES (p_idUsager, p_codeForfait);

    INSERT INTO carteCredit (idClient, typeCarte, numCarte, expMois, expAnnee, CVV)
    VALUES (p_idUsager, p_typeCarte, p_numCarte, p_expMois, p_expAnnee, p_cvv);
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
    p_affiche         IN Film.affiche%TYPE,
    p_typeGenre       IN Film_Genre.typeGenre%TYPE,
    p_nomPays         IN Film_Pays.nomPays%TYPE,
    p_urlBande        IN Bandes_Annonces.url%TYPE DEFAULT NULL
) AS
    v_filmExiste        NUMBER;
    v_realisateurExiste NUMBER;
    v_genreExiste       NUMBER;
    v_paysExiste        NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_filmExiste
    FROM Film
    WHERE noFilm = p_noFilm;

    IF v_filmExiste > 0 THEN
        RAISE_APPLICATION_ERROR(-20101, 'Un film avec ce numéro existe déjà.');
    END IF;

    SELECT COUNT(*) INTO v_realisateurExiste
    FROM Personne
    WHERE noPersonne = p_noRealisateur;

    IF v_realisateurExiste = 0 THEN
        RAISE_APPLICATION_ERROR(-20102, 'Le réalisateur n''existe pas.');
    END IF;

    SELECT COUNT(*) INTO v_genreExiste
    FROM Genre
    WHERE typeGenre = p_typeGenre;

    IF v_genreExiste = 0 THEN
        INSERT INTO Genre (typeGenre) VALUES (p_typeGenre);
    END IF;

    SELECT COUNT(*) INTO v_paysExiste
    FROM Pays_Production
    WHERE nomPays = p_nomPays;

    IF v_paysExiste = 0 THEN
        INSERT INTO Pays_Production (nomPays) VALUES (p_nomPays);
    END IF;

    INSERT INTO Film (noFilm, titre, anneeSortie, langueOriginale, dureeMinutes,
                      noRealisateur, resumeScenario, affiche)
    VALUES (p_noFilm, p_titre, p_anneeSortie, p_langueOriginale, p_dureeMinutes,
            p_noRealisateur, p_resumeScenario, p_affiche);

    INSERT INTO Film_Genre (noFilm, typeGenre)
    VALUES (p_noFilm, p_typeGenre);

    INSERT INTO Film_Pays (noFilm, nomPays)
    VALUES (p_noFilm, p_nomPays);

    IF p_urlBande IS NOT NULL THEN
        INSERT INTO Bandes_Annonces (noFilm, url)
        VALUES (p_noFilm, p_urlBande);
    END IF;
END;
/
INSERT INTO Forfait (codeForfait, nomForfait, prix, locationMax, dureeMax)
VALUES ('D', 'Débutant', 5, 1, 10);

INSERT INTO Forfait (codeForfait, nomForfait, prix, locationMax, dureeMax)
VALUES ('I', 'Intermédiaire', 10, 5, 30);

INSERT INTO Forfait (codeForfait, nomForfait, prix, locationMax, dureeMax)
VALUES ('A', 'Avancé', 15, 10, NULL);

COMMIT;
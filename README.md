# CY Virtuel
CY Virtuel est le projet noté de développement web de l'année 2023-2024 de CY Tech, réalisé par:
- Gaétan Retel
- Julien Guyot
- Matéo Lopez
- Guillaume Androny
- Ulrich Sautreuil

## Installation

### Cloner le projet

Vous pouvez cloner le projet depuis `git` via la commande suivante:

```sh
git clone https://github.com/na-teag/JEE-project
```

### Installer Maven (Linux)

Si vous avez déjà Maven installé sur votre système, vous pouvez passer cette étape, car vous avez déjà les dépendances
nécessaires.

Maven est l'outil qui permet d'installer les dépendances du projet. Pour fonctionner sur votre ordinateur, Composer
nécessite certaines dépendances.

Pour les installer sous Ubuntu (et distributions similaires), il faut exécuter :

```sh
sudo apt install maven
```

### Environnement

Le projet utilise Hibernate, les variables sont stockées dans le fichier `ressources/META-INF/hibernate.cfg.xml`.

Le projet utilise Mysql, pour l'installer il faudra taper les commandes suivantes :

```sh
sudo apt install mysql-server
sudo mysql_secure_installation
```

Le projet est maintenant prêt à être utilisé.

### Mettre en place la base de données
Pour initialiser la base de données, il vous faut d'abord la créer en tapant

```sh
mysql -u root -p < resetDatabase.sql
```

Le projet dispose d'un fichier pour mettre en place et peupler la base de données, avec des données par défaut.
Pour ce faire, il va falloir compiler le projet et lancer le fichier Main.java :

```php
mvn compile
mvn exec:java -Dexec.mainClass="cyu.schoolmanager.Main"
```

Par défaut, trois utilisateurs sont déjà présents:
- John Doe
	- Admin
    - Nom d'utilisateur `admin`
	- Mot de passe: `admin`
- Alex Smith
	- Professeur
	- Nom d'utilisateur: `prof`
	- Mot de passe: `prof`
- Emma Johnson
	- Elève
	- Nom d'utilisateur: `student`
	- Mot de passe: `student`

### Gestion des mails

Par défaut, les mails sont affichés dans le terminal, et son envoyé sur un serveur SMTP de [DebugMail](https://debugmail.io).

## Lancer le serveur

Pour lancer le projet avec tomcat, on peut utiliser l'interface d'IntelliJ IDEA Ultimate

## Architecture du projet

Le projet utilise Hibernate et Jakarta EE. Les fichiers sont séparés en controller et en services

### Front-end

Le front-end du projet est fait avec le trio HTML/CSS/JS et JSP.

### Back-end

Le back-end du projet est fait en Java
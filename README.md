# TD DAW
## PREPARATION
Lors des préparations, je n'ai eu aucun problème pour l'installation de la librairie et de xampp.
## PREMIERS PAS AVEC JDBC
Après changement du driver, on peut facilement afficher le sport avec une requête SQL.
 ```java  ResultSet results = myStatement.executeQuery("SELECT * FROM sport");
            while(results.next())
            {
                String sport_name = results.getString("name");
                int nb_participant = results.getInt("required_participants");
                sports.add(new Sport(id, sport_name, nb_participant));
                System.out.println(sport_name + ": " + nb_particpant);
            }
```
```console
Badminton (simple): 2
Badminton (double): 4
Basket: 10
````
## Structurons tout cela
### MYSQLDATABASE
La classe MySQLDatabase a pour objectif de garantir qu'il y a eu un seul et unique changement du driver. De plus, cette classe va permettre de générer différentes connexions sur des BDD. L'attribut statique 'driverLoaded' joue un rôle important pour vérifier s'il y a eu un 
changement du driver. 
Le constructeur de la classe prend pour entrer les variables nécessaires pour ouvrir une connexion. Notons de même, que la méthode loadDriver est appelle lors de la création d'un objet MySQLDatabase 
 ```java   MySQLDatabase(String host, int port, String databaseName, String user ,String password )
    {
        this.host = host;
        this.port = port;
        this.DatabaseName = databaseName;
        this.user = user;
        this.password = password;
        this.connection = null;
        this.driverLoaded = false;
        loadDriver();
    }
```


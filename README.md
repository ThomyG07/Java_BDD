# TD DAW
## PREPARATION
Lors des préparations, je n'ai eu aucun problème pour l'installation de la librairie et de xampp.
## PREMIERS PAS AVEC JDBC
Après changement du driver, on peut facilement afficher le sport avec une requête SQL.
 ```java
ResultSet results = myStatement.executeQuery("SELECT * FROM sport");
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
 ```java
MySQLDatabase(String host, int port, String databaseName, String user ,String password )
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
La méthode suivante permet de charger le driver si ce dernier n'est pas déjà chargé. Il y a un problème lors du chargement, une exception sera levée.
 ```java
private static void loadDriver()
    {
        if(driverLoaded == false)
        {
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
                driverLoaded = true;
            }
            catch (Exception e) {
               System.err.println("Erreur load Driver : " + e.getMessage());
            }
             


        }
```
### POLYSPORTSDATABASE
La classe PolySportsDatabase hérite de la classe MySQLDatabase, la classe a pour but de garantir un unique accès à la base de données sport.
 ```java
public class PolySportsDatabase extends MySQLDatabase {
    static private PolySportsDatabase instance = null;

    private PolySportsDatabase()
    {
        super("localhost",3306,"poly_sports", "Toto", "test" );
        this.instance = null;

    }
    public static PolySportsDatabase getInstance() 
    {
        if(instance == null)
        {
            PolySportsDatabase polySportsDatabase = new PolySportsDatabase();
            instance = polySportsDatabase;
            return instance;

        }
        else
        {

            return instance;
        }


    }

}
```
Dans le main() il faut faire appel à l'instance de la classe :
```java
PolySportsDatabase polySportsDatabase = PolySportsDatabase.getInstance();
polySportsDatabase.connect();
```
### SportDAO
La classe SportDAO permet de faire le lien entre l'objet métier sport et la base de données.
 ```java
public  ArrayList<Sport> findAll()
      {
        ArrayList<Sport> sports = new ArrayList<>();
         Statement myStatement = this.database.createStatement();
         try
         {
            ResultSet results = myStatement.executeQuery("SELECT * FROM sport");
            while(results.next())
            {
                String sport_name = results.getString("name");
                int nb_participant = results.getInt("required_participants");
                int id = results.getInt("id");
                sports.add(new Sport(id, sport_name, nb_participant));
                
            }

         }
         catch(SQLException e)
         {
            System.err.println("Erreur SQL selection table : " + e.getMessage());
         }
         return sports;
         
      }
```
La fonction renvoie un tableau d'objet sport.
 ```java
 public static void main(String[] args) throws Exception {
        PolySportsDatabase polySportsDatabase = PolySportsDatabase.getInstance();
        polySportsDatabase.connect();
        SportsDAO sportsDAO = new SportsDAO(polySportsDatabase);
    
        ArrayList<Sport> sports = sportsDAO.findAll();
        for (Sport sport : sports) 
        {
            System.out.println(sport.getName() + ": " + sport.getParticipants());
            
        }
}
```
### FINDBYID
 ```java
 public static void main(String[] args) throws Exception {
        PolySportsDatabase polySportsDatabase = PolySportsDatabase.getInstance();
        polySportsDatabase.connect();
        SportsDAO sportsDAO = new SportsDAO(polySportsDatabase);
        Sport sport = sportsDAO.findById(1);
        System.out.println(sport.getName() + ": " + sport.getParticipants());
}
```

```console
Badminton (simple): 2
```

### FINDBYNAME
 ```java
 System.out.println("Hello, World!"); 
        PolySportsDatabase polySportsDatabase = PolySportsDatabase.getInstance();
        polySportsDatabase.connect();
        SportsDAO sportsDAO = new SportsDAO(polySportsDatabase);
        Scanner myScanner = new Scanner(System.in);
        String input = myScanner.nextLine();
        
        //Sport new_sport = new Sport(4, "football", 10);
        //sportsDAO.delete(4);

        ArrayList<Sport> sports = sportsDAO.findByName(input);
        for (Sport sport : sports) 
        {
            System.out.println(sport.getName() + ": " + sport.getParticipants());
            
        }
```
```console
bad
Badminton (double): 4
Badminton (simple): 2
```
## INJECTION SQL
### ALLOWMULTIQUERIES
La chaîne de caractères suivante a effacé toute la table. :(
```SQL
';␣DELETE␣FROM␣sport␣WHERE␣1;␣--␣

```
### PREPAREDSTATEMENT
Dans les fonctions findById et findByName de la classe SportsDAO, il faut remplacer: 
```java  
Statement myStatement = this.database.createStatement();
````
par 
```java
//findById
 PreparedStatement myStatement = this.database.preparableStatement("SELECT * FROM `sport` WHERE `id`=?")
//Ajouter
 myStatement.setInt(1, id);
 ResultSet results = myStatement.executeQuery();
//

//findByName
 PreparedStatement myStatement = this.database.preparableStatement("SELECT * FROM `sport` WHERE name LIKE ? ORDER BY name");
 String requete = "%" + name +"%"; 
 myStatement.setString(1, requete);
 ResultSet results = myStatement.executeQuery();
//

````

## BONUS DU DEVELOPPEUR
```java 
     public boolean insert(Sport sport)
     {

        PreparedStatement myStatement = this.database.preparableStatement("INSERT INTO sport (id, name, required_participants) VALUES (?,?,?)");
        try
         {

            myStatement.setInt(1, sport.getId());
            myStatement.setString(2, sport.getName());
            myStatement.setInt(3, sport.getParticipants());
            myStatement.executeUpdate();
            return true;

         }
         catch(SQLException e)
         {
            System.err.println("Erreur SQL insertion table : " + e.getMessage());
            return false;
            
         }
         



    }

    public boolean update(int id, Sport sport)
    {

        PreparedStatement myStatement = this.database.preparableStatement("UPDATE sport\r\n" + //
                        "SET name = ?, required_participants = ?\r\n" + //
                        "WHERE id = ?;\r\n" + //
                        "");
        try
         {

            myStatement.setString(1, sport.getName());
            myStatement.setInt(2, sport.getParticipants());
            myStatement.setInt(3, id);
            myStatement.executeUpdate();
            return true;

         }
         catch(SQLException e)
         {
            System.err.println("Erreur SQL update table : " + e.getMessage());
            return false;
            
         }
         



    }
    public boolean delete(int id)
    {

        PreparedStatement myStatement = this.database.preparableStatement("DELETE FROM sport WHERE id = ?");
        try
         {
            myStatement.setInt(1, id);
            myStatement.executeUpdate();
            return true;

         }
         catch(SQLException e)
         {
            System.err.println("Erreur SQL update table : " + e.getMessage());
            return false;
            
         }
         



    }
  



```
Nous pouvons insérer de nouveaux éléments dans BDD :
  ![image](https://github.com/ThomyG07/Java_BDD/assets/93085354/c6a30bdb-7cca-4eda-9148-cdfd835ed82a)

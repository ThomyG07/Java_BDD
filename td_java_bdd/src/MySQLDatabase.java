import static java.lang.StringTemplate.STR;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.xdevapi.PreparableStatement;

public class MySQLDatabase {
    private final String host;
    private final int port;
    private final String DatabaseName;
    private final String user;
    private final String password;
    private Connection connection;
    private static boolean driverLoaded; 

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
    public void connect()
    {
        try {
            String url = "jdbc:mysql://" + this.host + ":"+this.port+"/"+ this.DatabaseName + "?allowMultiQueries=true";
            Connection connection = DriverManager.getConnection(
            url,
            this.user,
            this.password);
            this.connection =connection;
        } catch (Exception e) {
            System.err.println("Erreur connection : " + e.getMessage());
        }
        

    }
    public Statement createStatement()
    {
        try {
            Statement myStatement = this.connection.createStatement();
            return myStatement;
        } 
        catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            return null;
        }
        

    }
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


    } 
    public PreparedStatement preparableStatement(String requete)
    {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = this.connection.prepareStatement(requete);
            return preparedStatement;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
         
    }


    
    

}

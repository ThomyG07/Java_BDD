import java.security.PublicKey;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.x.protobuf.MysqlxCrud.Find;

public class SportsDAO {
    private MySQLDatabase database;
    SportsDAO(MySQLDatabase database)
    {
        this.database = database;
    }
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
    public  Sport findById(int id)
    {
        Sport sport = null;
         PreparedStatement myStatement = this.database.preparableStatement("SELECT * FROM `sport` WHERE `id`=?");
         
         try
         {
            myStatement.setInt(1, id);
            ResultSet results = myStatement.executeQuery();
            while(results.next())
            {
                String sport_name = results.getString("name");
                int nb_participant = results.getInt("required_participants");
                sport = new Sport(id, sport_name, nb_participant);
               
            }

         }
         catch(SQLException e)
         {
            System.err.println("Erreur SQL selection table : " + e.getMessage());
            
         }
         return sport;
         
         
        

    }

    public  ArrayList<Sport> findByName(String name)
    {
        ArrayList<Sport> sports = new ArrayList<>();
        PreparedStatement myStatement = this.database.preparableStatement("SELECT * FROM `sport` WHERE name LIKE ? ORDER BY name");
         try
         {
            String requete = "%" + name +"%"; 
            myStatement.setString(1, requete);
            ResultSet results = myStatement.executeQuery();
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
    

}

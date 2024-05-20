import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!"); 
        PolySportsDatabase polySportsDatabase = PolySportsDatabase.getInstance();
        polySportsDatabase.connect();
        SportsDAO sportsDAO = new SportsDAO(polySportsDatabase);
        Scanner myScanner = new Scanner(System.in);
        String input = myScanner.nextLine();
        
        Sport new_sport = new Sport(4, "football", 10);
        sportsDAO.delete(5);

        ArrayList<Sport> sports = sportsDAO.findByName(input);
        for (Sport sport : sports) 
        {
            System.out.println(sport.getName());
            
        }


        
    }   

    

}

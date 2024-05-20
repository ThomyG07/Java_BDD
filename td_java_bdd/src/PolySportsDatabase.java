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

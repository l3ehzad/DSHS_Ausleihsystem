import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Person {
    private String dshsID;          //P.K.
    private String lastName;
    private String firstName;


    public Person(String dshsID, String lastName, String firstName) {
        this.dshsID = dshsID;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public String getDshsID() {
        return dshsID;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public static void addPersonToSQL(Person person) throws Exception{
        try{
            Connection con = DriverManager.getConnection(Main.url, Main.user, Main.pass);
            PreparedStatement posted = con.prepareStatement("INSERT INTO person (dshsID , lastName, firstName) VALUES ('"+person.getDshsID()+"', '"+person.getLastName()+"', '"+person.getFirstName()+"')");
            posted.executeUpdate();}
        catch (Exception e){
            System.out.println(e);
        } finally {
            System.out.println("Insert completed!");
        }
    }

}


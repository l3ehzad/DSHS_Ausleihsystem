import java.sql.*;
import java.util.*;

public class Inventory {            //to check nr. of availablity
    /*    private static int inventID=0;           //P.K.*/
    private static int inventID;
    private String inventType;
    private int inventSum;


    //CONSTRUCTOR:
    public Inventory(String inventType, int inventSum) {
        try {
            inventID = setInventID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.inventType = inventType;
        this.inventSum = inventSum;
    }

    //get last item of inventID and add +1 to it:
    private static int setInventID() throws Exception{
        try{
            Connection con = DriverManager.getConnection(Main.url, Main.user, Main.pass);
            PreparedStatement statement = con.prepareStatement("SELECT inventID FROM inventory;");

            ResultSet result = statement.executeQuery();
            ArrayList<Integer> array = new ArrayList<Integer>();

            while (result.next()){
                array.add(result.getInt(1));
            }

            inventID = array.get(array.size()-1);
            inventID++;

        } catch (Exception e) {
            System.out.println(e);}
        return inventID;
    }


    public int getInventID() {
        return inventID;
    }

    public String getInventType() {
        return inventType;
    }

    public int getInventSum() {
        return inventSum;
    }

    public static boolean checkInventIdAvailablity(int inventID){
        ArrayList<Integer> array = new ArrayList<Integer>();
        try{
            Connection con = DriverManager.getConnection(Main.url, Main.user, Main.pass);
            PreparedStatement statement = con.prepareStatement("SELECT inventID FROM inventory;");

            ResultSet result = statement.executeQuery();

            while (result.next()){
                array.add(result.getInt(1));
            }

        } catch (Exception e) {
            System.out.println(e);}
        return (array.contains(inventID));
    }

    //define method to edit inventSum!!! XXXX

    @Override
    public String toString() {
        return "Inventory{" +
                "inventType='" + inventType + '\'' +
                ", inventSum=" + inventSum +
                ", inventID=" + inventID +
                '}';
    }
}

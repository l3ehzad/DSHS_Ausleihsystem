import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class BorrowedItem {
    private static int borrID=0;      //P.K.
    private int deviceID;           //F.K. to Device class
    //private int inventID;         //F.K. to Inventory class
    private String dshsID;          //F.K. to Person class
    private String borrowDate;
    private String reason;

    //CONSTRUCTOR
    public BorrowedItem(int deviceID, String dshsID, String borrowDate, String reason) {
        try {
            this.borrID = setBorrID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.deviceID = deviceID;
//      this.inventID = inventID;
        this.dshsID = dshsID;
        this.borrowDate = borrowDate;
        this.reason = reason;
    }
    //end of CONSTRUCTOR

    public int getBorrID() {
        return borrID;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public String getDshsID() {
        return dshsID;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReason() {
        return reason;
    }

    public int getInventID(int deviceID){
        int inventID = 0;
        try{
            Connection con = DriverManager.getConnection(Main.url, Main.user, Main.pass);
            PreparedStatement statement = con.prepareStatement("SELECT inventID FROM device WHERE deviceID = "+ deviceID+";");

            ResultSet result = statement.executeQuery();
            inventID = result.getInt(1);  //XXXXX does it get the value properly? XXX

        } catch (Exception e) {
            System.out.println(e);}
        return inventID;
    }

    public static void addBorrowedItemToSQL(BorrowedItem borrowedItem) throws Exception{
        try{
            Connection con = DriverManager.getConnection(Main.url, Main.user, Main.pass);
            PreparedStatement posted = con.prepareStatement("INSERT INTO borroweditem (borrID, deviceID, inventID, dshsID, borrowDate, reason ) VALUES ('"+borrowedItem.getBorrID()+"', '"+borrowedItem.getDeviceID()+"', '"+borrowedItem.getInventID(borrowedItem.getDeviceID())+"', '"+borrowedItem.getDshsID()+"', '"+borrowedItem.getBorrowDate()+"', '"+borrowedItem.getReason()+"')");
            posted.executeUpdate();}
        catch (Exception e){
            System.out.println(e);
        } finally {
            System.out.println("Insert completed!");
        }
    }

    public static boolean checkDeviceIdAvailablity(int deviceID){
        ArrayList<Integer> array = new ArrayList<Integer>();
        try{
            Connection con = DriverManager.getConnection(Main.url, Main.user, Main.pass);
            PreparedStatement statement = con.prepareStatement("SELECT deviceID FROM device;");

            ResultSet result = statement.executeQuery();

            while (result.next()){
                array.add(result.getInt(1));
            }

        } catch (Exception e) {
            System.out.println(e);}
        return (array.contains(deviceID));
    }


    //get last item of borrowedID and add +1 to it:
    private int setBorrID() throws Exception{
        try{
            Connection con = DriverManager.getConnection(Main.url, Main.user, Main.pass);
            PreparedStatement statement = con.prepareStatement("SELECT borrID FROM borroweditem;");

            ResultSet result = statement.executeQuery();
            ArrayList<Integer> array = new ArrayList<Integer>();

            while (result.next()){
                array.add(result.getInt(1));
            }

            borrID = array.get(array.size()-1);         //does it work for the very first number? =borrID->1?XXX
            borrID++;

        } catch (Exception e) {
            System.out.println(e);}
        return borrID;
    }
}

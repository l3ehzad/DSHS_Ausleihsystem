import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Device {
    private static int deviceID;           //P.K.
    private String deviceName;
    private String labelName;
    private int inventID;           //F.K. Inventory
    private boolean available;

    public Device(String deviceName, String labelName, int inventID) {
        try {
            deviceID = setDeviceID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.deviceName = deviceName;
        this.labelName = labelName;
        this.inventID = inventID;
        this.available = true;
    }

    //get last item of deviceID and add +1 to it:
    private static int setDeviceID() throws Exception{
        try{
            Connection con = DriverManager.getConnection(Main.url, Main.user, Main.pass);
            PreparedStatement statement = con.prepareStatement("SELECT deviceID FROM device;");

            ResultSet result = statement.executeQuery();
            ArrayList<Integer> array = new ArrayList<Integer>();

            while (result.next()){
                array.add(result.getInt(1));
            }

            deviceID = array.get(array.size()-1);
            deviceID++;

        } catch (Exception e) {
            System.out.println(e);}
        return deviceID;
    }



    public int getDeviceID() {
        return deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getLabelName() {
        return labelName;
    }

    public int getInventID() {
        return inventID;
    }

    public boolean isAvailable() {
        return available;
    }




    @Override
    public String toString() {
        return "Device{" +
                "deviceName='" + deviceName + '\'' +
                ", labelName='" + labelName + '\'' +
                ", inventID=" + inventID +
                ", available=" + available +
                ", deviceID=" + deviceID +
                '}';
    }

}

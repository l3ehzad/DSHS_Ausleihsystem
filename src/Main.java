import java.sql.*;
import java.util.*;
import java.util.Date;

public class Main {

    boolean exit;
    public static String url = "jdbc:mysql://localhost:3306/dshs_ausleihsystem";
    public static String user = "root";
    public static String pass = "";

    private void printHeader() {
        System.out.println("--------------------------------");
        System.out.println("|      Ausleihsystemprogram    |");
        System.out.println("|        Menu Application      |");
        System.out.println("--------------------------------");
    }

    private void printMenu(){
        System.out.println("\nMain Menu:");
        System.out.println("1)  Borrow device");
        System.out.println("2)  Return device");
        System.out.println("3)  Add device");
        System.out.println("4)  List of borrowed items");
        System.out.println("5)  List of available items");
        System.out.println("6)  Search (Device or Person)");
        System.out.println("7)  Add inventory item");
        System.out.println("0)  Exit");

    }

    protected void runMenu(){
        printHeader();
        while (!exit){
            printMenu();
            int choice = getInput();
            performAction (choice);
        }
    }

    private int getInput(){
        Scanner sc = new Scanner(System.in);
        int choice = -1;
        while (choice< 0 || choice>6)
            try {
                System.out.println("\nPlease select a number between 1-6:");
                choice = Integer.parseInt(sc.nextLine());
                if (choice>6 || choice<0){
                    System.out.println("Wrong number! Your selected number should be between 1-6.");
                }
            } catch (NumberFormatException e){
                System.out.println("Invalid selection: Please try again.");
            }
        return choice;}

    private void performAction(int choice){
        switch (choice){
            case 1:
                borrowDevice();
                break;
            /*case 2:
                returnDevice();
                break;*/
            case 3:
                addDevice();
                break;
/*            case 4:
                listOfBorrowed();
                break;*/
/*            case 5:
                listOfAvailable();
                break;*/
/*            case 6:
                search();
                break;*/
            case 7:
                addInventory();
                break;
            case 0:
                exit = true;
                System.out.println("Goodbye and have a nice day!");
                break;
            default:
                System.out.println("Sorry! an unknown error has occured.");
        }
    }

    //#1
    private void borrowDevice(){
        System.out.println("\nADD NEW ITEM TO BORROW LIST:");
        String lastName="", firstName="", dshsID="", reason="";
        int deviceID=0;
        String borrowDate = DateStamp.printTime();

        //GETTING PERSON INFORMATION
        System.out.println("Enter last name:");
        Scanner sc1 = new Scanner(System.in);
        try {
            lastName = sc1.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Error with entering last Name.");
        }

        System.out.println("Enter first name:");
        Scanner sc2 = new Scanner(System.in);
        try {
            firstName = sc2.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Error with entering first Name.");
        }

        System.out.println("Enter DSHS ID:");
        Scanner sc3 = new Scanner(System.in);
        try {
            dshsID = sc3.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Error with entering DSHS ID.");
        }

        //ENTER PERSON INFORMATION TO MySQL
        Person person = new Person(dshsID, lastName, firstName);
        try {
            Person.addPersonToSQL(person);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //GETTING DEVICE ID AND CHECK IF IT MATCHES THE DEVICE LIST
        System.out.println("Enter device ID from device list:");
        Scanner sc4 = new Scanner(System.in);
        try {
            deviceID = sc4.nextInt();
            while (!BorrowedItem.checkDeviceIdAvailablity(deviceID)){
                System.out.println("Your entered device ID is invalid. Please check device list and choose an available device ID from the list.");
                System.out.println("Enter device ID from device list:");
                Scanner sc5 = new Scanner(System.in);
                deviceID = sc5.nextInt();
            }
        } catch (InputMismatchException e) {
            System.out.println("Error with entering Device ID.");
        }

        //GET BORROWING REASON
        System.out.println("Enter borrowing reason:");
        Scanner sc5 = new Scanner(System.in);
        try {
            reason = sc5.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Error with entering Borrowing reason.");
        }

        //CREATE OBJECT OF BorrowedItem class:
        BorrowedItem borrowedItem = new BorrowedItem(deviceID, dshsID, borrowDate, reason);

        //POST BorrowedItem OBJECT TO SQL TABLE
        try {
            BorrowedItem.addBorrowedItemToSQL(borrowedItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //#3
    private void addDevice () {
        System.out.println("\nADD NEW ITEM TO DEVICE LIST:");

        String deviceName = null;
        String labelName = null;
        int inventID = 0;
        boolean available;

        System.out.println("Enter device name:");
        Scanner sc1 = new Scanner(System.in);
        try {
            deviceName = sc1.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Error with entering Device Name.");
        }

        System.out.println("Enter label name on device (if available):");
        Scanner sc2 = new Scanner(System.in);
        try {
            labelName = sc2.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Error with entering Label Name.");
        }

//HOW TO CHEECK IF DATA TYPE IS NOT INTEGER AND REJECT IT? XXXXXXX

        System.out.println("Enter inventory ID for category of device:");
        Scanner sc3 = new Scanner(System.in);

        try {
            inventID = sc3.nextInt();
            while (!Inventory.checkInventIdAvailablity(inventID)){
                System.out.println("Invalid input. Please try again.");
                System.out.println("Enter inventory ID for category of device:");
                Scanner sc4 = new Scanner(System.in);
                inventID = sc4.nextInt();
            }

        } catch (InputMismatchException e) {
            System.out.println("Error with entering Label Name.");
        }
        Device device1 = new Device(deviceName, labelName, inventID);
        try {
            postDevice(device1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //SQL Befehl zum INSERT Device
    public static void postDevice (Device device) throws Exception{
        try{
            Connection con = DriverManager.getConnection(url, user, pass);
            PreparedStatement posted = con.prepareStatement("INSERT INTO device (deviceID, deviceName, labelName, inventID) VALUES ('"+device.getDeviceID()+"', '"+device.getDeviceName()+"', '"+device.getLabelName()+"','"+device.getInventID()+"')");
            posted.executeUpdate();}
        catch (Exception e){
            System.out.println(e);
        } finally {
            System.out.println("Insert completed!");
        }
    }

    //#7
    //SQL Befehl zum INSERT Inventory
    public static void addInventory(){
        String inventType=null;
        int inventSum=0;
        System.out.println("ADD INVENTORY ITEM:");
        System.out.println("Enter category of inventory (laptop, cable, etc.):");
        Scanner sc1 = new Scanner(System.in);
        try {
            inventType = sc1.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Error with entering category type.");
        }

        System.out.println("Enter number of available items of the category:");
        Scanner sc2 = new Scanner(System.in);
        try {
            inventSum = sc2.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Error with entering number of category.");
        }

        Inventory inventory = new Inventory(inventType, inventSum);
        try {
            postInventory(inventory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        inventory.toString();

    }

    //SQL Befehl zum INSERT Inventory
    public static void postInventory (Inventory inventory) throws Exception{
        try{
            Connection con = DriverManager.getConnection(url, user, pass);
            PreparedStatement posted = con.prepareStatement("INSERT INTO inventory (inventID , inventType, inventNumber) VALUES ('"+inventory.getInventID()+"', '"+inventory.getInventType()+"', '"+inventory.getInventSum()+"')");
            posted.executeUpdate();}
        catch (Exception e){
            System.out.println(e);
        } finally {
            System.out.println("Insert completed!");
        }
    }



    //Main Programm
    public static void main (String [] args){
        Connection conn = null;

        //setup connection to database
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.print("Database is connected successfully!\n");
        }
        catch(ClassNotFoundException | SQLException e){
            System.out.print("Not connected to DB - Error:"+e);
            //Logger.getLogger(SQLConnection.class.getName()).log(Level.SEVERE, null, e);
        }

        //run main menu of programm
        Main main = new Main();
        main.runMenu();

        //close connection
        try{
            conn.close();
            System.out.println("Connection closed successfully!");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}

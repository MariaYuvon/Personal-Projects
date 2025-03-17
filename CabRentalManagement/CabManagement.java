
import java.beans.Statement;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CabManagement {

    static final String ASSIGNED_DRIVERS = "driver_cab_assignments.csv";
    static final String FILE_PATH = "users.csv";
    static final String TAXIS = "taxi.csv";
    static final String CABS_FILE = "cabs.csv";
    static final String DRIVER_REQUEST_FILE = "driver_requests.csv";
    static final String APPROVED_DRIVER_FILE = "approved_drivers.csv";
    static Scanner getInput;
    private static final int SHIFT = 3;

    public static String encrypt(String text) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : text.toCharArray()) {
            encrypted.append((char) (c + SHIFT));
        }
        return encrypted.toString();
    }

    public static String decrypt(String text) {
        StringBuilder decrypted = new StringBuilder();
        for (char c : text.toCharArray()) {
            decrypted.append((char) (c + SHIFT));
        }
        return decrypted.toString();
   
    }

    public static String passwordHider() {
        Console console = System.console();
        if (console == null) {
            System.out.println("No console available. Enter password in plain text:");
            Scanner scanner = new Scanner(System.in); 
            return scanner.nextLine();
        }
        char[] passArray = console.readPassword("Enter your password:");
        return new String(passArray);
    }

    static void signIn() throws SQLException {
        Scanner getInput = new Scanner(System.in);
        System.out.println("Enter your email to sign in:");
        String mail = getInput.nextLine();
        String password = decrypt(passwordHider());
        // System.out.println(password);
        User user = login(mail, password);
        if (user != null) {
            roleBasedFlow(user);
        } else {
            System.out.println("Login failed!");
        }
    }

    static User login(String mail, String password) {
        try (Connection cn=Database_Connectivity.createConnection()) {
        	String query="Select * from users";
            PreparedStatement st=cn.prepareStatement(query);
        	ResultSet resultSet=st.executeQuery();
            while (resultSet.next()) {
                String[] userData = { String.valueOf(resultSet.getInt(1)),
              resultSet.getString(2),
           	 resultSet.getString(3),
           	 resultSet.getString(4),
           	 resultSet.getString(5),
           	 resultSet.getString(6)};
                String storedMail = resultSet.getString(3);
                String storedPassword = resultSet.getString(5);
                if (storedMail.equals(mail) && storedPassword.equals(password)) {
                    System.out.println("Login successful.");
                    String role = resultSet.getString(6);
                    return createUserBasedOnRole(role, userData);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error reading user data: " + e.getMessage());
        }
        return null;
    }

    public static void displayDrivers() {
        try {

           Connection cn=Database_Connectivity.createConnection();
            String query="select * from approved_driver";
            PreparedStatement statement=cn.prepareStatement(query); 
           ResultSet resultSet= statement.executeQuery();
            List<Object[]> userData = new ArrayList<>();
            while (resultSet.next()) {
                Object[] values = {resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)};
                userData.add(values);
            }

            System.out.printf("%-5s %-12s %-25s %-12s\n",
                    "S.NO", "NAME", "EMAIL","STATUS");
            System.out.println("-----------------------------------------------------------");

            for (Object[] user : userData) {
                    Integer userId =(Integer) user[0];
                    String name =(String) user[1];
                    String email =(String)  user[2];
                    String status=(String) user[3];
                    System.out.printf("%-5s %-12s %-25s %-12s\n",
                            userId, name, email,status);
                }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
    }

   



    static User createUserBasedOnRole(String role, String[] userData) {
        int userId = Integer.parseInt(userData[0]);
        String name = userData[1];
        String email = userData[2];
        String phoneNumber = userData[3];
        String password = userData[4];

        switch (role) {
            case "Customer":
                return new Customer(userId, name, email, phoneNumber, password);
            case "Driver":
                return new Driver(userId, name, email, phoneNumber, password);
            case "Admin":
                return new Admin(userId, name, email, phoneNumber, password);
            default:
                return null;
        }
    }

    static void addDriverRequest(Driver driver) {
        try {
           Connection cn=Database_Connectivity.createConnection();
           String query="Select driver_status from approved_driver where driver_mail=?";
           PreparedStatement statement=cn.prepareStatement(query);
           statement.setString(1, driver.email);
            ResultSet resultSet=  statement.executeQuery();
            while (resultSet.next()) {
                    System.out.println("Driver request already exists for this driver.");
                    return;
                }
            String query2="Insert into approved_driver(driver_name,driver_mail,driver_status) values(?,?,?);";
            PreparedStatement statement2=cn.prepareStatement(query2);
            statement2.setString(1, driver.name);
            statement2.setString(2, driver.email);
            statement2.setString(3, "requested");
            statement2.executeUpdate();
            System.out.println("Driver request added successfully.");          
        } catch (SQLException e) {
            System.out.println("Error saving driver request: " + e.getMessage());
        }
    }

    static boolean driverCheck(Driver driver) {
        try ( Connection cn=Database_Connectivity.createConnection();) {
        	 String query="Select driver_status from approved_driver where driver_mail=?;";
        	 PreparedStatement statement=cn.prepareStatement(query);
             statement.setString(1, driver.email);
             ResultSet resultSet=  statement.executeQuery();
            while (resultSet.next()) {
                    return true;
            }
        } catch (SQLException e) {
            System.out.println("Error reading user data: " + e.getMessage());
        }
        System.out.println("Driver is not approved!");
        return false;
    }

    static void signUp() {
        Scanner getInput = new Scanner(System.in);
        System.out.println("Enter your name:");
        String name = getInput.nextLine();
        String role = "";
        role: while (true) {
            System.out.println("Enter your role\n1)Customer\n2)Driver");
            try {
                byte roleSelect = getInput.nextByte();
                switch (roleSelect) {
                    case 1:
                        role = "Customer";
                        break role;
                    case 2:
                        role = "Driver";
                        break role;
                    // case 3:
                    // role = "Admin";
                    // break role;
                    default:
                        System.out.println("Enter a valid number!");
                }
            } catch (Exception e) {
                System.out.println("Invalid entry!");
                continue;
            }
        }
        getInput.nextLine();
        String email = "";
        do {
            System.out.println("Enter email address:");
            email = getInput.nextLine();
        } while (mailCheck(email));
        System.out.println("Enter phone number:");
        String phoneNumber = getInput.nextLine();
        String password = encrypt(passwordHider());
        User user = User.signUp(name, email, phoneNumber, password, role);
    }

    static void addCabToSystem(User user) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Cab Model:");
        String cabModel = input.nextLine();
        System.out.println("Enter Car Brand:");
        String brand = input.nextLine();
        System.out.println("Enter Car Number (license plate):");
        String carNo = input.nextLine();
        int seatNo = 0;
        while (true) {
            try {
                System.out.println("Enter Number of Seats:");
                seatNo = input.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("Invalid entry");
                continue;
            }
        }
        // input.nextLine();

        if (user instanceof Admin) {
            Admin admin = (Admin) user;
            admin.addCab(cabModel, user.getName(), carNo, seatNo, brand, user.getEmail());
        }
        else if(user instanceof Driver) {
        	Driver driver=(Driver) user;      	
        	 driver.addCab(cabModel, user.getName(), carNo, seatNo, brand, user.getEmail());
        }
        else {
            System.out.println("Invalid role for adding a cab.");
        }
    }

    public static void displayCabs() {
        try {
            Connection cn = Database_Connectivity.createConnection();
            String query = "SELECT * FROM cabs";
            PreparedStatement st = cn.prepareStatement(query);
            ResultSet result = st.executeQuery();
            
            List<Object[]> cabData = new ArrayList<>();
            while (result.next()) {
                Object[] values = {
                    result.getInt(1),  
                    result.getString(2),
                    result.getString(3),
                    result.getString(4), 
                    result.getString(5), 
                    result.getInt(6),   
                    result.getString(7) 
                };
                cabData.add(values);
            }

            System.out.println("+-----+-----------------+------------+----------------------+------------+-------+----------+");
            System.out.printf("| %-3s | %-15s | %-10s | %-20s | %-10s | %-5s | %-8s |\n", 
                              "ID", "OWNER", "CAB TYPE", "CAB NAME", "CAR NO", "SEATS", "STATUS");
            System.out.println("+-----+-----------------+------------+----------------------+------------+-------+----------+");

            for (Object[] cab : cabData) {
                Integer cabId = (Integer) cab[0];
                String owner = (String) cab[1];
                String cabType = (String) cab[2];
                String cabName = (String) cab[3];
                String carNo = (String) cab[4];
                Integer carSeats = (Integer) cab[5];
                String status = (String) cab[6];

                System.out.printf("| %-3s | %-15s | %-10s | %-20s | %-10s | %-5s | %-8s |\n", 
                                  cabId, owner, cabType, cabName, carNo, carSeats, status);
            }

            System.out.println("+-----+-----------------+------------+----------------------+------------+-------+----------+");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    static void viewDriverRequests() {
    	try (Connection cn = Database_Connectivity.createConnection()) {
    	    String query = "SELECT * FROM approved_driver WHERE driver_status = ?";
    	    PreparedStatement statement = cn.prepareStatement(query);
    	    statement.setString(1, "requested");
    	    ResultSet resultSet = statement.executeQuery();

    	    boolean found = false;

    	    while (resultSet.next()) {
    	        found = true;
    	        int userId = resultSet.getInt(1);
    	        String driverName = resultSet.getString(2);
    	        String email = resultSet.getString(3);
    	        Driver driver = new Driver(userId, driverName, email, "", ""); 
    	        System.out.println("Driver " + driverName + " has requested approval.");
    	        Admin.receiveDriverRequest(driver);
    	    }

    	    if (!found) {
    	        System.out.println("No request found.");
    	    }
    	} catch (SQLException e) {
    	    System.out.println("Error reading driver requests: " + e.getMessage());
    	}

    }

    public static void displayUser() {
        try {
             Connection cn=Database_Connectivity.createConnection();
            String query="select * from users";
            PreparedStatement statement=cn.prepareStatement(query);
            ResultSet result=statement.executeQuery();
            List<Object[]> userData = new ArrayList<>();
            while (result.next()) {
                Object[] values = {result.getInt(1),result.getString(2),result.getString(3),result.getString(4),result.getString(5),result.getString(6)};
                userData.add(values);
            }
            System.out.printf("%-5s %-12s %-25s %-15s %-10s\n",
                    "ID", "NAME", "EMAIL", "PHONE","ROLE");
            System.out.println("-----------------------------------------------------------------------------");

            for (Object[] user : userData) {
                Integer userId =(Integer) user[0];
                String name = (String)user[1];
                String email =(String) user[2];
                String phone =(String) user[3];
//                String password =(String) user[4];
                String role = (String) user[5];

                System.out.printf("%-5s %-12s %-25s %-15s %-10s\n",
                        userId, name, email, phone, role);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
    }

//    static void removeDriverRequest(int driverId) {
//        try {
//            File inputFile = new File(DRIVER_REQUEST_FILE);
//            File tempFile = new File("temp_driver_requests.csv");
//
//            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
//            FileWriter writer = new FileWriter(tempFile);
//            String line;
//            boolean driverFound = false;
//            while ((line = reader.readLine()) != null) {
//                String[] requestData = line.split(",");
//                int requestId = Integer.parseInt(requestData[0]);
//                // System.out.println(driverId);
//                if (requestId == driverId) {
//                    driverFound = true;
//                    continue;
//                }
//                writer.write(line + "\n");
//            }
//            reader.close();
//            writer.close();
//            if (driverFound) {
//                if (inputFile.delete()) {
//                    tempFile.renameTo(inputFile);
//                    System.out.println("Driver request removed successfully.");
//                } else {
//                    System.out.println("Error deleting the original file.");
//                }
//            } else {
//                tempFile.delete();
//            }
//        } catch (IOException e) {
//            System.out.println("Error removing driver request: " + e.getMessage());
//        }
//    }

    static boolean mailCheck(String mail) {
        try (Connection cn=Database_Connectivity.createConnection()) {
        	String query="Select driver_mail from approved_driver";
        	PreparedStatement statement=cn.prepareStatement(query);
        	ResultSet resultSet=statement.executeQuery();
            while (resultSet.next()) {
                String storedMail = resultSet.getString(1);
                if (storedMail.equals(mail)) {
                    System.out.println("Email id exists");
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error reading user data: " + e.getMessage());
        }
        return false;
    }

    static void roleBasedFlow(User user) throws SQLException {
        Scanner getInput = new Scanner(System.in);
//        List<Cab> taxis = Booking.createTaxis(ASSIGNED_DRIVERS);
//        Booking.saveTaxisToCSV(taxis, TAXIS);
        if (user.role.equals("Driver")) {
            Driver driver = (Driver) user;
            driver.setUserIdCheck();
            byte optionSelect=0;
            while (true) {
                try{
                System.out.println(
                        "Welcome Driver!\n1) View Assigned rides\n2) Track earnings\n3) Request Driver Approval\n4)Add Cabs\n5)Set status\n6) Log Out");
                System.out.println("Choose options by number:");

                 optionSelect = 0;
                optionSelect = getInput.nextByte();
                }
                catch(Exception e){
                    System.out.println("Invalid Entry!");
                    getInput.nextLine();
                    continue;
                }

                if (!(optionSelect == 3 || optionSelect == 4)) {
                    if (!CabManagement.driverCheck(driver)) {
                        System.out.println("Driver not approved or inactive.");
                        continue;
                    }
                }

                switch (optionSelect) {
                    case 1:
                        driver.displayHistory();
                        break;
                    case 2:
                        driver.earnings();
                        break;
                    case 3:
                        driver.driverStatusRequest();
                        break;
                    case 4:
                    	  CabManagement.addCabToSystem(driver);
                    	  break;
                    case 5:
                    	driver.setStatus();
                    	break;
                    case 6:
                        System.out.println("Logging out...");
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
                if (optionSelect == 6) {
                    break;
                }
           
            }
        } 
        else if (user.role.equals("Admin")) {
            Admin admin = (Admin) user;
            admin.setUserIdCheck();
         
            while (true) {
                byte optionSelect=0;
                try{
                System.out.println(
                        "Welcome Admin!\n1) Add Cabs\n2) Track earnings\n3) Assign Driver \n4) Driver Approval\n5) Driver Management\n6) Cab Management\n7) User Management\n8)Live taxi management\n9) Log Out");
                System.out.println("Choose options by number:");
                 optionSelect = getInput.nextByte();
            }
                catch(Exception e){
                    System.out.println("Invalid entry!");
                    getInput.nextLine();
                    continue;
                   }
                switch (optionSelect) {
                    case 1:
                        CabManagement.addCabToSystem(admin);
                        break;
                    case 2:
                        admin.earnings();
                        break;
                    case 3:
                        System.out.println(admin.assignDriverToCab());
//                        taxis = Booking.createTaxis(ASSIGNED_DRIVERS);
//                        Booking.saveTaxisToCSV(taxis, TAXIS);
                        break;
                    case 4:
                        viewDriverRequests();
                        break;
                    case 5:
                        displayDrivers();
                        break;
                    case 6:
                        displayCabs();
                        break;
                    case 7:
                        displayUser();
                        break;
                    case 8:
                        admin.displayLiveTaxi();
                        break;
                    case 9:
                        System.out.println("Logging out...");
                        break;
                    default:
                        System.out.println("Invalid option.");
                }

                if (optionSelect == 9) {
                    break;
                }
            }
            }
        else if (user.role.equals("Customer")) {
            Customer customer = (Customer) user;
            customer.setUserIdCheck();
            
                while (true) {
                    byte optionSelect = 0;
                    try {
                    System.out.println(
                            "Welcome Customer!\n1) View Available Cabs\n2) Book a Ride\n3) View RideHistory\n4)Ride status update\n5)exit");
                    System.out.println("Choose options by number:");
                    optionSelect = getInput.nextByte();
                    }
                    catch (Exception e) {
                        System.out.println("Enter a valid number!");
                        getInput.nextLine();
                        continue;
                    }

                    switch (optionSelect) {
                        case 1:
                            customer.viewAvailableCabs();
                            break;
                        case 2:
                        String pickupLocation="";
                        String dropLocation="";
                        String pickupPoint="";
                        String dropPoint="";
                        // int pickupTime=0;
                        String timeInput;
                      double timeInHours = 0;
                        while(true){
                        try{
                            pick:while(true){
                            System.out.println("Stops:\n1)Agasthiyar falls\n2)Courtallam Waterfalls\n3)Kasi Viswanathar Temple\n4)Manimuthar Dam\n5)Palaruvi Falls\n6)Panchavarneshwarar Temple");
                            System.out.println("Enter Pickup point (number)");
                            byte select=getInput.nextByte();
                            switch (select){
                                case 1:
                                pickupLocation="Agasthiyar falls";
                                break pick;
                                case 2:
                                pickupLocation="Courtallam Waterfalls";
                                break pick;
                                case 3:
                                pickupLocation="Kasi Viswanathar Temple";
                                break pick;
                                case 4:
                                pickupLocation="Manimuthar Dam";
                                break pick;
                                case 5:
                                pickupLocation="Palaruvi Falls";
                                break pick;
                                case 6:
                                pickupLocation="Panchavarneshvarar Temple";
                                break pick;
                                default:
                                System.out.println("Invalid number!");
                                continue;
                            }
                        }                            
                            pickupPoint=pickupLocation.toLowerCase().trim();
                            pickupLocation=pickupLocation.toLowerCase().trim();
                            System.out.println("Enter Drop point");
                            drop:while(true){
                                System.out.println("Stops:\n1)Agasthiyar falls\n2)Courtallam Waterfalls\n3)Kasi Viswanathar Temple\n4)Manimuthar Dam\n5)Palaruvi Falls\n6)Panchavarneshwarar Temple");
                                System.out.println("Enter Drop point (number)");
                                byte select=getInput.nextByte();
                                switch (select){
                                    case 1:
                                    dropLocation="Agasthiyar falls";
                                    break drop;
                                    case 2:
                                    dropLocation="Courtallam Waterfalls";
                                    break drop;
                                    case 3:
                                    dropLocation="Kasi Viswanathar Temple";
                                    break drop;
                                    case 4:
                                    dropLocation="Manimuthar Dam";
                                    break drop;
                                    case 5:
                                    dropLocation="Palaruvi Falls";
                                    break drop;
                                    case 6:
                                    dropLocation="Panchavarneshvarar Temple";
                                    break drop;
                                    default:
                                    System.out.println("Invalid number!");
                                    continue;
                                }
                            }          
                            dropPoint =dropLocation.toLowerCase().trim();
                            dropLocation=dropLocation.toLowerCase().trim();
                            System.out.println("Enter Pickup time (available only after 6)");
                            while (true) {
                                System.out.println("Enter time in HH:MM format (24-hour clock):");
                                timeInput = getInput.next();
                    
                               try {
                                    if (timeInput.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
                                        String[] parts = timeInput.split(":");
                                        int hours = Integer.parseInt(parts[0]);
                                        int minutes = Integer.parseInt(parts[1]);
                                        timeInHours = hours + (minutes / 60.0);
                                        break; 
                                    } else {
                                        System.out.println("Invalid format! Please enter time as HH:MM (e.g., 09:30 or 18:45).");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Invalid input! Please try again.");
                                }
                            }
                            System.out.println("===========================");
                            System.out.println("Booking Details:");
                            System.out.println("Pickup Location: " + pickupLocation);
                            System.out.println("Drop Point: " + dropLocation+"\n");
                            System.out.println("Pickup Time: " + timeInput);
                            System.out.println("===========================");
                            break;
                        }
                        catch(Exception e){
                        System.out.println("Invalid entry!");
                        continue;
                        }
                    }       System.out.println("===========================");
                            customer.bookRide(pickupPoint, dropPoint, timeInHours,pickupLocation,dropLocation);
                            System.out.println("===========================");
                            break;
                        case 3:
                            customer.displayHistory();
                            break;
                        case 4:
                            customer.journeyStatus();
                            break;
                        case 5:
                            System.out.println("Logging out...");
                            break;
                        default:
                            System.out.println("Invalid option.");
                    }
                    if (optionSelect == 5) {
                        break;
                    }   
        }

        }
    }

    static void exit() {
        System.out.println("Exiting system...");
        System.exit(0);
    }

    public static void main(String[] args) {
        getInput = new Scanner(System.in);
        sign: while (true) {
            try {
                System.out.println("Welcome to Cab Management System!");
                System.out.println("1)Sign In\n2)Sign Up\n3)Exit");
                System.out.println("Choose options by number:");
                byte option = getInput.nextByte();
                switch (option) {
                    case 1:
                        signIn();
                        break;
                    case 2:
                        signUp();
                        break;
                    case 3:
                        System.out.println("Exiting....");
                        break sign;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Caught exception");
            }
        }
    }
}

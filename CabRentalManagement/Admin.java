
import java.util.Scanner;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


class Admin extends User {
    LinkedList<Customer> customerManagement;
    LinkedList<Driver> driverManagement;
    static LinkedList<Cab> cabManagement= new LinkedList<>();;
    double earnings;

    Admin(int userId, String name, String email, String phoneNumber, String password) {
        super(name, email, phoneNumber, password, "Admin");
        
    }
    public void addCab(String cabModel, String ownerName, String carNo, int seatNo, String brand, String mail) {
        Cab newCab = new Cab(cabModel, ownerName, carNo, seatNo, brand);
        cabManagement.add(newCab);

        try (Connection cn=Database_Connectivity.createConnection()) {
        	String query="insert into cabs(cab_owner_mail,car_brand,car_type,car_no,seat_no) values(?,?,?,?,?)";
        	PreparedStatement statement=cn.prepareStatement(query);
        	statement.setString(1, mail);
        	statement.setString(2, cabModel);
        	statement.setString(3, brand);
        	statement.setString(4, carNo);
        	statement.setInt(5, seatNo);
            statement.execute();
            System.out.println("Cab added successfully!");
        } catch (SQLException e) {
            System.out.println("Error saving cab data: " + e.getMessage());
        }
    }

    static void receiveDriverRequest(Driver driver) {
        System.out.println("Admin received a request from Driver " + driver.getName() + " for approval.");
        System.out.println("Do you want to approve the driver? (yes/no)");
        Scanner input = new Scanner(System.in);
        String response = input.nextLine().toLowerCase();

        if (response.equals("yes")) {
            driver.approveDriver();
            System.out.println("Driver approved successfully.");
//            CabManagement.removeDriverRequest(driver.userId);
        } else {
            driver.rejectDriver();
            System.out.println("Driver rejected.");
//            CabManagement.removeDriverRequest(driver.userId);
        }
    }
    String assignDriverToCab() {
        Scanner input = new Scanner(System.in);

        LinkedList<String> assignedDrivers = new LinkedList<>();
        LinkedList<ArrayList<Object>> assignedCabIds = new LinkedList<>();

        try (Connection cn = Database_Connectivity.createConnection()) {
            
            String query = "select driver_mail from approved_driver where driver_status= ?";
            PreparedStatement st = cn.prepareStatement(query);
            st.setString(1, "approved");
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                assignedDrivers.add(resultSet.getString(1));
            }
           

          
            String query1 = "select cab_id, car_type, car_no from cabs where status= ?";
            PreparedStatement st1 = cn.prepareStatement(query1);
            st1.setString(1, "notmapped");
            ResultSet resultSet1 = st1.executeQuery();
            while (resultSet1.next()) {
                ArrayList<Object> cab = new ArrayList<>();
                cab.add(resultSet1.getInt(1));
                cab.add(resultSet1.getString(2));
                cab.add(resultSet1.getString(3));
                assignedCabIds.add(cab);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        LinkedList<String> drivers = assignedDrivers;
        if (drivers.isEmpty()) {
            System.out.println("No available drivers.");
            return "";
        }

        System.out.println("Available Drivers:");
        drivers.forEach(System.out::println);

        System.out.println("Enter Driver ID to assign:");
        String selectedDriverId = input.nextLine();
        String selectedDriver = selectDriver(selectedDriverId, drivers);
        if (selectedDriver == null) {
            System.out.println("Invalid Driver selection.");
            return "";
        }

        LinkedList<ArrayList<Object>> cabs = assignedCabIds;
        assignedCabIds.forEach(a -> System.out.println("=================\nCab_id:" + a.get(0) + "\nCab_model:" + a.get(1) + "\nCab no:" + a.get(2) + "\n================="));
        if (cabs.isEmpty()) {
            System.out.println("No available cabs.");
            return "";
        }

        System.out.println("Available Cabs:");
        cabs.forEach(System.out::println);

        System.out.println("Enter Cab ID to assign:");
        int selectedCabId = input.nextInt();
        Integer selectedCab = selectCab(selectedCabId, assignedCabIds);
        if (selectedCab == null) {
            System.out.println("Invalid Cab selection.");
            return "";
        }

        try {
            Connection cn = Database_Connectivity.createConnection();
            String query = "insert into assigned_cab (cab_Id, driver_mail) values (?, ?)";
            PreparedStatement statement2 = cn.prepareStatement(query);
            statement2.setString(2, selectedDriver);
            statement2.setInt(1, selectedCab);
            statement2.executeUpdate();
            System.out.println("Driver assigned to cab successfully!");
            String updateQuery = "UPDATE cabs SET status = ? WHERE cab_id = ?";
            PreparedStatement updateStatement = cn.prepareStatement(updateQuery);
            updateStatement.setString(1, "mapped");
            updateStatement.setInt(2, selectedCab);
            updateStatement.executeUpdate();

            String updateQuery1 = "UPDATE approved_driver SET status = ? WHERE driver_mail = ?";
            PreparedStatement updateStatement1 = cn.prepareStatement(updateQuery1);
            updateStatement1.setString(1, "mapped");
            updateStatement1.setString(2, selectedDriver);
            updateStatement1.executeUpdate();

//            System.out.println("Cab status updated to 'mapped'.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return "";
    }

    private Integer selectCab(int selectedCabId, LinkedList<ArrayList<Object>> cabs) {
        for (ArrayList<Object> cab : cabs) {
            if (selectedCabId == (Integer) cab.get(0)) {
                return (Integer) cab.get(0);
            }
        }
        System.out.println("Invalid Cab ID or Cab is already assigned!");
        return null;
    }

    private String selectDriver(String selectedDriverId, LinkedList<String> drivers) {
        for (String driver : drivers) {
            if (driver.equals(selectedDriverId)) {
                return driver;
            }
        }
        System.out.println("Invalid Driver ID!");
        return null;
    }

//    private Integer selectCab(int selectedCabId, LinkedList<ArrayList<Object>> cabs) {
//        for (ArrayList<Object> cab : cabs) {
//            if (selectedCabId == (Integer)cab.get(0)) {
//                return (Integer)cab.get(0);
//            }
//        }
//        System.out.println("Invalid Cab ID or Cab is already assigned!");
//        return null;
//    }

//    private int getNextSerialNumber(String fileName) {
//        int serialNo = 1;
//        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                serialNo++;
//            }
//        } catch (IOException e) {
//            System.out.println("Error reading file for serial number: " + e.getMessage());
//        }
//        return serialNo;
//    }
//    private LinkedList<String> readFileAndFilter(String fileName, LinkedList<String> assignedIds, boolean isDriver) {
//        LinkedList<String> list = new LinkedList<>();
//        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] fields = line.split(",");
//                if (fields.length >= (isDriver ? 3 : 5) && !assignedIds.contains(fields[0])) {
//                    list.add(line);
//                    // System.out.println(line);
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("Error reading file " + fileName + ": " + e.getMessage());
//        }
//        return list;
//    }

//    private String selectDriver(String selectedDriverId, LinkedList<String> drivers) {
//        for (String driver : drivers) {
//            if (driver.equals(selectedDriverId)) {
//            	return driver;
//            }     
//         }
//        System.out.println("Invalid Driver ID!");
//        return null;
//    }

    void earnings(){
    	    String query="select earnings from bookings";
    	    String query1="select earnings  from assigned_cab where driver_mail in (select driver_mail from cabs where cab_owner_mail=?)";
    	        try (Connection cn=Database_Connectivity.createConnection()) {
    	        	PreparedStatement statement1 = cn.prepareStatement(query1);
    	        	PreparedStatement statement2 = cn.prepareStatement(query);
    	        	statement1.setString(1, this.email);
    	        	ResultSet set1=statement1.executeQuery();
    	        	ResultSet set2=statement2.executeQuery();
    	        	double earnings1=0;
    	        	double earnings=0;
    	        	while(set1.next()) {
    	       		earnings1=(set1.getDouble(1))*0.2;
    	       	}  
    	        	while(set2.next()) {
    	        		earnings+=(set2.getDouble(1))*0.2;
    	        	}
    	            System.out.println("You have earned:"+earnings1+" rupees as cab owner!!!"+"\nAnd You have earned "+earnings+" rupees as admin");
    	        } catch (SQLException e) {
    	            System.out.println(e.getMessage());
    	        }
    }
       
    public  void  displayLiveTaxi() {
        try {
        	 Connection cn = Database_Connectivity.createConnection();
             String query = "select d.driver_name, c.car_type, ac.cur_stop, ac.earnings, ac.status, ac.freetime from assigned_cab ac inner join cabs c ON ac.cab_Id = c.cab_id inner join approved_driver d ON d.driver_mail = ac.driver_mail";

             PreparedStatement statement = cn.prepareStatement(query);
             ResultSet set = statement.executeQuery();
             System.out.println("+-------------+----------+---------------------------+----------+---------+----------+");
             System.out.println("| Driver Name | Car Type | Current Stop              | Earnings | Status  | FreeTime |");
             System.out.println("+-------------+----------+---------------------------+----------+---------+----------+");

             while (set.next()) {
                 System.out.printf("| %-11s | %-8s | %-25s | %8d | %-7s | %8d |\n",
                     set.getString("driver_name"),
                     set.getString("car_type"),
                     set.getString("cur_stop"),
                     set.getInt("earnings"),
                     set.getString("status"),
                     set.getInt("freetime")
                 );
             }

             System.out.println("+-------------+----------+---------------------------+----------+---------+----------+");
        }
        catch (Exception e) {
        	e.printStackTrace();
		}
    }
}



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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedWriter;

class Customer extends User {
    public static final String RESET = "\033[0m";
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String BLUE = "\033[34m";
    public static final String CYAN = "\033[36m";
    public static final String MAGENTA = "\033[35m";
    public static final String BOLD = "\033[1m";

    public Customer(int userId, String name, String email, String phoneNumber, String password) {
        super(name, email, phoneNumber, password, "Customer");
    }

    // public LinkedList<Ride> getRideHistory() {
    // return rideHistory;
    // }

    // public void setRideHistory( List<String> rideHistory) {
    // this.rideHistory = rideHistory;
    // }

    // public void completeRide(boolean booked, int count) {
    // int num = 0;
    // List<String> fileLines = new ArrayList<>();
    // try (BufferedReader br = new BufferedReader(new FileReader("taxi.csv"))) {
    // String line;
    // br.readLine();
    // while ((line = br.readLine()) != null) {
    // String[] parts = line.split(",");
    // fileLines.add(line);
    // parts[5] = String.valueOf(booked);
    // line = String.join(",", parts);
    // if (num > count) {
    // break;
    // }
    // fileLines.add(line);
    // ++num;
    // }
    // } catch (IOException e) {
    // System.err.println("Error reading file: " + e.getMessage());
    // }
    // try (BufferedWriter bw = new BufferedWriter(new FileWriter("taxi.csv"))) {
    // for (String fileLine : fileLines) {
    // bw.write(fileLine);
    // bw.newLine();
    // }
    // } catch (IOException e) {
    // System.err.println("Error writing file: " + e.getMessage());
    // }
    // }

    void displayHistory() {
    	try(Connection cn=Database_Connectivity.createConnection()){
        	System.out.println("=============Journey Status=======================");
           String query="select b.booking_id, b.status, b.init, b.dest,b.inittime,u.user_name, d.driver_name from bookings b  inner join users u on u.email = b.customer_mail  inner join approved_driver d on d.driver_mail = b.driver_mail  where b.customer_mail =?";
           PreparedStatement statement=cn.prepareStatement(query);
           statement.setString(1,this.email);
           ResultSet set=statement.executeQuery();
           boolean found=true;
           while(set.next()){
        	   found=false;
        	  int bid=set.getInt(1);
        	  String status=set.getString(2);
        	  String init=set.getString(3);
        	  String dest=set.getString(4);
        	  double inittime=set.getDouble(5);
        	  String dname=set.getString(7);
        	  System.out.println("Booking id:"+bid+"\n From:"+init+"\n To:"+dest+"\n Time:"+inittime+"\n driver name:"+dname);
        	  System.out.println("====================================");
           }
           if(found) {
        	   System.out.println("No booking has made");
        	   return;
           }
           System.out.println("====================================");
    }
    	catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    }

  

    void viewAvailableCabs() {
        try (Connection cn=Database_Connectivity.createConnection()) {
            String query=" SELECT c.car_brand AS \"Cab Name\",c.car_type AS \"Cab Model\",d.driver_name AS \"Driver Name\",c.seat_no AS \"Seats\" FROM assigned_cab ac INNER JOIN cabs c ON ac.cab_Id = c.cab_id INNER JOIN approved_driver d ON ac.driver_mail = d.driver_mail";
            PreparedStatement statement=cn.prepareStatement(query);
            ResultSet resultSet=statement.executeQuery();
            int serialNo = 1;
            System.out.println("╔════════════╦════════════╦════════════════╦════════════════════╦════════════╗");
            System.out.printf("║ %-10s ║ %-10s ║ %-16s ║ %-18s ║ %-10s ║\n",
                    "Serial No", "Cab Name", "Cab Model", "Driver Name", "Seats");
            System.out.println("╠════════════╬════════════╬════════════════╬════════════════════╬════════════╣");
            while (resultSet.next()) {
                    String cabName = resultSet.getString(1);
                    String cabModel = resultSet.getString(2);
                    String driverName = resultSet.getString(3);
                    int seats = resultSet.getInt(4);
                    System.out.printf("║ %-10d ║ %-10s ║ %-16s ║ %-18s ║ %-10s ║\n",
                            serialNo, cabName, cabModel, driverName, seats);
                    serialNo++;
                
            }

            System.out.println("╚════════════╩════════════╩════════════════╩════════════════════╩════════════╝");

        } catch (SQLException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    void bookRide(String pickupPoint, String dropPoint, double pickupTime,String pickupLocation,String dropLocation) throws SQLException{
        int customerID = this.userId;
        List<Cab> taxis1 = Booking.createTaxisNew(CabManagement.TAXIS);
        String[] location= {"agasthiyar falls","courtallam waterfalls","kasi viswanathar temple","manimuthar dam","palaruvi falls","panchavarneshvarar temple"};
        if (Arrays.asList(location).contains(pickupPoint.trim().toLowerCase()) && Arrays.asList(location).contains(dropPoint.trim().toLowerCase())) {
        	 List<Cab> freeTaxis = Booking.getFreeTaxis(taxis1, pickupTime,
                     pickupPoint);
             if (freeTaxis.size() == 0) {
                 System.out.println("No Taxi can be alloted. Exitting");
                 return;
             }
            
             Cab bookedCab=freeTaxis.get(0);
             Booking.book(bookedCab,pickupLocation,dropLocation,pickupTime,this.email);        	
        }
        else {
            System.out.println("Valid pickup and drop are \\n" + //
                                "1)Agasthiyar falls\\n" + //
                                "2)Courtallam Waterfalls\\n" + //
                                "3)Kasi Viswanathar Temple\\n" + //
                                "4)Manimuthar Dam\\n" + //
                                "5)Palaruvi Falls\\n" + //
                                "6)Panchavarneshwarar Temple\".Exitting");
            return;
        }
       
    }

    public void journeyStatus() {
    	Scanner scanner=new Scanner(System.in);
    try(Connection cn=Database_Connectivity.createConnection()){
    	System.out.println("=============Journey Status=======================");
       String query="select b.booking_id, b.status, b.init, b.dest,b.inittime,u.user_name, d.driver_name from bookings b  inner join users u on u.email = b.customer_mail  inner join approved_driver d on d.driver_mail = b.driver_mail  where b.customer_mail =? and b.status=?";
       PreparedStatement statement=cn.prepareStatement(query);
       statement.setString(1,this.email);
       statement.setString(2,"in travel");
       ResultSet set=statement.executeQuery();
       boolean found=true;
       ArrayList<Integer> bidArrayList=new ArrayList<Integer>();
       while(set.next()){
    	   found=false;
    	  int bid=set.getInt(1);
    	  String status=set.getString(2);
    	  bidArrayList.add(bid);
    	  String init=set.getString(3);
    	  String dest=set.getString(4);
    	  double inittime=set.getDouble(5);
    	  String dname=set.getString(7);
    	  System.out.println("Booking id:"+bid+"\n From:"+init+"\n To:"+dest+"\n Time:"+inittime+"\n driver name:"+dname);
       }
       if(found) {
    	   System.out.println("No booking has made");
    	   return;
       }
       System.out.println("====================================");
       System.out.println("1)Change status\n2)Cancel");
       int option=scanner.nextInt();
       if(option==2) {
    	   return;
       }
       else if(option==1) {
       System.out.println("Enter the booking id to change status:");
       int bid1=scanner.nextInt();
       if(!(bidArrayList.contains(bid1))){
    	   System.out.println("Invalid entry!");
    	   return;
       }
       String query2="update bookings set status=? where booking_id=?";
       PreparedStatement statement2=cn.prepareStatement(query2);
       System.out.println("1)Completed 2)Booking cancel");
       int option1=scanner.nextInt();
       if(option1==1) {
    	   statement2.setString(1,"completed");
       }
       else if(option1==2) {
    	   statement2.setString(1, "cancelled");
    	   String query3=" select b.earnings , ac.earnings as total from bookings b inner join assigned_cab ac on ac.driver_mail=b.driver_mail where b.booking_id=?";
    	   PreparedStatement statement3=cn.prepareStatement(query3);
    	   statement3.setInt(1, bid1);
    	   ResultSet set2=statement3.executeQuery();
    	   boolean found1=true;
    	   int totalearnings=0;
    	   while(set2.next()) {
    		   found1=false;
    		   totalearnings=set2.getInt(2)-set2.getInt(1);
    	   }
    	   if(found1) {
    		   System.out.println("Error booking id not found.");
    		   return;
    	   }
    	   String query4="Update assigned_cab set earnings=? where driver_mail=(select driver_mail from bookings where booking_id=?)";
    	   PreparedStatement statement4=cn.prepareStatement(query4);
    	   statement4.setDouble(1, totalearnings);
    	   statement4.setInt(2, bid1);
           statement4.execute();    	  
       }
       else {
    	   System.out.println("Invalid option.Exitting");
    	   return;
       }
       statement2.setInt(2, bid1);
       statement2.execute();
       System.out.println("Booking status updated!");
       return;
       }
       else {
    	   System.out.println("Invalid entry. Exitting");
    	   return;
       }
       
    }
    catch (SQLException e) {
		System.out.println("Error in entry.Please try again");
	}
  
       
    }
}

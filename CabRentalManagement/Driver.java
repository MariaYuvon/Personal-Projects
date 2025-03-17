
import java.util.Scanner;
import java.util.LinkedList;
import java.util.List;
import java.util.Date;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;

import java.io.FileWriter;
import java.io.IOException;

 class Driver extends User {
    boolean availabilityStatus;
    double earnings;
    boolean isApproved;
//    static final String APPROVED_DRIVER_FILE = "approved_drivers.csv";


    public Driver(int userId, String name, String email, String phoneNumber, String password) {
        super(name, email, phoneNumber, password, "Driver");
        this.availabilityStatus = true;
        this.isApproved = false;
    }

    public boolean isAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }


    public double getEarnings() {
        return earnings;
    }

    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }


    public boolean isApproved() {
        return isApproved;
    }

    public void driverStatusRequest() {
        System.out.println("Sending request to the admin to approve your driver status...");
        boolean found = false;
        try (Connection cn=Database_Connectivity.createConnection()) {
            String query="Select driver_status from approved_driver where driver_mail=?";
            PreparedStatement statement=cn.prepareStatement(query);
            statement.setString(1, this.email);
          ResultSet resultSet=  statement.executeQuery();
            while (resultSet.next()) {
            	if(resultSet.getString(1)=="Approved")
                    found = true;
                    break; 
                }
            if (!found) {
                CabManagement.addDriverRequest(this); 
            }
        } catch (SQLException e) {
            System.out.println("An unexpected error occurred.");
            e.printStackTrace();
        }
    }

    
    
    
    
    
    
    

    void displayHistory() {
        System.out.println("Your history:");
        System.out.println("===========================");
        try (Connection cn=Database_Connectivity.createConnection()) {
            String query="select b.booking_id, b.status, b.init, b.dest,b.inittime,u.user_name, d.driver_name from bookings b  inner join users u on u.email = b.customer_mail  inner join approved_driver d on d.driver_mail = b.driver_mail  where b.driver_mail = ?";
            PreparedStatement statement=cn.prepareStatement(query);
            statement.setString(1, this.email);
            ResultSet set=statement.executeQuery();
            boolean found=true;
            while (set.next()){
            	found=false;
            	int bid=set.getInt(1);
            	String status=set.getString(2);
            	String init=set.getString(3);
            	String dest=set.getString(4);
            	double inittime=set.getDouble(5);
            	String cusName=set.getString(6);
             System.out.println("============As a driver===============\nB.id:"+bid+"\nFrom:"+init+"\nTo:"+dest+"\nTime:"+inittime+"\nStatus:"+status+"\n==========================="); 
            }
            if(found){
                System.out.println("No assigned rides as driver");
            }
             String query1=" select c.cab_Id, c.car_type, d.driver_name,  b.booking_id, b.status,  b.init, b.dest,  b.inittime from    assigned_cab ac  inner join      cabs c on c.cab_Id = ac.cab_Id  inner join      approved_driver d ON ac.driver_mail = d.driver_mail inner join      bookings b on b.driver_mail = ac.driver_mail where   c.cab_owner_mail =?";
             PreparedStatement statement1=cn.prepareStatement(query1);
             statement1.setString(1, this.email);
             ResultSet set1=statement1.executeQuery();
             boolean found1=true;
             while (set1.next()){
             	found1=false;
             	int cid=set.getInt(1);
             	String car_type=set.getString(2);
             	String driver=set.getString(3);
             	int bid1=set.getInt(4);
             	String status1=set.getString(5);
             	String init1=set.getString(6);
             	String dest1=set.getString(7);
             	double inittime1=set.getDouble(8);
              System.out.println("============As an owner===============\nB.id+"+bid1+"\nDriver Name:"+driver+"\nCar Model:"+car_type+"\nFrom:"+init1+"\nTo:"+dest1+"\nTime:"+inittime1+"\nStatus:"+status1+"\n==========================="); 
            }
            if(found1){
                System.out.println("No assigned rides for your cab");
            }
        }
            
         catch (Exception e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    public void approveDriver() {
        this.isApproved = true;
        this.availabilityStatus = true;
        try (Connection cn=Database_Connectivity.createConnection()) {
            String query = "Update approved_driver set driver_status=? where driver_mail=?;";
            PreparedStatement statement=cn.prepareStatement(query);
            statement.setString(1, "approved");
            statement.setString(2, this.email);
            statement.executeUpdate();	
            System.out.println("Driver " + this.name + " has been approved and is now available for rides.");
        } catch (SQLException e) {
            System.out.println("Error saving approved driver data: " + e.getMessage());
        }
    }

    public void rejectDriver() {
    	 this.isApproved = true;
         this.availabilityStatus = true;
         try (Connection cn=Database_Connectivity.createConnection()) {
             String query = "delete from approved_driver where driver_mail=?;";
             PreparedStatement statement=cn.prepareStatement(query);
             statement.setString(1, this.email);
             statement.executeUpdate();	
             System.out.println("Driver " + this.name + " has been rejected for now.");
         } catch (SQLException e) {
             System.out.println("Error saving approved driver data: " + e.getMessage());
         }

    }
    public void addCab(String cabModel, String ownerName, String carNo, int seatNo, String brand, String mail) {
        Cab newCab = new Cab(cabModel, ownerName, carNo, seatNo, brand);
        Admin.cabManagement.add(newCab);
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
    void earnings(){
    String query="select earnings  from assigned_cab where driver_mail=?";
    String query1="select earnings  from assigned_cab where driver_mail in (select driver_mail from cabs where cab_owner_mail=?)";
        try (Connection cn=Database_Connectivity.createConnection()) {
        	PreparedStatement statement = cn.prepareStatement(query);
        	PreparedStatement statement1 = cn.prepareStatement(query1);
        	statement.setString(1, this.email);
        	statement1.setString(1, this.email);
        	ResultSet set=statement.executeQuery();
        	ResultSet set1=statement1.executeQuery();
        	double earnings1=0;
        	double earnings=0;
        	while(set.next()) {
        		earnings=(set.getDouble(1))*0.6;
        	}
        	while(set1.next()) {
       		earnings1=(set1.getDouble(1))*0.2;
       	}
         
            System.out.println("You have earned:"+earnings+" rupees as driver!!!"+"\nYou have earned:"+earnings1+" rupees as cab owner!!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

	public void setStatus() {
		Scanner getInput=new Scanner(System.in);
		String query="Select status from assigned_cab where driver_mail=? ";
		String query1="update assigned_cab set status=? where driver_mail=? ";
		try(Connection cn=Database_Connectivity.createConnection()){
			PreparedStatement statement=cn.prepareStatement(query);
			statement.setString(1, this.email);
			ResultSet set=statement.executeQuery();
			boolean found=true;
			String status="";
			while(set.next()) {
				found=false;
				status=set.getString(1);
				System.out.println("Current Status:"+status);
			}
			if(found) {
				System.out.println("Error finding status");
				return;
			}
			System.out.println("Enter the option (1) to switch status (2) to exit");
			int option=getInput.nextInt();
			if(option==2) {
				System.out.println("Exitting..");
				return;
			}
			else if(option==1) {
				PreparedStatement statement2=cn.prepareStatement(query1);
				if(status.equals("free")) {
					statement2.setString(1, "engaged");
				}
				else {
					statement2.setString(1, "free");
				}
				statement2.setString(2, this.email);
				statement2.execute();
				System.out.println("Status switched !");
				return;
			}
			else {
				System.out.println("Invalid entry !!");
				return;
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}

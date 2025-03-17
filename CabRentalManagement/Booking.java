import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.time.chrono.IsoChronology;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Booking {
//    public static void bookTaxi(int customerID, char pickupPoint, char dropPoint, double pickupTime,
//            List<Cab> freeTaxis,String pickupLocation,String dropLocation) {
//
//        int min = 999;
//        int distanceBetweenpickUpandDrop = 0;
//        int earning = 0;
//        double nextfreeTime = 0;
//        char nextSpot = 'Z';
//        Cab bookedTaxi = null;
//        String tripDetail = "";
//        String rideHistory = "";
//        String driverHistory = "";
//        String rides="";
//
//        for (Cab t : freeTaxis) {
//            int distanceBetweenCustomerAndTaxi = Math.abs((t.currentSpot - 'A') - (pickupPoint - 'A')) * 13;
//            if (distanceBetweenCustomerAndTaxi < min) {
//                bookedTaxi = t;
//                distanceBetweenpickUpandDrop = Math.abs((dropPoint - 'A') - (pickupPoint - 'A')) * 13;
//                earning = (distanceBetweenpickUpandDrop - 3*5) * 10 + 50;
//                double dropTime = pickupTime + distanceBetweenpickUpandDrop / 13;
//                nextfreeTime = dropTime;
//                nextSpot = dropPoint;
//                int rideId=rideIdCheck();
//                tripDetail = customerID + "," + bookedTaxi.cabId + "," + pickupPoint + ","
//                        + dropPoint + "," + pickupTime + "," + dropTime + "," + earning + "," + bookedTaxi.driverId+","+bookedTaxi.driver+","+bookedTaxi.ownerMail;
//                rideHistory = customerID + "," + "You have taken a trip from " + pickupLocation + " to " + dropLocation
//                        + "in cab " + bookedTaxi.cabName + " on " + pickupTime + "hrs and paid RS." + earning
//                        + " to driver " + bookedTaxi.driver+","+"Ongoing"+","+rideId+","+earning;
//                rides=  customerID + "," + "Ride from " + pickupLocation + " to " + dropLocation
//                + " in cab " + bookedTaxi.cabName + " on " + pickupTime + "hrs and it costs RS." + earning
//                + " to driver " + bookedTaxi.driver+","+rideId;
//                driverHistory = customerID+ "," + "Assigned a trip from " + pickupLocation + " to " + dropLocation
//                        + " in cab " + bookedTaxi.cabName + " on " + pickupTime + "hrs and got RS." + earning+","+rideId+","+earning+","+"Ongoing"+","+bookedTaxi.ownerMail;
//                writeDriverHistoryToFile(driverHistory, bookedTaxi.driverId);
//                String earnfileName = "earnings.csv";
//                String earningDetails = bookedTaxi.driverId+ "," + bookedTaxi.ownerMail + "," + earning;
//                try (FileWriter writer = new FileWriter(earnfileName, true)) {
//                    writer.write(earningDetails + "\n");
//                } catch (IOException e) {
//                    System.err.println("Error writing to file: " + e.getMessage());
//                }
//
//                min = distanceBetweenCustomerAndTaxi;
//            }
//        }
//
//        if (bookedTaxi != null) {
//            bookedTaxi.setDetails(true, nextSpot, nextfreeTime, bookedTaxi.totalEarnings + earning, tripDetail,
//                    bookedTaxi.cabId);
//            System.out.println("Taxi " + bookedTaxi.cabName + " booked and your driver is "+bookedTaxi.driver);
//            writeHistoryToFile(rideHistory);
//            writeBookingToFile(tripDetail);
//            // writeOngoingRides(rides,bookedTaxi.driverId);
//            writeOngoingRides(rides);
//        } else {
//            System.out.println("No available taxi for the requested time and location.");
//        }
//    }

//    private static void writeHistoryToFile(String tripDetail) {
//        String fileName = "rideHistory.csv";
//
//        try (FileWriter writer = new FileWriter(fileName, true)) {
//            writer.write(tripDetail + "\n");
//        } catch (IOException e) {
//            System.err.println("Error writing to file: " + e.getMessage());
//        }
//    }
//    static int rideIdCheck() {
//        try (BufferedReader reader = new BufferedReader(new FileReader("rideHistory.csv"))) {
//            String line;
//            int storedId = 1;
//            while ((line = reader.readLine()) != null) {
//                String[] userData = line.split(",");
//                storedId = Integer.parseInt(userData[3]);
//            }
//            return ++storedId;
//        } catch (IOException e) {
//            System.out.println("Error reading user data: " + e.getMessage());
//        }
//        return 0;
//    }
//
//    private static void writeDriverHistoryToFile(String tripDetail, String driverId) {
//        String fileName = "driverHistory.csv";
//        try (FileWriter writer = new FileWriter(fileName, true)) {
//            writer.write(driverId+","+tripDetail+ "\n");
//        } catch (IOException e) {
//            System.err.println("Error writing to file: " + e.getMessage());
//        }
//    }
    // private static void writeOngoingRides(String tripDetail, String driverId) {
    //     String fileName = "ongoingHistory.csv";
    //     try (FileWriter writer = new FileWriter(fileName, true)) {
    //         writer.write(driverId+","+tripDetail+ "\n");
    //     } catch (IOException e) {
    //         System.err.println("Error writing to file: " + e.getMessage());
    //     }
    // }
//    private static void writeOngoingRides(String tripDetail) {
//        String fileName = "ongoingHistory.csv";
//        try (FileWriter writer = new FileWriter(fileName, true)) {
//            writer.write(tripDetail+ "\n");
//        } catch (IOException e) {
//            System.err.println("Error writing to file: " + e.getMessage());
//        }
//    }
//
//    private static void writeBookingToFile(String tripDetail) {
//        String fileName = "bookings.csv";
//
//        try (FileWriter writer = new FileWriter(fileName, true)) {
//            writer.write(tripDetail + "\n");
//        } catch (IOException e) {
//            System.err.println("Error writing to file: " + e.getMessage());
//        }
//    }


//    public static List<Cab> createTaxis(String csvFilePath) {
//        List<Cab> taxis = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] values = line.split(",");
//                int cabId = Integer.parseInt(values[0].trim());
//                String cabName = values[1].trim();
//                int seats = Integer.parseInt(values[4].trim());
//                String driver = values[7].trim();
//                String driverId=values[9].trim();
//                String status = values[8].trim();
//                String ownerMail =values[6].trim();
//                Cab taxi = new Cab( cabId, cabName, seats, driver, status,ownerMail,driverId);
//                taxis.add(taxi);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return taxis;
//    }

    public static List<Cab> createTaxisNew(String csvFilePath) {
        List<Cab> taxis = new ArrayList<>();
        int count = 0;
        try (Connection cn=Database_Connectivity.createConnection()) {
            String query="SELECT ac.cab_Id, c.car_type, c.seat_no, ad.driver_name, ac.driver_mail, c.cab_owner_mail, ac.earnings, ac.cur_stop, ac.freetime FROM assigned_cab ac INNER JOIN cabs c ON ac.cab_Id = c.cab_id INNER JOIN approved_driver ad ON ad.driver_mail = ac.driver_mail";
            PreparedStatement statement=cn.prepareStatement(query);
           ResultSet resultSet= statement.executeQuery();
            
 
            while (resultSet.next()) {
                int cabId = resultSet.getInt(1);
                String cabName = resultSet.getString(2);
                int seats = resultSet.getInt(3);
                String driverName = resultSet.getString(4);
                String driverMail=resultSet.getString(5);
                String currentSpot = resultSet.getString(8);
                double freeTime =resultSet.getDouble(9);
                double earnings = resultSet.getDouble(7);
                String ownerMail=resultSet.getString(6);
                Cab taxi = new Cab(cabId, cabName, seats, driverMail,driverName, currentSpot, freeTime,
                        earnings,ownerMail);
                taxis.add(taxi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taxis;
    }

//    public static void saveTaxisToCSV(List<Cab> taxis, String csvFilePath) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
//            writer.write("Id,CabName,Seats,Driver,Status,Booked,CurrentSpot,FreeTime,TotalEarnings,Trips\n");
//            for (Cab taxi : taxis) {
//                writer.write(
//                        taxi.getCabId() + "," +
//                                taxi.getCabName() + "," +
//                                taxi.getSeats() + "," +
//                                taxi.getDriverId() + "," +
//                                taxi.getDriver() + "," +
//                                taxi.getStatus() + "," +
//                                taxi.isBooked() + "," +
//                                taxi.getCurrentSpot() + "," +
//                                taxi.getFreeTime() + "," +
//                                taxi.getTotalEarnings() + "," +
//                                taxi.getOwnerMail() + "," +
//                                String.join(";", taxi.getTrips()) + "\n");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    static void book(Cab chosen,String pick,String drop,double pickupTime,String email)throws SQLException {
    	String[] location= {"agasthiyar falls","courtallam waterfalls","kasi viswanathar temple","manimuthar dam","palaruvi falls","panchavarneshvarar temple"};
    	int max=Math.max(Arrays.asList(location).indexOf(pick), Arrays.asList(location).indexOf(drop));
    	int min=Math.min(Arrays.asList(location).indexOf(pick), Arrays.asList(location).indexOf(drop));
    	int distance=(max-min)*10;
    	double earnings1=(distance*11.5);
    	double earnings=(distance*11.5)+chosen.earnings;
    	double freeTime=pickupTime+(distance%3);
    	Connection cn=Database_Connectivity.createConnection();
    	int cabId=chosen.cabId;
    	String query="update assigned_cab set cur_stop=?,earnings=?,status=?,freetime=? where driver_mail=?";
    	PreparedStatement statement=cn.prepareStatement(query);
    	statement.setString(1, drop);
    	statement.setDouble(2, earnings);
    	statement.setString(3, "engaged");
    	statement.setDouble(4, freeTime);
    	statement.setString(5, chosen.driverMail);
    	statement.execute();
    	System.out.println("======================\nDriver "+chosen.driver+" is assigned for you with car "+chosen.cabName+"\n======================\nCost for the ride:"+earnings1+"\n======================");
    	
    	//
    	String query1="insert into bookings(driver_mail,init,dest,inittime,customer_mail,earnings) values(?,?,?,?,?,?)";
    	PreparedStatement statement2=cn.prepareStatement(query1);
    	statement2.setString(1, chosen.driverMail);
    	statement2.setString(2, pick);
    	statement2.setString(3, drop);
    	statement2.setDouble(4, pickupTime);
    	statement2.setString(5, email);
    	statement2.setDouble(6, earnings1);
    	statement2.execute();
    	
    }
    public static List<Cab> getFreeTaxis(List<Cab> taxis, double pickupTime, String pickupPoint) {
        List<Cab> freeTaxis = new ArrayList<Cab>();
        String[] locations= {"agasthiyar falls","courtallam waterfalls","kasi viswanathar temple","manimuthar dam","palaruvi falls","panchavarneshvarar temple"};
        int des=Arrays.asList(locations).indexOf(pickupPoint);
        for(int i=0;i<=locations.length;i++) {
        	String loc="";
        	if(i==0) {
        		loc=locations[des];
        		   for (Cab t : taxis){
        	            if (t.freeTime <= pickupTime && t.currentSpot.equals(loc.toLowerCase().trim()) )
        	                freeTaxis.add(t);
        	        }
        	}
        	else {
        	if(des-i>=0) {
        		loc=locations[des-i];
        		   for (Cab t : taxis){
       	            if (t.freeTime <= pickupTime && t.currentSpot.equals(loc.toLowerCase().trim()) )
       	                freeTaxis.add(t);
       	        }
        	}
        	if(des+i<locations.length) {
        		loc=locations[des+i];
        		   for (Cab t : taxis){
       	            if (t.freeTime <= pickupTime && t.currentSpot.equals(loc.toLowerCase().trim()) )
       	                freeTaxis.add(t);
       	        }
        	}
        	if(des-i<0 || des+i>=locations.length) {
        		break;
        	}
        	}
     
        }
        Collections.sort(freeTaxis, (a, b) -> {
            if (a.currentSpot.equals(b.currentSpot)) {
                return Double.compare(a.earnings, b.earnings); 
            }
            return 0;
        });
        return freeTaxis;
    }
}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Cab{
    int cabId;
    String cabModel;
    String ownerName;
    String carNo;
    int seatNo;
    String brand;
    boolean availability;
    String location;
    boolean booked;
//    char currentSpot;
    double freeTime;
    int totalEarnings;
    List<String> trips;
    String cabName;
    int seats;
    String driver;
    String status;
    String ownerMail;
    String driverId;
    String currentSpot;
    String driverMail;
    double earnings;
    

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

//    public char getCurrentSpot() {
//        return currentSpot;
//    }
//
//    public void setCurrentSpot(char currentSpot) {
//        this.currentSpot = currentSpot;
//    }

    public double getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(double freeTime) {
        this.freeTime = freeTime;
    }
   
    public int getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(int totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public List<String> getTrips() {
        return trips;
    }

    public void setTrips(List<String> trips) {
        this.trips = trips;
    }

    public String getCabName() {
        return cabName;
    }

    public void setCabName(String cabName) {
        this.cabName = cabName;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCabId() {
        return cabId;
    }

    public void setCabId(int cabId) {
        this.cabId = cabId;
    }

    public String getCabModel() {
        return cabModel;
    }

    public void setCabModel(String cabModel) {
        this.cabModel = cabModel;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    static int count = cabIdCheck();

    static int cabIdCheck() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CabManagement.CABS_FILE))) {
            String line;
            String storedId = "";
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                storedId = userData[0];
            }
            return Integer.parseInt(storedId);
        } catch (IOException e) {
            System.out.println("Error reading user data: " + e.getMessage());
        }
        return 0;
    }

    public Cab(String cabModel, String ownerName, String carNo, int seatNo, String brand) {
        this.cabId = ++count;
        this.cabModel = cabModel;
        this.ownerName = ownerName;
        this.carNo = carNo;
        this.seatNo = seatNo;
        this.brand = brand;
        this.availability = true;
    }

    public Cab(int cabId,String cabName,int seats,String driverMail,String driverName,String currentSpot,double freeTime,
            double earnings,String ownerMail) {
        this.cabId = cabId;
        this.cabName = cabName;
        this.ownerMail = ownerMail;
        this.driverMail=driverMail;
        this.seats = seats;
        this.driver=driverName;
        this.currentSpot=currentSpot;
        this.earnings=earnings;
        this.freeTime=freeTime;
      
        
    }

    void updateAvailability(boolean availabilityStatus) {
        this.availability = availabilityStatus;
    }
    String getOwnerMail(){
return this.ownerMail;
    }
}

    //
//   public  Cab(int cabId, String cabName,int seats,String driver,String status,String ownerMail,String driverId){
//    this.cabName = cabName;
//    this.cabId = cabId;
//    this.seats = seats;
//    this.driver = driver;
//    this.status = status;
//    booked = false;
//    currentSpot = 'A';
//    freeTime = 6;
//    totalEarnings = 0;
//    trips = new ArrayList<String>();
//    this.ownerMail=ownerMail;
//    this.driverId=driverId;
//    }
//


//    public Cab(int cabId, String cabName, int seats, String driverId, String driverName, String availability, boolean booked,
//            char currentSpot, double freeTime, int totalEarnings,String ownerMail) {
//        this.cabName = cabName;
//        this.cabId = cabId;
//        this.seats = seats;
//        this.driverId=driverId;
//        this.driver = driverName;
//        this.status = availability;
//        this.booked = booked;
//        this.currentSpot = currentSpot;
//        this.freeTime = freeTime;
//        this.totalEarnings = totalEarnings;
//        this.trips = new ArrayList<String>();
//        this.ownerMail=ownerMail;
//    }

    //
//    private static final String FILE_NAME = "taxi.csv";

//    public void setDetails(boolean booked, char currentSpot, double freeTime, int totalEarnings, String tripDetail,
//            int cabId) {
//        this.booked = booked;
//        this.currentSpot = currentSpot;
//        this.freeTime = freeTime;
//        this.totalEarnings = totalEarnings;
//        this.trips.add(tripDetail);
//
//        List<String> fileLines = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts[0].equals(String.valueOf(cabId))) {
//                    parts[6] = String.valueOf(this.booked);
//                    parts[7] = String.valueOf(this.currentSpot);
//                    parts[8] = String.valueOf(this.freeTime);
//                    parts[9] = String.valueOf(this.totalEarnings);
//
//                    line = String.join(",", parts);
//                }
//                fileLines.add(line);
//            }
//        } catch (IOException e) {
//            System.err.println("Error reading file: " + e.getMessage());
//        }
//
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
//            for (String fileLine : fileLines) {
//                bw.write(fileLine);
//                bw.newLine();
//            }
//        } catch (IOException e) {
//            System.err.println("Error writing file: " + e.getMessage());
//        }
//    }
//
//}
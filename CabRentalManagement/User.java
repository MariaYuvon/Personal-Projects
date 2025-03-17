

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

import hotel_Booking_Sytem.Database_Connectivity;

import java.util.stream.Collectors;
import java.util.NoSuchElementException;

public class User {
    int userId;
    String name;
    String email;
    String phoneNumber;
    String password;
    String role;
//    static int count = userIdCheck();
//    static final String USER_PATH = "users.csv";

//    static int userIdCheck() {
//        try (BufferedReader reader = new BufferedReader(new FileReader(USER_PATH))) {
//            String line;
//            String storedId = "";
//            while ((line = reader.readLine()) != null) {
//                String[] userData = line.split(",");
//                storedId = userData[0];
//            }
//            return Integer.parseInt(storedId);
//        } catch (IOException e) {
//            System.out.println("Error reading user data: " + e.getMessage());
//        }
//        return 0;
//    }

    public User(String name, String email, String phoneNumber, String password, String role) {
//        this.userId = ++count;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;

    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    static User signUp(String name, String email, String phoneNumber, String password, String role) {
        User user = new User(name, email, phoneNumber, password, role);
        createAccount(user);
        return user;
    }

    private static void createAccount(User user) {
        try (Connection cn=Database_Connectivity.createConnection()) {
        	String query="Insert into users (user_name,email,phone_number,password,role)values(?,?,?,?,?)";
        	 PreparedStatement statement=cn.prepareStatement(query);
        	 statement.setString(1, user.name);
        	 statement.setString(2, user.email);
        	 statement.setString(3, user.phoneNumber);
        	 statement.setString(4, user.password);
        	 statement.setString(5, user.role);
            statement.executeUpdate();
            System.out.println("User saved successfully.");
        } catch (SQLException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }
    void setUserIdCheck(){
        try (BufferedReader reader = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] requestData = line.split(",");
                if(requestData[2].equals(this.email)){
                 this.setUserId(Integer.parseInt(requestData[0]));
                 return;
            }
            else{
                continue;
            }
        }
        }
        catch (IOException e) {
            System.out.println("Error reading driver requests: " + e.getMessage());
        }
    }
    static boolean login(String mail, String password) {
        try (Connection cn=Database_Connectivity.createConnection()) {
        String query="Select * from users";
        PreparedStatement st=cn.prepareStatement(query);
    	ResultSet resultSet=st.executeQuery();
    	System.out.println("n");
    	while(resultSet.next()) {
    		System.out.println(resultSet.getString(3));
    		 String storedMail = resultSet.getString(3);
             String storedPassword = resultSet.getString(5);
             if (storedMail.equals(mail) && storedPassword.equals(password)) {
                 System.out.println("Login successful.");
                 return true;
             }
		}
              }
       catch (SQLException e) {
            System.out.println("Error reading user data: " + e.getMessage());
        }
        System.out.println("Invalid login credentials.");
        return false;
    }
}


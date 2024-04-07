package HospitalManagementSystem;

import java.sql.Connection;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;



public class Patient {
    private final Connection connection;
    private final Scanner sc;

    public Patient(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }
    
    public void addPatient(){
        System.out.print("Enter Patient Name: ");
        String name = sc.next();

        System.out.print("Enter Patient's Age: ");
        int age = sc.nextInt();

        System.out.print("Enter Patient's Gender: ");
        String gender = sc.next();

        try{
            String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                System.out.println("Patient Added Successfully!!");
            } else{
                System.out.println("Failed To Add Patient!!");
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatients(){
        String query = "SELECT * FROM patients";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Patients: ");
            System.out.println("+------------+--------------------+----------+-------------+");
            System.out.println("| Patient ID |        Name        |   Age    |    Gender   |");
            System.out.println("+------------+--------------------+----------+-------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");

                System.out.printf("|%-12s|%-20s|%-10s|%-13s|\n", id, name, age, gender);
                System.out.println("+------------+--------------------+----------+-------------+");

            }

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean checkPatientById(int id){
        String query = "SELECT * FROM patients WHERE id = ?";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }
    
}

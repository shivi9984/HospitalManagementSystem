package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;


import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class HospitalManagement {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Shivam@9984";

    public static void main(String[] args) {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }


        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner sc = new Scanner(System.in);

            Patient patient = new Patient(connection, sc);
            Doctor doctor = new Doctor(connection);

            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");

                System.out.println("Enter Your Choice: ");
                int choice = sc.nextInt();
                switch(choice){
                    case 1:
                        // Add Patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        // View Patient
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        // View Doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        // Book Appointment
                        bookAppointment(patient, doctor, connection, sc);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("THANK YOU!!");
                        return;
                    default: 
                        System.out.println("Enter Valid Choice.");
                        break;
                }
            }


        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner sc){
        System.out.println("Enter Patient ID: ");
        int patientId = sc.nextInt();
        System.out.println("Enter Doctor ID: ");
        int doctorId = sc.nextInt();
        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
        String appointmentDate = sc.next();

        if(patient.checkPatientById(patientId) && doctor.checkDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId, appointmentDate, connection)){
                String appointmentQuery = "INSERT INTO appointments(patient_id, doctors_id, appointment_date) VALUES(?, ?, ?)";

                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected > 0){
                        System.out.println("Appointments Booked.");
                    }
                    else{
                        System.out.println("Failed To Book Appointment!!!");
                    }


                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Doctor Is Not Available On This Date!!!");
            }
        }
        else{
            System.out.println("Either Doctor OR Patient Doesn't Exist!!!");
        }

    }

    public static boolean checkDoctorAvailability(int doctor_id, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE doctors_id = ? AND appointment_date = ?";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctor_id);
            preparedStatement.setString(2, appointmentDate);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                return count == 0;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }
}


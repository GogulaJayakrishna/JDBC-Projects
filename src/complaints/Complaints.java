package complaints;

import java.sql.*;
import java.util.Scanner;

public class Complaints {

    static final String url = "jdbc:mysql://localhost:3306/complaint_db";
    static final String user = "root";
    static final String pass = "Deepthi@123";

    static Connection con;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(url, user, pass);

            System.out.println("Database Connected Successfully");

            int choice;

            do {

                System.out.println("\n===== Complaint Management System =====");

                System.out.println("1. User Registration");
                System.out.println("2. View Users");
                System.out.println("3. Register Complaint");
                System.out.println("4. View Complaints");
                System.out.println("5. Assign Officer");
                System.out.println("6. View Resolution");
                System.out.println("7. Update Complaint");
                System.out.println("8. Delete Complaint");
                System.out.println("9. Complaint Details");
                System.out.println("10. Officer Report");
                System.out.println("11. Complaint Statistics");
                System.out.println("12. Pending Complaints Report");
                System.out.println("13. Exit");

                System.out.print("Enter Choice : ");
                choice = sc.nextInt();

                switch(choice){

                    case 1:
                        registerUser();
                        break;

                    case 2:
                        viewUsers();
                        break;

                    case 3:
                        registerComplaint();
                        break;

                    case 4:
                        viewComplaints();
                        break;

                    case 5:
                        assignOfficer();
                        break;

                    case 6:
                        viewResolution();
                        break;

                    case 7:
                        updateComplaint();
                        break;

                    case 8:
                        deleteComplaint();
                        break;

                    case 9:
                        complaintDetails();
                        break;

                    case 10:
                        officerReport();
                        break;

                    case 11:
                        complaintStatistics();
                        break;

                    case 12:
                        pendingComplaintsReport();
                        break;

                    case 13:
                        System.out.println("Thank You...");
                        break;

                    default:
                        System.out.println("Invalid Choice");

                }

            }while(choice != 13);

            con.close();

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void registerUser() {

        try {

            sc.nextLine();

            System.out.print("Enter User Name : ");
            String name = sc.nextLine();

            System.out.print("Enter Phone Number : ");
            String phone = sc.nextLine();

            System.out.print("Enter Email : ");
            String email = sc.nextLine();

            System.out.print("Enter Address : ");
            String address = sc.nextLine();

            System.out.print("Enter Registration Date (YYYY-MM-DD) : ");
            String date = sc.nextLine();

            String sql = "INSERT INTO users(user_name,phone,email,address,registration_date) VALUES(?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, address);
            ps.setDate(5, Date.valueOf(date));

            int rows = ps.executeUpdate();

            if(rows > 0){

                System.out.println("User Registered Successfully");

            }else{

                System.out.println("User Registration Failed");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void viewUsers() {

        try {

            String sql = "SELECT * FROM users";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- User Details ----------------");

            System.out.printf("%-10s %-20s %-15s %-25s %-20s %-20s\n",
                    "User ID", "User Name", "Phone",
                    "Email", "Address", "Register Date");

            while(rs.next()){

                System.out.printf("%-10d %-20s %-15s %-25s %-20s %-20s\n",

                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getDate("registration_date"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }
    static void registerComplaint() {

        try {

            System.out.print("Enter User ID : ");
            int userId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Complaint Type : ");
            String complaintType = sc.nextLine();

            System.out.print("Enter Complaint Date (YYYY-MM-DD) : ");
            String complaintDate = sc.nextLine();

            System.out.print("Enter Description : ");
            String description = sc.nextLine();

            String sql = "INSERT INTO complaints(user_id,complaint_type,complaint_date,description,complaint_status) VALUES(?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setString(2, complaintType);
            ps.setDate(3, Date.valueOf(complaintDate));
            ps.setString(4, description);
            ps.setString(5, "Pending");

            int rows = ps.executeUpdate();

            if(rows > 0){

                System.out.println("Complaint Registered Successfully");

            }else{

                System.out.println("Complaint Registration Failed");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void viewComplaints() {

        try {

            String sql = "SELECT * FROM complaints";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Complaint Details ----------------");

            System.out.printf("%-15s %-10s %-12s %-20s %-15s %-30s %-15s %-15s\n",
                    "Complaint ID", "User ID", "Officer ID", "Complaint Type",
                    "Complaint Date", "Description", "Status", "Assigned");

            while(rs.next()){

                System.out.printf("%-15d %-10d %-12d %-20s %-15s %-30s %-15s %-15s\n",

                        rs.getInt("complaint_id"),
                        rs.getInt("user_id"),
                        rs.getInt("officer_id"),
                        rs.getString("complaint_type"),
                        rs.getDate("complaint_date"),
                        rs.getString("description"),
                        rs.getString("complaint_status"),
                        rs.getInt("officer_id"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void assignOfficer() {

        try {

            System.out.print("Enter Complaint ID : ");
            int complaintId = sc.nextInt();

            System.out.print("Enter Officer ID : ");
            int officerId = sc.nextInt();

            String sql = "UPDATE complaints SET officer_id=?, complaint_status=? WHERE complaint_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, officerId);
            ps.setString(2, "In Progress");
            ps.setInt(3, complaintId);

            int rows = ps.executeUpdate();

            if(rows > 0){

                System.out.println("Officer Assigned Successfully");

            }else{

                System.out.println("Complaint ID Not Found");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }
    static void viewResolution() {

        try {

            String sql = "SELECT * FROM complaints WHERE complaint_status=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "Resolved");

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Resolved Complaints ----------------");

            System.out.printf("%-15s %-10s %-12s %-20s %-15s %-30s %-15s\n",
                    "Complaint ID", "User ID", "Officer ID",
                    "Complaint Type", "Complaint Date",
                    "Description", "Status");

            while(rs.next()){

                System.out.printf("%-15d %-10d %-12d %-20s %-15s %-30s %-15s\n",

                        rs.getInt("complaint_id"),
                        rs.getInt("user_id"),
                        rs.getInt("officer_id"),
                        rs.getString("complaint_type"),
                        rs.getDate("complaint_date"),
                        rs.getString("description"),
                        rs.getString("complaint_status"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }


    static void updateComplaint() {

        try {

            System.out.print("Enter Complaint ID : ");
            int complaintId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter New Complaint Type : ");
            String complaintType = sc.nextLine();

            System.out.print("Enter New Description : ");
            String description = sc.nextLine();

            System.out.print("Enter New Status (Pending/In Progress/Resolved) : ");
            String complaintStatus = sc.nextLine();

            String sql = "UPDATE complaints SET complaint_type=?, description=?, complaint_status=? WHERE complaint_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, complaintType);
            ps.setString(2, description);
            ps.setString(3, complaintStatus);
            ps.setInt(4, complaintId);

            int rows = ps.executeUpdate();

            if(rows > 0){

                System.out.println("Complaint Updated Successfully");

            }else{

                System.out.println("Complaint ID Not Found");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void deleteComplaint() {

        try {

            System.out.print("Enter Complaint ID : ");
            int complaintId = sc.nextInt();

            String sql = "DELETE FROM complaints WHERE complaint_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, complaintId);

            int rows = ps.executeUpdate();

            if(rows > 0){

                System.out.println("Complaint Deleted Successfully");

            }else{

                System.out.println("Complaint ID Not Found");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void complaintDetails() {

        try {

            String sql = "SELECT u.user_name, o.officer_name, c.complaint_id, " +
                    "c.complaint_type, c.complaint_date, c.description, c.complaint_status " +
                    "FROM complaints c " +
                    "INNER JOIN users u ON c.user_id = u.user_id " +
                    "LEFT JOIN officers o ON c.officer_id = o.officer_id";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Complaint Details ----------------");

            System.out.printf("%-15s %-20s %-20s %-20s %-15s %-30s %-15s\n",
                    "Complaint ID", "User Name", "Officer Name",
                    "Complaint Type", "Complaint Date",
                    "Description", "Status");

            while(rs.next()){

                System.out.printf("%-15d %-20s %-20s %-20s %-15s %-30s %-15s\n",

                        rs.getInt("complaint_id"),
                        rs.getString("user_name"),
                        rs.getString("officer_name"),
                        rs.getString("complaint_type"),
                        rs.getDate("complaint_date"),
                        rs.getString("description"),
                        rs.getString("complaint_status"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }
    static void officerReport() {

        try {

            String sql = "SELECT o.officer_name, COUNT(c.complaint_id) AS total_complaints " +
                    "FROM officers o " +
                    "INNER JOIN complaints c " +
                    "ON o.officer_id = c.officer_id " +
                    "GROUP BY o.officer_name " +
                    "HAVING COUNT(c.complaint_id) > 0";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Officer Report ----------------");

            System.out.printf("%-20s %-20s\n",
                    "Officer Name", "Total Complaints");

            while(rs.next()){

                System.out.printf("%-20s %-20d\n",

                        rs.getString("officer_name"),
                        rs.getInt("total_complaints"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void complaintStatistics() {

        try {

            String sql = "SELECT COUNT(complaint_id) AS total_complaints, " +
                    "SUM(officer_id) AS total_officers_assigned, " +
                    "AVG(officer_id) AS average_officer_id, " +
                    "MAX(officer_id) AS maximum_officer_id, " +
                    "MIN(officer_id) AS minimum_officer_id " +
                    "FROM complaints";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                System.out.println("\n----------- Complaint Statistics -----------");

                System.out.println("Total Complaints        : " + rs.getInt("total_complaints"));
                System.out.println("Total Officers Assigned : " + rs.getInt("total_officers_assigned"));
                System.out.println("Average Officer ID      : " + rs.getDouble("average_officer_id"));
                System.out.println("Maximum Officer ID      : " + rs.getInt("maximum_officer_id"));
                System.out.println("Minimum Officer ID      : " + rs.getInt("minimum_officer_id"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void pendingComplaintsReport() {

        try {

            String sql = "SELECT u.user_name, o.officer_name, c.complaint_type, " +
                    "c.complaint_date, c.description, c.complaint_status " +
                    "FROM complaints c " +
                    "INNER JOIN users u ON c.user_id = u.user_id " +
                    "LEFT JOIN officers o ON c.officer_id = o.officer_id " +
                    "WHERE c.complaint_status=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "Pending");

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Pending Complaints Report ----------------");

            System.out.printf("%-20s %-20s %-20s %-15s %-35s %-15s\n",
                    "User Name",
                    "Officer Name",
                    "Complaint Type",
                    "Complaint Date",
                    "Description",
                    "Status");

            while(rs.next()){

                System.out.printf("%-20s %-20s %-20s %-15s %-35s %-15s\n",

                        rs.getString("user_name"),
                        rs.getString("officer_name"),
                        rs.getString("complaint_type"),
                        rs.getDate("complaint_date"),
                        rs.getString("description"),
                        rs.getString("complaint_status"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }
}
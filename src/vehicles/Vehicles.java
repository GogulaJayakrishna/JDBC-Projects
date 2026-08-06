package vehicles;

import java.sql.*;
import java.util.Scanner;

public class Vehicles {

    static final String url = "jdbc:mysql://localhost:3306/vehicle_service_db";
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

                System.out.println("\n===== Vehicle Service Center Management =====");

                System.out.println("1. Customer Registration");
                System.out.println("2. View Customers");
                System.out.println("3. Vehicle Registration");
                System.out.println("4. View Vehicles");
                System.out.println("5. Service Booking");
                System.out.println("6. Service History");
                System.out.println("7. Update Vehicle");
                System.out.println("8. Delete Vehicle");
                System.out.println("9. Customer Vehicle Details");
                System.out.println("10. Service Report");
                System.out.println("11. Service Statistics");
                System.out.println("12. Pending Services Report");
                System.out.println("13. Exit");

                System.out.print("Enter Choice : ");
                choice = sc.nextInt();

                switch(choice){

                    case 1:
                        registerCustomer();
                        break;

                    case 2:
                        viewCustomers();
                        break;

                    case 3:
                        registerVehicle();
                        break;

                    case 4:
                        viewVehicles();
                        break;

                    case 5:
                        serviceBooking();
                        break;

                    case 6:
                        serviceHistory();
                        break;

                    case 7:
                        updateVehicle();
                        break;

                    case 8:
                        deleteVehicle();
                        break;

                    case 9:
                        customerVehicleDetails();
                        break;

                    case 10:
                        serviceReport();
                        break;

                    case 11:
                        serviceStatistics();
                        break;

                    case 12:
                        pendingServicesReport();
                        break;

                    case 13:
                        System.out.println("Thank You...");
                        break;

                    default:
                        System.out.println("Invalid Choice");

                }

            } while(choice != 13);

            con.close();

        } catch(Exception e) {

            e.printStackTrace();

        }

    }

    static void registerCustomer() {

        try {

            sc.nextLine();

            System.out.print("Enter Customer Name : ");
            String name = sc.nextLine();

            System.out.print("Enter Phone Number : ");
            String phone = sc.nextLine();

            System.out.print("Enter Email : ");
            String email = sc.nextLine();

            System.out.print("Enter Address : ");
            String address = sc.nextLine();

            System.out.print("Enter Registration Date (YYYY-MM-DD) : ");
            String date = sc.nextLine();

            String sql = "INSERT INTO customers(customer_name,phone,email,address,registration_date) VALUES(?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, address);
            ps.setDate(5, Date.valueOf(date));

            int rows = ps.executeUpdate();

            if(rows > 0){

                System.out.println("Customer Registered Successfully");

            }else{

                System.out.println("Customer Registration Failed");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }
    static void viewCustomers() {

        try {

            String sql = "SELECT * FROM customers";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Customer Details ----------------");

            System.out.printf("%-12s %-20s %-15s %-25s %-20s %-20s\n",
                    "Customer ID", "Customer Name", "Phone",
                    "Email", "Address", "Register Date");

            while(rs.next()){

                System.out.printf("%-12d %-20s %-15s %-25s %-20s %-20s\n",

                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getDate("registration_date"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }
    static void registerVehicle() {

        try {

            System.out.print("Enter Customer ID : ");
            int customerId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Vehicle Number : ");
            String vehicleNumber = sc.nextLine();

            System.out.print("Enter Vehicle Name : ");
            String vehicleName = sc.nextLine();

            System.out.print("Enter Model : ");
            String model = sc.nextLine();

            System.out.print("Enter Vehicle Type (Bike/Car) : ");
            String vehicleType = sc.nextLine();

            String sql = "INSERT INTO vehicles(customer_id,vehicle_number,vehicle_name,model,vehicle_type) VALUES(?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, customerId);
            ps.setString(2, vehicleNumber);
            ps.setString(3, vehicleName);
            ps.setString(4, model);
            ps.setString(5, vehicleType);

            int rows = ps.executeUpdate();

            if(rows > 0){

                System.out.println("Vehicle Registered Successfully");

            }else{

                System.out.println("Vehicle Registration Failed");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void viewVehicles() {

        try {

            String sql = "SELECT * FROM vehicles";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Vehicle Details ----------------");

            System.out.printf("%-12s %-12s %-20s %-20s %-15s %-15s\n",
                    "Vehicle ID", "Customer ID", "Vehicle No",
                    "Vehicle Name", "Model", "Type");

            while(rs.next()){

                System.out.printf("%-12d %-12d %-20s %-20s %-15s %-15s\n",

                        rs.getInt("vehicle_id"),
                        rs.getInt("customer_id"),
                        rs.getString("vehicle_number"),
                        rs.getString("vehicle_name"),
                        rs.getString("model"),
                        rs.getString("vehicle_type"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }
    static void serviceBooking() {

        try {

            System.out.print("Enter Vehicle ID : ");
            int vehicleId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Service Date (YYYY-MM-DD) : ");
            String serviceDate = sc.nextLine();

            System.out.print("Enter Service Type : ");
            String serviceType = sc.nextLine();

            System.out.print("Enter Service Cost : ");
            double serviceCost = sc.nextDouble();
            sc.nextLine();

            System.out.print("Enter Service Status (Pending/Completed) : ");
            String serviceStatus = sc.nextLine();

            String sql = "INSERT INTO service_records(vehicle_id,service_date,service_type,service_cost,service_status) VALUES(?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, vehicleId);
            ps.setDate(2, Date.valueOf(serviceDate));
            ps.setString(3, serviceType);
            ps.setDouble(4, serviceCost);
            ps.setString(5, serviceStatus);

            int rows = ps.executeUpdate();

            if(rows > 0){

                System.out.println("Service Booked Successfully");

            }else{

                System.out.println("Service Booking Failed");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void serviceHistory() {

        try {

            String sql = "SELECT * FROM service_records";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Service History ----------------");

            System.out.printf("%-12s %-12s %-15s %-20s %-15s %-15s\n",
                    "Service ID", "Vehicle ID", "Service Date",
                    "Service Type", "Service Cost", "Status");

            while(rs.next()){

                System.out.printf("%-12d %-12d %-15s %-20s %-15.2f %-15s\n",

                        rs.getInt("service_id"),
                        rs.getInt("vehicle_id"),
                        rs.getDate("service_date"),
                        rs.getString("service_type"),
                        rs.getDouble("service_cost"),
                        rs.getString("service_status"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void updateVehicle() {

        try {

            System.out.print("Enter Vehicle ID : ");
            int vehicleId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter New Vehicle Number : ");
            String vehicleNumber = sc.nextLine();

            System.out.print("Enter New Model : ");
            String model = sc.nextLine();

            System.out.print("Enter New Vehicle Type (Bike/Car) : ");
            String vehicleType = sc.nextLine();

            String sql = "UPDATE vehicles SET vehicle_number=?, model=?, vehicle_type=? WHERE vehicle_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, vehicleNumber);
            ps.setString(2, model);
            ps.setString(3, vehicleType);
            ps.setInt(4, vehicleId);

            int rows = ps.executeUpdate();

            if(rows > 0){

                System.out.println("Vehicle Updated Successfully");

            }else{

                System.out.println("Vehicle ID Not Found");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void deleteVehicle() {

        try {

            System.out.print("Enter Vehicle ID : ");
            int vehicleId = sc.nextInt();

            String sql = "DELETE FROM vehicles WHERE vehicle_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, vehicleId);

            int rows = ps.executeUpdate();

            if(rows > 0){

                System.out.println("Vehicle Deleted Successfully");

            }else{

                System.out.println("Vehicle ID Not Found");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }
    static void customerVehicleDetails() {

        try {

            String sql = "SELECT c.customer_id, c.customer_name, c.phone, " +
                    "v.vehicle_id, v.vehicle_number, v.vehicle_name, v.model, v.vehicle_type " +
                    "FROM customers c " +
                    "INNER JOIN vehicles v " +
                    "ON c.customer_id = v.customer_id";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Customer Vehicle Details ----------------");

            System.out.printf("%-12s %-20s %-15s %-12s %-18s %-20s %-12s %-10s\n",
                    "Customer ID", "Customer Name", "Phone",
                    "Vehicle ID", "Vehicle Number", "Vehicle Name",
                    "Model", "Type");

            while(rs.next()){

                System.out.printf("%-12d %-20s %-15s %-12d %-18s %-20s %-12s %-10s\n",

                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("phone"),
                        rs.getInt("vehicle_id"),
                        rs.getString("vehicle_number"),
                        rs.getString("vehicle_name"),
                        rs.getString("model"),
                        rs.getString("vehicle_type"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void serviceReport() {

        try {

            String sql = "SELECT v.vehicle_type, COUNT(s.service_id) AS total_services, " +
                    "SUM(s.service_cost) AS total_cost " +
                    "FROM vehicles v " +
                    "INNER JOIN service_records s " +
                    "ON v.vehicle_id = s.vehicle_id " +
                    "GROUP BY v.vehicle_type " +
                    "HAVING COUNT(s.service_id) > 0";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Service Report ----------------");

            System.out.printf("%-15s %-20s %-20s\n",
                    "Vehicle Type", "Total Services", "Total Cost");

            while(rs.next()){

                System.out.printf("%-15s %-20d %-20.2f\n",

                        rs.getString("vehicle_type"),
                        rs.getInt("total_services"),
                        rs.getDouble("total_cost"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void serviceStatistics() {

        try {

            String sql = "SELECT COUNT(service_id) AS total_services, " +
                    "SUM(service_cost) AS total_cost, " +
                    "AVG(service_cost) AS average_cost, " +
                    "MAX(service_cost) AS maximum_cost, " +
                    "MIN(service_cost) AS minimum_cost " +
                    "FROM service_records";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                System.out.println("\n------------ Service Statistics ------------");

                System.out.println("Total Services    : " + rs.getInt("total_services"));
                System.out.println("Total Cost        : " + rs.getDouble("total_cost"));
                System.out.println("Average Cost      : " + rs.getDouble("average_cost"));
                System.out.println("Maximum Cost      : " + rs.getDouble("maximum_cost"));
                System.out.println("Minimum Cost      : " + rs.getDouble("minimum_cost"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void pendingServicesReport() {

        try {

            String sql = "SELECT c.customer_name, v.vehicle_number, v.vehicle_name, " +
                    "s.service_type, s.service_date, s.service_cost, s.service_status " +
                    "FROM customers c " +
                    "INNER JOIN vehicles v ON c.customer_id = v.customer_id " +
                    "INNER JOIN service_records s ON v.vehicle_id = s.vehicle_id " +
                    "WHERE s.service_status = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "Pending");

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Pending Services Report ----------------");

            System.out.printf("%-20s %-18s %-18s %-20s %-15s %-15s %-15s\n",
                    "Customer Name",
                    "Vehicle Number",
                    "Vehicle Name",
                    "Service Type",
                    "Service Date",
                    "Service Cost",
                    "Status");

            while(rs.next()){

                System.out.printf("%-20s %-18s %-18s %-20s %-15s %-15.2f %-15s\n",

                        rs.getString("customer_name"),
                        rs.getString("vehicle_number"),
                        rs.getString("vehicle_name"),
                        rs.getString("service_type"),
                        rs.getDate("service_date"),
                        rs.getDouble("service_cost"),
                        rs.getString("service_status"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

}
package employee;

import java.sql.*;
import java.util.Scanner;

public class Employee {

    static final String url = "jdbc:mysql://localhost:3306/employee_leave_db";
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

                System.out.println("\n===== Employee Leave Management System =====");

                System.out.println("1. Employee Registration");
                System.out.println("2. View Employees");
                System.out.println("3. Apply Leave");
                System.out.println("4. View Leave Requests");
                System.out.println("5. Approve Leave");
                System.out.println("6. Reject Leave");
                System.out.println("7. View Leave Balance");
                System.out.println("8. Update Employee");
                System.out.println("9. Delete Employee");
                System.out.println("10. Employee Leave Details");
                System.out.println("11. Department Leave Report");
                System.out.println("12. Leave Statistics");
                System.out.println("13. Pending Leave Report");
                System.out.println("14. Exit");

                System.out.print("Enter Choice : ");
                choice = sc.nextInt();

                switch(choice){

                    case 1:
                        registerEmployee();
                        break;

                    case 2:
                        viewEmployees();
                        break;

                    case 3:
                        applyLeave();
                        break;

                    case 4:
                        viewLeaveRequests();
                        break;

                    case 5:
                        approveLeave();
                        break;

                    case 6:
                        rejectLeave();
                        break;

                    case 7:
                        leaveBalance();
                        break;

                    case 8:
                        updateEmployee();
                        break;

                    case 9:
                        deleteEmployee();
                        break;

                    case 10:
                        employeeLeaveDetails();
                        break;

                    case 11:
                        departmentLeaveReport();
                        break;

                    case 12:
                        leaveStatistics();
                        break;

                    case 13:
                        pendingLeaveReport();
                        break;

                    case 14:
                        System.out.println("Thank You...");
                        break;

                    default:
                        System.out.println("Invalid Choice");
                }

            } while(choice != 14);

            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    static void registerEmployee() {

        try {

            sc.nextLine();

            System.out.print("Enter Employee Name : ");
            String name = sc.nextLine();

            System.out.print("Enter Department : ");
            String department = sc.nextLine();

            System.out.print("Enter Email : ");
            String email = sc.nextLine();

            System.out.print("Enter Phone : ");
            String phone = sc.nextLine();

            System.out.print("Enter Designation : ");
            String designation = sc.nextLine();

            System.out.print("Enter Join Date (YYYY-MM-DD) : ");
            String joinDate = sc.nextLine();

            System.out.print("Enter Salary : ");
            double salary = sc.nextDouble();

            con.setAutoCommit(false);

            String sql1 = "INSERT INTO employees(emp_name,department,email,phone,designation,join_date,salary) VALUES(?,?,?,?,?,?,?)";

            PreparedStatement ps1 = con.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);

            ps1.setString(1, name);
            ps1.setString(2, department);
            ps1.setString(3, email);
            ps1.setString(4, phone);
            ps1.setString(5, designation);
            ps1.setDate(6, Date.valueOf(joinDate));
            ps1.setDouble(7, salary);

            int rows = ps1.executeUpdate();

            if(rows > 0){

                ResultSet rs = ps1.getGeneratedKeys();

                if(rs.next()){

                    int empId = rs.getInt(1);

                    String sql2 = "INSERT INTO leave_balance(emp_id,total_leaves,used_leaves,remaining_leaves) VALUES(?,?,?,?)";

                    PreparedStatement ps2 = con.prepareStatement(sql2);

                    ps2.setInt(1, empId);
                    ps2.setInt(2, 20);
                    ps2.setInt(3, 0);
                    ps2.setInt(4, 20);


                    ps2.executeUpdate();

                }

                con.commit();

                System.out.println("Employee Registered Successfully");

            }
            else{

                con.rollback();

                System.out.println("Registration Failed");

            }

            con.setAutoCommit(true);

        }
        catch(Exception e){

            try{
                con.rollback();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }

            e.printStackTrace();
        }

    }

    static void viewEmployees() {

        try {

            String sql = "SELECT * FROM employees";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n------------------- Employee Details -------------------");

            System.out.printf("%-5s %-15s %-15s %-25s %-15s %-20s %-15s %-10s\n",
                    "ID", "Name", "Department", "Email", "Phone",
                    "Designation", "Join Date", "Salary");

            while(rs.next()){

                System.out.printf("%-5d %-15s %-15s %-25s %-15s %-20s %-15s %-10.2f\n",

                        rs.getInt("emp_id"),
                        rs.getString("emp_name"),
                        rs.getString("department"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("designation"),
                        rs.getDate("join_date"),
                        rs.getDouble("salary"));

            }

        }
        catch(Exception e){

            e.printStackTrace();

        }

    }

    static void applyLeave() {

        try {

            System.out.print("Enter Employee ID : ");
            int empId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Leave Type : ");
            String leaveType = sc.nextLine();

            System.out.print("Enter From Date (YYYY-MM-DD) : ");
            String fromDate = sc.nextLine();

            System.out.print("Enter To Date (YYYY-MM-DD) : ");
            String toDate = sc.nextLine();

            System.out.print("Enter Reason : ");
            String reason = sc.nextLine();

            String sql = "INSERT INTO leave_requests(emp_id,leave_type,from_date,to_date,reason,status) VALUES(?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, empId);
            ps.setString(2, leaveType);
            ps.setDate(3, Date.valueOf(fromDate));
            ps.setDate(4, Date.valueOf(toDate));
            ps.setString(5, reason);
            ps.setString(6, "Pending");

            int rows = ps.executeUpdate();

            if(rows > 0){

                System.out.println("Leave Applied Successfully");

            }else{

                System.out.println("Leave Application Failed");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void viewLeaveRequests() {

        try {

            String sql = "SELECT * FROM leave_requests";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Leave Requests ----------------");

            System.out.printf("%-10s %-10s %-15s %-15s %-15s %-25s %-15s\n",
                    "Leave ID", "Emp ID", "Leave Type", "From Date",
                    "To Date", "Reason", "Status");

            while(rs.next()){

                System.out.printf("%-10d %-10d %-15s %-15s %-15s %-25s %-15s\n",

                        rs.getInt("leave_id"),
                        rs.getInt("emp_id"),
                        rs.getString("leave_type"),
                        rs.getDate("from_date"),
                        rs.getDate("to_date"),
                        rs.getString("reason"),
                        rs.getString("status"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void approveLeave() {

        try {

            System.out.print("Enter Leave ID : ");
            int leaveId = sc.nextInt();

            con.setAutoCommit(false);

            String sql1 = "SELECT emp_id FROM leave_requests WHERE leave_id=?";

            PreparedStatement ps1 = con.prepareStatement(sql1);
            ps1.setInt(1, leaveId);

            ResultSet rs = ps1.executeQuery();

            if(rs.next()){

                int empId = rs.getInt("emp_id");

                String sql2 = "UPDATE leave_requests SET status=? WHERE leave_id=?";

                PreparedStatement ps2 = con.prepareStatement(sql2);
                ps2.setString(1, "Approved");
                ps2.setInt(2, leaveId);

                ps2.executeUpdate();

                String sql3 = "UPDATE leave_balance SET used_leaves=used_leaves+1, remaining_leaves=remaining_leaves-1 WHERE emp_id=?";

                PreparedStatement ps3 = con.prepareStatement(sql3);
                ps3.setInt(1, empId);

                ps3.executeUpdate();

                con.commit();

                System.out.println("Leave Approved Successfully");

            }else{

                con.rollback();

                System.out.println("Leave ID Not Found");

            }

            con.setAutoCommit(true);

        }catch(Exception e){

            try{
                con.rollback();
            }catch(Exception ex){
                ex.printStackTrace();
            }

            e.printStackTrace();

        }

    }

    static void rejectLeave() {

        try {

            System.out.print("Enter Leave ID : ");
            int leaveId = sc.nextInt();

            String sql = "UPDATE leave_requests SET status=? WHERE leave_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "Rejected");
            ps.setInt(2, leaveId);

            int rows = ps.executeUpdate();

            if(rows > 0){

                System.out.println("Leave Rejected Successfully");

            }else{

                System.out.println("Leave ID Not Found");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void leaveBalance() {

        try {

            String sql = "SELECT * FROM leave_balance";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Leave Balance ----------------");

            System.out.printf("%-12s %-10s %-15s %-15s %-20s\n",
                    "Balance ID", "Emp ID", "Total Leaves", "Used Leaves", "Remaining Leaves");

            while(rs.next()){

                System.out.printf("%-12d %-10d %-15d %-15d %-20d\n",

                        rs.getInt("balance_id"),
                        rs.getInt("emp_id"),
                        rs.getInt("total_leaves"),
                        rs.getInt("used_leaves"),
                        rs.getInt("remaining_leaves"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void updateEmployee() {

        try {

            System.out.print("Enter Employee ID : ");
            int empId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter New Department : ");
            String department = sc.nextLine();

            System.out.print("Enter New Phone : ");
            String phone = sc.nextLine();

            System.out.print("Enter New Designation : ");
            String designation = sc.nextLine();

            System.out.print("Enter New Salary : ");
            double salary = sc.nextDouble();

            String sql = "UPDATE employees SET department=?, phone=?, designation=?, salary=? WHERE emp_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, department);
            ps.setString(2, phone);
            ps.setString(3, designation);
            ps.setDouble(4, salary);
            ps.setInt(5, empId);

            int rows = ps.executeUpdate();

            if(rows > 0){

                System.out.println("Employee Updated Successfully");

            }else{

                System.out.println("Employee ID Not Found");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void deleteEmployee() {

        try {

            System.out.print("Enter Employee ID : ");
            int empId = sc.nextInt();

            String sql = "DELETE FROM employees WHERE emp_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, empId);

            int rows = ps.executeUpdate();

            if(rows > 0){

                System.out.println("Employee Deleted Successfully");

            }else{

                System.out.println("Employee ID Not Found");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void employeeLeaveDetails() {

        try {

            String sql = "SELECT e.emp_id, e.emp_name, e.department, l.leave_id, l.leave_type, l.from_date, l.to_date, l.status " +
                    "FROM employees e " +
                    "INNER JOIN leave_requests l " +
                    "ON e.emp_id = l.emp_id";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n--------------- Employee Leave Details ---------------");

            System.out.printf("%-8s %-15s %-15s %-10s %-15s %-15s %-15s %-12s\n",
                    "Emp ID", "Name", "Department", "Leave ID",
                    "Leave Type", "From Date", "To Date", "Status");

            while(rs.next()){

                System.out.printf("%-8d %-15s %-15s %-10d %-15s %-15s %-15s %-12s\n",

                        rs.getInt("emp_id"),
                        rs.getString("emp_name"),
                        rs.getString("department"),
                        rs.getInt("leave_id"),
                        rs.getString("leave_type"),
                        rs.getDate("from_date"),
                        rs.getDate("to_date"),
                        rs.getString("status"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }
    static void departmentLeaveReport() {

        try {

            String sql = "SELECT e.department, COUNT(l.leave_id) AS total_leaves " +
                    "FROM employees e " +
                    "INNER JOIN leave_requests l " +
                    "ON e.emp_id = l.emp_id " +
                    "GROUP BY e.department " +
                    "HAVING COUNT(l.leave_id) > 0";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n----------- Department Leave Report -----------");

            System.out.printf("%-20s %-20s\n",
                    "Department", "Total Leave Requests");

            while(rs.next()){

                System.out.printf("%-20s %-20d\n",

                        rs.getString("department"),
                        rs.getInt("total_leaves"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void leaveStatistics() {

        try {

            String sql = "SELECT COUNT(*) AS total_employees, " +
                    "SUM(total_leaves) AS total_leaves, " +
                    "AVG(remaining_leaves) AS average_remaining, " +
                    "MAX(used_leaves) AS maximum_used, " +
                    "MIN(used_leaves) AS minimum_used " +
                    "FROM leave_balance";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                System.out.println("\n----------- Leave Statistics -----------");

                System.out.println("Total Employees        : " + rs.getInt("total_employees"));
                System.out.println("Total Leaves           : " + rs.getInt("total_leaves"));
                System.out.println("Average Remaining      : " + rs.getDouble("average_remaining"));
                System.out.println("Maximum Used Leaves    : " + rs.getInt("maximum_used"));
                System.out.println("Minimum Used Leaves    : " + rs.getInt("minimum_used"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void pendingLeaveReport() {

        try {

            String sql = "SELECT e.emp_id, e.emp_name, e.department, l.leave_type, l.from_date, l.to_date, l.reason " +
                    "FROM employees e " +
                    "INNER JOIN leave_requests l " +
                    "ON e.emp_id = l.emp_id " +
                    "WHERE l.status = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "Pending");

            ResultSet rs = ps.executeQuery();

            System.out.println("\n----------- Pending Leave Report -----------");

            System.out.printf("%-8s %-15s %-15s %-15s %-15s %-15s %-25s\n",
                    "Emp ID", "Name", "Department", "Leave Type",
                    "From Date", "To Date", "Reason");

            while(rs.next()){

                System.out.printf("%-8d %-15s %-15s %-15s %-15s %-15s %-25s\n",

                        rs.getInt("emp_id"),
                        rs.getString("emp_name"),
                        rs.getString("department"),
                        rs.getString("leave_type"),
                        rs.getDate("from_date"),
                        rs.getDate("to_date"),
                        rs.getString("reason"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }


}
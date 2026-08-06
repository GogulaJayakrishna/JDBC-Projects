package bankinng;

import java.sql.*;
import java.util.Scanner;

public class Banking {

    static final String url = "jdbc:mysql://localhost:3306/banking_db";
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

                System.out.println("\n===== Banking Management System =====");

                System.out.println("1. Account Creation");
                System.out.println("2. View Accounts");
                System.out.println("3. Deposit");
                System.out.println("4. Withdrawal");
                System.out.println("5. Fund Transfer");
                System.out.println("6. Transaction History");
                System.out.println("7. Update Account");
                System.out.println("8. Delete Account");
                System.out.println("9. Customer Account Details");
                System.out.println("10. Account Summary");
                System.out.println("11. Banking Statistics");
                System.out.println("12. Active Accounts Report");
                System.out.println("13. Exit");

                System.out.print("Enter Choice : ");
                choice = sc.nextInt();

                switch(choice){

                    case 1:
                        createAccount();
                        break;

                    case 2:
                        viewAccounts();
                        break;

                    case 3:
                        deposit();
                        break;

                    case 4:
                        withdrawal();
                        break;

                    case 5:
                        fundTransfer();
                        break;

                    case 6:
                        transactionHistory();
                        break;

                    case 7:
                        updateAccount();
                        break;

                    case 8:
                        deleteAccount();
                        break;

                    case 9:
                        customerAccountDetails();
                        break;

                    case 10:
                        accountSummary();
                        break;

                    case 11:
                        bankingStatistics();
                        break;

                    case 12:
                        activeAccountsReport();
                        break;

                    case 13:
                        System.out.println("Thank You...");
                        break;

                    default:
                        System.out.println("Invalid Choice");

                }

            } while(choice != 13);

            con.close();

        } catch(Exception e){

            e.printStackTrace();

        }

    }

    static void createAccount() {

        try {

            sc.nextLine();

            System.out.print("Enter Customer Name : ");
            String name = sc.nextLine();

            System.out.print("Enter Email : ");
            String email = sc.nextLine();

            System.out.print("Enter Phone : ");
            String phone = sc.nextLine();

            System.out.print("Enter Address : ");
            String address = sc.nextLine();

            System.out.print("Enter Created Date (YYYY-MM-DD) : ");
            String createdDate = sc.nextLine();

            System.out.print("Enter Account Type (Savings/Current) : ");
            String accountType = sc.nextLine();

            System.out.print("Enter Initial Balance : ");
            double balance = sc.nextDouble();

            con.setAutoCommit(false);

            String sql1 = "INSERT INTO customers(customer_name,email,phone,address,created_date) VALUES(?,?,?,?,?)";

            PreparedStatement ps1 = con.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);

            ps1.setString(1, name);
            ps1.setString(2, email);
            ps1.setString(3, phone);
            ps1.setString(4, address);
            ps1.setDate(5, Date.valueOf(createdDate));

            int rows = ps1.executeUpdate();

            if(rows > 0){

                ResultSet rs = ps1.getGeneratedKeys();

                if(rs.next()){

                    int customerId = rs.getInt(1);

                    String sql2 = "INSERT INTO accounts(customer_id,account_type,balance,status) VALUES(?,?,?,?)";

                    PreparedStatement ps2 = con.prepareStatement(sql2);

                    ps2.setInt(1, customerId);
                    ps2.setString(2, accountType);
                    ps2.setDouble(3, balance);
                    ps2.setString(4, "Active");

                    ps2.executeUpdate();

                }

                con.commit();

                System.out.println("Account Created Successfully");

            }else{

                con.rollback();

                System.out.println("Account Creation Failed");

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

    static void viewAccounts() {

        try {

            String sql = "SELECT * FROM accounts";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Account Details ----------------");

            System.out.printf("%-12s %-15s %-15s %-15s %-15s\n",
                    "Account ID", "Customer ID", "Account Type", "Balance", "Status");

            while(rs.next()){

                System.out.printf("%-12d %-15d %-15s %-15.2f %-15s\n",

                        rs.getInt("account_id"),
                        rs.getInt("customer_id"),
                        rs.getString("account_type"),
                        rs.getDouble("balance"),
                        rs.getString("status"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void deposit() {

        try {

            System.out.print("Enter Account ID : ");
            int accountId = sc.nextInt();

            System.out.print("Enter Deposit Amount : ");
            double amount = sc.nextDouble();

            con.setAutoCommit(false);

            String sql1 = "UPDATE accounts SET balance = balance + ? WHERE account_id=?";

            PreparedStatement ps1 = con.prepareStatement(sql1);

            ps1.setDouble(1, amount);
            ps1.setInt(2, accountId);

            int rows = ps1.executeUpdate();

            if(rows > 0){

                String sql2 = "INSERT INTO transactions(account_id,transaction_type,amount,transaction_date) VALUES(?,?,?,?)";

                PreparedStatement ps2 = con.prepareStatement(sql2);

                ps2.setInt(1, accountId);
                ps2.setString(2, "Deposit");
                ps2.setDouble(3, amount);
                ps2.setDate(4, new Date(System.currentTimeMillis()));

                ps2.executeUpdate();

                con.commit();

                System.out.println("Amount Deposited Successfully");

            }else{

                con.rollback();

                System.out.println("Account ID Not Found");

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

    static void withdrawal() {

        try {

            System.out.print("Enter Account ID : ");
            int accountId = sc.nextInt();

            System.out.print("Enter Withdrawal Amount : ");
            double amount = sc.nextDouble();

            con.setAutoCommit(false);

            String sql1 = "SELECT balance FROM accounts WHERE account_id=?";

            PreparedStatement ps1 = con.prepareStatement(sql1);
            ps1.setInt(1, accountId);

            ResultSet rs = ps1.executeQuery();

            if(rs.next()){

                double balance = rs.getDouble("balance");

                if(balance >= amount){

                    String sql2 = "UPDATE accounts SET balance = balance - ? WHERE account_id=?";

                    PreparedStatement ps2 = con.prepareStatement(sql2);

                    ps2.setDouble(1, amount);
                    ps2.setInt(2, accountId);

                    ps2.executeUpdate();

                    String sql3 = "INSERT INTO transactions(account_id,transaction_type,amount,transaction_date) VALUES(?,?,?,?)";

                    PreparedStatement ps3 = con.prepareStatement(sql3);

                    ps3.setInt(1, accountId);
                    ps3.setString(2, "Withdrawal");
                    ps3.setDouble(3, amount);
                    ps3.setDate(4, new Date(System.currentTimeMillis()));

                    ps3.executeUpdate();

                    con.commit();

                    System.out.println("Amount Withdrawn Successfully");

                }else{

                    con.rollback();

                    System.out.println("Insufficient Balance");

                }

            }else{

                con.rollback();

                System.out.println("Account ID Not Found");

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

    static void fundTransfer() {

        try {

            System.out.print("Enter Sender Account ID : ");
            int sender = sc.nextInt();

            System.out.print("Enter Receiver Account ID : ");
            int receiver = sc.nextInt();

            System.out.print("Enter Transfer Amount : ");
            double amount = sc.nextDouble();

            con.setAutoCommit(false);

            String sql1 = "SELECT balance FROM accounts WHERE account_id=?";

            PreparedStatement ps1 = con.prepareStatement(sql1);
            ps1.setInt(1, sender);

            ResultSet rs = ps1.executeQuery();

            if(rs.next()){

                double balance = rs.getDouble("balance");

                if(balance >= amount){

                    String sql2 = "UPDATE accounts SET balance=balance-? WHERE account_id=?";

                    PreparedStatement ps2 = con.prepareStatement(sql2);

                    ps2.setDouble(1, amount);
                    ps2.setInt(2, sender);

                    ps2.executeUpdate();

                    String sql3 = "UPDATE accounts SET balance=balance+? WHERE account_id=?";

                    PreparedStatement ps3 = con.prepareStatement(sql3);

                    ps3.setDouble(1, amount);
                    ps3.setInt(2, receiver);

                    int rows = ps3.executeUpdate();

                    if(rows > 0){

                        String sql4 = "INSERT INTO transactions(account_id,transaction_type,amount,transaction_date) VALUES(?,?,?,?)";

                        PreparedStatement ps4 = con.prepareStatement(sql4);

                        ps4.setInt(1, sender);
                        ps4.setString(2, "Transfer Sent");
                        ps4.setDouble(3, amount);
                        ps4.setDate(4, new Date(System.currentTimeMillis()));

                        ps4.executeUpdate();

                        PreparedStatement ps5 = con.prepareStatement(sql4);

                        ps5.setInt(1, receiver);
                        ps5.setString(2, "Transfer Received");
                        ps5.setDouble(3, amount);
                        ps5.setDate(4, new Date(System.currentTimeMillis()));

                        ps5.executeUpdate();

                        con.commit();

                        System.out.println("Fund Transfer Successful");

                    }else{

                        con.rollback();

                        System.out.println("Receiver Account Not Found");

                    }

                }else{

                    con.rollback();

                    System.out.println("Insufficient Balance");

                }

            }else{

                con.rollback();

                System.out.println("Sender Account Not Found");

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

    static void transactionHistory() {

        try {

            String sql = "SELECT * FROM transactions";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Transaction History ----------------");

            System.out.printf("%-15s %-15s %-20s %-15s %-20s\n",
                    "Transaction ID", "Account ID", "Transaction Type", "Amount", "Transaction Date");

            while(rs.next()){

                System.out.printf("%-15d %-15d %-20s %-15.2f %-20s\n",

                        rs.getInt("transaction_id"),
                        rs.getInt("account_id"),
                        rs.getString("transaction_type"),
                        rs.getDouble("amount"),
                        rs.getDate("transaction_date"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void updateAccount() {

        try {

            System.out.print("Enter Account ID : ");
            int accountId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter New Account Type (Savings/Current) : ");
            String accountType = sc.nextLine();

            System.out.print("Enter New Status (Active/Inactive) : ");
            String status = sc.nextLine();

            String sql = "UPDATE accounts SET account_type=?, status=? WHERE account_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, accountType);
            ps.setString(2, status);
            ps.setInt(3, accountId);

            int rows = ps.executeUpdate();

            if(rows > 0){

                System.out.println("Account Updated Successfully");

            }else{

                System.out.println("Account ID Not Found");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void deleteAccount() {

        try {

            System.out.print("Enter Account ID : ");
            int accountId = sc.nextInt();

            String sql = "DELETE FROM accounts WHERE account_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, accountId);

            int rows = ps.executeUpdate();

            if(rows > 0){

                System.out.println("Account Deleted Successfully");

            }else{

                System.out.println("Account ID Not Found");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void customerAccountDetails() {

        try {

            String sql = "SELECT c.customer_id, c.customer_name, c.email, a.account_id, a.account_type, a.balance, a.status " +
                    "FROM customers c " +
                    "INNER JOIN accounts a " +
                    "ON c.customer_id = a.customer_id";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Customer Account Details ----------------");

            System.out.printf("%-12s %-20s %-25s %-12s %-15s %-15s %-15s\n",
                    "Customer ID", "Customer Name", "Email",
                    "Account ID", "Account Type", "Balance", "Status");

            while(rs.next()){

                System.out.printf("%-12d %-20s %-25s %-12d %-15s %-15.2f %-15s\n",

                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("email"),
                        rs.getInt("account_id"),
                        rs.getString("account_type"),
                        rs.getDouble("balance"),
                        rs.getString("status"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }
    static void accountSummary() {

        try {

            String sql = "SELECT account_type, COUNT(account_id) AS total_accounts, " +
                    "SUM(balance) AS total_balance " +
                    "FROM accounts " +
                    "GROUP BY account_type " +
                    "HAVING COUNT(account_id) > 0";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n--------------- Account Summary ---------------");

            System.out.printf("%-20s %-20s %-20s\n",
                    "Account Type", "Total Accounts", "Total Balance");

            while(rs.next()){

                System.out.printf("%-20s %-20d %-20.2f\n",

                        rs.getString("account_type"),
                        rs.getInt("total_accounts"),
                        rs.getDouble("total_balance"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }
    static void bankingStatistics() {

        try {

            String sql = "SELECT COUNT(account_id) AS total_accounts, " +
                    "SUM(balance) AS total_balance, " +
                    "AVG(balance) AS average_balance, " +
                    "MAX(balance) AS maximum_balance, " +
                    "MIN(balance) AS minimum_balance " +
                    "FROM accounts";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                System.out.println("\n----------- Banking Statistics -----------");

                System.out.println("Total Accounts      : " + rs.getInt("total_accounts"));
                System.out.println("Total Balance       : " + rs.getDouble("total_balance"));
                System.out.println("Average Balance     : " + rs.getDouble("average_balance"));
                System.out.println("Maximum Balance     : " + rs.getDouble("maximum_balance"));
                System.out.println("Minimum Balance     : " + rs.getDouble("minimum_balance"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    static void activeAccountsReport() {

        try {

            String sql = "SELECT c.customer_id, c.customer_name, a.account_id, a.account_type, a.balance, a.status " +
                    "FROM customers c " +
                    "INNER JOIN accounts a " +
                    "ON c.customer_id = a.customer_id " +
                    "WHERE a.status = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "Active");

            ResultSet rs = ps.executeQuery();

            System.out.println("\n---------------- Active Accounts Report ----------------");

            System.out.printf("%-12s %-20s %-12s %-15s %-15s %-15s\n",
                    "Customer ID", "Customer Name", "Account ID",
                    "Account Type", "Balance", "Status");

            while(rs.next()){

                System.out.printf("%-12d %-20s %-12d %-15s %-15.2f %-15s\n",

                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getInt("account_id"),
                        rs.getString("account_type"),
                        rs.getDouble("balance"),
                        rs.getString("status"));

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

}
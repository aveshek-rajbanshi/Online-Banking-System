import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.PreparedStatement;

public class User {
    private final Connection conn;
    private final Scanner scanner;

    public User(Connection conn, Scanner scanner){
        this.conn = conn;
        this.scanner = scanner;
    }

    public void register(){
        System.out.println();
        scanner.nextLine();
        System.out.println("****  Register Your Account  ****");
        System.out.print("Enter Your Full Name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter Your Email Address: ");
        String userEmail = scanner.next();
        System.out.print("Enter Your Password: ");
        String userPassword = scanner.next();

        if(user_exist(userEmail)) {
            System.out.println("\n>>  User already exits for this email address !!");
            return;
        }

        String register_query = "INSERT INTO user(user_FullName, user_Email, user_Password) VALUES(?, ?, ?);";
        try{
            PreparedStatement preparedStmt = conn.prepareStatement(register_query);
            preparedStmt.setString(1, userName);
            preparedStmt.setString(2, userEmail);
            preparedStmt.setString(3, userPassword);
            int rowAffected = preparedStmt.executeUpdate();
            if(rowAffected > 0){
                System.out.println("\n>>  Registration Successful.");
            }else{
                System.out.println("\n>>  Registration Failed.");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }


    public String login(){
        System.out.println("\n****  Login Your Account  ****");
        scanner.nextLine();
        System.out.print("Enter Your Email: ");
        String loginEmail = scanner.next();
        System.out.print("Enter Your Password: ");
        String loginPassword = scanner.next();

        String login_query = "SELECT * from user WHERE user_Email = ? AND user_Password = ?;";
        try{
            PreparedStatement preparedStmt = conn.prepareStatement(login_query);
            preparedStmt.setString(1, loginEmail);
            preparedStmt.setString(2, loginPassword);
            ResultSet rs = preparedStmt.executeQuery();
            if(rs.next()){
                return loginEmail;
            }else{
                return null;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    public boolean user_exist(String userEmail){
        String email_Check_query = "SELECT * from user WHERE user_Email = ?";
        try{
            PreparedStatement preparedStmt = conn.prepareStatement(email_Check_query);
            preparedStmt.setString(1, userEmail);
            ResultSet rs = preparedStmt.executeQuery();
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}

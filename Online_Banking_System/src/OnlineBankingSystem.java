import java.io.IOException;
import java.math.BigInteger;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class OnlineBankingSystem{

    private static final String url = "jdbc:mysql://localhost:3306/online_atm";
    private static final String username = "root";
    private static final String password = "";

    public static void main(String[] args){

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection conn = DriverManager.getConnection(url, username, password);

            OnlineBankingSystem Nabil = new OnlineBankingSystem();
            Scanner scanner = new Scanner(System.in);
            User user = new User(conn, scanner);
            Account account = new Account(conn, scanner);
            Operation operation = new Operation(conn, scanner);

            String userEmail = "";
            int choice1;
            do {
                System.out.println("\n****  Welcome to Online Nabil Bank Ltd.  ****");
                System.out.println("\t\t\t 1. Register");
                System.out.println("\t\t\t 2. Login");
                System.out.println("\t\t\t 0. Exit");
                System.out.print("\n--> Enter your choice: ");
                choice1 = scanner.nextInt();

                switch (choice1) {
                    case 1:
                        user.register();
                        break;
                    case 2:
                        userEmail = user.login();
                        if (userEmail != null) {
                            System.out.println("\n>>  User Login Successful.");
                            //case: if user have no account then,
                            if (!account.account_exist(userEmail)) {
                                int choice2;
                                do {
                                    System.out.println("\n**** Welcome to Nabil Bank Ltd. ****");
                                    System.out.println("\t 1. Create an Account");
                                    System.out.println("\t 2. Logout");
                                    System.out.print("\n--> Enter your choice: ");
                                    choice2 = scanner.nextInt();

                                    if(choice2 == 1){
                                        account.create_Account(userEmail);
                                        //Getting user account number.
                                        BigInteger getUserAccountNumber = account.getAccount_Number(userEmail);
                                        //After creating account instantly get access to Atm Functionality.
                                        operation.atmOperation(getUserAccountNumber);
                                    }else if(choice2 == 2){
                                        break;
                                    }else{
                                        System.out.println("\n>>  Please enter a valid choice!!");
                                    }
                                }while (choice2 != 2);
                            }else{
                                //Getting user account number.
                                BigInteger getUserAccountNumber = account.getAccount_Number(userEmail);
                                //if user have an account already.
                                operation.atmOperation(getUserAccountNumber);
                            }
                        }else{
                            System.out.println("\n>>  User Not Found!!");
                        }
                        break;
                    case 0:
                        System.out.println();
                        System.out.println("Thank You for Visiting Online Nabil Bank platform,");
                        System.out.println("Have a Wonderful & Nice Day ahead.");
                        System.out.print("System Exiting");
                        Nabil.systemExisting();
                        break;
                    default:
                        System.out.println("\n>>  Please enter a valid choice!!");
                        break;
                }

            }while (choice1 != 0);

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
    public void systemExisting() {
        int i = 5;
        while (i != 0) {
            System.out.print(".");
            try {
                Thread.sleep(450);
            } catch (InterruptedException e) {
                e.printStackTrace(); // handling the interruption
            }
            i--;
        }
    }
}
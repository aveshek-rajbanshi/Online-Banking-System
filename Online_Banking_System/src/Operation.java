import java.math.BigInteger;
import java.sql.Connection;
import java.util.Scanner;

public class Operation {
    private final Scanner scanner;
    //private final Connection conn;
    private BigInteger userAccountNumber = BigInteger.ZERO;

    public Operation(Connection conn, Scanner scanner){
       // this.conn = conn;
        this.scanner = scanner;
    }

    public Operation(Scanner scanner){
        this.scanner = scanner;
    }

    public void atmOperation(BigInteger userAccountNumber){
        this.userAccountNumber = userAccountNumber;

            int choice3;
            System.out.println();
            System.out.println("\n****  ATM Operation  ****");
            System.out.println("\t 1. Debit Account");
            System.out.println("\t 2. Credit Account");
            System.out.println("\t 3. Transfer Amount");
            System.out.println("\t 4. Check Balance");
            System.out.println("\t 5. Logout");
            System.out.println();
            System.out.print("--> Enter your choice: ");
            choice3 = scanner.nextInt();

            switch (choice3) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
                    break;

            }

    }
}

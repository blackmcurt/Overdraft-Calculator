import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

/*
This class contains the full process for the Overdraft Calculator. It takes input from the user, checks if it is valid, and then reports on the calculations done from those inputs.
 */
public class Calculator {
    private BufferedReader reader;
    private double interestRate = 0.139; //For the cases where an account exceeds the credit limit,
    // I have gone with the assumption that a percentage fee is added on top of the amount that exceeds the credit limit
    private int step = 0; //global variable used to determine what step the program is up to in the procedure

    public Calculator() {
        start();
    }

    /*
    The introductory and variable gathering method. Variables are initialised and collected from user input. This method
    also lays out the order that the program goes through, using the step variable to distinguish between separate steps.
     */
    public void start() {
        String name = "";
        double balance = 0;
        double spending = 0;
        double credit = 0;
        try {
            while (step < 5) {
                if (step ==0){
                    reader = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("-------The Overdraft Calculator-------");
                    System.out.println();
                    System.out.println("At any point during input, input 'b' to go back one step, or 'r' to restart.");
                    step++;
                }
                if (step == 1) {
                    System.out.println("Please input the customer's account name:");
                    name = reader.readLine();
                    step++;
                }
                if (step == 2) {
                    System.out.println("Please input the account balance at the start of the month:");
                    balance = checkInputFormat();
                    step++;
                }
                if (step == 3) {
                    System.out.println("Please input the total spending for the whole month");
                    spending = checkInputFormat();
                    step++;
                }
                if (step == 4) {
                    System.out.println("Please input the credit limit for this account:");
                    credit = checkInputFormat();
                    step++;
                }
                if (step == 5) {
                    reporting(name,balance,spending,credit);
                }
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public double checkInputFormat() {//checks that the input is a either a command (back/restart) or a number with 2 decimal places maximum
        while (true) {
            try {
                String input = reader.readLine();
                input.replace("\n","");
                input.replace(" ","");
                input = input.toLowerCase();
                if (input.contains("$")) { //can handle the case where a "$" is entered at the start
                    input =input.replace("$", "");
                }
                if (input.equals("b") || input.equals("back")) { //if user made a mistake, and wishes to go back to previous step
                    step=step-2;
                    return 0;
                }
                if (input.equals("r") || input.equals("restart")) { //if user wishes to restart the input process
                    step=0;
                    return 0;
                }
                double numInput = Double.parseDouble(input);
                if(BigDecimal.valueOf(numInput).scale() > 2){ //number inputted must be
                    System.out.println("Please input a number with a maximum of 2 decimal places");
                } else {
                    return numInput;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException n) {
                System.out.println("Please input a number with a maximum of 2 decimal places");
            }
        }
    }

    /*
    This method does the calculations and reports to the user on the current state of the account.
    */
    public void reporting(String name, double balance, double spending, double credit){
        double endBalance = balance - spending;
        double finalValue = endBalance + credit;
        System.out.println("-----Report-----");
        System.out.println();
        if (finalValue > 0) {
            System.out.println("The account named " + name + " has not exceeded their credit limit.");
            if (endBalance > 0) {
                System.out.println("With a starting balance of $" + balance + " and a total spending of $" + spending + ", this account is not in overdraft.");
                System.out.println("This account has $" + endBalance +" left.");
            } else {
                System.out.println("With a starting balance of $" + balance + " and a total spending of $" + spending + ", this account is in overdraft but has not exceeded the" +
                        "credit limit of $" + credit + ".");
            }
            System.out.println("This account still has $" + finalValue + " available until the credit limit is reached.");
        } else {
            System.out.println("The account named " + name + " has exceeded their credit limit by $" + Math.abs(finalValue));
            System.out.println("Applying an interest rate of " + String.format("%.2f", interestRate * 100.0) + "% on the exceeded amount, the total interest charged is $"
                    + String.format("%.2f", (Math.abs(finalValue) * interestRate)));
            System.out.println("Adding the charged interest, the new exceeded total is $" + String.format("%.2f", (Math.abs(finalValue) + Math.abs(finalValue) * interestRate)));
        }
    }

}

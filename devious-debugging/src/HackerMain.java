import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class HackerMain {

    public static void main(String[] args) {
        Scanner studentInputReader = new Scanner(System.in);

        stealAdminPasscode(studentInputReader);
        bypassFirewall();
        bypass2FactorAuthentication();
        commitCreditFraud();
        frameSomeoneElse(studentInputReader);

        printEquifaxDatabase();
    }

    private static void stealAdminPasscode(Scanner studentInputReader) {
        // Don't even try to figure out what this line is doing, just use the debugger to see the value after it executes
        long passcodeValue = (8 * Math.random()) > 4 ? 65535 >> 2 + 1 * (int) (4 * Math.random()) : 7 + 32 << 9 & 2 | 12;
        long studentAnswer = studentInputReader.nextLong();

        if (studentAnswer != passcodeValue) {
            System.out.println("Your input value does not match the passcode, you have been locked out of the system");
            System.exit(-1);
        }
    }

    private static void bypassFirewall() {
        boolean firewall = true; // Airtight security
        while (firewall) {
            System.out.println("This loop will not stop until you change the firewall's value to false");
        }
    }

    // This method calls a random method from the "SecurityQuestions" class
    // Don't worry about how it does that, you really don't need to understand the code
    private static void bypass2FactorAuthentication() {
        List<Method> randomSecQuestions = Arrays.asList(SecurityQuestions.class.getDeclaredMethods());

        long systemTime = System.currentTimeMillis();
        Random rng = new Random();
        rng.setSeed(systemTime);

        try {
            // This line is written this way so only "Step Into" can show what SecurityQuestion method is being called
            randomSecQuestions.get(rng.nextInt(randomSecQuestions.size())).invoke(null);

            // Use "Step Over" to advance past when this randomSecurityAnswer variable is declared
            // Overwrite this variable using the debugger with what the return value of the method you saw was
            String randomSecurityAnswer = "Edit this value";

            // The exact same method you "Stepped Into" will be called again to check that you wrote the right return value
            rng.setSeed(systemTime);
            if (!randomSecurityAnswer.equals(randomSecQuestions.get(rng.nextInt(randomSecQuestions.size())).invoke(null))) {
                System.out.println("The random method that was called returned a different value than your input, please try again");
                System.exit(-1);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            // These exceptions should absolutely never happen with the methods we are using
            // About the only way these could happen is if someone hacked too hard and threw an exception from the debugger
            System.exit(-1);
        }
    }

    private static void commitCreditFraud() {
        // Create the BankAccount database with 1000 accounts in random order
        List<BankAccount> randomBankAccounts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            randomBankAccounts.add(new BankAccount(i, (int) (Math.random() * 5000)));
        }
        Collections.shuffle(randomBankAccounts);

        // You happen to know your BankAccount has ID 126, give yourself at least one million dollars
        // You COULD press "Step Over" or "Resume" ~500 times, but it is much easier to just use a conditional breakpoint

        for (BankAccount currentBankAccount : randomBankAccounts) {
            if (currentBankAccount.getId() == 126 && currentBankAccount.getMoney() < 1000000) {
                System.out.println("Your bank account with ID 126 does not have one million dollars, please try again");
                System.exit(-1);
            }
        }
    }

    private static void frameSomeoneElse(Scanner studentInputReader) {
        long systemTime = System.currentTimeMillis();
        Random userAccessLog = new Random();
        userAccessLog.setSeed(systemTime);

        // Use "Evaluate Expression" to see what userAccessLog.nextInt(241374) will return before it is called in the code
        // Find out what account you're going to frame
        int fakeAccountId = studentInputReader.nextInt();

        // Resetting the seed ensures the next call to userAccessLog.nextInt(241374) returns the same value you saw
        userAccessLog.setSeed(systemTime);

        if (fakeAccountId != userAccessLog.nextInt(241374)) {
            System.out.println("You failed to frame an unsuspecting victim, please try again");
            System.exit(-1);
        }
    }

    private static void printEquifaxDatabase() {
        /* In order to prevent students from seeing how we generate their unique output String and just running the algorithm
            themselves, the class that outputs the String is loaded in dynamically from compiled bytes
           Using your new knowledge of the debugger, it would still be extremely difficult to reverse engineer
            how the unique String is generated
        */
        FinalOutputClassLoader finalOutputClassLoader = new FinalOutputClassLoader();
        FinalOutputPrinter finalOutputPrinter = finalOutputClassLoader.loadFinalOutputPrinter();
        finalOutputPrinter.printFinalOutput();
    }
}

package Activities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class Activity2
{
   /* @Test
    BankAccount ba=new BankAccount(100);
*/

//class ExpectedExceptionTest
    @Test
    void notEnoughFunds()
    {
        // Create an object for BankAcc class
        BankAccount acc = new BankAccount(9);

        // Assertion for exception
        assertThrows(NotEnoughFundsException.class, () -> acc.withdraw(10),
                "Balance should be greator than amount of withdrawal");
    }

    @Test
    void enoughFunds()
    {
        // Create an object for BankAccount class
        BankAccount acc = new BankAccount(100);

        // Assertion for no exceptions
        assertDoesNotThrow(() -> acc.withdraw(100));
    }
}
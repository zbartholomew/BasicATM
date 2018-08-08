package com.zbartholomew;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * TODO: class description
 *
 * @author Zach Bartholomew
 */
public class AccountTest
{
    // Good place to mock this using a mocking framework like Mockito since we are not testing the Customer class here
    private Customer customer = new Customer("johnsmith", "123456");

    @Test
    public void testGetAccountForUser()
    {
        Account account = Account.getAccountForUser(customer);
        Assert.assertEquals(customer.getAccount(), account);
        Assert.assertEquals(BigDecimal.ZERO, account.getBalance());
        Assert.assertEquals(account.getAccountCustomer(), customer);
        Assert.assertNotNull(account.getUuid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTransactionDailyLimit()
    {
        Account account = customer.getAccount();
        int sum = 0;
        Transaction t1 = new Transaction(50, account);
        sum += t1.getAmount();
        Assert.assertEquals(BigDecimal.valueOf(sum), account.getBalance());
        Transaction t2 = new Transaction(500, account);
        sum += t2.getAmount();
        Assert.assertEquals(BigDecimal.valueOf(sum), account.getBalance());
        Transaction t3 = new Transaction(1000, account);
        sum += t3.getAmount();
        Assert.assertEquals(BigDecimal.valueOf(sum), account.getBalance());

        new Transaction(-601, account);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTransactionDailyLimitMultipleWithdraws()
    {
        Account account = customer.getAccount();
        int sum = 0;
        Transaction t1 = new Transaction(5000, account);
        sum += t1.getAmount();
        Assert.assertEquals(BigDecimal.valueOf(sum), account.getBalance());
        Transaction t2 = new Transaction(-200, account);
        sum += t2.getAmount();
        Assert.assertEquals(BigDecimal.valueOf(sum), account.getBalance());
        Transaction t3 = new Transaction(-300, account);
        sum += t3.getAmount();
        Assert.assertEquals(BigDecimal.valueOf(sum), account.getBalance());

        new Transaction(-400, account);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTransactionNegativeBalance()
    {
        Account account = customer.getAccount();
        int sum = 0;
        Transaction t1 = new Transaction(100, account);
        sum += t1.getAmount();
        Assert.assertEquals(BigDecimal.valueOf(sum), account.getBalance());
        new Transaction(-400, account);
    }
}
package com.zbartholomew;

import org.junit.Assert;
import org.junit.Test;

/**
 * TODO: class description
 *
 * @author Zach Bartholomew
 */
public class TransactionTest
{
    // Good place to mock this using a mocking framework like Mockito since we are not testing the Customer class here
    private Customer customer = new Customer("johnsmith", "123456");

    @Test
    public void testConstructorGood()
    {
        Assert.assertNotNull(customer.getAccount());
        Transaction transaction = new Transaction(50, customer.getAccount());
        Assert.assertNotNull("Timestamp should not be null", transaction.getTimestamp());
        Assert.assertNotNull("UUID should not be null", transaction.getUuid());
        Assert.assertEquals("Customer account should be same as Transaction account", customer.getAccount(), transaction.getAccount());
        Assert.assertEquals("Amount should equal transaction amount", 50, transaction.getAmount());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNull()
    {
        new Transaction(50, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorAmountOverBalance()
    {
        Assert.assertNotNull(customer.getAccount());
        new Transaction(-60, customer.getAccount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeAmount()
    {
        Assert.assertNotNull(customer.getAccount());
        new Transaction(5000, customer.getAccount());
        new Transaction(-601, customer.getAccount());
    }
}
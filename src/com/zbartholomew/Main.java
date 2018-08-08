package com.zbartholomew;

public class Main
{
    public static void main(String[] args)
    {
        generateDummyData();
        new ATM();
    }

    public static void generateDummyData()
    {
        Customer dummyCustomer1 = new Customer("johnsmith", "1234");
        new Transaction(500, dummyCustomer1.getAccount());
        Customer dummyCustomer2 = new Customer("johndoe", "1111");
        new Transaction(6000, dummyCustomer2.getAccount());
        Customer dummyCustomer3 = new Customer("billybob", "2222");
        new Transaction(5, dummyCustomer3.getAccount());
    }
}

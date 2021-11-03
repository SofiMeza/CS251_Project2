/**
 * CS 251: Data Structures and Algorithms
 * Project 2: Part2Test
 *
 * @author Chirayu Garg
 * @username garg104
 * @sources -
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Part2Test {
    public static ArrayList<CustomerOrder> customers;
    public static int index;
    public static int time = 0;
    public static int testNum = 0;
    public static ArrayList<CustomerOrder> list;


    public static void main(String[] args) throws NoSuchAlgorithmException {
        customers = new ArrayList<CustomerOrder>();
        int capacity = 100;
        getCustomers(capacity);
        ArrayList<CustomerOrder> list = new ArrayList<CustomerOrder>();
        double score = 0;

        //Test 1
        testNum++;
        score += test1();
        
        score = Math.max(0.0, score);

        if (score == 12.0) {
            System.out.println("\nTests for part 2 passed");
            System.out.println("\nTotal score for Part 2: " + 25.0 + "/25.0");
        } else {
            System.out.println("\nTests for part 2 failed");
            System.out.println("\nTotal score for Part 2: " + 0.0 + "/25.0");
        }

    }

    private static double test1() throws NoSuchAlgorithmException {
        System.out.println("\n*****Begin Test " + testNum + "*****");

        Random gen = new Random(System.currentTimeMillis());
        int capacity = gen.nextInt(51) + 100;
        CustomerOrderHash table = new CustomerOrderHash(capacity);

        double score = 0;
        index = gen.nextInt(10);

        //check operations on empty HT
        System.out.println("\nHT is empty...");
        if (table.size() == 0) {
            score += 1.0;//1.0
        } else {
            printMsg(false, "size");
        }

        // generate customers
        getCustomers(capacity);

        //insert some customers
        System.out.println("Adding some customers...");
        int num = gen.nextInt(20) + 10;
        list = new ArrayList<>();
        insertCustomers(table, num);

        if (table.size() == list.size()) {
            score += 1.0;//2.0
        } else {
            printMsg(false, "size");
        }

        //remove some customer from table
        boolean pass = true;

        System.out.println("Remove some Customers...");
        pass = true;
        for (int k = 0; k < gen.nextInt(4) + 5 && pass; k++) {
            int j = gen.nextInt(list.size());
            CustomerOrder p1 = list.remove(j);
            CustomerOrder p2 = table.remove(p1.getName());
            if (p1 != p2) {
                pass = false;
                System.out.println("Customers removed do not match.");
                printExpAct(p1.toString(), p2.toString());
            }
            if (list.size() != table.size()) {
                pass = false;
                System.out.println("Sizes do not match.");
                printExpAct(Integer.toString(list.size()), Integer.toString(table.size()));
            }//7.0
        }
        if (pass) {
            score += 2.0;
        } else {
            printMsg(false, "remove");
        }


        //add the rest of the customer
        System.out.println("Adding the rest of the customers...");
        insertCustomers(table, customers.size() - (index + num));


        if (table.size() == list.size()) {
            score += 1.0;//3.0
        } else {
            printMsg(false, "size");
        }

        //check get for a few random customer in table
        System.out.println("Search for some random Customers...");

        for (int k = 0; k < gen.nextInt(30) + 5; k++) {
            int j = gen.nextInt(list.size());
            CustomerOrder p1 = list.get(j);
            String s = p1.getName();
            CustomerOrder p2 = table.get(s);
            if (p1 != p2) {
                System.out.println("Customers do not match");
                printExpAct(p1 == null ? "null" : p1.toString(), p2 == null ? "null" : p2.toString());
                pass = false;
            }
        }

        if (!pass) {
            printMsg(false, "get");
        }
        else {
            score += 2.0; //5.0
        }

        //remove some customer from table
        System.out.println("Remove some Customers...");
        pass = true;
        for (int k = 0; k < gen.nextInt(30) + 5 && pass; k++) {
            int j = gen.nextInt(list.size());
            CustomerOrder p1 = list.remove(j);
            CustomerOrder p2 = table.remove(p1.getName());
            if (p1 != p2) {
                pass = false;
                System.out.println("Customers removed do not match.");
                printExpAct(p1.toString(), p2.toString());
            }
            if (list.size() != table.size()) {
                pass = false;
                System.out.println("Sizes do not match.");
                printExpAct(Integer.toString(list.size()), Integer.toString(table.size()));
            }//7.0
        }
        if (pass) {
            score += 2.0;
        } else {
            printMsg(false, "remove");
        }

        //search for a customer not in table
        System.out.println("Search for a Customer not in table...");
        String name = "Customer Name";
        CustomerOrder p1 = new CustomerOrder(name, 0, 0);
        CustomerOrder p2 = table.get(name);
        if (p2 == null) {
            score += 1.0;//8.0
        } else {
            printMsg(false, "get");
            printExpAct("null", p2.toString());
        }
        p2 = table.remove(name);
        if (p2 == null) {
            score += 2.0;//10.0
        } else {
            printMsg(false, "remove");
            printExpAct("null", p2.toString());
        }

        ArrayList<CustomerOrder>[] temp = table.getArray();
        if (temp.length != getNextPrime(capacity)) {
            System.out.println("Incorrect Hashtable");
            score = 0.0;
        }

        System.out.println("Total score for test " + testNum + ": " + (score/4.0) + "/3.0");
        return score;
    }

    private static void printMsg(boolean passed, String method) {
        if (passed)
            System.out.println(method + " passed");
        else
            System.out.println(method + " failed");
    }

    private static void printExpAct(String exp, String act) {
        System.out.println("Expected: " + exp);
        System.out.println("Actual: " + act);
    }

    private static void insertCustomers(CustomerOrderHash cq, int num) throws NoSuchAlgorithmException {
        int count = 0;
        while (count < num && index < customers.size()) {
            CustomerOrder c = customers.get(index);
            cq.put(c);
            incIndex();
            list.add(c);
            count++;
        }
    }

    private static void getCustomers(int capacity) {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 7;
        Random random = new Random();

        for (int i = 0; i < capacity; i++) {
            String generatedString = random.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            generatedString = generatedString + " " + random.ints(leftLimit, rightLimit + 1)
                    .limit(5)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString() + " " + i;

            CustomerOrder order = new CustomerOrder(generatedString, time, -1);
            time++;
            customers.add(order);

        }

    }

    private static void incIndex() {
        index = (index + 1) % customers.size();
    }

    //get the next prime number p >= num
    private static int getNextPrime(int num) {
        if (num == 2 || num == 3)
            return num;

        int rem = num % 6;

        switch (rem) {
            case 0:
            case 4:
                num++;
                break;
            case 2:
                num += 3;
                break;
            case 3:
                num += 2;
                break;
        }

        while (!isPrime(num)) {
            if (num % 6 == 5) {
                num += 2;
            } else {
                num += 4;
            }
        }

        return num;
    }

    //determines if a number > 3 is prime
    private static boolean isPrime(int num) {
        if (num % 2 == 0) {
            return false;
        }

        int x = 3;

        for (int i = x; i < num; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }

        return true;
    }

}




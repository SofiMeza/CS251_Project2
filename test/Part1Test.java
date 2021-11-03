/**
 * CS 251: Data Structures and Algorithms
 * Project 2: Part1Test
 *
 * @author Chirayu Garg
 * @username garg104
 * @sources -
 */


import java.util.*;
import java.util.Random;


public class Part1Test {
    public static ArrayList<CustomerOrder> customers;
    public static int index;
    public static int time = 0;
    public static int testNum = 0;

    public static void main(String[] args) {
        customers = new ArrayList<CustomerOrder>();
        int capacity = 100;
        getCustomers(capacity);
        ArrayList<CustomerOrder> list = new ArrayList<CustomerOrder>();
        double score = 0;

        //Test 1
        testNum++;
        score += test1_2( 32, 25, list);

        //Test 2
        testNum++;
        score += test1_2(7, customers.size(), list);


        System.out.println("\n**********************");

        score = Math.max(0.0, score);

        if (score == 18.0) {
            System.out.println("\nTotal score for Part 1: 25.0/25.0");
        } else {
            System.out.println("\nTotal score for Part 1: 0.0/25.0");
        }
    }

    private static double test1_2(int i, int cap, ArrayList<CustomerOrder> list) {
        System.out.println("\n*****Begin Test " + testNum + "*****");
        index = i;
        CustomerOrderQueue cq = new CustomerOrderQueue(cap);
        double score = 0;
        //check operations on empty cq
        System.out.println("\nCQ is empty...");
        if (cq.size() == 0) {
            score += 0.5;//0.5
        } else {
            printMsg(false, "size");
        }

        if (cq.isEmpty()) {
            score += 0.5;//1.0
        } else {
            printMsg(false, "isEmpty");
        }

        if (cq.getMax() == null) {
            score += 0.5;//1.5
        } else {
            printMsg(false, "getMax");
        }

        if (cq.delMax() == null) {
            score += 0.5;//2.0
        } else {
            printMsg(false, "delMax");
        }

        //insert some customers
        System.out.println("Adding some customers...");
        insertCustomers(list, cq, (cap / 2));
        if (cq.size() == list.size()) {
            score += 0.5;//2.5
        } else
            printMsg(false, "size");
        if (!cq.isEmpty()) {
            score += 0.5;//3.0
        } else
            printMsg(false, "isEmpty");
        if (cq.getMax() == list.get(0)) {
            score += 1.0;//4.0
        } else {
            printMsg(false, "getMax");
        }
        if (cq.delMax() == list.remove(0) && cq.size() == list.size()) {
            score += 1.0;//5.0
        } else
            printMsg(false, "delMax");

        //fill up the queue
        System.out.println("Filling up the cq...");
        insertCustomers(list, cq, cap - list.size());
        if (!checkPriorityQueue(cq, 0)) {
            System.out.println("INCORECT PRIORITY QUEUE");
            score = 0.0;
        }

        if (cq.size() == list.size()) {
            score += 0.5;//5.5
        } else
            printMsg(false, "size");
        if (cq.insert(customers.get(index)) == -1 && cq.size() == list.size()) {
            score += 1.0;//6.5
        } else
            printMsg(false, "insert");

        //remove all remaining customers
        boolean pass = true;
        while (!list.isEmpty()) {
            CustomerOrder lc = list.remove(0);
            CustomerOrder qc = cq.delMax();
            if (lc != qc) {
                pass = false;
                System.out.println("Max customers not equal");
                printExpAct(lc.toString(), qc.toString());
                break;
            }
            if (!checkPriorityQueue(cq, 0)) {
                System.out.println("INCORECT PRIORITY QUEUE");
                score = 0.0;
            }
        }
        if (!cq.isEmpty())
            pass = false;
        if (pass) {
            score += 2.5;//9.0
        } else
            printMsg(false, "delMax until empty");
        System.out.println("\nTotal score for Test " + testNum + ": " + score);
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

    private static void insertCustomers(ArrayList<CustomerOrder> list, CustomerOrderQueue cq, int num) {
        int count = 0;
        while (count < num) {
            CustomerOrder c = customers.get(index);
            cq.insert(c);
            insertToList(list, c);
            incIndex();
            count++;
        }
    }

    private static void insertToList(ArrayList<CustomerOrder> list, CustomerOrder c) {
        for (int i = 0; i < list.size(); i++) {
            if (c.compareTo(list.get(i)) > 0) {
                list.add(i, c);
                return;
            }
        }
        list.add(c);
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

    private static boolean checkPriorityQueue(CustomerOrderQueue cq, int i) {
        int left = (2 * i) + 1;
        int right = (2 * i) + 2;
        CustomerOrder[] temp = cq.getOrderList();

        boolean checkLeft = false;
        boolean checkRight = false;

        if (left < cq.getNumOrders() && temp[left].getOrderDeliveryTime() >= temp[i].getOrderDeliveryTime()) {
            checkLeft = checkPriorityQueue(cq, left);
        } else if (left >= cq.getNumOrders() || temp[left] == null) {
            checkLeft = true;
        } else {
            return false;
        }

        if (right < cq.getNumOrders() && temp[right].getOrderDeliveryTime() >= temp[i].getOrderDeliveryTime()) {
            checkRight = checkPriorityQueue(cq, right);
        } else if (right >= cq.getNumOrders() || temp[right] == null) {
            checkRight = true;
        } else {
            return false;
        }

        return checkLeft && checkRight;
    }

}



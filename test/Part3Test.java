/**
 * CS 251: Data Structures and Algorithms
 * Project 2: Part3Test
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

public class Part3Test {
    public static ArrayList<CustomerOrder> customers;
    public static int index;
    public static int time = 0;
    public static int testNum = 0;
    private static ArrayList<CustomerOrder> list;

    
    public static void main(String[] args) throws NoSuchAlgorithmException {
        int passed = 0;
        int fail = 0;
        for(int k  =0; k <1000; k++) {
            Random gen = new Random(System.currentTimeMillis());
            customers = new ArrayList<>();
            int capacity = gen.nextInt(51) + 100;
            index = 0;
            time = 0;
            getCustomers(capacity);
            BetterCustomerOrderQueue cq = new BetterCustomerOrderQueue(capacity);
            double score = 0.0;

            //insert some customers
            System.out.println("Adding some Customers...");
            int num = gen.nextInt(20) + 35;
            list = new ArrayList<>();
            insertCustomers(cq, num);


            if (list.size() != cq.size()) {
                System.out.println("Sizes do not match after inserts!");
                score = -1.0;
            }
            //check posInQueue
            boolean pass = checkPosInQueue(cq);
            if (pass) {
                score += 5.0;//5.0
            } else {
                printMsg(false, "posInQueue after inserts");
            }

            //check posInQueue after delMax
            System.out.println("Deleting max Customer...");
            if (list.size() == 0) {
                System.out.println(list);
            }
            CustomerOrder max1 = list.remove(0);
            CustomerOrder max2 = cq.delMax();
            if (max1 == max2 && max2.getPosInQueue() == -1) {
                score += 2.0;//7.0
            } else {
                printMsg(false, "posInQueue after delMax");
            }
            if (list.size() != cq.size())
                System.out.println("Sizes do not match after delMax!");

            //update a Customer to highest investment
            System.out.println("Update a Customer to highest investment...");
            int i = gen.nextInt(list.size() / 2) + list.size() / 2 - 1;
            CustomerOrder c = list.remove(i);

            cq.update(c.getName(), cq.getMax().getOrderDeliveryTime() - 1);
            if (c == cq.delMax()) {
                score += 2.0;//9.0
            } else {
                printMsg(false, "update to max");
            }
            if (list.size() != cq.size()) {
                System.out.println("Sizes do not match after update!");
                score = -1.0;
            }


            //remove a Customer
            System.out.println("Removing a Customer...");
            i = gen.nextInt(list.size() / 2) + list.size() / 2 - 1;
            c = list.remove(i);
            CustomerOrder c2 = cq.remove(c.getName());
            if (c == c2 && c2.getPosInQueue() == -1 && !inQueue(c2, cq)) {
                score += 2.0;//11.0
            } else {
                printMsg(false, "remove");
                printExpAct(c.toString(), c2.toString());
                System.out.println("If the Customers match, check that posInQueue is set to -1 and that the Customer is really removed from the queue!");
            }
            if (list.size() != cq.size()) {
                System.out.println("Sizes do not match after update!");
                score = -1.0;
            }

            // checkPriorityQueue
            if (!checkPriorityQueue(cq, 0)) {
                System.out.println("INCORECT PRIORITY QUEUE");
                score = 0.0;
            }


            //remove several Customers
            System.out.println("Removing several Customers...");
            pass = true;
            for (int j = 0; j < gen.nextInt(10) + 10; j++) {
                i = gen.nextInt(list.size());
                c = list.remove(i);
                c2 = cq.remove(c.getName());
                if (c != c2) {
                    printMsg(false, "remove several");
                    pass = false;
                    printExpAct(c == null ? "null" : c.toString(), c2 == null ? "null" : c2.toString());
                    break;
                }
                // checkPriorityQueue
                if (!checkPriorityQueue(cq, 0)) {
                    System.out.println("INCORECT PRIORITY QUEUE");
                    score = 0.0;
                }
            }
            if (pass) {
                score += 2.0;//13.0
            }
            if (list.size() != cq.size()) {
                System.out.println("Sizes do not match after update!");
                score = -1.0;
            }
            //check the posInQueue
            pass = checkPosInQueue(cq);
            if (pass) {
                score += 3.0;//16.0
            } else
                printMsg(false, "posInQueue after removing several Customers");


            // checkPriorityQueue
            if (!checkPriorityQueue(cq, 0)) {
                System.out.println("INCORECT PRIORITY QUEUE");
                score = 0.0;
            }


            //update several
            System.out.println("Updating several Customers...");
            int[] diffs = new int[]{-3, -2, -1, 1, 2, 3};
            for (int j = 0; j < 10; j++) {
                i = gen.nextInt(list.size());
                while (i < 0) {
                    System.out.println(i);
                    i = gen.nextInt(list.size());
                }
                int diff = diffs[gen.nextInt(diffs.length)];
                int newDeliveryTime = list.get(i).getOrderDeliveryTime() + diff;
                if (newDeliveryTime < list.get(i).getOrderPlacedTime())
                    newDeliveryTime += 5;
                cq.update(list.get(i).getName(), newDeliveryTime);
                updateInList(i, newDeliveryTime);
            }
            pass = checkPosInQueue(cq);
            if (pass) {
                score += 4.0;//20.0
            } else {
                printMsg(false, "posInQueue after updates");
            }
            if (list.size() != cq.size()) {
                System.out.println("Sizes do not match after update!");
                score = -1.0;
            }

            if (!checkPriorityQueue(cq, 0)) {
                System.out.println("INCORECT PRIORITY QUEUE");
                score = 0.0;
            }


            // insert some more
            System.out.println("Inserting more Customers...");
            index += 20;
            insertCustomers(cq, customers.size() - index);

            if (list.size() != cq.size()) {
                System.out.println("Sizes do not match after update!");
                score = -1.0;
            }
            //check posInQueue
            pass = checkPosInQueue(cq);
            if (!pass) {
                score = -1.0;
                printMsg(false, "posInQueue after more inserts");
            }

            if (!checkPriorityQueue(cq, 0)) {
                System.out.println("INCORECT PRIORITY QUEUE");
                score = 0.0;
            }


            //delMax until empty
            System.out.println("Deleting max till empty...");
            pass = true;
            while (!list.isEmpty()) {
                CustomerOrder pExp = list.get(0);
                CustomerOrder pAct = cq.getMax();
                if (pExp != pAct) {
                    printMsg(false, "delMax until empty");
                    printExpAct(pExp.toString(), pAct.toString());
                    pass = false;
                    break;
                } else {
                    cq.delMax();
                    list.remove(0);
                }
                if (list.size() != cq.size()) {
                    System.out.println("Sizes do not match after update!");
                    score = -1.0;
                }
            }
            if (pass) {
                score += 5.0;//25.0
            }


            if (score == 25.0) {
                System.out.println("\nTotal score for Part 3: 25.0/25.0");
                passed++;
            } else {
                System.out.println("\nTotal score for Part 3: 0.0/25.0");
                fail++;
            }
        }

        System.out.println("passed: " + passed);
        System.out.println("failed: " + fail);

    }


    
    private static void updateInList(int i, int newOrderDeliveryTime) {
        CustomerOrder c = list.remove(i);
        c.setOrderDeliveryTime(newOrderDeliveryTime);
        insertToList(c);
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

    private static void insertCustomers(BetterCustomerOrderQueue cq, int num) throws NoSuchAlgorithmException {
        int count = 0;
        while (count < num && index < customers.size()) {
            CustomerOrder c = customers.get(index);
            cq.insert(c);
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

    private static boolean checkPosInQueue(BetterCustomerOrderQueue cq) {
        CustomerOrder[] array = cq.getOrderList();
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                if (array[i].getPosInQueue() != i) {
                    System.out.println("posInQueue does not match index");
                    printExpAct("" + i, "" + array[i].getPosInQueue());
                    return false;
                }
            }
        }
        return true;
    }

    private static void insertToList(CustomerOrder p) {
        for (int i = 0; i < list.size(); i++) {
            if (p.compareTo(list.get(i)) > 0) {
                list.add(i, p);
                return;
            }
        }
        list.add(p);
    }

    //checks if Customer c is currently in the queue
    private static boolean inQueue(CustomerOrder c, BetterCustomerOrderQueue cq) {
        CustomerOrder[] array = cq.getOrderList();
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && array[i].getName().equals(c.getName()))
                return true;
        }
        return false;
    }

    private static boolean checkPriorityQueue(BetterCustomerOrderQueue cq, int i) {
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



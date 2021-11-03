import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

/**
 * CS 251: Data Structures and Algorithms
 * Project 2: Part4Test
 *
 * @author Chirayu Garg
 * @username garg104
 * @sources -
 */

public class Part4Test {
    private static ArrayList<CustomerOrder> customers;
    public static ArrayList<CustomerOrder> inQueue;//Customers in the queue
    public static ArrayList<CustomerOrder> list;
    private static int time;
    private static OrderSystemModel model;
    private static int processTime;



    public static void main(String[] args) throws NoSuchAlgorithmException {
        boolean flag = true;
        Random random = new Random();
        int capacity = random.nextInt(51) + 50;
        time = 0;
        processTime = 0;
        customers = new ArrayList<>();
        inQueue = new ArrayList<>();
        list = new ArrayList<>();
        model = new OrderSystemModel(capacity);


        getCustomers(capacity);

        // process few customers

        int num = random.nextInt(20) + 5;
        for (int i = 0; i < num; i++) {
            process();
        }

        if (inQueue.size() != model.getOrderList().size()) {
            System.out.println("Incorrect size after precessing a few customers");
            flag = false;
        }


        while ((!customers.isEmpty() || !inQueue.isEmpty()) && flag) {
            int p = random.nextInt(100);
            if (p <= 30) {
                System.out.println("Processing a customer");
                process();
            } else if (p <= 60) {
                System.out.println("Completing next order");
                nextOrder();
            } else if (p <= 70) {
                System.out.println("Cancelling an order");
                if (cancelOrder() == -1) {
                    System.out.println("cancel order is incorrect");
                    flag = false;
                    break;
                }
            } else {
                System.out.println("Updating an order");
                updateOrder();
            }
        }


        if (model.getOrdersDelayed() + model.getOrdersCanceled() + model.getOrdersOnTime() != capacity) {
            flag = false;
        }

        if (!flag) {
            System.out.println("\nTotal score for Part 4: " + 0.0 + "/25.0");
        } else {
            System.out.println("\nTotal score for Part 4: " + 25.0 + "/25.0");
        }

    }




    private static void process() throws NoSuchAlgorithmException {
        if (customers.isEmpty())
            return;
        Random gen = new Random();
        int i = gen.nextInt(customers.size());
        CustomerOrder c = customers.remove(i);
        String name = model.process(c.getName(), c.getOrderPlacedTime(), c.getOrderDeliveryTime());
        if (name != null) {
            inQueue.add(c);
            if (!name.equals(c.getName()))
                removeFromList(name);
        }
    }

    private static void nextOrder() throws NoSuchAlgorithmException {
        if (inQueue.isEmpty()) {
            return;
        }
        String name = model.completeNextOrder();
        if (name != null) {
            removeFromList(name);
        }
    }

    private static int cancelOrder() throws NoSuchAlgorithmException {
        if (inQueue.isEmpty()) {
            return 0;
        }
        Random gen = new Random();
        int i = gen.nextInt(inQueue.size());
        CustomerOrder c = inQueue.remove(i);
        c.setPosInQueue(-1);
        CustomerOrder canceled = model.cancelOrder(c.getName());

        return ((canceled.equals(c)) ? 1 : -1);
    }

    private static void updateOrder() throws NoSuchAlgorithmException {
        if (inQueue.isEmpty()) {
            return;
        }
        Random gen = new Random();
        int i = gen.nextInt(inQueue.size());
        int[] diffs = new int[]{-6, -5, -4, -3, -2, -1, 1, 2, 3, 4, 5, 6};
        int diff = diffs[gen.nextInt(diffs.length)];
        int newDeliveryTime = inQueue.get(i).getOrderDeliveryTime() + diff;
        if (newDeliveryTime <= inQueue.get(i).getOrderPlacedTime()) {
            newDeliveryTime += 7;
        }

        inQueue.get(i).setOrderDeliveryTime(newDeliveryTime);
        String name = model.updateOrderTime(inQueue.get(i).getName(), newDeliveryTime);
        if(name != null) {
            removeFromList(name);
        }
    }


    // helper functions

    private static void insertToList(CustomerOrder p) {
        for (int i = 0; i < list.size(); i++) {
            if (p.compareTo(list.get(i)) > 0) {
                list.add(i, p);
                return;
            }
        }
        list.add(p);
    }

    private static void removeFromList(String name) {
        for (int i = 0; i < inQueue.size(); i++) {
            if (inQueue.get(i).getName().equals(name)) {
                inQueue.remove(i);
                break;
            }
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


            int prob = random.nextInt(10);
            int deliveryTime = -1;
            if (prob <= 3) {
                deliveryTime = time + random.nextInt(30) + 15;
            }


            CustomerOrder order = new CustomerOrder(generatedString, time, deliveryTime);
            time++;
            customers.add(order);
        }

    }

}

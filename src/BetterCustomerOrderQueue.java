/**
 * CS 251: Data Structures and Algorithms
 * Project 2: Part 3
 * <p>
 * TODO: Complete CustomerOrderQueue.
 *
 * @author Chirayu Garg, Sofia Meza
 * @username garg104, meza8
 * @sources TODO: list your sources here
 */

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;


public class BetterCustomerOrderQueue {
    private CustomerOrder[] orderList;
    private CustomerOrderHash table;
    private int numOrders;

    /**
     *
     * Return the CustomerOrderQueue
     *
     */
    public CustomerOrder[] getOrderList() {
        return orderList;
    }

    /**
     *
     * Return the number of orders in the queue
     *
     */
    public int getNumOrders() {
        return numOrders;
    }

    /**
     * Constructor of the class.
     * TODO: complete the default Constructor of the class
     *
     * Initialize a new CustomerOrderQueue and CustomerOrderHash
     *
     */
    public BetterCustomerOrderQueue(int capacity) {
        this.orderList = new CustomerOrder[capacity];
        this.table = new CustomerOrderHash(capacity);
        this.numOrders = 0;
    }

    /**
     * TODO: insert a new customer order.
     *
     * @return return the index at which the customer was inserted;
     * return -1 if the Customer could not be inserted
     *
     */
    public int insert(CustomerOrder c) throws NoSuchAlgorithmException {
        if (isQueueFull()) {
            return -1;
        }

        //insert element
        this.orderList[size()] = c;
        this.orderList[size()].setPosInQueue(size());
        this.numOrders = this.getNumOrders() + 1;


        //swim up if needed
        int i = size() - 1;
        while (this.orderList[i].compareTo(this.orderList[findParent(i)]) == 1 && i != 0) {
            swap(i, findParent(i));
            i = findParent(i);
        }

        this.table.put(c);

        return (size() - 1); // return index in which element was inserted
    }

    /**
     * TODO: remove the customer with the highest priority from the queue
     *
     * @return return the customer removed
     *
     */
    public CustomerOrder delMax() throws NoSuchAlgorithmException {
        if (isEmpty()) {
            return null;
        }

        CustomerOrder toReturn = this.orderList[0];
        swap(0, size()-1);
        this.orderList[size() - 1] = null;
        this.numOrders = this.numOrders - 1;
        sink(0); // to sink the root and reorganize the heap

        // remove from hashtable
        this.table.remove(toReturn.getName());

        // Set position in queue to -1
        toReturn.setPosInQueue(-1);

        return toReturn;
    }

    /**
     * TODO: return but do not remove the customer with the maximum priority
     *
     * @return return the customer with the maximum priority
     *
     */
    public CustomerOrder getMax() {
        if (isEmpty()) {
            return null;
        }
        return this.orderList[0];
    }

    /**
     * TODO: check if the priority queue is empty or not
     *
     * @return return true if the queue is empty; false else
     *
     */
    public boolean isEmpty() {
        return (this.size() == 0);
    }

    /**
     * TODO: return the number of Customers currently in the queue
     *
     * @return return the number of Customers currently in the queue
     *
     */
    public int size() {
        return this.numOrders;
    }

    /**
     *
     * TODO: return the CustomerOrder with the given name
     *
     */
    public CustomerOrder get(String name) throws NoSuchAlgorithmException {
        int hashKey = this.table.getHashKey(name);

        if (this.table.getArray()[hashKey] != null) {
            for (int i = 0; i < this.table.getArray()[hashKey].size(); i++) {
                CustomerOrder comparing = (CustomerOrder) this.table.getArray()[hashKey].get(i);
                if (comparing.getName().equals(name)) {
                    return comparing;
                }
            }
        }
        return null;
    }

    /**
     *
     * TODO: remove and return the CustomerOrder with the specified name from the queue;
     * TODO: return null if the CustomerOrder isn't in the queue
     *
     */
    public CustomerOrder remove(String name) throws NoSuchAlgorithmException {
        CustomerOrder c = get(name);

        if (c != null) {
            int oldTime = c.getOrderDeliveryTime();
            int newTime = this.orderList[size()-1].getOrderDeliveryTime();
            int index = c.getPosInQueue();
            this.table.remove(name);

            swap(index, size()-1);

            this.orderList[size() - 1] = null;
            this.numOrders = this.numOrders - 1;

            if (oldTime > newTime) {
                //swim up if needed
                int i = c.getPosInQueue();
                while (this.orderList[index].compareTo(this.orderList[findParent(index)]) == 1 && index != 0) {
                    swap(index, findParent(index));
                    index = findParent(index);
                }
            } else if (oldTime < newTime) {
                //sink down if needed
                sink(index);
            }

            c.setPosInQueue(-1);
        }

        return c;
    }

    /**
     *
     * TODO: update the orderDeliveryTime of the Customer with the specified name to newTime
     *
     */
    public void update(String name, int newTime) throws NoSuchAlgorithmException {
        CustomerOrder c = get(name);

        if (c != null) {
            int oldTime = c.getOrderDeliveryTime();
            c.setOrderDeliveryTime(newTime);

            if (oldTime > newTime) {
                //swim up if needed
                int i = c.getPosInQueue();
                while (this.orderList[i].compareTo(this.orderList[findParent(i)]) == 1 && i != 0) {
                    swap(i, findParent(i));
                    i = findParent(i);
                }
            } else if (oldTime < newTime) {
                //sink down if needed
                sink(c.getPosInQueue());
            }

        }
    }

    public boolean isQueueFull() {
        return (this.size() == this.orderList.length);
    }

    private int findParent(int current) {
        return ((current -1)/2);
    }

    private int findLeftChild(int current) {
        return ((2*current) + 1);
    }

    private int findRightChild(int current) {
        return ((2*current) + 2);
    }

    private void swap(int i, int j) {
        CustomerOrder hold = orderList[i];
        int posOfI = orderList[i].getPosInQueue();
        int posOfJ = orderList[j].getPosInQueue();

        orderList[i].setPosInQueue(posOfJ);
        orderList[j].setPosInQueue(posOfI);
        orderList[i] = orderList[j];
        orderList[j] = hold;
    }

    private void sink(int c) {
        int left = findLeftChild(c);
        int right = findRightChild(c);
        int priority = c;

        if (left < size() && orderList[left] != null && (orderList[left].compareTo(orderList[priority]) == 1)) {
            priority = left;
        }

        if (right < size() && orderList[right] != null && (orderList[right].compareTo(orderList[priority]) == 1)) {
            priority = right;
        }

        if (priority != c) {
            swap(c, priority);
            sink(priority);
        }
    }

    private void print() {
        for (int i = 0; i < orderList.length; i++) {
            System.out.print(orderList[i]);
            System.out.println();
        }
    }


    /*
    public static void main(String[] args) throws NoSuchAlgorithmException {
        BetterCustomerOrderQueue bcoq = new BetterCustomerOrderQueue(7);

        while (true) {
            System.out.println("what to do?");
            System.out.println("1: enter new customer");
            System.out.println("2: remove");
            System.out.println("3: update");
            System.out.println("4: print");

            Scanner s = new Scanner(System.in);
            int choice = s.nextInt();
            s.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Name: ");
                    String name = s.nextLine();
                    System.out.println("orderPlacedTime: ");
                    int placeTime = s.nextInt();
                    s.nextLine();
                    int deliveryTime = -1;
                    CustomerOrder newOrder = new CustomerOrder(name, placeTime, deliveryTime);
                    bcoq.insert(newOrder);
                    break;
                case 2:
                    System.out.println("enter name: ");
                    String name1 = s.nextLine();
                    System.out.println("removed " + bcoq.remove(name1));
                    break;
                case 3:
                    System.out.println("enter name: ");
                    name = s.nextLine();
                    System.out.println("enter new time: ");
                    int time = s.nextInt();
                    s.nextLine();
                    bcoq.update(name, time);
                    break;
                case 4:
                    bcoq.print();
                default:
                    break;
            }
            System.out.println();
        }

    }

     */


}

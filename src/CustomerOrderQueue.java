/**
 * CS 251: Data Structures and Algorithms
 * Project 2: Part 1
 *
 * Complete CustomerOrderQueue.
 *
 * @author Chirayu Garg, Sofia Meza
 * @username garg104, meza8
 * @sources TODO: list your sources here
 */


public class CustomerOrderQueue {
    private CustomerOrder[] orderList;
    private int numOrders;

    /**
     *
     * @return return the priority queue
     *
     */
    public CustomerOrder[] getOrderList() {
        return orderList;
    }

    /**
     *
     * @return return the number of orders
     *
     */
    public int getNumOrders() {
        return this.numOrders;
    }

    /**
     * Constructor of the class.
     * TODO: complete the default Constructor of the class
     *
     * Initilalize a new CustomerOrder array with the argument passed.
     *
     */
    public CustomerOrderQueue(int capacity) {
        this.orderList = new CustomerOrder[capacity];
        this.numOrders = 0;
    }

    /**
     * TODO: insert a new customer order into the priority queue.
     *
     * @return return the index at which the customer was inserted
     *
     */
    public int insert(CustomerOrder c) {
        if (isFull()) {
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

        return (size() - 1); // return index in which element was inserted
    }

    /**
     * remove the customer with the highest priority from the queue
     *
     * @return return the customer removed
     *
     */
    public CustomerOrder delMax() {
        if (isEmpty()) {
            return null;
        }

        CustomerOrder toReturn = this.orderList[0];
        this.orderList[0] = this.orderList[size() - 1]; //last element added goes to the top
        this.orderList[size() - 1] = null;
        this.numOrders = this.numOrders -1;
        sink(0); // to sink the root and reorganize the heap

        return toReturn;
    }

    /**
     * return the number of Customers currently in the queue
     *
     * @return return the number of Customers currently in the queue
     *
     */
    public int size() {
        return this.numOrders;
    }

    /**
     * check if the priority queue is empty or not
     *
     * @return return true if the queue is empty; false else
     *
     */
    public boolean isEmpty() {
        return (this.size() == 0);
    }

    /**
     * return but do not remove the customer with the maximum priority
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

    private boolean isFull() {
        return (this.size() == this.orderList.length);
    }

    /**
     * This function rearranges the binary tree after delMax is
     * called. Works by finding the most urgent order between a node
     * and its children, the most urgent will go to the "root", called recursively
     * until it is all heapified :)
     */
    private void sink(int c) {
        int left = findLeftChild(c);
        int right = findRightChild(c);
        int priority = c;

        if (left <= size() && orderList[left] != null && (orderList[left].compareTo(orderList[priority]) == 1)) {
            priority = left;
        }

        if (right <= size() && orderList[right] != null && (orderList[right].compareTo(orderList[priority]) == 1)) {
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
    public static void main(String[] args) {
        CustomerOrderQueue queue = new CustomerOrderQueue(5);

        while (true) {
            System.out.println("what to do?");
            System.out.println("1: enter new customer");
            System.out.println("2: delmax");
            System.out.println("3: getmax");
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
                    queue.insert(newOrder);
                    break;
                case 2:
                    System.out.println("removed " + queue.delMax());
                    break;
                case 3:
                    System.out.println("The max is " + queue.getMax());
                    break;
                case 4:
                    queue.print();
                    break;
                default:
                    break;
            }
            System.out.println();
        }

    }

     */
}

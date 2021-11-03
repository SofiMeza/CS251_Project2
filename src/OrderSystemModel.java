/**
 * CS 251: Data Structures and Algorithms
 * Project 2: Part 4
 *
 * TODO: Complete OrderSystemModel.
 *
 * @author Chirayu Garg, Sofia Meza
 * @username garg104, meza8
 * @sources TODO: list your sources here
 */

import java.security.NoSuchAlgorithmException;

public class OrderSystemModel {
    private BetterCustomerOrderQueue orderList;
    private int capacityThreshold;
    private int ordersDelayed;
    private int ordersOnTime;
    private int ordersCanceled;
    private int time;
    private int totalDelayTime;

    public int getOrdersDelayed() {
        return ordersDelayed;
    }

    public int getOrdersOnTime() {
        return ordersOnTime;
    }

    public int getOrdersCanceled() {
        return ordersCanceled;
    }

    public int getTotalDelayTime() {
        return totalDelayTime;
    }

    public BetterCustomerOrderQueue getOrderList() {
        return orderList;
    }

    /**
     * Constructor of the class.
     *
     * Initialize a new OrderSystemModel and OrderSystemModel
     *
     */
    public OrderSystemModel(int capacityThreshold) {
        this.capacityThreshold = capacityThreshold;
        this.orderList = new BetterCustomerOrderQueue(this.capacityThreshold);
        this.ordersDelayed = 0;
        this.ordersOnTime = 0;
        this.ordersCanceled = 0;
        this.time = 0;
        this.totalDelayTime = 0;
    }

    /**
     *
     * TODO: Process a new CustomerOrder with a given name.
     *
     */
    public String process(String name, int orderTime, int deliveryTime) throws NoSuchAlgorithmException {
        CustomerOrder newOrder = new CustomerOrder(name, orderTime, deliveryTime);

        if (!this.orderList.isQueueFull()) {
            this.orderList.insert(newOrder);
            return name;
        }

        CustomerOrder currentEarliest = this.orderList.getMax();

        if (currentEarliest.getOrderDeliveryTime() > newOrder.getOrderDeliveryTime()) {

            if (newOrder.getOrderDeliveryTime() < time) { //order is late
                int delayTime = time - newOrder.getOrderDeliveryTime();
                this.totalDelayTime = this.totalDelayTime + delayTime;
                this.ordersDelayed++;
            } else { //order is in time
                this.ordersOnTime++;
            }
            this.time++;

            return null;
        }

        completeNextOrder();

        return currentEarliest.getName();
    }

    /**
     *
     * TODO: Complete the highest priority order
     *
     */
    public String completeNextOrder() throws NoSuchAlgorithmException {
        if (this.orderList.isEmpty()) {
            return null;
        }

        CustomerOrder completed = this.orderList.delMax();

        if (completed.getOrderDeliveryTime() < time) { //order is late
            int delayTime = time - completed.getOrderDeliveryTime();
            this.totalDelayTime = this.totalDelayTime + delayTime;
            this.ordersDelayed++;
        } else { //order is in time
            this.ordersOnTime++;
        }

        this.time++;

        return completed.getName();
    }

    /**
     *
     * TODO: Update the delivery time of the order for the given name
     *
     */
    public String updateOrderTime(String name, int newDeliveryTime) throws NoSuchAlgorithmException {
        CustomerOrder customer = this.orderList.get(name);

        if (customer == null) {
            return null;
        }

        int earliestDelivery = this.orderList.getMax().getOrderDeliveryTime();

        this.orderList.update(name, newDeliveryTime);

        if (newDeliveryTime < earliestDelivery) {
            if (newDeliveryTime < time) {
                cancelOrder(name);
            } else {
                completeNextOrder();
            }
            return name;
        }
        return null;
    }

    /**
     *
     * TODO: Cancel the order for the given name
     *
     */
    public CustomerOrder cancelOrder(String name) throws NoSuchAlgorithmException {
        CustomerOrder cancelled = this.orderList.get(name);

        if (cancelled == null) {
            return null;
        }

        this.orderList.remove(name);
        this.ordersCanceled++;

        return cancelled;
    }


}

import java.util.Objects;

/**
 * CS 251: Data Structures and Algorithms
 * Project 2:
 *
 * @author Chirayu Garg
 * @username garg104
 * @sources -
 */


public class CustomerOrder {
    private String name;
    private int orderPlacedTime;
    private int orderDeliveryTime;
    private int posInQueue;


    public CustomerOrder(String name, int orderPlacedTime, int orderDeliveryTime) {
        this.name = name;
        this.orderPlacedTime = orderPlacedTime;
        if (orderDeliveryTime == -1) {
            this.orderDeliveryTime = orderPlacedTime + 8;
        } else {
            this.orderDeliveryTime = orderDeliveryTime;
        }
        this.posInQueue = -1;
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
                "name='" + name + '\'' +
                ", orderPlacedTime=" + orderPlacedTime +
                ", orderDeliveryTime=" + orderDeliveryTime +
                ", posInQueue=" + posInQueue +
                '}';
    }

    public int compareTo(CustomerOrder other) {

        if (this.orderDeliveryTime < other.orderDeliveryTime) {
            return 1;
        } else if (this.orderDeliveryTime > other.orderDeliveryTime) {
            return -1;
        } else {
            return (this.orderPlacedTime < other.orderPlacedTime ? 1 : -1);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerOrder)) return false;
        CustomerOrder that = (CustomerOrder) o;
        return orderPlacedTime == that.orderPlacedTime &&
                orderDeliveryTime == that.orderDeliveryTime &&
                posInQueue == that.posInQueue &&
                Objects.equals(name, that.name);
    }


    public String getName() {
        return name;
    }

    public int getOrderPlacedTime() {
        return orderPlacedTime;
    }

    public int getOrderDeliveryTime() {
        return orderDeliveryTime;
    }

    public int getPosInQueue() {
        return posInQueue;
    }

    public void setOrderDeliveryTime(int orderDeliveryTime) {
        this.orderDeliveryTime = orderDeliveryTime;
    }

    public void setPosInQueue(int posInQueue) {
        this.posInQueue = posInQueue;
    }

}

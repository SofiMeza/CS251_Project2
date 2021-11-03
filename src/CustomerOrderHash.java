/**
 * CS 251: Data Structures and Algorithms
 * Project 2: Part 2
 *
 * TODO: Complete CustomerOrderHash.
 *
 * @author Chirayu Garg, Sofia Meza
 * @username garg104, meza8
 * @sources TODO: list your sources here
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class CustomerOrderHash {
    private ArrayList[] table;
    private int numOrders;
    private int tableCapacity;

    /**
     * Constructor of the class.
     * TODO: complete the default Constructor of the class
     *
     * Initilalize a new CustomerOrder array with the argument passed.
     *
     */
    public CustomerOrderHash(int capacity) {
       this.table = new ArrayList[getNextPrime(capacity)];
       this.numOrders = 0;
    }

    /**
     *
     * TODO: return the CustomerOrder with the given name
     * TODO: return null if the CustomerOrder is not in the table
     *
     */
    public CustomerOrder get(String name) throws NoSuchAlgorithmException {
        int hashKey = getHashKey(name);
        CustomerOrder customer = getObjFromName(name, hashKey);

        if (customer != null) {
            int index = this.table[hashKey].indexOf(customer);
            return (CustomerOrder) this.table[hashKey].get(index);
        }

        return null;
    }

    /**
     *
     * TODO: put CustomerOrder c into the table
     *
     */
    public void put(CustomerOrder c) throws NoSuchAlgorithmException {
        int hashKey = getHashKey(c.getName());

        if (this.table[hashKey] == null) { //if null, initialize and add
            this.table[hashKey] = new ArrayList();
            this.table[hashKey].add(c);
            numOrders = numOrders +1;
        } else { // if not null and customer already in it return
            if(this.table[hashKey].contains(c)) {
                return;
            } else { // if not null and customer not in it, add customer
                this.table[hashKey].add(c);
                numOrders = numOrders +1;
            }
        }
    }

    /**
     *
     * TODO: remove and return the CustomerOrder with the given name;
     * TODO: return null if CustomerOrder doesn't exist
     *
     */
    public CustomerOrder remove(String name) throws NoSuchAlgorithmException {
        int hashKey = getHashKey(name);
        CustomerOrder customer = getObjFromName(name, hashKey);

        if (customer != null) {
            int index = this.table[hashKey].indexOf(customer);
            CustomerOrder toReturn = (CustomerOrder) this.table[hashKey].get(index);
            this.table[hashKey].remove(index);
            numOrders = numOrders -1;

            return toReturn;
        }

        return null;
    }

    /**
     *
     * TODO: return the number of Customers in the table
     *
     */
    public int size() {
        return this.numOrders;
    }

    //get the next prime number p >= num
    private int getNextPrime(int num) {
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
    private boolean isPrime(int num) {
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

    public ArrayList<CustomerOrder>[] getArray() {
        return this.table;
    }

    public int getHashKey(String name) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashName = md.digest(name.getBytes());
        String hashString = new String(hashName);
        int hashKey = hashString.hashCode();
        hashKey = hashKey % this.table.length;

        if (hashKey < 0) {
            hashKey = hashKey + this.table.length;
        }

        return hashKey;
    }
    
    private CustomerOrder getObjFromName(String name, int hashkey) {
        if (this.table[hashkey] != null) {
            for (int i = 0; i < this.table[hashkey].size(); i++) {
                CustomerOrder comparing = (CustomerOrder) this.table[hashkey].get(i);
                if (comparing.getName() == name) {
                    return comparing;
                }
            }
        }
        return null;
    }
}

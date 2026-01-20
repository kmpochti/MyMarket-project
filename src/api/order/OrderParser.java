package api.order;

import java.io.*;
import java.util.ArrayList;

/**
 * Η κλάση OrderParser είναι βοηθητική κλάση για την ανάγνωση και εγγραφή των παραγγελιών σε αρχεία
 */
public class OrderParser {
    public static void saveOrders(String fileName, ArrayList<Order> orders) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))){
            out.writeObject(orders);
        }
    }

    public static ArrayList<Order> readOrders(String fileName) throws IOException,ClassNotFoundException{
        ArrayList<Order> orders;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))){
            orders = (ArrayList<Order>) in.readObject();
        }
        return orders;
    }
}

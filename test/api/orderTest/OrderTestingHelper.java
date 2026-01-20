package api.orderTest;

import api.order.Order;
import api.order.OrderBase;
import api.order.OrderedProduct;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrderTestingHelper {

    static public OrderBase setup() {
        OrderBase ob = new OrderBase("");

        ArrayList<OrderedProduct> l1,l2,l3,l4;
        l1 = new ArrayList<>();
        l1.add(new OrderedProduct("product1",1,1));
        l1.add(new OrderedProduct("product2",2,2));
        l1.add(new OrderedProduct("product3",3,3));
        l1.add(new OrderedProduct("product4",4,4));

        l2 = new ArrayList<>();
        l2.add(new OrderedProduct("product1",4,1));
        l2.add(new OrderedProduct("product2",7,2));
        l2.add(new OrderedProduct("product3",1,3));
        l2.add(new OrderedProduct("product4",3,4));

        l3 = new ArrayList<>();
        l3.add(new OrderedProduct("product1",2,1));
        l3.add(new OrderedProduct("product2",2,2));
        l3.add(new OrderedProduct("product3",2,3));
        l3.add(new OrderedProduct("product4",2,4));

        l4 = new ArrayList<>();
        l4.add(new OrderedProduct("product2",2,2));
        l4.add(new OrderedProduct("product3",3,3));
        l4.add(new OrderedProduct("product4",4,4));

        ob.addOrder(new Order("user1",l1, LocalDateTime.now()));
        ob.addOrder(new Order("user1",l2, LocalDateTime.now()));
        ob.addOrder(new Order("user1",l3, LocalDateTime.now()));
        ob.addOrder(new Order("user1",l4, LocalDateTime.now()));

        ob.addOrder(new Order("user2",l2, LocalDateTime.now()));

        return ob;
    }

}

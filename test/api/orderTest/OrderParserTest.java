package api.orderTest;

import api.order.Order;
import api.order.OrderBase;
import api.order.OrderedProduct;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class OrderParserTest {


    @Test
    public void testOrderParser() throws IOException {
        OrderBase ob = OrderTestingHelper.setup();
        ob.saveOrderFile("Data/ordersTest.dat");
        OrderBase obNew = new OrderBase("Data/ordersTest.dat");

        assertEquals(ob.searchByProduct("product1").size(),obNew.searchByProduct("product1").size());
        assertEquals(ob.searchByUser("user1").size(),obNew.searchByUser("user1").size());
        assertEquals(ob.totalSoldOfProduct("product1"),obNew.totalSoldOfProduct("product1"));


    }
}

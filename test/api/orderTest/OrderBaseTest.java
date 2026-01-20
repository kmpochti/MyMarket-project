package api.orderTest;
import api.order.Order;
import api.order.OrderBase;
import api.order.OrderedProduct;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class OrderBaseTest {
    private OrderBase orderBase;


    @Test
    public void testSearchByProduct(){
        OrderBase a = OrderTestingHelper.setup();
        assertEquals(4,a.searchByProduct("product1").size());
        assertEquals(5,a.searchByProduct("product2").size());
        assertEquals(0,a.searchByProduct("product5").size());
    }

    @Test
    public void testSearchByUser(){
        OrderBase a = OrderTestingHelper.setup();
        assertEquals(4,a.searchByUser("user1").size());
        assertEquals(1,a.searchByUser("user2").size());
        assertEquals(0,a.searchByUser("user3").size());
    }

    @Test
    public void testTotalSoldOfProduct() throws IOException {
        OrderBase a = OrderTestingHelper.setup();
        assertEquals(11,a.totalSoldOfProduct("product1"));
        assertEquals(0,a.totalSoldOfProduct("product5"));
    }
}

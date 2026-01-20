package api.orderTest;
import api.orderHandler;
import api.product.queryProduct;
import api.productTest.ProductTestingHelper;
import api.user.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderHandlerTest {
    orderHandler oh;

    @Test
    public void testAddToCart(){
         oh = new orderHandler(OrderTestingHelper.setup(), ProductTestingHelper.setup());
        assertTrue(oh.addToCart(oh.search(new queryProduct("product1",null,null)).getFirst(), 10));
        assertTrue(oh.addToCart(oh.search(new queryProduct("product1",null,null)).getFirst(), 10));

        int product1Amount = oh.getCart().getFirst().getAmount();
        assertEquals(20,product1Amount);

        assertTrue(oh.addToCart(oh.search(new queryProduct("product2",null,null)).getFirst(), 10));
        assertFalse(oh.addToCart(oh.search(new queryProduct("product3",null,null)).getFirst(), 100));
    }

    @Test
    public void testRemoveFromCart(){
        testAddToCart();
        assertTrue(oh.removeFromCart(oh.search(new queryProduct("product1",null,null)).getFirst(), 10));

        int product1Amount = oh.getCart().getFirst().getAmount();
        assertEquals(10,product1Amount);

        assertTrue(oh.removeFromCart(oh.search(new queryProduct("product2",null,null)).getFirst(), 10));
        assertFalse(oh.removeFromCart(oh.search(new queryProduct("product2",null,null)).getFirst(), 10));
    }

    @Test
    public void testAddOrder(){
        testAddToCart();
        oh.addOrder(new User("user66","12345","fname","lname"));

        assertEquals(1,oh.searchByUser("user66").size());
        assertEquals(2,oh.searchByUser("user66").getFirst().getProducts().size());
        assertEquals("product1",oh.searchByUser("user66").getFirst().getProducts().get(0).getTitle());
        assertEquals("product2",oh.searchByUser("user66").getFirst().getProducts().get(1).getTitle());
    }

    @Test
    public void testGetBestSelling(){
        testAddToCart();

        assertEquals("product2",oh.getBestSelling(2).getFirst().getTitle());
        assertEquals("product4",oh.getBestSelling(2).get(1).getTitle());
    }
}

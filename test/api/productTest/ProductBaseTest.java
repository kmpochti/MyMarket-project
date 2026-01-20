package api.productTest;
import api.product.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class ProductBaseTest {


    @Test
    public void testSearchProduct(){
        ProductBase pb = ProductTestingHelper.setup();
        assertEquals(2,pb.search(new queryProduct(null,"cat1",null)).size());
        assertEquals(1,pb.search(new queryProduct(null,"cat2",null)).size());
        assertEquals(1,pb.search(new queryProduct(null,"cat1","sub1")).size());
        assertEquals(1,pb.search(new queryProduct("product2","cat1","sub2")).size());
        assertEquals(1,pb.search(new queryProduct("product2",null,null)).size());
    }

    @Test
    public void testGetOutOfStock(){
        ProductBase pb = ProductTestingHelper.setup();
        assertEquals(1,pb.getOutOfStock().size());
    }
}

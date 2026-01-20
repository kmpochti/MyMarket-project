package api.productTest;
import api.product.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class ProductTest {

    @Test
    public void testRemoveAmount(){
        Product p = new PackagedProduct("Πορτοκάλια 1kg", 200,"Φρέσκα πορτοκάλια, ιδανικά για χυμό ή κατανάλωση","Φρέσκα τρόφιμα","Φρούτα",1.20);
        p.removeAmount(20);
        assertEquals(180,p.getAmount());
        assertFalse(p.removeAmount(200));
        assertEquals(180,p.getAmount());
    }

    @Test
    public void testAddAmount(){
        Product p = new PackagedProduct("Πορτοκάλια 1kg", 200,"Φρέσκα πορτοκάλια, ιδανικά για χυμό ή κατανάλωση","Φρέσκα τρόφιμα","Φρούτα",1.20);
        p.addAmount(20);
        assertEquals(220,p.getAmount());
        p.addAmount(-50);
        assertEquals(220,p.getAmount());
    }

    @Test
    public void testGetAndSet(){
        PackagedProduct p = new PackagedProduct("Πορτοκάλια 1kg", 200,"Φρέσκα πορτοκάλια, ιδανικά για χυμό ή κατανάλωση","Φρέσκα τρόφιμα","Φρούτα",1.20);
        assertEquals("Πορτοκάλια 1kg",p.getTitle());
        assertEquals(200,p.getAmount());
        assertEquals(1.2,p.getPrice(),0.1);
        assertEquals("Φρέσκα πορτοκάλια, ιδανικά για χυμό ή κατανάλωση",p.getDescription());
        assertEquals(" τεμάχια",p.getMeasurement());
        assertEquals("Φρέσκα τρόφιμα",p.getCategory());
        assertEquals("Φρούτα",p.getSubCategory());

        WeightedProduct w = new WeightedProduct("Πορτοκάλια 1kg", 200,"Φρέσκα πορτοκάλια, ιδανικά για χυμό ή κατανάλωση","Φρέσκα τρόφιμα","Φρούτα",1.20);
        assertEquals("kg",w.getMeasurement());

        p.setAmount(100);
        p.setTitle("test");
        p.setCategory("cat");
        p.setSubCategory("sub");
        p.setDescription("desc");
        p.setPrice(1.4);

        assertEquals("test",p.getTitle());
        assertEquals(100,p.getAmount());
        assertEquals(1.4,p.getPrice(),0.01);
        assertEquals("desc",p.getDescription());
        assertEquals("cat",p.getCategory());
        assertEquals("sub",p.getSubCategory());
    }

}

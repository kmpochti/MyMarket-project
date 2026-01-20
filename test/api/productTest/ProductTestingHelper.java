package api.productTest;

import api.product.PackagedProduct;
import api.product.ProductBase;

public class ProductTestingHelper {
    static public ProductBase setup(){
        ProductBase pb = new ProductBase("");
        pb.add(new PackagedProduct("product1",100,"desc","cat1","sub1",1.5));
        pb.add(new PackagedProduct("product2",50,"desc","cat1","sub2",2));
        pb.add(new PackagedProduct("product3",60,"desc","cat2","sub3",3));
        pb.add(new PackagedProduct("product4",0,"desc","cat3","sub4",2.5));
        return pb;
    }
}

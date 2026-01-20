package api.product;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Η κλάση ProductParser είναι βοηθητική κλάση για την ανάγνωση και εγγραφή των προϊόντων σε αρχεία
 */
public class ProductParser {
    /**
     * Μέθοδος που αποθηκεύει ενα HashMap<String,MarketProduct> σε ενα αρχείο txt
     * @param directory που θα αποθηκευτεί
     * @param products HashMap με προϊόντα
     * @throws IOException
     */
    public static void saveProductFile(String directory,HashMap<String,MarketProduct> products) throws IOException {
        FileWriter fw = new FileWriter(directory,false);
        for (String key : products.keySet()){
            MarketProduct product = products.get(key);
            fw.write("Τίτλος: " + product.getTitle() +
                    "\nΠεριγραφή: " + product.getDescription() +
                    "\nΚατηγορία: " + product.getCategory() +
                    "\nΥποκατηγορία: " + product.getSubCategory() +
                    "\nΤιμή: " + product.getPrice() + '€' +
                    "\nΠοσότητα: " + product.getAmount() + product.getMeasurement()+
                    "\n\n");
        }
        fw.close();
    }

    /**
     * Μέθοδος που διαβάζει απο ενα αρχείο προϊόντα
     * @param f αρχείο απο το οποίο θα διαβάσει τα προϊόντα
     * @return HashMap<String,MarketProduct> με προϊόντα
     * @throws FileNotFoundException
     */
    public static HashMap<String,MarketProduct> readProductFile(String f) throws FileNotFoundException {
        HashMap<String,MarketProduct> products = new HashMap<>();
        Scanner scanner = new Scanner(new File(f));
        String title = "";
        String desc = "";
        String cat = "";
        String subCat = "";
        double price = 0;
        int amount = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split(":",2);
            if (split.length == 2){
                String key = split[0].trim();
                String value = split[1].trim();

                switch (key){
                    case "Τίτλος":
                        title = value;
                        break;
                    case "Περιγραφή":
                        desc = value;
                        break;
                    case "Κατηγορία":
                        cat = value;
                        break;
                    case "Υποκατηγορία":
                        subCat = value;
                        break;
                    case "Τιμή":
                        price = Double.parseDouble(value.replace(",", ".").replace("€",""));
                        break;
                    case "Ποσότητα":
                        if (value.contains("kg")){
                            amount = Integer.parseInt(value.replaceAll("\\D",""));
                            products.put(title,new WeightedProduct(title,amount,desc,cat,subCat,price));
                        }else{
                            amount = Integer.parseInt(value.replaceAll("\\D",""));
                            products.put(title,new PackagedProduct(title,amount,desc,cat,subCat,price));
                        }
                        break;
                }
            }
        }
        scanner.close();
        return products;
    }
}

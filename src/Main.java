import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Store store = Store.getInstance();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        label:
        while(true){
            String line = null;
            try{ line = reader.readLine(); }
            catch (IOException e) { e.printStackTrace(); }

            String [] lineWords;
            if(line != null) lineWords = line.split(" ");
            else lineWords = new String[]{""};

            String command = lineWords[0];
            switch (command) {
                case "exit":
                case "quit":
                    break label;
                case "listcurrencies":
                    store.listCurrencies();
                    break;
                case "loadcsv":
                    store.loadStore(lineWords[1]);
                    break;
                case "savecsv":
                    store.saveStore(lineWords[1]);
                    break;
                case "getstorecurrency":
                    System.out.println(store.getCurrentCurrency().getName());
                    break;
                case "addcurrency":
                    store.createCurrency(lineWords[1], lineWords[2],
                            Double.parseDouble(lineWords[3]));
                    break;
                case "setstorecurrency":
                    try {
                        store.changeCurrency(lineWords[1]);
                        System.out.println(store.getCurrentCurrency().getName());
                    } catch (CurrencyNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "updateparity":
                    store.updateParity(lineWords[1],
                            Double.parseDouble(lineWords[2]));
                    break;
                case "listproducts":
                    store.listProducts();
                    break;
                case "showproduct":
                    store.showProduct(lineWords[1]);
                    break;
                case "listmanufacturers":
                    store.showManufacturers();
                    break;
                case "listproductsbymanufacturarer":
                    store.listProductByManufacturer(lineWords[1]);
                    break;
                case "listdiscounts":
                    store.listDiscounts();
                    break;
                case "addiscount":
                    store.createDiscount(lineWords[1],
                            Double.parseDouble(lineWords[2]),
                            lineWords[3]);
                    break;
                case "applydiscount":
                    try {
                        store.applyDiscount(lineWords[1],
                                Double.parseDouble(lineWords[2]));
                    } catch (DiscountNotFoundException e) {
                        System.out.println(Arrays.toString(e.getStackTrace()));
                        e.printStackTrace();
                    } catch (NegativePriceException e) {
                        e.printStackTrace();
                    }
                    break;
                case "calculatetotal":
                    ArrayList<Product> products = new ArrayList<>();
                    for(int i = 1; i < lineWords.length; i++){
                        for(Product j : store.getProducts()){
                            if(j.getUniqueid().equals(lineWords[i]))
                                products.add(j);
                        }
                    }
                    store.calculateTotal(products);
                    break;
                default:
                    System.out.println("Invalid command. Command not found.");
                    break;
            }
        }

    }
}

import org.apache.commons.csv.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;


public class Store{
    private static Store obj;
    private String name = "MyStore";

    private ArrayList<Currency> currencies = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();
    private final ArrayList<Manufacturer> manufacturers = new ArrayList<>();
    private ArrayList<Discount> discounts = new ArrayList<>();

    public Currency currentCurrency;


    private Store() {
        this.currentCurrency = createCurrency("EUR", "€", 1.0);
    }

    public static Store getInstance(){
        if(obj == null) obj = new Store();
        return obj;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public ArrayList<Product> getProducts() { return products; }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Currency> getCurrencies() { return currencies; }

    public Currency getCurrentCurrency() { return currentCurrency; }

    public void readCSV(String filePath) throws CurrencyNotFoundException {
        try {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(new FileReader(filePath));

            boolean invalidPrice = true;
            double productParityToEur = 1.0;
            for (CSVRecord record : records) {
                String price = record.get(3);
                if(price.isEmpty()) continue;
                if(invalidPrice) {
                    for (Currency i : currencies) {
                        if (i.getSymbol().equals(currencySymbol(price))){
                            invalidPrice = false;
                            productParityToEur = i.getParityToEur();
                        }
                    }
                }

                String uniqueid = record.get(0);
                String name = record.get(1);

                String manufacturerName = record.get(2);
                if(manufacturerName.isEmpty())
                    manufacturerName = "Not Available";
                Manufacturer manufacturer = new Manufacturer(manufacturerName);
                manufacturer.setName(manufacturerName);
                addManufacturer(manufacturer);

                String quantity = record.get(4);

                Product product = new Product.Builder().uniqueid(uniqueid)
                        .name(name)
                        .manufacturer(manufacturer)
                        .quantitiy(getCorrectQuantity(quantity))
                        .build();
                if(productParityToEur != 1.0) {
                    product.setPrice(getCorrectPrice(price) *
                            getCurrentCurrency().getParityToEur() /
                            productParityToEur);
                }
                addProduct(product);
            }

            if(invalidPrice){
                throw new CurrencyNotFoundException();
            }
        } catch (Exception e) {
            System.out.println((e.getMessage()));
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public void saveCSV(String filePath){
        try (PrintWriter writer = new PrintWriter(new
                File(filePath))){
            StringBuilder sb = new StringBuilder();
            sb.append("uniq_id");
            sb.append(',');
            sb.append("product_name");
            sb.append(',');
            sb.append("manufacturer");
            sb.append(',');
            sb.append("price");
            sb.append(',');
            sb.append("number_available_in_stock");
            sb.append('\n');

            for(Product i : products){
                sb.append(i.getUniqueid());
                sb.append(',');
                sb.append(i.getName());
                sb.append(',');
                sb.append(i.getManufacturer().getName());
                sb.append(',');
                sb.append(i.getPrice());
                sb.append(',');
                sb.append(i.getQuantitiy());
                sb.append('\n');
            }

            writer.write(sb.toString());

        }catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    void addProduct(Product product){
        products.add(product);
    }

    void addManufacturer(Manufacturer manufacturer) {
        boolean ok = true;
        for (Manufacturer i : manufacturers)
            if (i.getName().equals(manufacturer.getName())) ok = false;
        if(ok) manufacturers.add(manufacturer);
    }

    void loadStore(String filename) {
        try {
            readCSV(filename);
        } catch (CurrencyNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    void saveStore(String filename) {
        saveCSV(filename);
    }

    Currency createCurrency(String name, String symbol, double parityToEur){
        Currency currency = new Currency(name, symbol, parityToEur);
        currencies.add(currency);
        return currency;
    }

    void changeCurrency(String name) throws CurrencyNotFoundException {
        for (Currency i : currencies) {
            if (i.getName().equals(name)) {
                this.currentCurrency = i;
                for (Product j : products) {
                    j.setPrice(j.getPrice() *
                            currentCurrency.getParityToEur() /
                            i.getParityToEur());
                }
                return;
            }
        }
        throw new CurrencyNotFoundException();
    }

    void createDiscount(String discountType, double value,
                            String name){
        DiscountType actualDiscountType;
        if(discountType.charAt(0) == 'P')
            actualDiscountType = DiscountType.PERCENTAGE_DISCOUNT;
        else actualDiscountType = DiscountType.FIXED_DISCOUNT;
        Discount discount = new Discount(name, actualDiscountType, value);
        discounts.add(discount);
    }

    void applyDiscount(String discountType, double value) throws
            DiscountNotFoundException, NegativePriceException{
        DiscountType actualDiscountType;
        if(discountType.charAt(0) == 'P')
            actualDiscountType = DiscountType.PERCENTAGE_DISCOUNT;
        else actualDiscountType = DiscountType.FIXED_DISCOUNT;

        boolean ok = false;
        for(Discount i : discounts)
            if(i.getValue() == value)
                if(i.getDiscountType() == actualDiscountType){
                    i.setAsAppliedNow();
                    ok = true;
                }

        if(ok)
            for(Product i: products) {
                if (actualDiscountType == DiscountType.FIXED_DISCOUNT) {
                    if (i.getPrice() < value) {
                        throw new NegativePriceException();
                    }
                    else i.setPrice(i.getPrice() - value);
                }
                else i.setPrice(((100 - value) * i.getPrice()) / 100);
            }
        else throw new DiscountNotFoundException();
    }

    ArrayList<Product> getProductsByManufacturer(Manufacturer manufacturer){
        ArrayList<Product> ProductsByManufacturer = new ArrayList<>();
        for (Product i : products) {
            if (i.getManufacturer().getName().equals(manufacturer.getName()))
                ProductsByManufacturer.add(i);
        }
        return ProductsByManufacturer;
    }

    void calculateTotal(ArrayList<Product> product){
        double sum = 0;
        double valueOfStock;
        for(Product i: product){
            valueOfStock = i.getPrice() * i.getQuantitiy();
            sum += valueOfStock;
        }
        System.out.println(currentCurrency.getSymbol() + sum);
    }

    Integer getCorrectQuantity(String quantity){
        StringBuilder correctQuantity = new StringBuilder();
        if(quantity == null || quantity.isEmpty()) return 0;
        for (char c : quantity.toCharArray()) {
            if (Character.isDigit(c)) correctQuantity.append(c);
            else break;
        }
        return Integer.parseInt(correctQuantity.toString());
    }

    Double getCorrectPrice(String price){
        boolean found = false;
        StringBuilder correctPrice = new StringBuilder();
        if(price == null || price.isEmpty()) return 0.0;

        for(char c : price.toCharArray()){
            if(c == ',') continue;
            if(Character.isDigit(c) || c == '.'){
                correctPrice.append(c);
                found = true;
            }
            else if(found) break;
        }
        return Double.parseDouble(String.valueOf(correctPrice));
    }

    public void listCurrencies(){
        for(Currency i : currencies){
            System.out.println(i.getName() + ' ' + i.getParityToEur());
        }
    }

    public void listProducts(){
        for(Product i : products){
            System.out.println(i.getUniqueid() +
                    ',' +
                    i.getName() +
                    ',' +
                    i.getManufacturer().getName() +
                    ',' +
                    getCurrentCurrency().getSymbol() +
                    i.getPrice() +
                    ',' +
                    i.getQuantitiy() );
        }
    }

    public void showProduct(String id){
        for(Product i : products){
            if(i.getUniqueid().equals(id)) {
                System.out.println(i.getUniqueid() +
                        ',' +
                        i.getName() +
                        ',' +
                        i.getManufacturer().getName() +
                        ',' +
                        i.getPrice() +
                        ',' +
                        i.getQuantitiy());
                break;
            }
        }
    }

    public void showManufacturers(){
        for(Manufacturer i : manufacturers)
            System.out.println(i.getName());
    }

    public void listProductByManufacturer(String name){
        Manufacturer manufacturer = new Manufacturer(name);

        ArrayList<Product> ProductsByManufacturer =
                getProductsByManufacturer(manufacturer);
        for(Product i : ProductsByManufacturer){
            System.out.println(i.getUniqueid() +
                    ',' +
                    i.getName() +
                    ',' +
                    i.getManufacturer().getName() +
                    ',' +
                    i.getPrice() +
                    ',' +
                    i.getQuantitiy() );
        }
    }

    public void listDiscounts(){
        for(Discount i: discounts) {
            String discountInfo = i.getDiscountType() +
                    " " +
                    i.getValue() +
                    " \"" +
                    i.getName() +
                    "\" " +
                    i.lastDateApplied;
            System.out.println(discountInfo);
        }
    }

    public void updateParity(String currencyName, Double parityToEur) {
        for (Currency i : currencies) {
            if (i.getName().equals(currencyName)) {
                i.updateParity(parityToEur);
            }
        }
    }

    //Intoarce simbolul din pretul preluat din csv(de obicei)
    public String currencySymbol(String price){
        StringBuilder symbol = new StringBuilder();
        if(price == null || price.isEmpty())
            return symbol.toString();
        for(int i = 0; i < price.length(); i++){
            symbol.append(price.charAt(i));
            if(Character.isDigit(price.charAt(i + 1)))
                break;
        }
        return symbol.toString();
    }

}




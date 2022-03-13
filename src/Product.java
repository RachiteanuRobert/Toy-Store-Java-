public class Product {
    private final String uniqueid;
    private final String name;
    private final Manufacturer manufacturer;
    private double price;
    private final int quantitiy;
    public Discount discount;

    public String getUniqueid() { return uniqueid; }

    public String getName() {
        return name;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) { this.price = price; }

    public int getQuantitiy() {
        return quantitiy;
    }

    private Product(Builder builder){
        this.uniqueid = builder.uniqueid;
        this.name = builder.name;
        this.manufacturer = builder.manufacturer;
        this.quantitiy = builder.quantitiy;
    }

    public static class Builder {
        private String uniqueid;
        private String name;
        private Manufacturer manufacturer;
        private int quantitiy;

        public Builder uniqueid(String uniqueid) {
            this.uniqueid = uniqueid;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder manufacturer(Manufacturer manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public Builder quantitiy(int quantitiy) {
            this.quantitiy = quantitiy;
            return this;
        }

        public Product build(){
            return new Product(this);
        }
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
}

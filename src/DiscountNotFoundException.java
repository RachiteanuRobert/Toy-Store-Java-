public class DiscountNotFoundException extends Exception {
    public DiscountNotFoundException(){
        super("Invalid Discount. Discount not found.");
    }
}

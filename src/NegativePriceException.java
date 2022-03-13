public class NegativePriceException extends Exception {
    public NegativePriceException(){
        super("Invalid Price. Price has negative value.");
    }
}

public class CurrencyNotFoundException extends Exception {
    public CurrencyNotFoundException(){
        super("Invalid Currency. Currency not found.");
    }
}

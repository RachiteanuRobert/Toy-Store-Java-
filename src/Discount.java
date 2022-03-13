import java.time.LocalDateTime;

public class Discount {
    private String name;
    private DiscountType discountType;
    private double value;
    LocalDateTime lastDateApplied;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public DiscountType getDiscountType() { return discountType; }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public double getValue() { return value; }

    public void setValue(double value) { this.value = value; }

    public Discount(String name, DiscountType discountType, double value) {
        this.name = name;
        this.discountType = discountType;
        this.value = value;
    }

    public void setAsAppliedNow(){
        this.lastDateApplied = LocalDateTime.now();
    }
}

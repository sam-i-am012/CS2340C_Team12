public class Discount {
    private DiscountType discountType;
    private double discountAmount;

    public Discount(DiscountType discountType, double discountAmount) {
        this.discountType = discountType;
        this.discountAmount = discountAmount;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double applyDiscount(double price) {
        switch (discountType) {
            case PERCENTAGE:
                return price - (price * discountAmount / 100);
            case FLAT:
                return price - discountAmount;
            default:
                return price; // No discount
        }
    }
}

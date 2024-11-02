
public class TaxableItem extends Item {
    private double taxRate = 7;
    // Field to store the current tax rate
    private double taxRate;
    // Field to hold the associated discount for the item
    private Discount discount;

    // Changed Old Constructor to a constructor that can account for new Discount class
//    public TaxableItem(String name, double price, int quantity, DiscountType discountType, double discountAmount){
//        super(name, price, quantity, discountType, discountAmount);
//    }
    // Constructor: Accepts item details and a Discount object
    public TaxableItem(String name, double price, int quantity, Discount discount) {
        // Call to the superclass constructor to set item properties
        super(name, price, quantity);
        this.taxRate = taxRate;
        // Store the Discount object, which handles discount logic separately
        this.discount = discount;
    }

    public double getTaxRate(){
        return taxRate;
    }

    public void setTaxRate(double rate) {
        if(rate>=0){
            taxRate = rate;
        }
    }

    // Method to calculate the tax amount based on the item's price
    public double calculateTax() {
        return getPrice() * taxRate / 100;
    }

    // Method to calculate the total price including tax and applying discount if applicable
    public double getTotalPriceWithTax() {
        double discountedPrice = discount != null ? discount.applyDiscount(getPrice()) : getPrice();
        return discountedPrice + calculateTax();
    }
}

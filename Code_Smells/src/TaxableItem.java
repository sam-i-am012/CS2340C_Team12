
public class TaxableItem extends Item {
    private double taxRate = 7;

    public TaxableItem(String name, double price, int quantity, DiscountType discountType, double discountAmount){
        super(name, price, quantity, discountType, discountAmount);
    }

    // -------------- Fixed Code Smell #4 ----------------------
    // Moved calculateTax() from Order.java to uphold the Single Responsibility Principle
    public double calculateItemTax() {
        return (taxRate / 100.0) * getPrice();
    }
    // ---------------------------------------------------------

    public double getTaxRate(){
        return taxRate;
    }

    public void setTaxRate(double rate) {
        if(rate>=0){
            taxRate = rate;
        }
    }
}

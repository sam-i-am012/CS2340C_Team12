package example;

public class GiftCardItem extends Item {
    public GiftCardItem(String name, double price, int quantity, DiscountType discountType, double discountAmount){
        super(name, price, quantity, discountType, discountAmount);
    }

    // -------------- Fixed Code Smell #5 ----------------------
    //create new method to maintain low coupling
    @Override
    public boolean isGiftCard() {
        return true;
    }
    // ---------------------------------------------------------

}
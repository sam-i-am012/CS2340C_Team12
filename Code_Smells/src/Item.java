class Item {
    private String name;
    private double price;
    private int quantity;
    // Added to the Discount class, no need for these attributes
//    private DiscountType discountType;
//    private double discountAmount;

//    public Item(String name, double price, int quantity, DiscountType discountType, double discountAmount) {
//        this.name = name;
//        this.price = price;
//        this.quantity = quantity;
//        this.discountType = discountType;
//        this.discountAmount = discountAmount;
//    }
    // New Constructor that just services Items
    public Item(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // Removed these getters because they are not needed in Items class anymore
//    public DiscountType getDiscountType() {
//        return discountType;
//    }
//
//    public double getDiscountAmount() {
//        return discountAmount;
//    }
}

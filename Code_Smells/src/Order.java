import java.util.List;

public class Order {
    private static final double GIFT_CARD_DISCOUNT = 10.0;
    private static final double LARGE_ORDER_DISCOUNT_THRESHOLD = 100.0;
    private static final double LARGE_ORDER_DISCOUNT_RATE = 0.9;

    private List<Item> items;
    private String customerName;
    private String customerEmail;

    public Order(List<Item> items, String customerName, String customerEmail) {
        this.items = items;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }

    // public double calculateTotalPrice() {
    // 	double total = 0.0;
    // 	for (Item item : items) {
    //     	double price = item.getPrice();
    //     	switch (item.getDiscountType()) {
    //         	case PERCENTAGE:
    //             	price -= item.getDiscountAmount() * price;
    //             	break;
    //         	case AMOUNT:
    //             	price -= item.getDiscountAmount();
    //             	break;
    //         	default:
    //             	// no discount
    //             	break;
    //     	}
    //     	total += price * item.getQuantity();
    //    	    if (item instanceof TaxableItem) {
    //             TaxableItem taxableItem = (TaxableItem) item;
    //             double tax = taxableItem.getTaxRate() / 100.0 * item.getPrice();
    //             total += tax;
    //         }
    //     }
    // 	if (hasGiftCard()) {
    //     	total -= 10.0; // subtract $10 for gift card
    // 	}
    // 	if (total > 100.0) {
    //     	total *= 0.9; // apply 10% discount for orders over $100
    // 	}
    // 	return total;
    // }

    // -------------- Fixed Code Smell #1 ----------------------
    public double calculateTotalPrice() {
        double total = 0.0;
        for (Item item : items) {
            double itemTotal = calculateItemDiscount(item) * item.getQuantity(); // new method to calculate discount
            // -------------- Fixed Code Smell #4 ----------------------
            // Modified usage of calculateTax() as it has been moved to TaxableItem.java
            if (item instanceof TaxableItem) {
                TaxableItem taxableItem = (TaxableItem) item;
                itemTotal += taxableItem.calculateItemTax();
            }
            // ---------------------------------------------------------
            total += itemTotal;
        }
        total = applyOrderDiscounts(total); // new method to see if any discounts can be applied
        return total;
    }

    private double calculateItemDiscount(Item item) {
        double price = item.getPrice();
        switch (item.getDiscountType()) {
            case PERCENTAGE:
                price -= item.getDiscountAmount() * price;
                break;
            case AMOUNT:
                price -= item.getDiscountAmount();
                break;
            default:
                // no discount
                break;
        }
        return price;
    }

    // -------------- Fixed Code Smell #4 ----------------------
    // Moved to TaxableItem.java to uphold Single Responsibility Principle
//    private double calculateItemTax(Item item) {
//        if (item instanceof TaxableItem) {
//            TaxableItem taxableItem = (TaxableItem) item;
//            return (taxableItem.getTaxRate() / 100.0) * taxableItem.getPrice();
//        }
//        return 0.0;
//    }

    private double applyOrderDiscounts(double total) {
        if (hasGiftCard()) {
            total -= GIFT_CARD_DISCOUNT; // subtract $10 for gift card
        }
        if (total > LARGE_ORDER_DISCOUNT_THRESHOLD) {
            total *= LARGE_ORDER_DISCOUNT_RATE; // apply 10% discount for orders over $100
        }
        return total;
    }
    // --------------------------------------------------------------------------

    public void sendConfirmationEmail() {
        String message = "Thank you for your order, " + customerName + "!\n\n" +
                "Your order details:\n";
        for (Item item : items) {
            message += item.getName() + " - " + item.getPrice() + "\n";
        }
        message += "Total: " + calculateTotalPrice();
    //  Code Smell: Couplers - Middle Man
    //  EmailSender.sendEmail(customerEmail, "Order Confirmation", message);
    //
    // Fixed Code Smell:
        System.out.println("Email to: " + customerEmail);
        System.out.println("Subject: Order Confirmation");
        System.out.println("Body: " + message);
    }


    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public boolean hasGiftCard() {
        boolean has_gift_card = false;
        for (Item item : items) {
            if (item instanceof GiftCardItem) {
                has_gift_card = true;
                break;
            }
        }
        return has_gift_card;
    }

   public void printOrder() {
        System.out.println("Order Details:");
        for (Item item : items) {
            System.out.println(item.getName() + " - " + item.getPrice());
        }
   }

   public void addItemsFromAnotherOrder(Order otherOrder) {
        for (Item item : otherOrder.getItems()) {
            items.add(item);
        }
   }

}


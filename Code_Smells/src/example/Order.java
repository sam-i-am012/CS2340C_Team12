package example;

import java.util.List;

public class Order {
    private static final double GIFT_CARD_DISCOUNT = 10.0;
    private static final double LARGE_ORDER_DISCOUNT_THRESHOLD = 100.0;
    private static final double LARGE_ORDER_DISCOUNT_RATE = 0.9;

    private List<Item> items;
    private String customerName;
    private String customerEmail;

    public Order(List<Item> items, String customerName, String customerEmail) {
        // -------------- Fixed Code Smell #6 ----------------------
        // added null and empty string checks and messages for `items`, `customerName`, and `customerEmail` in the constructor
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("example.Item list cannot be null or empty");
        }
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        if (customerEmail == null || customerEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer email cannot be null or empty");
        }
        // ---------------------------------------------------------
        this.items = items;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }

    // public double calculateTotalPrice() {
    // 	double total = 0.0;
    // 	for (example.Item item : items) {
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
    //    	    if (item instanceof example.TaxableItem) {
    //             example.TaxableItem taxableItem = (example.TaxableItem) item;
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
            // Modified usage of calculateTax() as it has been moved to example.TaxableItem.java
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
    // Moved to example.TaxableItem.java to uphold Single Responsibility Principle
//    private double calculateItemTax(example.Item item) {
//        if (item instanceof example.TaxableItem) {
//            example.TaxableItem taxableItem = (example.TaxableItem) item;
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
    //  EmailSender.sendEmail(customerEmail, "example.Order Confirmation", message);
    //
    // Fixed Code Smell:
        System.out.println("Email to: " + customerEmail);
        System.out.println("Subject: example.Order Confirmation");
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
        // -------------- Fixed Code Smell #6 ----------------------
        // added null and empty string check for `items` in the appropriate setter
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("example.Item list cannot be null or empty");
        }
        // ---------------------------------------------------------
        this.items = items;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        // -------------- Fixed Code Smell #6 ----------------------
        // added null and empty string check and message for `customerName` in the appropriate setter
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        // ---------------------------------------------------------
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        // -------------- Fixed Code Smell #6 ----------------------
        // added null and empty string check and message for `customerEmail` in the appropriate setter
        if (customerEmail == null || customerEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer email cannot be null or empty");
        }
        // ---------------------------------------------------------
        this.customerEmail = customerEmail;
    }

    // -------------- Fixed Code Smell #5 ----------------------
    //`example.Order` doesn't depend on example.GiftCardItem subclass
    public boolean hasGiftCard() {
        for (Item item : items) {
            if (item.isGiftCard()) {
                return true;
            }
        }
        return false;
    }
    // --------------------------------------------------------------------------

   public void printOrder() {
        System.out.println("example.Order Details:");
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


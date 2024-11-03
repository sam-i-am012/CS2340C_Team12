Participating members: Samantha Mau, Kyle Sanquist, Jackson Li, Jiayi Liang, Riley Oliver, Braden Flournoy


1. A code smell can be found in the `Order` class in the `calculateTotalPrice` method. This method violates the **single responsibility principle** because the method has multiple responsibilites. In this one method, it applies discounts based on the `DiscountType`, adds taxes if the item is a `TaxableItem`, and applies a $10 deduction if there's a gift card and applies a 10% discount for orders over $100. All these responsiblities under one method make it hard to read, test, and maintain. 

    To fix this code smell, I would move each responsbility into a separate method and have `calculateTotalPrice` orchestrate the total calculation. The new methods I would add: 
    - `calculateItemDiscount`: apply discount 
    - `calculateItemTax`: calculate tax if the item is taxable
    -  `applyOrderDiscounts`: handle discounts like gift card and the total > $100 discount. 


2. A code smell can be found in the 'Item' class. 
   Issue: The Item class currently handles both item attributes and discount-related properties. This violates the **single responsibility principle**.
   To fix this code smell I have:
   - **Created `Discount` Class**: The `Discount` class is introduced to encapsulate discount-related logic, including types and amounts. It now manages how discounts are applied, isolating this functionality from `Item`.
   - **Refactored `Item` Class**: The `Item` class was simplified to focus only on item attributes: `name`, `price`, and `quantity`.
   - **Updated `TaxableItem` Class**: The `TaxableItem` class was modified to use the new `Discount` class. It now calculates the total price including tax while utilizing the discount logic from the `Discount` class.
   After the fix:
   - Each class now has a single reason to change, which adheres to the SRP. `Item` handles item properties, while `Discount` manages discount logic.

3. A code smell can be found in the 'EmailSender' class.
   Issue: The EmailSender class only serves one purpose, which includes a static method that prints out three lines of information. This class is violating the **single responsibility principle**, delegating work to the 'Order' class.
   Currently, the 'EmailSender' class is acting as a middle man, only delegating work to the 'Order' class
   To fix this code smell, I have:
   - Completely deleted the 'EmailSender' class
   - Incorporated the 'sendEmail' method into the 'Order' class by simply moving the code to where the method was called
   - Removed the calling of the 'sendEmail' method in the 'Order' class
   This has simplified the code as there is now one less class to worry about. This refactoring has simplified the design without sacrificing functionality.
4. 
5. 
6. 




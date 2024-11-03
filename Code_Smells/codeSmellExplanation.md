Participating members: Samantha Mau, Kyle Sanquist, Jackson Li, Jiayi Liang, Riley Oliver, Braden Flournoy


1. A code smell can be found in the `Order` class in the `calculateTotalPrice` method. This method violates the **single responsibility principle** because the method has multiple responsibilites. In this one method, it applies discounts based on the `DiscountType`, adds taxes if the item is a `TaxableItem`, and applies a $10 deduction if there's a gift card and applies a 10% discount for orders over $100. All these responsiblities under one method make it hard to read, test, and maintain. 

    To fix this code smell, I would move each responsbility into a separate method and have `calculateTotalPrice` orchestrate the total calculation. The new methods I would add: 
    - `calculateItemDiscount`: apply discount 
    - `calculateItemTax`: calculate tax if the item is taxable
    -  `applyOrderDiscounts`: handle discounts like gift card and the total > $100 discount. 


2. A code smell can be found in the `Order` class. 
   Issue: The code smell identified was the Magic Numbers issue.
   In the Order class, discount values were hard-coded, making the code harder to understand and modify. The values 10.0 for gift card discounts and 0.9 for large-order discounts were used directly in calculations.
   Refactoring:
   - By defining these values as constants with meaningful names, we improve code readability and maintainability. The constants make it easier to adjust values in the future without searching through the codebase.
   - Added 3 new constants to resolve this issue.

3. A code smell can be found in the 'EmailSender' class.
   Issue: The EmailSender class only serves one purpose, which includes a static method that prints out three lines of information. This class is violating the **single responsibility principle**, delegating work to the 'Order' class.
   Currently, the 'EmailSender' class is acting as a middle man, only delegating work to the 'Order' class
   To fix this code smell, I have:
   - Completely deleted the 'EmailSender' class
   - Incorporated the 'sendEmail' method into the 'Order' class by simply moving the code to where the method was called
   - Removed the calling of the 'sendEmail' method in the 'Order' class
   This has simplified the code as there is now one less class to worry about. This refactoring has simplified the design without sacrificing functionality.

4. A code smell can be found in the `Order` class.  
   Issue: the `calculateItemTax` method in `Order` should be moved to the `TaxableItem` class to uphold the
   **single responsibility principle** as it makes sense that a taxable item should be in charge of calculating
   its own tax.  
   To fix this code smell the following has been done:
   - Removed the calculateItemTax method from the `Order` class.
   - The implementation of the calculateItexTax has been modified as it no longer needs to check if the item is taxable.
   - The usage of the `calculateItemTax` method has been modified to check if the item is taxable before adding the tax.
5. 
6. 




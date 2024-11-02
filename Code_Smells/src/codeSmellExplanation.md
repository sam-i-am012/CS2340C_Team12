Participating members: Samantha Mau, Kyle Sanquist, Jackson Li, Jiayi Liang, Riley Oliver, Braden Flournoy


1. A code smell can be found in the `Order` class in the `calculateTotalPrice` method. This method violates the **single responsibility principle** because the method has multiple responsibilites. In this one method, it applies discounts based on the `DiscountType`, adds taxes if the item is a `TaxableItem`, and applies a $10 deduction if there's a gift card and applies a 10% discount for orders over $100. All these responsiblities under one method make it hard to read, test, and maintain. 

    To fix this code smell, I would move each responsbility into a separate method and have `calculateTotalPrice` orchestrate the total calculation. The new methods I would add: 
    - `calculateItemDiscount`: apply discount 
    - `calculateItemTax`: calculate tax if the item is taxable
    -  `applyOrderDiscounts`: handle discounts like gift card and the total > $100 discount. 


2. 
3. 
4. 
5. 
6. 




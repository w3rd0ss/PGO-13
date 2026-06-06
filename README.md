# PGO-13
# PGO13

Description of Classes
Main:
The starting point of the program. It sets up the initial data and runs the console menu loop to take user input.

Student:
A simple data class representing the person renting the gear. It stores their basic info and keeps track of their loyalty points.

Equipment:
The abstract parent class for all rentable items. It holds shared data like the ID, name, and base price, and it forces all child classes to create their own price calculation method.

LaptopSet:
A concrete class extending Equipment. It adds laptop-specific details like RAM size and whether it has a docking station, and uses these to calculate its own specific daily price.

CameraKit:
Another concrete class extending Equipment. It tracks the number of lenses and tripods, calculating its daily price based on that specific gear.

Reservation: 
The link that connects a Student to a piece of Equipment for a set number of days. It also calculates the final checkout price by applying a discount policy.

ReservationService:
The actual "brain" of the program. It handles the main business logic: checking equipment availability, creating or returning reservations, and generating the final reports.

LoyaltyDiscountPolicy:
The specific rule that implements the discount algorithm, giving a 10% price cut to students with 100 or more loyalty points.

ReservationException:
A custom exception I created to cleanly handle business logic errors, like when someone tries to rent an item that is already taken.


Displayable: 
This interface forces any class that implements it to have a getDisplayText() method. Both Equipment (and its subclasses) and Reservation implement this, making it super easy to print standardized, readable text to the console.

DiscountPolicy:
An interface used to calculate price reductions. By using this, the Reservation class doesn't need to know *how* the discount is calculated, just that it will be. LoyaltyDiscountPolicy implements this, making it easy to add completely new discount rules in the future without breaking existing code.

Polymorphism Example:
When the program iterates through a List<Equipment>, it calls equipment.calculateDailyPrice() on every item. Because of polymorphism, Java dynamically figures out exactly which math to use at runtime. If the item happens to be a LaptopSet, it calculates the base price plus the extra fees for RAM. If it’s a CameraKit, it calculates the fee based on the lenses. The calling code (like the Reservation class) doesn't need to write messy if/else statements to check what type of equipment it is looking at; it just trusts the object to calculate its own price correctly using the overridden method.

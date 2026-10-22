# SC2002 Restaurant Rush Group Assignment 

Repository: [Restaurant Rush](https://github.com/Varat-S/SC2002_Restaurant_Rush_Group_Assignment)

## 1. Game Overview 

The game lasts 18 turns, with 2 tables and scheduled customer arrivals on turns 1, 4, 7, 10, 13 and 16.

To win, the restaurant must finish turn 18 with:
- At least 4 paid customers
- At least $45.00 in revenue
- Average satisfaction of paid customers of at least 60

The roles:
- Customers choose their own menu items deterministically.
- The Manager chooses the Waiter's task (seat, take order, serve, or wait) and which order the Chef prepares; 
- The Cashier automatically processes eligible payments.

## 2. Requirements and running the game 

To run the game, you need to have: 
- Java Development Kit (JDK) installed, including javac and java
- Terminal or command prompt

No additional libraries and no internet connection is required during gameplay.

From the repository root, run one stage at a time by typing these in the terminal:

Stage 1:
```
  cd stage-1
  javac Main.java boundary/*.java control/*.java entity/*.java
  java Main
```

Stage 2:
```
  cd stage-2
  javac Main.java boundary/*.java control/*.java entity/*.java
  java Main
```
  
Stage 3 (final version):
```
  cd stage-3
  javac Main.java boundary/*.java control/*.java entity/*.java
  java Main
```

If you are already in a stage folder, only run its javac and java Main commands. 
Return to the repository root (cd ..) before entering a different stage folder.

At each Waiter or Chef prompt, type the number of the action shown in the menu. 
0 means wait. An invalid menu choice prompts the player to try again. 
In Stage 3, type y or n when asked whether to activate Happy Hour.

## 5. Development Stages and Changes 

### Stage 1: Basic Restaurant Operation 
Tag: [Stage 1](https://github.com/Varat-S/SC2002_Restaurant_Rush_Group_Assignment/tree/stage-1) Commit: [ebf8485](https://github.com/Varat-S/SC2002_Restaurant_Rush_Group_Assignment/commit/ebf8485)
- Implemented regular customers, waiting queues and tables, a basic menu, and the Waiter/Chef/Cashier workflow. 
- Added order and customer states, satisfaction loss, abandonment, revenue tracking, and victory/defeat evaluation.

### Stage 2: Customer Behaviour (Addition of VIP and Critic customers)
Tag: [Stage 3](https://github.com/Varat-S/SC2002_Restaurant_Rush_Group_Assignment/tree/stage-2) Commit: [a45eb39](https://github.com/Varat-S/SC2002_Restaurant_Rush_Group_Assignment/commit/a45eb39)

New files:
| File | Purpose |
| ---- | ---- | 
| `RegularCustomer.java` | Keeps standard customer attributes |
| `Order.java` | Applies the customer-specific discount when the order is created to reflect |
| `CriticCustomer.java` | Loses 12 satisfaction per waiting turn and chooses the most expensive menu item |

Changed files: 
| File | Purpose |
| ---- | ---- | 
| `Customer.java` | Changed into an abstract base class with polymorphic satisfaction-loss and discount methods, allowing different customer behaviour without duplicating the basic state flow. |
| `Order.java` | Applies the customer-specific discount when the order is created so the bill reflects it. |
| `GameController.java` | Creates Regular, VIP and Critic customers at deterministic arrival turns while preserving the same order and service process. |

Deleted files: None 

### Stage 3: New Role and Menu Policy (Addition of Host, Combo Meals and Happy Hour)
Tag: [Stage 3](https://github.com/Varat-S/SC2002_Restaurant_Rush_Group_Assignment/tree/stage-3) Commit: [1a6aa09](https://github.com/Varat-S/SC2002_Restaurant_Rush_Group_Assignment/commit/1a6aa09)

New files:
| File | Purpose |
| ---- | ---- | 
| `Host.java` | Adds a new staff role that can seat one waiting customer per turn. |
| `ComboMeal.java` | Combines two menu items, with combined preparation units and 10% off their total price. |
| `PricingPolicy.java` | Defines baseline pricing policy behaviour. |
| `HappyHour.java` | Implements a two-turn promotion with 20% order discounts and +15 satisfaction recovery. |

Changed files: 
| File | Purpose |
| ---- | ---- | 
| `MenuItem.java` | Converted from a concrete class to an interface so both standard dishes and combo meals can be treated as menu items |
| `Menu.java` | Added the Salad + Pasta combo |
| `Customer.java` | Added satisfaction recovery for eligible waiting customers. |
| `Order.java` | Accepts the final calculated price at order creation to lock in applicable discounts |
| `Waiter.java` | Passes the calculated bill when creating an order |
| `Restaurant.java` | Owns the Host and pricing policy and manages Happy Hour activation/duration |
| `GameController.java` | Incorporated Host seating, Happy Hour activation and policy-based order pricing into the turn sequence |
| `GameUI.java` | Displays Host availability and Happy Hour status, and collects the activation decision |

Deleted files: None 

## 6. Reproducible Runs

### To achieve a successful Stage 3 run: 
- Prioritise available Waiter actions in the order: Serve → Take order → Seat → Wait
- Have the Chef advance the first available order (otherwise Wait). 
- Activate Happy Hour at the start of turn 8.

Outcome: VICTORY — 5 paid customers, $94.80 revenue, and 82.0 average paid satisfaction.

### To achieve a failed run on any stage:
- Choose 0 for both Waiter and Chef on every turn 

Outcome: DEFEAT — 0 paid customers, $0.00 revenue, average paid satisfaction N/A as the Restaurant will complete all 16 turns without serving any customers. 

## 7. Video Timestamps 
[To Be Completed]

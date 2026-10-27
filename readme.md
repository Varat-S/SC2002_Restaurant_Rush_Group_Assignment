# SC2002 Restaurant Rush Group Assignment 

Repository: [Restaurant Rush](https://github.com/Varat-S/SC2002_Restaurant_Rush_Group_Assignment)

## 1. Game Overview 

Restaurant Rush is a turn-based restaurant management game developed across three stages:

- Stage 1: Basic restaurant operations involving customers, Waiter, Chef and Cashier.
- Stage 2: Introduces Regular, VIP and Critic customers with different satisfaction-loss rates, discounts and ordering behaviours.
- Stage 3: Introduces the Host, combo meals and Happy Hour promotions.

The game lasts 18 turns, with 2 tables and scheduled customer arrivals on turns 1, 4, 7, 10, 13 and 16.

To win, the restaurant must finish turn 18 with:
- At least 4 paid customers
- At least $45.00 in revenue
- Average satisfaction of paid customers of at least 60

Roles:
- Manager (Player): Chooses the Waiter's task and which order the Chef prepares. In Stage 3, the Manager can also activate Happy Hour once per game for 2 consecutive turns, which provides a 20% discount on new orders and recovers 15 satisfaction points for eligible customers at the start of each active turn.
- Waiter: Seats customers, takes orders or serves food.
- Chef: Prepares orders.
- Cashier: Automatically processes eligible payments.
- Host (Stage 3): Automatically seats waiting customers when tables are available, independent of the Waiter
- Customers: Choose their own menu items deterministically and lose satisfaction while awaiting service.

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

## 3. Development Stages and Changes 

### Stage 1: Basic Restaurant Operation 
Tag: [Stage 1](https://github.com/Varat-S/SC2002_Restaurant_Rush_Group_Assignment/tree/stage-1) Commit: [ebf8485](https://github.com/Varat-S/SC2002_Restaurant_Rush_Group_Assignment/commit/ebf8485)
- Implemented customers, waiting queues and tables, a basic menu, and the Waiter/Chef/Cashier workflow. 
- Added order and customer states, satisfaction loss, abandonment, revenue tracking, and victory/defeat evaluation.
- Customers choose their orders based on the menu selection rule: customerId % menu.size()

### Stage 2: Customer Behaviour (Addition of VIP and Critic customers)
Tag: [Stage 2](https://github.com/Varat-S/SC2002_Restaurant_Rush_Group_Assignment/tree/stage-2) Commit: [a45eb39](https://github.com/Varat-S/SC2002_Restaurant_Rush_Group_Assignment/commit/a45eb39)

New files:
| File | Purpose |
| ---- | ---- | 
| `RegularCustomer.java` | Implements regular customer behaviour: loses 8 satisfaction per waiting turn and pays full price. |
| `VIPCustomer.java` | Implements VIP behaviour: loses 5 satisfaction per waiting turn and receives a 10% discount. |
| `CriticCustomer.java` | Implements Critic behaviour: loses 12 satisfaction per waiting turn and chooses the most expensive menu item. |

Changed files: 
| File | Purpose |
| ---- | ---- | 
| `Customer.java` | Changed into an abstract superclass with overrideable satisfaction-loss and discount methods, allowing different customer behaviour while sharing the same service flow. |
| `Order.java` | Applies the customer-specific discount when the order is created so that the bill is not accidentally altered. |
| `GameController.java` | Creates Regular, VIP and Critic customers at scheduled arrivals while preserving the existing order and service flow. |

Deleted files: None.

### Stage 3: New Role and Menu Policy (Addition of Host, Combo Meals and Happy Hour)
Tag: [Stage 3](https://github.com/Varat-S/SC2002_Restaurant_Rush_Group_Assignment/tree/stage-3) Commit: [1a6aa09](https://github.com/Varat-S/SC2002_Restaurant_Rush_Group_Assignment/commit/1a6aa09)

New files:
| File | Purpose |
| ---- | ---- | 
| `Host.java` | Adds a new staff role that can seat one waiting customer per turn and is independent of the Waiter. |
| `ComboMeal.java` | Combines two existing menu items, with preparation units equal to the sum of both items' preparation units, and a price equal to 90% of their combined price, rounded to the nearest cent. |
| `PricingPolicy.java` | Defines baseline pricing policy behaviour used by the restaurant, allowing Happy Hour pricing to be handled through a policy object. |
| `HappyHour.java` | Implements a once-per-game two-turn promotion with 20% off orders taken while active and +15 points satisfaction recovery for customers awaiting service at turn start. Existing order bills do not change. |
| `StandardMenuItem.java` | Holds the standard menu item (Salad, Burger and Pasta) constants in an enum implementing MenuItem, preserving them after MenuItem becomes an interface. |

Changed files: 
| File | Purpose |
| ---- | ---- | 
| `MenuItem.java` | Changes from an enum to an interface so standard dishes and combo meals share the same name, price and preparation-unit operations. |
| `Menu.java` | Builds the menu using standard dishes and adds the Salad + Pasta combo. |
| `Customer.java` | Adds satisfaction recovery for customers awaiting service, capped at 100. The existing menu selection rule (customerId % menu.size()) also supports selecting combo meals from the expanded menu for VIP and regular customers, while Critic customers continue choosing the most expensive menu item. |
| `Order.java` | Accepts the final calculated price at order creation to ensure later pricing policy changes do not alter an existing bill. |
| `Waiter.java` | Passes the calculated bill when creating an order. |
| `Restaurant.java` | Owns the Host and pricing policy, manages Happy Hour activation and duration, and exposes the active pricing and satisfaction-recovery behaviour. |
| `GameController.java` | Integrates automatic Host seating, Happy Hour activation, satisfaction recovery and policy-based order pricing into the turn sequence. |
| `GameUI.java` | Displays Host availability and Happy Hour status, and collects the manager's Happy Hour activation decision. |

Deleted files: None.

## 4. Reproducible Runs

Use the base setting for all runs. Start a new game for each scenario.

Menu numbers can change as available actions change. Follow the action
descriptions below rather than a fixed sequence of numbers. When multiple
actions have the same priority, choose the first one listed.

### Stage 1 - VICTORY: 
Follow these rules when running Stage 1 to achieve victory:

Waiter:
1. Serve the first available ready order.
2. Or else, take the first available customer's order.
3. Otherwise, seat the first available customer.
4. Else, choose Wait.

Chef:
1. Advance the first available order.
2. If no order is available, choose Wait.

Expected final output:

```
=== VICTORY ===
Paid customers: 4 (target 4)
Revenue: $57.00 (target $45.00)
Average paid satisfaction: 78.0 (target 60.0)
```

### Stage 2 - VICTORY: 
Follow these rules when running Stage 2 to achieve victory:

Waiter:
1. Serve the first available ready order.
2. Or else, take the first available customer's order.
3. Otherwise, seat the first available customer.
4. Else, choose Wait.

Chef:
1. Advance the first available order.
2. If no order is available, choose Wait.

Expected final output:

```
=== VICTORY ===
Paid customers: 4 (target 4)
Revenue: $58.20 (target $45.00)
Average paid satisfaction: 74.3 (target 60.0)
```


### Stage 3 - VICTORY: 
Follow these rules when running Stage 3 to achieve victory:

Happy Hour: 
1. Answer n on turns 1 through 7.
2. Answer y at the start of turn 8 such that Happy Hour is active during turns 8 and 9.

Host:
Seating happens automatically; no player input is required.

Waiter:
1. Serve the first available ready order.
2. Or else, take the first available customer's order.
3. Otherwise, seat the first available customer.
4. Else, choose Wait.

Chef:
1. Advance the first available order.
2. If no order is available, choose Wait.

Expected final output:

```
=== VICTORY ===
Paid customers: 5 (target 4)
Revenue: $94.80 (target $45.00)
Average paid satisfaction: 82.0 (target 60.0)
```


### ALL STAGES - DEFEAT:
Follow these rules when starting a new game in Stage 1, Stage 2, or Stage 3 for DEFEAT:

1. Choose 0 (Wait) at every Waiter prompt.
2. Choose 0 (Wait) at every Chef prompt.
3. In Stage 3, answer n whenever asked to activate Happy Hour.

In Stage 3, the Host still seats customers automatically, however,
without the Waiter taking orders, no food will be prepared or served and no
payments will be collected.

Expected final output all three stages:

```
=== DEFEAT ===
Paid customers: 0 (target 4)
Revenue: $0.00 (target $45.00)
Average paid satisfaction: N/A (target 60.0)
```

Average paid satisfaction is N/A because no customers have paid.

## 5. Video Timestamps 
[To Be Completed]

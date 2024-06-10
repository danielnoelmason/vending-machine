package part01;

import java.util.Scanner;

/**
 * This class models a Vending Machine UI, it provides the user with choices and
 * the choices call upon Vending Machine states and behaviours * @author <Danie
 * Mason>
 */
public class VendingMachineTestMenu {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);

		// the following is the vending machine and four items being instantiated
		VendingMachine myVM = new VendingMachine("Daniels VM", 5);
		VendItem newItem1 = new VendItem("Monster Mango-Loco", 1.50, 10);
		VendItem newItem2 = new VendItem("Monster Pacific-Rim", 1.50, 5);
		VendItem newItem3 = new VendItem("Monster Ultra Zero", 1.40, 1);
		VendItem newItem4 = new VendItem("Red Bull", 2);//example of constructor without qtyAvaible

		// the following is the items being added to the system (HARD CODED STARTING
		// DATA)
		myVM.addNewItem(newItem1);
		myVM.addNewItem(newItem2);
		myVM.addNewItem(newItem3);
		myVM.addNewItem(newItem4);

		String options[] = new String[] { "View Items", "Purchase Item", "Insert Coin", "View Balance",
				"EXIT\n----Additonal features(Part02)----", "View System Information", "Reset System", "Set Mode",
				"Add New Item", "Restock Item" }; // options for
													// the
													// constructor
													// of the
													// menu
													// class

		Menu menu = new Menu("Daniels VM", options); // menu being instantiated

		menu.display();
		int num = menu.getChoice();

		System.out.println();
		while (num != 5) { // i.e. "Quit"
			switch (num) {
			case 0: // Display menu
				menu.display();
				break;

			case 1: // view items
				System.out.println(myVM.printListItems());
				break;

			case 2:// purchase item
				if (myVM.getItemCount() == 0) {
					System.out.println("No Items Available at this time");
					break;
				}
				System.out.print("------PURCHASING ITEM------\nEnter Item Id: ");
				int choice = input.nextInt();
				while (!(choice > 0 && choice <= myVM.getItemCount())) {
					System.out.println("INVALID ID - Enter Item Id: ");
					choice = input.nextInt();
				}
				System.out.println(myVM.purchaseItem(choice));
				break;

			case 3:// insert coins
				System.out.print("------INSERT COIN (2, 1, 0.50, 0.20, 0.10, 0.05)----- \n£");
				double coinDouble = input.nextDouble();
				int coinInt = myVM.coinFormatterReverse(coinDouble);

				while (myVM.insertCoin(coinInt) == false) {
					System.out.print("INVALID COIN - Enter valid coin: £");
					coinDouble = input.nextDouble();
					coinInt = myVM.coinFormatterReverse(coinDouble);
				}
				System.out.println("Coin Inserted: " + myVM.currencyFormatter(coinDouble) + "\t" + myVM.getBalance());
				break;

			case 4:// view balance
				System.out.println(myVM.getBalance());
				break;

			// THE FOLLOWING OPTIONS ARE PART02, UNSURE IF THEY ARE REQUIRED FOR PART01
			case 6:// get system information
				System.out.println(myVM.getSystemInfo());
				break;

			case 7: // reset system (empty items and money)
				System.out.println("....system resetting....");
				myVM.reset();
				System.out.println("....System Reset");
				break;

			case 8: // set the vm status
				System.out.println("----- VENDING MODE SELECTION----\n1.Vending Mode \t 2.Service Mode\nEnter Choice:");
				int mode = input.nextInt();
				while (mode != 1 && mode != 2) {
					System.out.println("Select 1 or 2:");
					mode = input.nextInt();
				}
				if (mode == 1) {
					myVM.setStatus(Status.VENDING_MODE);
				}
				if (mode == 2) {
					myVM.setStatus(Status.SERVICE_MODE);
				}
				System.out.println(myVM.getStatus());
				break;

			case 9: // add new product to the vending machine
				if (myVM.getItemCount() == myVM.getMaxItems()) {
					System.out.print("-----VENDING MACHINE FULL------\n");
				}

				if (myVM.getItemCount() < myVM.getMaxItems()) {
					System.out.print("------ADDING NEW PRODUCT------\nEnter Item Name: ");
					String itemName = input.next();
					System.out.print("Enter Item Price: £");
					double itemPrice = input.nextDouble();

					System.out.print("Enter Item Quantity: ");
					int itemQty = input.nextInt();
					while (itemQty > VendItem.getMaxQty()) {
						System.out.print("Vending Machine can only store 10, enter a valid value: ");
						itemQty = input.nextInt();
					}

					VendItem newItem = new VendItem(itemName, itemPrice, itemQty);
					myVM.addNewItem(newItem);
					System.out.print("Succesfully Addded: " + newItem.getName());

				} else {
					System.out.print("-----VENDING MACHINE FULL------\n");
				}
				break;

			case 10: // re-stock an item in the vm
				if (myVM.getItemCount() == 0) {
					System.out.println("No items to Restock");
					break;
				}
				System.out.println("Enter Item ID for Restock:");
				int itemId = input.nextInt();

				// (itemId<=0 ||itemId>myVM.getItemCount())
				while (itemId <= 0 || itemId > myVM.getItemCount()) {
					System.out.print("INVALID ID - Enter Item Id: ");
					itemId = input.nextInt();
				}
				if (myVM.stockOf(itemId) == 10) {
					System.out.println("Stock Full for this Item");
				} else {
					System.out.println(
							"Enter Restock Ammount (Must be less then or " + (10 - myVM.stockOf(itemId)) + "): ");
					int restockQty = input.nextInt();
					while (restockQty <= 0 || restockQty > (10 - myVM.stockOf(itemId))) {
						System.out.println("Invalid Restock, Enter valid amount: ");
						restockQty = input.nextInt();
					}
					if (myVM.restock(itemId, restockQty)) {
						System.out.println("Succesfully restocked!");
					} else {
						System.out.println("Unsuccessful restock");
					}
				}
				break;

			default: // catches invalid choices
				System.out.println("Error - Should not get here!");
			}
			
			// THE FOLLOWING REPEATS AFTER SELECTED CASE HAS BEEN RAN THROUGH UNTIL USER
			// EXITS(5)
			System.out.println(
					"\n1.VIEW ITEMS------ 2.PURCHASE ITEM------ 3.INSERT COIN------ 4.VIEW BALANCE------ 5.EXIT ------- 0-MAIN MENU\n");
			num = menu.getChoice();
			System.out.println();
		}
		
		System.out.println(myVM.returnChange()); // vm returns user change and thanks them

		input.close();

	}

}

package part02;

import java.util.Scanner;

/**
 * This class is for testing part02 features, this is different from the other
 * as it includes the csv read write function. I made this a separate class so
 * assessor could see other part02 functions without csv causing problems if it
 * doesnt work 
 * Hidden Menu Option = 99, Username = u, Password = p
 * @author <DanielMason>
 */

public class VendingMachineTestMenuPart02CSV {

	// PART 02 2.2
	// Please do not edit the csv files in excel, they should only be edited in the
	// program or for some reason it says the csv has no lines
	// i have included a different test menu if this one has errors
	
	// ************Change these strings to csv. directory in submission***************
	private static String csvInPath = "/Users/danielmason/Desktop/CSC1029 40230007/VendingMachineState.csv";// csv file
																											// being
																											// read from
	private static String csvOutPath = "/Users/danielmason/Desktop/CSC1029 40230007/VendingMachineState.csv";// csv file
																												// being
																												// wrote
																												// to

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);

		// PART 02 2.2
		// The following is the creation of the VendingMachine object using the
		// VendingMachineReadClass
		VendingMachineRead vmImport = new VendingMachineRead(); // reader class created
		VendingMachine myVM = vmImport.importVmState(csvInPath, true); // VendingMachine object returned using csv. data

		String options[] = new String[] { "View Items", "Purchase Item", "Insert Coin", "View Balance", "EXIT" };
		Menu menu = new Menu("Imported VM", options); // menu being instantiated

		// PART 02 2.1
		// This is the new menu for the engineer which allows the functions a customer
		// should not have access to
		// NOTE: it can be accessed by secret choice 999
		String mOptions[] = new String[] { "View System Information", "Reset System", "Set Mode", "Add New Item",
				"Restock Item" };
		Menu mMenu = new Menu("Maintenance Menu", mOptions);

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
					System.out.println("INVALID COIN - Enter valid coin: £");
					coinDouble = input.nextDouble();
					coinInt = myVM.coinFormatterReverse(coinDouble);
				}
				System.out.println("COIN INSERTED: " + myVM.currencyFormatter(coinDouble) + "\t" + myVM.getBalance());
				break;

			case 4:// view balance
				System.out.println(myVM.getBalance());
				break;

			case 99: // PART02 2.1 MAINTENANCE MENU

				// part02 2.1
				// This case asks the user for the username and password
				String username = "u";
				String password = "p";

				System.out.print("Enter Username: ");
				String cUsername = input.next();
				System.out.print("Enter Password: ");
				String cPassword = input.next();
				System.out.println(".....Loading.....");
				if (cUsername.contentEquals(username) && cPassword.contentEquals(password)) {
					System.out.println("Access Granted!");
					mMenu.display();
					int mNum = mMenu.getChoice();

					while (mNum != 99) { // i.e. "Quit"
						switch (mNum) {
						case 0: // Display Maintenance menu
							mMenu.display();
							break;
						case 1:// get system information
							System.out.println(myVM.getSystemInfo());
							break;

						case 2: // reset system (empty items and money)
							System.out.println("....system resetting....");
							myVM.reset();
							System.out.println("....System Reset");
							break;

						case 3: // set the vm status
							System.out.println(
									"----- VENDING MODE SELECTION----\n1.Vending Mode \t 2.Service Mode\nEnter Choice:");
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

						case 4: // add new product to the vending machine
							if (myVM.getItemCount() < myVM.getMaxItems()) {
								System.out.print("------ADDING NEW PRODUCT------\nEnter Item Name: ");
								String itemName = input.next();
								System.out.print("\nEnter Item Price: ");
								double itemPrice = input.nextDouble();

								System.out.print("\nEnter Item Quantity: ");
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

						case 5: // re-stock an item in the vm
							if (myVM.getItemCount() == 0) {
								System.out.println("No items to Restock");
								break;
							}
							System.out.println("Enter Item ID for Restock:");
							int itemId = input.nextInt();

							// (itemId<=0 ||itemId>myVM.getItemCount())
							while (itemId <= 0 || itemId > myVM.getItemCount()) {
								System.out.println("INVALID ID - Enter Item Id: ");
								itemId = input.nextInt();
							}
							if (myVM.stockOf(itemId) == 10) {
								System.out.println("-----STOCK FULL-----");
							} else {
								System.out.println("Enter Restock Ammount (Must be less then or "
										+ (10 - myVM.stockOf(itemId)) + "): ");
								int restockQty = input.nextInt();
								while (restockQty <= 0 || restockQty > (10 - myVM.stockOf(itemId))) {
									System.out.println("INVALID RESTOCK, ENTER VALID RESTOCK AMMOUNT: ");
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
							System.out.println("Error - Should not get here!MAINTENANCE");
						}
						System.out.println(
								"\nMaintenance Menu \n+++++++++++++\n1.System Information------ 2.Reset------ 3.Set Status------ 4.Add a new item------ 5.Restock an item -------0-Maintenance Menu ------- 99- Return\n");
						mNum = mMenu.getChoice();
						System.out.println();
					}

				} else { // if user inputs incorrect username or password they are denied access
					System.out.println("Access Denied!");
					menu.display();
					break;
				}
				// when 99 is pressed again, it breaks out of the while loop and returns it to
				// the user while loop
				System.out.println("....Returning to User Interface....");
				menu.display();
				break;

			default: // catches invalid choices
				System.out.println("Error - Should not get here!VENDINGMACHINE");
			}
			// THE FOLLOWING REPEATS AFTER SELECTED CASE HAS BEEN RAN THROUGH UNTIL USER
			// EXITS(5)
			System.out.println(
					"Choices \n+++++++\n1.VIEW ITEMS------ 2.PURCHASE ITEM------ 3.INSERT COIN------ 4.VIEW BALANCE------ 5.EXIT ------- 0-MAIN MENU\n");
			num = menu.getChoice();
			System.out.println();
		}

		input.close();
		System.out.println(myVM.returnChange()); // vm returns user change and thanks them

		// PART 02 2.2
		// csv storage export, this updates the VM state with any changes
		VendingMachineWrite.exportVmState(myVM, csvOutPath);

	}

}

package part02;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * this class is used so that when the the program is exited, the state of the
 * VendingMachine will be saved to a csv
 * 
 * @author danielmason
 *
 */
public class VendingMachineWrite {
	/**
	 * exportVmState takes a VendingMachine object and a csv file directory and
	 * exports its states to the csv
	 * 
	 * @param myVm       - the programs Vending Machine
	 * @param csvOutPath - the directory for the csv output file
	 */
	public static void exportVmState(VendingMachine myVm, String csvOutPath) {
		try {
			PrintWriter myPw = new PrintWriter(csvOutPath);// the myPw object will write to the csvOutPath parameter
			// print VendingMachine header
			myPw.println(
					"Owner, Max Items, Item Count, Total Money, User Money, VM Status, 5p Coins, 10p Coins, 20p Coins, 50p Coins, £1 Coins, £2 Coins");

			//print VendingMachine States
			myPw.print(myVm.getOwner() + ", ");
			myPw.print(myVm.getMaxItems() + ", ");
			myPw.print(myVm.getItemCount() + ", ");
			myPw.print(myVm.getTotalMoney() + ", ");
			myPw.print(myVm.getUserMoney() + ", ");
			myPw.print(myVm.getStatus() + ", ");
			myPw.println(myVm.getCoinDenominations());

			// print VendItem headers
			myPw.println("Item Name, Unit Price, QTY Available");

			//print VendItem States
			for (int i = 0; myVm.getStockArray()[i] != null; i++) {
				myPw.print(myVm.getStockArray()[i].getName() + ", ");
				myPw.print(myVm.getStockArray()[i].getPrice() + ", ");
				myPw.println(myVm.getStockArray()[i].getQty());
			}
			System.out.println("Vending Machine Data Output Complete.");//confirmation of files being updated
			myPw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}

package part02;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * PART 02 2.2
 * This class is for reading from the VendingMachineState.csv and importing the
 * VendingMachine state and the VendItems in the VM
 * 
 * @author danielmason
 *
 */
public class VendingMachineRead {

	/**
	 * Method takes the file path of the CSV to be read & a Boolean to indicate if it has a header.
	 * @param csvFilePath - the directory for the csv file storing the Vending Machine State
	 * @param hasHeader - will skip a line in the csv if has header if true
	 * @return - VendingMachine object fully intialised wih data and VendItems stored in csvFile
	 */
	public VendingMachine importVmState(String csvFilePath, boolean hasHeader) {
		
		try {
			File myFile = new File(csvFilePath);//the csv file
			Scanner mySc = new Scanner(myFile);//a scanner taking the csv as a parameter
			if (hasHeader) {//skip a line if it has a header
				mySc.nextLine();
			}
			String myVM = mySc.nextLine();//the data associated with the state of the VendingMachine
			String[] myVmParts = myVM.split(",");//array separating and holding the different fields of data
			
			String owner = myVmParts[0].trim();
			int maxItems = Integer.parseInt(myVmParts[1].trim());
			
			VendingMachine VM = new VendingMachine(owner, maxItems);//creation of the VendingMachine that method returns

			//the rest is involved with the state of the VendingMachine
			double totalMoney = Double.parseDouble(myVmParts[3].trim());
			double userMoney = Double.parseDouble(myVmParts[4].trim());
			Status vmStatus = Status.valueOf(myVmParts[5].trim());

			VM.setTotalMoney(totalMoney);
			VM.setUserMoney(userMoney);
			VM.setStatus(vmStatus);

			int coin5 = Integer.parseInt(myVmParts[6].trim());
			int coin10 = Integer.parseInt(myVmParts[7].trim());
			int coin20 = Integer.parseInt(myVmParts[8].trim());
			int coin50 = Integer.parseInt(myVmParts[9].trim());
			int coin100 = Integer.parseInt(myVmParts[10].trim());
			int coin200 = Integer.parseInt(myVmParts[11].trim());

			VM.setCoinDenominations(coin5, coin10, coin20, coin50, coin100, coin200);

			mySc.nextLine();//this skip line is due to headers for the VendItem instances, this data is only for display purposes

			while (mySc.hasNextLine()) {//while there is data under the VendItem headers
				String vendItem = mySc.nextLine();//the next line contains the VendItem States
				String[] vendItemsParts = vendItem.split(","); //array separates and holds the VendItem states

				//these are the states of a VendItem instance
				String name = vendItemsParts[0].trim();
				double unitPrice = Double.parseDouble(vendItemsParts[1].trim());
				int qtyAvailable = Integer.parseInt(vendItemsParts[2].trim());

				// Add new VendItem to the VendingMachine using csv data.
				VM.addNewItem(new VendItem(name, unitPrice, qtyAvailable));
			}

	
			mySc.close();
			return VM;//return the VendingMachine with the state it was last used
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// A null object is returned in the event of any errors.
			return null;
		}
	}
}
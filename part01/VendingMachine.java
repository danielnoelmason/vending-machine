package part01;

/**
 * This class models a Vending Machine, also defining some the functions
 * * @author <Danie Mason>
 */
public class VendingMachine {

	private String owner; // String to define owner of vm
	private int maxItems; // Don't allow more then this no of products to be added
	private int itemCount; // increment with each item add
	private VendItem stock[]; // array for holding VendItem instances
	private double totalMoney; // stores the Vend Machines money
	private double userMoney; // stores the Users Money
	private Status vmStatus; // Status of the vm to be changed appropriately

	/**
	 * This is the constructor method for the Vending Machine object. It sets the
	 * owner and maximum number of items It also sets the other variables to that of
	 * a new empty vm
	 * 
	 * @param owner    - the vm owner
	 * @param maxItems - the vm maximum items
	 */
	public VendingMachine(String owner, int maxItems) {
		this.owner = owner;
		this.maxItems = maxItems;
		this.itemCount = 0;
		this.stock = new VendItem[maxItems];
		this.vmStatus = Status.SERVICE_MODE;

	}

	/**
	 * The method getSystemInfo() should return a formatted string containing all
	 * details related to a VendingMachine instance
	 * 
	 * @return – the system information
	 */
	public String getSystemInfo() {
		String systemInfo;
		systemInfo = "Owner: " + this.owner + "\tMax Items: " + this.maxItems + "\tProducts: " + this.itemCount
				+ "\tItems In Stock: " + getStock() + "\tVM Balance: " + currencyFormatter(this.totalMoney)
				+ "\tUser Balance: " + currencyFormatter(this.userMoney) + "\tVM Status: " + this.vmStatus;

		return systemInfo;
	}

	/**
	 * The method reset() should reinitialise a VendingMachine instance:- empty of
	 * items and cash
	 */
	public void reset() {

		this.itemCount = 0;

		int index = 0;
		while (index<maxItems&&stock[index] != null) { // while stock array isn't null
			stock[index] = null; // resets stock array
			index++;
		}
		this.totalMoney = 0;
		this.userMoney = 0;
		setStatus(Status.SERVICE_MODE);
		VendItem.reset();

	}

	/**
	 * The method purchaseItem() should enable a user to request purchase of a
	 * specified item
	 * 
	 * @param itemId - represents the index of the VendItem in the stock array
	 * @return true – if a purchase request is approved, the deliver() method for a
	 *         VendItem object should be called and should return a string specific
	 *         to the item name, in line with: “Thanks for purchasing: <item-name>”
	 *         (or null if not available). This should be integrated with the result
	 *         of purchaseItem(). o if the last item in the machine is purchased,
	 *         the machine state should be set to SERVICE_MODE.
	 * @return - false if invalid request
	 */
	public String purchaseItem(int itemId) {

		String res = ""; // the return of this method

		if (itemId <= 0 || itemId > this.maxItems) { // if itemID not valid
			res += "INVALID ITEM SELECTION";
			return res;
		}

		int i = itemId - 1;
		if (stock[i] != null && i >= 0) {
			if (userMoney >= stock[i].getPrice() && stock[i].getQty() > 0) { // if userMoney enough for product and
																				// there
																				// is stock
				userMoney -= stock[i].getPrice(); // update userMoney
				totalMoney += stock[i].getPrice(); // update totalMoney
				res += stock[i].deliver(); // call deliver method (confirming purchase)
				res += "\n" + getBalance(); // calls getBalance (make more user friendly)
				if (stock[i].getQty() < 1) { // if after purchase, stock is empty
					setStatus(Status.SERVICE_MODE);
				}

				return res;
			}
			if (stock[i].getQty() == 0) { // if item selected is out of stock
				res += "OUT OF STOCK";
				return res;
			}
			if (userMoney < stock[i].getPrice() && stock[i].getQty() > 0) { // if userMoney is not enough
				res += "INSUFFICIENT FUNDS -- Insert " + currencyFormatter((stock[i].getPrice() - this.userMoney));
				return res;
			}

		}
		if (stock[i] == null || i < 0) {// another invalid itemID check
			res += "INVALID ITEM SELECTION";
		}
		return res;
	}

	/**
	 * A user should be able to (repeatedly) enter a coin – limited to 5p, 10p, 20p,
	 * 50p, £1 and £2 denominations (formatting performed with other methods)
	 * 
	 * @param userMoney - represents the amount user has input
	 * @return – true if user has input a valid coin, add it to their userMoney
	 * @return – false if user has not input a valid coin
	 */
	public boolean insertCoin(int userMoney) {
		if (userMoney == 5 || userMoney == 10 || userMoney == 20 || userMoney == 50 || userMoney == 100
				|| userMoney == 200) {
			this.userMoney += coinFormatter(userMoney);// turns int penny value into double money value and adds to
														// balance
			return true;
		} else {
			return false;
		}
	}

	/**
	 * allows addition of a new VendItem instance to stock[]
	 * 
	 * @param newItem - represents an instance of VendItem
	 * @return – true if VendItem is not null
	 * @return – else return false
	 */
	public boolean addNewItem(VendItem newItem) {

		if (itemCount >= maxItems) {
			return false;
		} else if (newItem != null) {
			stock[itemCount] = newItem;
			itemCount++;
			if (newItem.getQty() > 0) {
				setStatus(Status.VENDING_MODE);
			} else {
				setStatus(Status.SERVICE_MODE);
			}
			setStatus();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns an array of strings, containing the details of each item for sale.
	 * 
	 * @return – the items in stock
	 */
	public String[] listItems() {
		String[] items = new String[maxItems];
		for (int i = 0; i < items.length; i++) {
			if (stock[i] != null) {
				items[i] = "\nItemID " + this.stock[i].getItemId() + ": " + this.stock[i].getName() + "\tPrice: "
						+ currencyFormatter(this.stock[i].getPrice()) + "\tQuantity Availble: "
						+ this.stock[i].getQty();
			}

		}
		return items;
	}

	/**
	 * This method sets the Status Enumeration for the vm
	 * 
	 * @param status - the Status of the vm
	 */
	public void setStatus(Status status) {

		if (status == Status.SERVICE_MODE) {
			this.vmStatus = Status.SERVICE_MODE;
		} else if (status == Status.VENDING_MODE) {
			this.vmStatus = Status.VENDING_MODE;
		}
	}

	// THE FOLLOWING ARE THE MEHTODS ADDE THAT WERE NOT IN THE UML

	/**
	 * When called, sets the vm to service mode if conditions are met
	 */
	private void setStatus() {
		for (int i = 0; i<maxItems; i++) {
			if (stock[i]!= null && stock[i].getQty() < 1) {
				setStatus(Status.SERVICE_MODE);
			}
		}
	}

	/**
	 * retrieves the current vmStatus for UI print purposes
	 * 
	 * @return vmStatus as string
	 */
	public String getStatus() {
		String status = "Status: " + this.vmStatus.toString();
		return status;
	}

	/**
	 * retrieves the current user balance as formatted string
	 * 
	 * @return - user balance
	 */
	public String getBalance() {
		String res = "Your balance: " + currencyFormatter(this.userMoney);
		return res;
	}

	/**
	 * retrieves the item count
	 * 
	 * @return item count
	 */
	public int getItemCount() {
		return this.itemCount;
	}

	/**
	 * retrieves the Max Items for statements on UI
	 * 
	 * @return max items
	 */
	public int getMaxItems() {
		return this.maxItems;
	}

	/**
	 * returns the number of the products that have some qtyAvailible
	 * 
	 * @return inStock
	 */
	private int getStock() {
		int inStock = 0;

		for (int i = 0; i < itemCount; i++) {
			if (stock[i] != null && stock[i].getQty() > 0) {
				inStock++;
			}
		}
		return inStock;
	}

	/**
	 * returns a formatted string with the vm's items
	 * 
	 * @return - the items and their details
	 */
	public String printListItems() {
		String res = "*****Items*****";
		String[] itemList = listItems();
		for (int i = 0; i < itemList.length; i++) {
			if (itemList[i] != null) {
				res += itemList[i];
			} else if (res.contentEquals("*****Items*****")) {
				res += "\nNO ITEMS AVAILABLE";
			}

		}

		return res;

	}

	/**
	 * takes a penny/coin value and turns it into £0.00 friendly value. works with insetCoin()/purchaseItem
	 * 
	 * @param value - the penny/coin value 
	 * @return - penny/coin as a double (£0.00)
	 */
	public double coinFormatter(double value) {
		double res = value / 100;
		return res;
	}

	/**
	 * takes £0.00 double value and turns into into its int penny value. works with insetCoin()/purchaseItem
	 * @param value - userMoney for example
	 * @return - £0.00 value in int penny value
	 */
	public int coinFormatterReverse(double value) {
		double conversion = value * 100;
		int res = (int) conversion;
		return res;
	}

	/**
	 * formats totalMoney, userMoney or other values being printed to a user friendly format
	 * @param value - money value i.e 0.85
	 * @return
	 */
	public String currencyFormatter(double value) {
		String res = String.format("£%.2f", value);
		return res;

	}

	/**
	 * Takes userMoney in vending machine and returns it to them with a thank you for shopping string
	 * @return- user change and thank you message
	 */
	public String returnChange() {
		String res = "";
		if (userMoney > 0) {
			res += "...returning change...\n";
			res += currencyFormatter(this.userMoney);
			this.userMoney = 0;
		} else {
			res += "You have no change";
		}
		res += "\nThank you for shopping!";
		return res;
	}

	/**
	 * Method uses restock(x) from VendItem only it allows you to select the item and qty you want re-stocked
	 * @param itemId - itemiD
	 * @param restockQty - The no you want re-stocked
	 * @return - true  if item exists and stock not full
	 * @return - else false
	 */
	public boolean restock(int itemId, int restockQty) {

		if (stock[itemId - 1] != null) {
			if (stock[itemId - 1].getQty() < 10) {
				if (stock[itemId - 1].restock(restockQty)) {
					return true;
				} else {
					return false;
				}
			}

		}
		return false;
	}

	/**
	 * used with UI to assist with re-stock function
	 * @param itemId - itemId
	 * @return - the qtyAvailable of the item
	 */
	public int stockOf(int itemId) {
		int res = stock[itemId - 1].getQty();
		return res;
	}

}

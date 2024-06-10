package part02;

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

	private int coin5;
	private int coin10;
	private int coin20;
	private int coin50;
	private int coin100;
	private int coin200;

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
		this.totalMoney = 0;
		this.userMoney = 0;

		this.coin5 = 0;
		this.coin10 = 0;
		this.coin20 = 0;
		this.coin50 = 0;
		this.coin100 = 0;
		this.coin200 = 0;

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
				+ "\tUser Balance: " + currencyFormatter(this.userMoney) + "\tVM Status: " + this.vmStatus + "\n"
				+ getCoinDenominationsInfo();//PART02

		return systemInfo;
	}

	/**
	 * The method reset() should reinitialise a VendingMachine instance:- empty of
	 * items and cash
	 */
	public void reset() {

		this.itemCount = 0;

		int index = 0;
		while (index < maxItems && stock[index] != null) { // while stock array isn't null
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
				res += "------OUT OF STOCK-----";
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
			if (userMoney == 5)
				this.coin5++;
			if (userMoney == 10)
				this.coin10++;
			if (userMoney == 20)
				this.coin20++;
			if (userMoney == 50)
				this.coin50++;
			if (userMoney == 100)
				this.coin100++;
			if (userMoney == 200)
				this.coin200++;
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
		for (int i = 0; i < maxItems; i++) {
			if (stock[i] != null && stock[i].getQty() < 1) {
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
		String status = this.vmStatus.toString();
		return status;
	}

	/**
	 * retrieves the current user balance as formatted string
	 * 
	 * @return - user balance
	 */
	public String getBalance() {
		String res = "YOUR BALANCE: " + currencyFormatter(this.userMoney);
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
	 * takes a penny/coin value and turns it into £0.00 friendly value. works with
	 * insetCoin()/purchaseItem
	 * 
	 * @param value - the penny/coin value
	 * @return - penny/coin as a double (£0.00)
	 */
	
	public double coinFormatter(double value) {
		double res = value / 100;
		return res;
	}

	/**
	 * takes £0.00 double value and turns into into its int penny value. works with
	 * insetCoin()/purchaseItem
	 * 
	 * @param value - userMoney for example
	 * @return - £0.00 value in int penny value
	 */
	public int coinFormatterReverse(double value) {
		double conversion = value * 100;
		conversion = Math.round(conversion / 1);
		int res = (int) conversion;
		return res;
	}

	/**
	 * formats totalMoney, userMoney or other values being printed to a user
	 * friendly format
	 * 
	 * @param value - money value i.e 0.85
	 * @return
	 */
	public String currencyFormatter(double value) {
		String res = String.format("£%.2f", value);
		return res;

	}

	/**
	 * Method uses restock(x) from VendItem only it allows you to select the item
	 * and qty you want re-stocked
	 * 
	 * @param itemId     - itemiD
	 * @param restockQty - The no you want re-stocked
	 * @return - true if item exists and stock not full
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
	 * 
	 * @param itemId - itemId
	 * @return - the qtyAvailable of the item
	 */
	public int stockOf(int itemId) {
		int res = stock[itemId - 1].getQty();
		return res;
	}

	/**
	 * Takes userMoney in vending machine and returns it to them with a thank you
	 * for shopping string
	 * 
	 * @return- user change and thank you message
	 */
	public String returnChange() {
		String res = "";
		if (userMoney > 0) {
			res += "Returning " + currencyFormatter(this.userMoney) + " change...\n";
			res += coinDenominationsReturn();// PART 02
		} else {
			res += "You have no change";
		}
		res += "\nThank you for shopping!";
		return res;
	}

	/**
	 * PART02 2.2 this method is called in returnChange, it returns a string (that
	 * represents) coins being returned as change it also updates the
	 * coinDenomianations as each coin is return if the VM does not have coins
	 * available for change, it will print the amount still owned and Service_Mode
	 * will be set
	 * 
	 * @return - string containing coins returned information
	 */
	public String coinDenominationsReturn() {
		int x = coinFormatterReverse(this.userMoney);
		String res = "";

		while (x > 0) { // while user is owed change

			while (x >= 200 && this.coin200 > 0 && x / 200 >= 1) {
				res += "...returning £2 coin....\n";
				this.coin200 -= 1;
				x -= 200;
				this.userMoney -= 2;

			}

			while (x >= 100 && this.coin100 > 0 && x / 100 >= 1) {
				res += "...returning £1 coin....\n";
				this.coin100 -= 1;
				x -= 100;
				this.userMoney -= 1;

			}
			while (x >= 50 && this.coin50 > 0 && x / 50 >= 1) {
				res += "...returning 50p coin....\n";
				this.coin50 -= 1;
				x -= 50;
				this.userMoney -= 0.5;

			}

			while (x >= 20 && this.coin20 > 0 && x / 20 >= 1) {
				res += "...returning 20p coin....\n";
				this.coin20 -= 1;
				x -= 20;
				this.userMoney -= 0.2;

			}
			while (x >= 10 && this.coin10 > 0 && x / 10 >= 1) {
				res += "...returning 10p coin....\n";
				this.coin100 -= 1;
				x -= 10;
				this.userMoney -= 0.2;

			}
			while (x >= 5 && this.coin10 > 0 && x / 5 >= 1) {
				res += "...returning 5p coin....\n";
				this.coin5 -= 1;
				x -= 5;
				this.userMoney -= 0.05;

			}
			if (x > 0) {// if after it has went through these change return whiles, and it still isnt
						// zero
				res += "\nMachine does not have " + currencyFormatter(coinFormatter(x));
				res += " avaiable in change ---- Contacting Support\n";
				setStatus(Status.SERVICE_MODE);// service mode as machine needs more coins that create value x
				break;
			} else {
				res += "\nChange Returned! ";
			}
		}
		return res;
	}
	
	/**
	 * this prints the coin denominations, this method is just to display
	 * information for my testing purposes to ensure coins updated accordingly i
	 * added it to the getSystemInfo()
	 * 
	 * @return - the coins and how many of them are in the system
	 */
	public String getCoinDenominationsInfo() {
		String res = "";
		if (coin5 > 0)
			res += "5p Coin: " + coin5 + "\t";
		if (coin10 > 0)
			res += "10p Coin: " + coin10 + "\t";
		if (coin20 > 0)
			res += "20p Coin: " + coin20 + "\t";
		if (coin50 > 0)
			res += "50p Coin: " + coin50 + "\t";
		if (coin100 > 0)
			res += "£1 Coin: " + coin100 + "\t";
		if (coin200 > 0)
			res += "£2 Coin: " + coin200 + "\t";

		return res;
	}

	/**
	 * PART02 2.2 used for VendingMachineRead to create VendingMachine state
	 * 
	 * @param totalMoney2
	 */
	public void setTotalMoney(double totalMoney2) {
		this.totalMoney = totalMoney2;

	}

	/**
	 * PART02 2.2 used for VendingMachineRead to create VendingMachine state
	 * 
	 * @param userMoney2
	 */
	public void setUserMoney(double userMoney2) {
		this.userMoney = userMoney2;

	}

	/**
	 * PART02 2.2 used for VendingMachineRead to create VendingMachine state
	 * 
	 * @param - ammount of each coin in the system
	 */
	public void setCoinDenominations(int coin52, int coin102, int coin202, int coin502, int coin1002, int coin2002) {
		this.coin5 = coin52;
		this.coin10 = coin102;
		this.coin20 = coin202;
		this.coin50 = coin502;
		this.coin100 = coin1002;
		this.coin200 = coin2002;

	}

	/**
	 * PART02 2.2 used for VendingMachineWrite to write coin values to csv
	 */
	public String getCoinDenominations() {
		String res = coin5 + ", " + coin10 + ", " + coin20 + ", " + coin50 + ", " + coin100 + ", " + coin200;

		return res;
	}

	/**
	 * PART02 2.2 used for VendingMachineWrite to write owner to csv
	 */
	public String getOwner() {
		return this.owner;
	}

	/**
	 * PART02 2.2 used for VendingMachineWrite to write userMoney to csv
	 */
	public double getUserMoney() {
		return this.userMoney;
	}

	/**
	 * PART02 2.2 used for VendingMachineWrite to write totalMoney to csv
	 */
	public double getTotalMoney() {
		return this.totalMoney;
	}

	/**
	 * PART02 2.2 used for VendingMachineWrite to write VendItems to csv
	 */
	public VendItem[] getStockArray() {

		return this.stock;
	}

	
}

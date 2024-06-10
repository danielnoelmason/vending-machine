package part02;

/**
 * This class models a Vending Machine item, also defining some the functions
 * * @author <Danie Mason>
 */
public class VendItem implements Vendible {

	private int itemId;	 // unique identifier for instance
	private static int nextId = 0; 	// static increment variable
	private String name; 	// attribute of item
	private double unitPrice; 	// price of item
	private int qtyAvailable; 	// quantity available of item

	private static final int MAX_QTY = 10; 	// static variable limiting big qtyAvailble can be

	/**
	 * This is the constructor method for the Vend Item object. It sets up the name
	 * and unitPrice. Sets qty to zero
	 * 
	 * @param name      - the vend item name
	 * @param unitPrice - the vend item unit price
	 */
	public VendItem(String name, double unitPrice) {

		nextId += 1; // each time instantiated, adds 1
		this.itemId = nextId;

		this.name = name;
		this.unitPrice = unitPrice;
		this.qtyAvailable = 0;
	}

	/**
	 * This is the constructor method for the Vend Item object. It sets up the name,
	 * unitPrice, and quantity available
	 * 
	 * @param name         - the vend item name
	 * @param unitPrice    - the vend item unit price
	 * @param qtyAvailable - the vend item quantity available
	 */
	public VendItem(String name, double unitPrice, int qtyAvailable) {

		nextId += 1; // each time instantiated, adds 1
		this.itemId = nextId;

		this.name = name;
		this.unitPrice = unitPrice;
		this.qtyAvailable = qtyAvailable;
	}

	/**
	 * Retrieves item name
	 * 
	 * @return item name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Retrieves item price
	 * 
	 * @return item price
	 */
	public double getPrice() {
		return this.unitPrice;
	}

	/**
	 * Retrieves item quantity available
	 * 
	 * @return item quantity available
	 */
	public int getQty() {
		return this.qtyAvailable;
	}

	/**
	 * Retrieves item id
	 * 
	 * @return item id
	 */
	public int getItemId() {
		return this.itemId;
	}

	/**
	 * The restock() method for VendItem should update the quantity of an item with
	 * x
	 * 
	 * @param x - the number of vend item instance restock
	 * @return - true if item restock does not go over limit of 10
	 */
	public boolean restock(int x) {
		if (x > 0 && x <= MAX_QTY && this.qtyAvailable + x <= MAX_QTY) { // if x is between 1 and MAX_QTY and stock not
																			// full
			this.qtyAvailable += x;	//perform re-stock
			return true;
		} else {
			return false;
		}

	}

	/**
	 * The decrement() method for VendItem should reduce available quantity of an
	 * item by 1 (as appropriate)
	 */
	public void decrement() {
		this.qtyAvailable -= 1;
	}

	/**
	 * Updates the object and confirms purchase to user
	 * @return a confirmation string of purchase
	 */
	@Override
	public String deliver() {
		String res = "Thanks for purchasing: " + this.name + "\nCOST: " + String.format("£%.2f", this.unitPrice);
		decrement();
		return res;
	}

	//THE FOLLOWING ARE METHODS I ADDED TO ASSIST IN THE PROGRAM
	
	/**
	 * retrieves Max Quantity per item
	 * 
	 * @return item max quantity
	 */
	public static int getMaxQty() {
		// TODO Auto-generated method stub
		return MAX_QTY;
	}
	
	public static void reset() {
		nextId=0;
	}

}

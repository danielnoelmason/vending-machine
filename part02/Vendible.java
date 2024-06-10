package part02;
/**
 * This interface represents what could be put into a vending machine as items
 * @author Daniel Mason
 * @version V1.0
 *
 */
public interface Vendible {

	/**
	 * Updates the object and confirms purchase to user
	 * @return a confirmation string of purchase
	 */
	public String deliver();
			
}

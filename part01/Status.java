package part01;

/**
 * This is a enumeration for Vending Machine Status, also defining some the functions
 * * @author <Daniel Mason>
 */
public enum Status {
	VENDING_MODE, SERVICE_MODE;

	private String status;

	public String getStatus() {
		return this.status;
	}
}

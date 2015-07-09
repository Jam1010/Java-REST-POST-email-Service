/**
 * 
 */
package usr.keerthy.email;

/**
 * @author sriramj
 *
 */
public class EmailStatus {
	
	public int statusCode;
	public String statusMessage;
	public String details;
	
	public boolean success() {
		return statusCode == 200;
	}

}

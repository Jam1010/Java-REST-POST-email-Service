package usr.keerthy.email;


/**
 * @author sriramj
 *
 */
public interface EmailProvider {
	
	/**
	 * Sends an email based on the provided {@link Email} data.
	 * @param email
	 * @return 
	 */
	EmailStatus sendMail(final Email email);

}

package usr.keerthy.email;

import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author sriramj
 *
 */
public class Email {

	private String to;
	@JsonProperty("to_name")
	private String toName;
	private String from;
	@JsonProperty("from_name")
	private String fromName;
	private String subject;
	private String body;

	// FIXME We can use lombok to remove these boiler-plate getter/setter code
	// https://projectlombok.org/
	// but I have skipped it here because the user needs to setup lombok then to
	// run the app.
	// NOTE! Getters and Setters needed by Jackson json library to convert to/from json
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	/**
	 * Apache Commons-lang library to escape html in the email body
	 * @return
	 */
	public String getHtmlEscapedBody() {
		return StringEscapeUtils.escapeHtml(body);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("to:").append(to);
		sb.append("|toName:").append(toName);
		sb.append("|from:").append(from);
		sb.append("|fromName:").append(fromName);
		sb.append("|subject:").append(subject);
		sb.append("|body:").append(body);
		return sb.toString();
	}

	/**
	 * Checks whether this Email instance is valid.
	 * @return null, if there are no validation errors; validation error message, otherwise.
	 */
	public String validate() {
		if (isEmailAddress(to) && isNotEmpty(toName) && isEmailAddress(from)
				&& isNotEmpty(fromName) && isNotEmpty(subject)
				&& isNotEmpty(body)) {
			return null;
		}
		
		return "Fields to, to_name, from, from_name, subject, body cannot be empty";
	}

	static boolean isNotEmpty(String field) {
		return field != null && field.length() > 0; // NOTE empty spaces will
													// still return true
	}

	/**
	 * Perfect validation is not possible with regex.
	 * https://en.wikipedia.org/wiki/Email_address#Validation_and_verification
	 * 
	 * @param emailAddress
	 * @return True, if nonempty email. False, otherwise.
	 */
	static boolean isEmailAddress(String emailAddress) {
		// TODO we just check for non-null values here and trusting our client and
		// the 3p email providers to do due-diligence on the email-address format.
		return isNotEmpty(emailAddress);
	}
}

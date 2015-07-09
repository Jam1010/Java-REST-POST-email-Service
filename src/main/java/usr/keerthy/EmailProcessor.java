package usr.keerthy;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import usr.keerthy.email.Email;
import usr.keerthy.email.EmailProviderFactory;
import usr.keerthy.email.EmailStatus;

/**
 * Email sender method-path implementation.
 */
@Path("email")
public class EmailProcessor {

	private EmailProviderFactory emailProviderFactory;

	public EmailProcessor() {
		this.emailProviderFactory = getEmailProviderFactory();
	}

	/**
	 * Package-private getter for this dependency. Useful to mock in tests
	 * @return
	 */
	EmailProviderFactory getEmailProviderFactory() {
		return EmailProviderFactory.getInstance();
	}

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to
	 * the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response sendmail(final String emailData) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			final Email email = mapper.readValue(emailData, Email.class);
			final String validationError = email.validate();
			if (validationError != null) {
				// TODO add logs
				return Response.notAcceptable(null).entity(validationError).build();
			}

			EmailStatus status = emailProviderFactory.getProvider().sendMail(
					email);
			if (!status.success()) {
				// TODO Add ERROR logs here, emit-metrics
				return Response.serverError().status(status.statusCode)
						.entity(status.details).build();
			}
			return Response.ok().build();
		} catch (IOException e) {
			// TODO logs, emit-metrics
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/provider/{providerName}")
	public Response changeEmailProvider(@PathParam("providerName") final String emailProviderName) {
		// TODO Implement auth for this uri, since changing email provider is an admin operation
		// Can use LDAP or explicit user access-control-list
		
		boolean changeSuccessful = emailProviderFactory.changeProvider(emailProviderName);
		if (!changeSuccessful) {
			return Response.notAcceptable(null).entity(emailProviderName + " - No such provider").build();
		}
		
		return Response.ok().build();
	}
}

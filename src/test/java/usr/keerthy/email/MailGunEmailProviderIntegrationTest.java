package usr.keerthy.email;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

/**
 * @author sriramj
 *
 */
public class MailGunEmailProviderIntegrationTest {

	@Test
	public void testSendmail() throws JsonParseException, JsonMappingException, IOException {
		EmailProvider provider = MailGunEmailProvider.getInstance();
		Email email = new ObjectMapper().readValue("{\"to\":\"sriramkeerthy@gmail.com\","
				+ "\"to_name\":\"sriram\","
				+ "\"from\":\"noreply@uber.com\","
				+ "\"from_name\":\"Uber\","
				+ "\"subject\":\"Testing\","
				+ "\"body\":\"<h1>Test MailGun email provider</h1>\"}", Email.class);
		final EmailStatus status = provider.sendMail(email);
		assertEquals(status.statusCode, Response.Status.OK.getStatusCode());
	}

}

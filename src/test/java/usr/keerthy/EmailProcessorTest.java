package usr.keerthy;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import usr.keerthy.email.Email;
import usr.keerthy.email.EmailProvider;
import usr.keerthy.email.EmailProviderFactory;
import usr.keerthy.email.EmailStatus;

/**
 * @author sriramj
 *
 */
public class EmailProcessorTest {

	private EmailProcessor processor;
	private IMocksControl mockControl;
	private EmailProviderFactory emailProviderFactory;
	private EmailProvider emailProvider;

	public EmailProcessorTest() {
		mockControl = EasyMock.createControl();
		emailProviderFactory = mockControl
				.createMock(EmailProviderFactory.class);
		emailProvider = mockControl.createMock(EmailProvider.class);
	}

	@Before
	public void setup() {
		this.processor = new EmailProcessor() {
			@Override
			EmailProviderFactory getEmailProviderFactory() {
				return emailProviderFactory;
			}
		};
	}

	@After
	public void teardown() {
		mockControl.reset();
	}
	
	@Test
	public void testSendmailJsonException() {
		Response response = processor.sendmail("{");
		assertEquals(response.getStatus(),
				Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
	}

	@Test
	public void testSendmailInvalidEmail() {
		Response response = processor.sendmail("{}");
		// verify that invalid email should return http-code 406 NOT ACCEPTABLE
		assertEquals(response.getStatus(),
				Response.Status.NOT_ACCEPTABLE.getStatusCode());
	}

	@Test
	public void testSendmailFailure() {
		expect(emailProviderFactory.getProvider()).andReturn(emailProvider);
		final EmailStatus emailStatus = new EmailStatus();
		emailStatus.statusCode = 401;
		expect(emailProvider.sendMail(isA(Email.class))).andReturn(emailStatus);
		mockControl.replay();
		
		Response response = processor.sendmail(getEmailJson());
		// email provider threw a un-authorized exception, email-processor will bubble up the same status code
		assertEquals(response.getStatus(),
				Response.Status.UNAUTHORIZED.getStatusCode());
	}
	
	@Test
	public void testSendmailSuccess() {
		expect(emailProviderFactory.getProvider()).andReturn(emailProvider);
		final EmailStatus emailStatus = new EmailStatus();
		emailStatus.statusCode = 200;
		expect(emailProvider.sendMail(isA(Email.class))).andReturn(emailStatus);
		mockControl.replay();
		
		Response response = processor.sendmail(getEmailJson());
		// email provider threw a un-authorized exception, email-processor will bubble up the same status code
		assertEquals(response.getStatus(),
				Response.Status.OK.getStatusCode());
	}

	private String getEmailJson() {
		return "{\"to\":\"sriramkeerthy@gmail.com\","
				+ "\"to_name\":\"sriram\","
				+ "\"from\":\"noreply@uber.com\","
				+ "\"from_name\":\"Uber\","
				+ "\"subject\":\"Testing\","
				+ "\"body\":\"<h1>Test</h1>\"}";
	}
	
	@Test
	public void testChangeProviderFailure() {
		String invalidProviderName = "invalid_provider";
		expect(emailProviderFactory.changeProvider(invalidProviderName)).andReturn(false);
		mockControl.replay();
		Response response = processor.changeEmailProvider(invalidProviderName);
		assertEquals(response.getStatus(),
				Response.Status.NOT_ACCEPTABLE.getStatusCode());
	}
	
	@Test
	public void testChangeProviderSuccess() {
		String validProviderName = "valid_provider";
		expect(emailProviderFactory.changeProvider(validProviderName)).andReturn(true);
		mockControl.replay();
		Response response = processor.changeEmailProvider(validProviderName);
		assertEquals(response.getStatus(),
				Response.Status.OK.getStatusCode());
	}

}

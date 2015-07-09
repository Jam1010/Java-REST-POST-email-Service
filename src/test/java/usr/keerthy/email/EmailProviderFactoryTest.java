package usr.keerthy.email;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author sriramj
 *
 */
public class EmailProviderFactoryTest {

	@Test
	public void testGetProvider() {
		final EmailProviderFactory factory = EmailProviderFactory.getInstance();
		
		// check default provider
		assertEquals(SendGridEmailProvider.class, factory.getProvider().getClass());
		
		factory.changeProvider("MailGun");
		assertEquals(MailGunEmailProvider.class, factory.getProvider().getClass());
		
		factory.changeProvider("SendGrid");
		assertEquals(SendGridEmailProvider.class, factory.getProvider().getClass());
		
		// invalid provider name should have no impact
		factory.changeProvider("InvalidProvider");
		assertEquals(SendGridEmailProvider.class, factory.getProvider().getClass());
	}

}

package usr.keerthy.email;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author sriramj
 *
 */
public class EmailTest {

	@Test
	public void testIsNotEmpty() {
		assertFalse(Email.isNotEmpty(null));
		assertFalse(Email.isNotEmpty(""));
		assertTrue(Email.isNotEmpty(" ")); // we allow whitespace for now
		assertTrue(Email.isNotEmpty("abc"));
	}
	
	@Test
	public void testIsEmailAddress() {
		assertFalse(Email.isEmailAddress(null));
		assertFalse(Email.isNotEmpty(""));
		assertTrue(Email.isNotEmpty("abc")); // we check only for non-emptiness and not xxx@yyy.zzz format
		assertTrue(Email.isNotEmpty("123@abc.com"));
	}
	
	@Test
	public void testValidate() {
		Email email = new Email();
		assertNotNull(email.validate());
		email.setTo("sriramkeerthy@gmail.com");
		assertNotNull(email.validate());
		email.setToName("Sriram");
		assertNotNull(email.validate());
		email.setFrom("noreply@uber.com");
		assertNotNull(email.validate());
		email.setFromName("Uber");
		assertNotNull(email.validate());
		email.setSubject("Test");
		assertNotNull(email.validate());
		email.setBody("email text");
		assertNull(email.validate()); // means Email instance is valid
	}

}

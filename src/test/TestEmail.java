package test;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.junit.*;

import project.validators.EmailValidator;


public class TestEmail {
	EmailValidator ev = new EmailValidator();

	@Test
	public void testInvalidEmail() {
		assertEquals(ev.emailValidator("{\"email\": \"admin\"}").getStatus(), Response.Status.CONFLICT.getStatusCode());
		assertEquals(ev.emailValidator("{\"email\": \"admin@xx\"}").getStatus(), Response.Status.CONFLICT.getStatusCode());
		assertEquals(ev.emailValidator("{\"email\": \"admin@.com\"}").getStatus(), Response.Status.CONFLICT.getStatusCode());
		assertEquals(ev.emailValidator("{\"email\": \"admin-asdf@\"}").getStatus(), Response.Status.CONFLICT.getStatusCode());
	}

	@Test
	public void testUsedEmail() {
		assertEquals(ev.emailValidator("{\"email\": \"admin@admin.com\"}").getStatus(), Response.Status.CONFLICT.getStatusCode());
	}
	
	@Test
	public void testValidEmail() {
		assertEquals(ev.emailValidator("{\"email\": \"admin@valid.com\"}").getStatus(), Response.Status.OK.getStatusCode());
	}
}

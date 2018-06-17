package test;

import static org.junit.Assert.*;

import org.junit.*;

import javax.ws.rs.core.HttpHeaders;
import static org.mockito.Mockito.*;
import java.util.ArrayList;

import project.models.*;
import project.utils.*;
import project.validators.HeaderValidator;
import project.dbutils.Database;
import project.exception.*;

public class TestHeaderValidator {

	@Test
	public void testHeaderWithNoToken() {
		HttpHeaders headers = mock(HttpHeaders.class);
		boolean f = true;
		try {
			HeaderValidator.validate(headers);
		} catch (HeaderException e) {
			f = false;
			Assert.assertTrue(true);
		}
		if(f==true) {
			fail("No exception occured");
		}
	}

	@Test
	public void testHeaderWithInvalidToken() {
		HttpHeaders headers = mock(HttpHeaders.class);
		ArrayList<String> a = new ArrayList<String>();
		a.add("randomInvalidToken");
		when(headers.getRequestHeader(HttpHeaders.AUTHORIZATION)).thenReturn(a);
		boolean f = true;
		try {
			HeaderValidator.validate(headers);
		} catch (HeaderException e) {
			f = false;
			assertTrue(true);
		}
		if(f==true) {
			fail("No exception occured");
		}
	}
	
	@Test
	public void testValidToken() {
		Database db = new Database();
		User usr = db.getUser(1);
		String t = Token.generateToken(usr).getToken();
		try {
			User u1 = Token.decodeToken(t);
			assertEquals(u1, usr);
		} catch (TokenException e) {
			fail("Exception thrown");
		}
	}
}

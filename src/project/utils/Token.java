package project.utils;

import java.io.Serializable;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;
import java.util.Date;
import project.dbutils.Database;
import project.exception.TokenException;
import project.models.*;

/**
 * Klasa udost�pniaj�ca interfejs do obs�ugi token�w uwierzytelniaj�cych
 * @see Serializable
 */
public class Token implements Serializable{

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Klucz aplikacji, u�ywany do szyfrowania tokena
	 */
	private static final String apiKey = "74^ac0.#@!8bc?xf";
	/**
	 * czas �ycia tokena
	 */
	private static final int ttl = 3600000;
	/**
	 * token uwierzytelniaj�cy
	 */
	private String token;
	/**
	 * algorytm szyfruj�cy
	 */
	private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	
	/**
	 * {@link Token#token}
	 */
	public String getToken() {
		return token;
	}
	
	/**
	 * {@link Token#token}
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	/**
	 * Metoda s�u��ca do generowania tokena na podstawie danych u�ytkownika.
	 * @param user obiekt typu User zawieraj�cy dane u�ytkownika
	 * @return token uwierzytelniaj�cy
	 */
	public static Token generateToken(User user) {
		Token ret = new Token();
		long nowm = System.currentTimeMillis();
		Date now = new Date(nowm);

	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey);
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

	    JwtBuilder builder = Jwts.builder().setId(Integer.toString(user.getUserid()))
	                                .setIssuedAt(now)
	                                .setSubject(user.getEmail())
	                                .setIssuer(user.getLogin())
	                                .signWith(signatureAlgorithm, signingKey).setExpiration(new Date(nowm + ttl));

	    ret.setToken(builder.compact());
		return ret;
	}
	
	/**
	 * Metoda wrapper s�u��ca do dekodowania podanego tokena
	 * @param tkn token uwierzytelniaj�cy
	 * @return obiekt typu user powsta�y z danych zakodowanych w tokenie
	 * @throws TokenException gdy token jest b��dny lub straci� wa�no�c
	 */
	public static User decodeToken(String tkn) throws TokenException {
		Token token = new Token();
		token.setToken(tkn);
		User usr = token.decodeToken();
		return usr;
	}
	
	/**
	 * Metoda s�u��ca do dekodowania podanego tokena
	 * @return obiekt typu user powsta�y z danych zakodowanych w tokenie
	 * @throws TokenException gdy token jest b��dny lub straci� wa�no�c
	 */
	public User decodeToken() throws TokenException{
		Claims claims = null;
		try {
		claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(apiKey))
		       					 	 .parseClaimsJws(token).getBody();
		}
		catch(SignatureException e) {
			throw new TokenException("Token Invalid"); 
		}
		catch(ExpiredJwtException e) {
			throw new TokenException("Token Expired"); 
		}
		Database db = new Database();
		User usr = db.getUser(Integer.parseInt(claims.getId()));
		if(usr != null && usr.getLogin().equals(claims.getIssuer()) && usr.getEmail().equals(claims.getSubject())) {
			if(claims.getExpiration().getTime() < System.currentTimeMillis()) {
				throw new TokenException("Token Expired");
			}
			return usr;
		}
		throw new TokenException("Token Invalid");
	}

}

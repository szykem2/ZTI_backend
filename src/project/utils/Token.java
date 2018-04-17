package project.utils;

import java.io.Serializable;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;
import java.util.Date;

import project.dbutils.Database;
import project.models.*;

public class Token implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final String apiKey = "74^ac0.#@!8bc?xf";
	private static final int ttl = 3600000;
	private String token;
	private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
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
	
	public static User decodeToken(String tkn) throws TokenException {
		Token token = new Token();
		token.setToken(tkn);
		User usr = token.decodeToken();
		return usr;
	}
	
	public User decodeToken() throws TokenException{
		Claims claims = null;
		try {
		claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(apiKey))
		       					 	 .parseClaimsJws(token).getBody();
		}
		catch(SignatureException e) {
			throw new TokenException("Token Invalid"); 
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

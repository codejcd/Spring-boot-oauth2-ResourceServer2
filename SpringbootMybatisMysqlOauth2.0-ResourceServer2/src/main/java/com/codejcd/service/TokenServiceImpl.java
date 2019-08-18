package com.codejcd.service;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.codejcd.common.CustomException;
import com.codejcd.common.MessageProperties;
import com.codejcd.util.DateUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
@PropertySource("classpath:jwt.properties")
public class TokenServiceImpl implements TokenService {
	
	@Value("${jwt.secretKey}")
	private String SECRET_KEY;
	
	@Value("${jwt.issuer}")
	private String ISSUER;
	
	@Value("${jwt.audience}")
	private String AUDIENCE;
	
	@Value("${jwt.subject}")
	private String SUBJECT;
	
	@Override
	public boolean checkAccessToken(String accessToken) throws Exception {
		if (null == accessToken || "".equals(accessToken)) {
			throw new CustomException(MessageProperties.prop("error.code.token.invalid")
					, MessageProperties.prop("error.message.token.invalid"));
		}
		
		String[] bearerToken = accessToken.split("Bearer "); 
		if (null == bearerToken[1]) {
			throw new CustomException(MessageProperties.prop("error.code.token.not.bearer")
					, MessageProperties.prop("error.message.token.not.bearer"));
		}
		
		boolean result = false;
		byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(Base64.encodeBase64String(SECRET_KEY.getBytes()));
		Claims claims = null;
		claims = Jwts.parser()
					.setSigningKey(secretKeyBytes)
					.parseClaimsJws(bearerToken[1]).getBody();
		
		Date expitration;
		Date now = DateUtil.getNowDate();
		String issuer;
		String audience;
		
		
		if (null == claims.getExpiration()) {
			throw new CustomException(MessageProperties.prop("error.code.token.expiration.invalid")
					, MessageProperties.prop("error.message.token.expiration.invalid"));	
		}
		expitration = claims.getExpiration();
		 
		if (DateUtil.isBefore(expitration, now)) {
			throw new CustomException(MessageProperties.prop("error.code.token.expiration")
					, MessageProperties.prop("error.message.token.expiration"));
		}
		
		if (null == claims.getIssuer() ||  "".equals(claims.getIssuer())) {
			throw new CustomException(MessageProperties.prop("error.code.token.issuer.invalid")
					, MessageProperties.prop("error.message.token.issuer.invalid"));		
		}
		issuer = claims.getIssuer();
		
		if (!ISSUER.equals(issuer)) {
			throw new CustomException(MessageProperties.prop("error.code.token.issuer.invalid")
					, MessageProperties.prop("error.message.token.issuer.invalid"));
		}
		
		if (null == claims.getAudience() ||  "".equals(claims.getAudience())) {
			throw new CustomException(MessageProperties.prop("error.code.token.audience.invalid")
					, MessageProperties.prop("error.message.token.audience.invalid"));		
		}
		audience = claims.getAudience();
		
		if (!AUDIENCE.equals(audience)) {
			throw new CustomException(MessageProperties.prop("error.code.token.audience.invalid")
					, MessageProperties.prop("error.message.token.audience.invalid"));
		}
		
		if (null == claims.getId() ||  "".equals(claims.getId())) {
			throw new CustomException(MessageProperties.prop("error.code.token.id.invalid")
					, MessageProperties.prop("error.message.token.id.invalid"));		
		}
		result = true;
		//claims.getIssuedAt();
		//claims.getSubject();
		
		return result;
	}
	
}

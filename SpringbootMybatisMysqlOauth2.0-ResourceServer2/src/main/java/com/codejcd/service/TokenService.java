package com.codejcd.service;

public interface TokenService {
	
	/**
	 * token 검증
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public boolean checkAccessToken(String accessToken) throws Exception;
}

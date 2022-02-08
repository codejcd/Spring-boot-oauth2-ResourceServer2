package com.codejcd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codejcd.common.CustomException;
import com.codejcd.common.MessageProperties;
import com.codejcd.common.Response;
import com.codejcd.service.TokenService;

/**
 * 리소스 컨트롤러
 */
@RestController
public class ResourceController {

	@Autowired
	private TokenService tokenService;
	
	/**
	 * 제한된 자원의 접근 API
	 * @param bearerToken
	 * @return
	 */
    @RequestMapping("/resource")
    public Response resource(@RequestHeader(value="Authorization") String bearerToken) {
    	Response response = new Response();
    	
    	try {
    		if (tokenService.checkAccessToken(bearerToken)) { // token 검증, token 검증은 각 API에 Aspect나 Intercepter를 활용하여 공통 처리도 가능. 
    			// 리소스 접근
            	response.setResponseCode(MessageProperties.prop("error.code.common.success"));
        		response.setResponseMessage(MessageProperties.prop("error.message.common.success"));
    		}
    	} catch (CustomException e) {
    		response.setResponseCode(MessageProperties.prop(e.getErrorCode()));
    		response.setResponseMessage(MessageProperties.prop(e.getErrorMessage()));
    	} catch (Exception e) {
            response.setResponseCode(MessageProperties.prop("error.code.common.occured.exception"));
            response.setResponseMessage(MessageProperties.prop("error.message.common.occured.exception"));
            e.printStackTrace();
    	} finally {
    		
    	}
    	return response;
    }
}

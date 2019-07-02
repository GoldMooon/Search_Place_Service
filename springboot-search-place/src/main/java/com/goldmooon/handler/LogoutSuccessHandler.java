package com.goldmooon.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.goldmooon.utils.GoldMoonUtils;

@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler
{

	@Override
	public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException
	{

		super.setDefaultTargetUrl(GoldMoonUtils.convertHttpToHttps(request) + "/login.html");
		super.onLogoutSuccess(request, response, authentication);
	}

}

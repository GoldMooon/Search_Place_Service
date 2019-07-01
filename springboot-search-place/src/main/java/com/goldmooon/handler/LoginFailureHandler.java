package com.goldmooon.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        // 로그인 실패시 username을 화면에 그대로 출력하도록 하기 위해 아래와 같은  
        // 핸들러를 작성하였는데 더 쉬운 방법 아시는분?? 
        request.setAttribute("username", request.getParameter("username"));
        request.getRequestDispatcher("/loginError").forward(request, response);
        
        
    }

//    private static final String ID_NOT_EXIST       = "30300001"; // ID없음
//    private static final String EXPIRED            = "30300002"; // 유효기간
//    // private static final String REST = "30300003"; //최종접속일 90일 이상
//    private static final String INCORRECT_PASSWORD = "30300004"; // 비밀번호 오류
//    private static final String LOCKED             = "30300005"; // 비밀번호 5회이상 입력 오류
//    private static final String IP_NOT_ALLOWED     = "30300006"; // 허용IP가 아님
//    private static final String LOGIN_FAILED       = "30300007"; // 로그인 실패
//    private static final String ROLE_IS_EMPTY      = "30300008"; // 역할 등록이 되지 않은 계정
//
//
//
//
//
//    @Override
//    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception) throws IOException, ServletException
//    {
//
//        String url = GoldMoonUtils.convertHttpToHttps(request) + "/loginError=";
//
//        if ( exception instanceof AccountExpiredException )
//        { // 계정 유효기간
//            super.setDefaultFailureUrl(url + EXPIRED);
//        }
//        else if ( exception instanceof BadCredentialsException )
//        { // 비밀번호 입력 오류
//            super.setDefaultFailureUrl(url + INCORRECT_PASSWORD);
//        }
//        else if ( exception instanceof UsernameNotFoundException )
//        { // ID입력 오류
//            super.setDefaultFailureUrl(url + ID_NOT_EXIST);
//        }
//        else if ( exception instanceof SessionAuthenticationException )
//        { // 허용IP 오류
//            super.setDefaultFailureUrl(url + IP_NOT_ALLOWED);
//        }
//        else if ( exception instanceof LockedException )
//        { // 비밀번호5회이상 입력 오류
//            super.setDefaultFailureUrl(url + LOCKED);
//        }
//        else if ( exception instanceof AuthenticationServiceException )
//        { // 비밀번호5회이상 입력 오류
//            super.setDefaultFailureUrl(url + ROLE_IS_EMPTY);
//        }
//        else
//        {
//            super.setDefaultFailureUrl(url + LOGIN_FAILED);
//        }
//
//        super.onAuthenticationFailure(request, response, exception);
//    }

}

package org.javaboy.vhr.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.javaboy.vhr.web.model.Hr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录过滤器
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    SessionRegistry sessionRegistry;

    /**
     * 该方法用来验证验证码是否正确，因为UsernamePasswordAuthenticationFilter中的自带程序只有获得用户名和密码，因此需要添加一个登录的过滤器
     * 来判断验证码是否正确，然后进入权限验证器的AuthenticationManager接口的实现类ProviderManager中，并且ProviderManager会判断具体的登录方式（就是来判断登陆方式满不满足）
     * 如果支持该方式，会进入支持AuthenticationProvider方法的抽象实现类中，调用authenticate进行用户的身份校验
     * AuthenticationProvider方法的抽象实现类AbstractUserDetailsAuthenticationProvider的扩展类DaoAuthenticationProvider利用
     * UserDetailsService接口的实现类loadUserByUsername来获取用户信息，**这时就可以编写自己的实现类实现UserDetailsService来进行数据库信息的验证
     *
     * 认证成功后就回去调用successfulAuthentication，也就是调用successHandler登录成功处理器，最后将认证的结果放入SecurityContextHolder中
     * SecurityContextHolder类是一个线程级别的全局变量，最后会被SecurityContextPersistenceFilter过滤器使用，这个过滤器的作用是什么呢？
     * 当一个请求来的时候，它会将session中的值传入到该线程中，当请求返回的时候，它会判断该请求线程是否有SecurityContext，如果有它会将其放入到session中
     * 因此保证了请求结果可以在不同的请求之间共享。
     *
     * SessionRegistry中保存的都是在线的用户，可以查看在线用户，提出用户等功能
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        String verify_code = (String) request.getSession().getAttribute("verify_code");
        if (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE) || request.getContentType().contains(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
            Map<String, String> loginData = new HashMap<>();
            try {
                loginData = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            } catch (IOException e) {
            }finally {
                String code = loginData.get("code");
                checkCode(response, code, verify_code);
            }
            String username = loginData.get(getUsernameParameter());
            String password = loginData.get(getPasswordParameter());
            if (username == null) {
                username = "";
            }
            if (password == null) {
                password = "";
            }
            username = username.trim();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    username, password);
            setDetails(request, authRequest);
            Hr principal = new Hr();
            principal.setUsername(username);
            sessionRegistry.registerNewSession(request.getSession(true).getId(), principal);
            return this.getAuthenticationManager().authenticate(authRequest);
        } else {
            checkCode(response, request.getParameter("code"), verify_code);
            return super.attemptAuthentication(request, response);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }

    public void checkCode(HttpServletResponse resp, String code, String verify_code) {
        if (code == null || verify_code == null || "".equals(code) || !verify_code.toLowerCase().equals(code.toLowerCase())) {
            //验证码不正确
            throw new AuthenticationServiceException("验证码不正确");
        }
    }


}

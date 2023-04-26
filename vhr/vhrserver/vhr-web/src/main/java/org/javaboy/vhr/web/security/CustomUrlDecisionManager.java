package org.javaboy.vhr.web.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 来进行角色的权限的判断，这里是，如果满足其中一种角色，那么就允许
 *
 * FilterSecurityInterceptor
 * 此过滤器为认证授权过滤器链中最后一个过滤器，该过滤器之后就是请求真正的地址 服务
 * before invocation重要 该过程中会调用 AccessDecisionManager来验证当前已认证成功的用户是否有权限访问该资源
 * 在before invocation过程中，会调用FilterInvocationSecurityMetadataSource来获得url对应的需要的角色
 * 然后把获得的角色，加上登陆凭证和过滤器调用FilterInvocation(fi)都传入到AccessDecisionManager中，来进行用户认证
 */
@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {

    // object是当前的请求地址，getAttributes方法就是使用当前的访问资源路径去匹配我们自己定义的匹配规则
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute configAttribute : configAttributes) {
            String needRole = configAttribute.getAttribute();
            if ("ROLE_LOGIN".equals(needRole)) {
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new AccessDeniedException("尚未登录，请登录!");
                }else {
                    return;
                }
            }
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足，请联系管理员!");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}

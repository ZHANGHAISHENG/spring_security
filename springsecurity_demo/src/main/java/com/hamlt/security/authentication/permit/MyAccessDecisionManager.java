package com.hamlt.security.authentication.permit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@Component
public class MyAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object resource, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (configAttributes == null || configAttributes.isEmpty()) {
            log.info("[决策管理器]:请求 {} 无需权限", ((FilterInvocation) resource).getRequestUrl());
            return;
        }
        log.info("[决策管理器]:请求 {} 需要的权限 - {}", ((FilterInvocation) resource).getRequestUrl(), configAttributes);
        // 判断用户所拥有的权限，是否符合对应的Url权限，用户权限是实现 UserDetailsService#loadUserByUsername 返回用户所对应的权限
        Iterator<ConfigAttribute> ite = configAttributes.iterator();
        log.info("[决策管理器]:用户 {} 拥有的权限 - {}", authentication.getName(), authentication.getAuthorities());
        while (ite.hasNext()) {
            ConfigAttribute neededAuthority = ite.next();
            String neededAuthorityStr = neededAuthority.getAttribute();
            for (GrantedAuthority existingAuthority : authentication.getAuthorities()) {
                if (neededAuthorityStr.equals(existingAuthority.getAuthority())) {
                    return;
                }
            }
        }
        log.info("[决策管理器]:用户 {} 没有访问资源 {} 的权限!", authentication.getName(), ((FilterInvocation) resource).getRequestUrl());
        throw new AccessDeniedException("权限不足!");
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


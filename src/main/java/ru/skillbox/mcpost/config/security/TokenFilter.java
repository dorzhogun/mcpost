package ru.skillbox.mcpost.config.security;

import ru.skillbox.mcpost.dto.ValidateJwtTokenRq;
import ru.skillbox.mcpost.feign.AuthFeignClient;
import feign.FeignException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenFilter extends GenericFilterBean {
    private final AuthFeignClient feignClient;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authenticate((HttpServletRequest) servletRequest));
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private Authentication authenticate(HttpServletRequest request) {
        String token = getToken(request);
        TokenAuthentication authentication =
                new TokenAuthentication(null, false, null);
        if (token != null) {
            try {
                Map<String, String> claims = feignClient.validateToken(new ValidateJwtTokenRq(token));
                authentication = new TokenAuthentication(
                        List.of(() -> claims.get("authorities")),
                        true,
                        claims.get("principal")
                );
            } catch (FeignException e) {
                log.info("Token {} не валиден", token);
            }
        }
        return authentication;
    }

    public String getToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}

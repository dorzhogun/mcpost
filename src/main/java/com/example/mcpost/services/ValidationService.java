package com.example.mcpost.services;

import com.example.mcpost.dto.ValidateJwtTokenRq;
import com.example.mcpost.feign.AuthFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationService {
    private final AuthFeignClient authClient;
    @SneakyThrows
    public boolean isValidToken(String tokenWithoutPrefix) {
        log.debug("Process remote token validation is started, token: {}", tokenWithoutPrefix);
        boolean result = authClient.validate(ValidateJwtTokenRq.builder().token(tokenWithoutPrefix).build());
        log.debug("Remote validation. Received result: {}", result);
        return result;
    }
}

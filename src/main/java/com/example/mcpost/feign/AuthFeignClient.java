package com.example.mcpost.feign;

import com.example.mcpost.dto.ValidateJwtTokenRq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "auth", url = "${app.request-validation.validator-path}")
public interface AuthFeignClient {
    @PostMapping
    Map<String, String> validateToken(@RequestBody ValidateJwtTokenRq request);
}

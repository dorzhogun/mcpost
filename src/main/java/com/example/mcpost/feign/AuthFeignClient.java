package com.example.mcpost.feign;

import com.example.mcpost.dto.ValidateJwtTokenRq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth", url = "${app.request-validation.validator-path}")
public interface AuthFeignClient {
    @PostMapping
    Boolean validate(@RequestBody ValidateJwtTokenRq request);
}

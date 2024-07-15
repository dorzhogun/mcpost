package ru.skillbox.mcpost.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.mcpost.dto.AccountRs;
import ru.skillbox.mcpost.dto.JwtRq;

import java.util.Map;

@FeignClient(name = "post", url = "${app.feignClient.url}")
public interface PostFeignClient {
    @PostMapping("/api/v1/auth/getclaims")
    Map<String, String> validateToken(@RequestBody JwtRq request);

    @GetMapping("/api/v1/account/search")
    AccountRs getAccounts(
            @RequestParam String author,
            @RequestParam Integer size,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    );

    @PostMapping(value = "/photo/{principal}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String upload(@PathVariable String principal,
                  @RequestParam String type,
                  @RequestPart("file") MultipartFile file,
                  @RequestHeader(HttpHeaders.COOKIE) String cookie);
}
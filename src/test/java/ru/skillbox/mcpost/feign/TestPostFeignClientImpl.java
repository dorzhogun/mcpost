package ru.skillbox.mcpost.feign;

import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.mcpost.dto.AccountRs;
import ru.skillbox.mcpost.dto.JwtRq;

import java.util.ArrayList;

import java.util.Map;

public class TestPostFeignClientImpl implements PostFeignClient {
    @Override
    public Map<String, String> validateToken(JwtRq request) {
        return Map.of("principal", request.getToken(), "authorities", "USER");
    }

    @Override
    public AccountRs getAccounts(String firstName, Integer size, String token) {
        return new AccountRs(2L, 1, new ArrayList<>());
    }

    @Override
    public String upload(String principal, String type, MultipartFile file, String cookie) {
        return "image_path_upload";
    }
}



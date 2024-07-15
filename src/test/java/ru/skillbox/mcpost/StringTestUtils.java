package ru.skillbox.mcpost;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;
import ru.skillbox.mcpost.exceptions.ResourceLoadingException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

public class StringTestUtils {
    private StringTestUtils() {
        // Private constructor to hide the implicit public one
    }
    public static String readStringFromResource(String resourcePath) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(MessageFormat.format("classpath:{0}", resourcePath));
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new ResourceLoadingException("Failed to read resource: " + resourcePath, e);
        }
    }

}

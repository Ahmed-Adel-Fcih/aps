package com.qeema.aps.common.utils;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AmazonUtils {

    public static String generateSignature(Map<String, Object> params) {

        log.info(" AmazonConstants.SHA_REQUEST_PHRASE : " + AmazonConstants.SHA_REQUEST_PHRASE);
        params = params.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        params.forEach((k, v) -> log.info(k + " : " + v));
        String shaRequestPhrase = AmazonConstants.SHA_REQUEST_PHRASE;
        String requestString = shaRequestPhrase;
        for (Entry<String, Object> entry : params.entrySet())
            requestString += entry.getKey() + "=" + entry.getValue();
        requestString += shaRequestPhrase;
        String signature = "";
        log.info(requestString);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashed = digest.digest(requestString.getBytes(StandardCharsets.UTF_8));
            signature = javax.xml.bind.DatatypeConverter.printHexBinary(hashed);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return signature;
    }

    public static void addToMapIfNotNull(Map<String, Object> params, Object request, Set<String> excludedFields) {
        Field[] fields = request.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (excludedFields.contains(field.getName())) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object value = field.get(request);
                if (value != null) {
                    params.put(field.getName(), value.toString());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}

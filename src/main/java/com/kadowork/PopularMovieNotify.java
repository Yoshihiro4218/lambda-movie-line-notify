package com.kadowork;

import com.amazonaws.services.lambda.runtime.*;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.util.*;
import org.springframework.web.client.*;

import java.util.*;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

public class PopularMovieNotify implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    RestTemplate restTemplate = new RestTemplate();

    private static final String LINE_ACCESS_TOKEN = System.getenv("LINE_ACCESS_TOKEN");
    private static final String MOVIE_API_ACCESS_KEY = System.getenv("MOVIE_API_ACCESS_KEY");
    private static final String LINE_NOTIFY_POST_URL = "https://notify-api.line.me/api/notify";

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        ResponseEntity<String> lineResponse = postLineNotify();
        System.out.println(lineResponse);

        Map<String, Object> output = new HashMap<>();
        output.put("input", input);
        output.put("context", context);
        output.put("res", lineResponse);
        return output;
    }

    private ResponseEntity<String> postLineNotify() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + LINE_ACCESS_TOKEN);
        headers.setContentType(MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("message", "message_from_lambda_java_local");
        map.add("imageFile", new ClassPathResource("static/genbaneko.jpeg"));
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
        System.out.println(httpEntity);
        return restTemplate.exchange(LINE_NOTIFY_POST_URL, POST, httpEntity, String.class);
    }
}
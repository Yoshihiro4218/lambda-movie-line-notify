package com.kadowork;

import com.amazonaws.services.lambda.runtime.*;
import org.apache.commons.io.*;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.util.*;
import org.springframework.web.client.*;
import org.springframework.web.util.*;

import java.io.*;
import java.net.*;
import java.util.*;

import static com.kadowork.PopularMovie.Movie;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

public class PopularMovieNotify implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    RestTemplate restTemplate = new RestTemplate();

    private static final String LINE_ACCESS_TOKEN = System.getenv("LINE_ACCESS_TOKEN");
    private static final String MOVIE_API_KEY = System.getenv("MOVIE_API_ACCESS_KEY");
    private static final String LINE_NOTIFY_POST_URL = "https://notify-api.line.me/api/notify";
    private static final String MOVIE_API_POPULAR_MOVIES_URL = "https://api.themoviedb.org/3/movie/popular";
    private static final String MOVIE_API_POSTER_URL = "https://image.tmdb.org/t/p/w500";

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        PopularMovie popularMovies = fetchPopularMovies();
        Movie mostPopularMovie = popularMovies.getResults()
                                              .stream()
                                              .findFirst()
                                              .orElseThrow(() -> new RuntimeException("todo exception"));
        // TODO: template 化
        String message = "■ タイトル\n" + mostPopularMovie.getTitle() + "\n\n" +
                         "■ 概要\n" + mostPopularMovie.getOverview();
        byte[] moviePoster = fetchMoviePoster(mostPopularMovie.getPosterPath());
        // TODO: うまくいかんので一度保存しているが、、
        try {
            FileUtils.writeByteArrayToFile(new File("src/main/resources/static/tmp/tmp.jpeg"), moviePoster);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResponseEntity<String> lineResponse = postLineNotify(message, new ClassPathResource("static/tmp/tmp.jpeg"));

        Map<String, Object> output = new HashMap<>();
        output.put("input", input);
        output.put("context", context);
        output.put("res", lineResponse);
        return output;
    }

    private ResponseEntity<String> postLineNotify(String message, Resource moviePoster) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + LINE_ACCESS_TOKEN);
        headers.setContentType(MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("message", "【現在人気の映画はこちら！】 \n " + message);
        map.add("imageFile", moviePoster);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
        return restTemplate.exchange(LINE_NOTIFY_POST_URL, POST, httpEntity, String.class);
    }

    private PopularMovie fetchPopularMovies() {
        URI uri = UriComponentsBuilder
                .fromUriString(MOVIE_API_POPULAR_MOVIES_URL)
                .queryParam("api_key", MOVIE_API_KEY)
                .queryParam("language", "ja")
                .queryParam("page", "1")
                .build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.GET, null, PopularMovie.class).getBody();
    }

    private byte[] fetchMoviePoster(String posterPath) {
        URI uri = UriComponentsBuilder
                .fromUriString(MOVIE_API_POSTER_URL + posterPath)
                .build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.GET, null, byte[].class).getBody();
    }
}
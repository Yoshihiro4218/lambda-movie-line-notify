package com.kadowork;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.*;
import com.fasterxml.jackson.databind.annotation.*;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(SnakeCaseStrategy.class)
public class PopularMovie {
    private List<Movie> results;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class Movie {
        private int id;
        private String title;
        private String overview;
        private float popularity;
        private float voteAverage;
        private int voteCount;
        private String posterPath;
        private boolean adult;
    }
}

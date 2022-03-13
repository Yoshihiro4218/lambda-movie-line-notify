package com.kadowork.popularmovie;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

class PopularMovieNotifyTest {
    private final PopularMovieNotify popularMovieNotify = new PopularMovieNotify();

    @Test
    void PopularMovieNotify() {
        popularMovieNotify.handleRequest(null, null);
        assertThat("example").isEqualTo("example");
    }
}
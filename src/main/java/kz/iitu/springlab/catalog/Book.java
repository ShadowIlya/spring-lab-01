package kz.iitu.springlab.catalog;

import jakarta.validation.constraints.NotBlank;
import kz.iitu.springlab.validation.NoDigits;
import kz.iitu.springlab.web.Genre;

import java.time.Instant;

public record Book(
        Long id,
        String title,
        @NotBlank(message = "{book.author.required}") @NoDigits String author,
        int year,
        Genre genre,
        Instant createdAt
) {
    // Конструктор для совместимости с кодом прошлых лабораторных (из 4 параметров)
    public Book(Long id, String title, String author, int year) {
        this(id, title, author, year, null, Instant.now());
    }
}
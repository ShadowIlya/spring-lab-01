package kz.iitu.springlab.service;

import kz.iitu.springlab.audit.Audited;
import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.web.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

@Service
public class CatalogService {

    // Внутреннее хранилище книг в памяти для Лабораторной № 7
    private final Map<Long, Book> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    // Used only for the fixed self-invocation (Task 4.5).
    @Autowired
    @Lazy
    private CatalogService self;

    public CatalogService() {
        // Заполняем 2 стартовые книги, чтобы при старте GET /api/books возвращал данные
        create(new Book(null, "Spring in Action", "Craig Walls", 2022, Genre.FANTASY, Instant.now()));
        create(new Book(null, "Clean Code", "Robert Martin", 2008, Genre.FANTASY, Instant.now()));
    }

    // =========================================================================
    // МЕТОДЫ ДЛЯ ЛАБОРАТОРНОЙ № 7 (REST API)
    // =========================================================================

    public List<Book> findAll(String author) {
        if (author != null && !author.isBlank()) {
            return store.values().stream()
                    .filter(b -> b.author() != null && b.author().equalsIgnoreCase(author))
                    .toList();
        }
        return new ArrayList<>(store.values());
    }

    public Optional<Book> findById(long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Book create(Book b) {
        boolean exists = store.values().stream().anyMatch(x ->
                x.title().equalsIgnoreCase(b.title()) && x.author().equalsIgnoreCase(b.author()));
        if (exists) {
            throw new DuplicateTitleException(b.title()); // -> Вызывает 409 Conflict
        }
        long id = seq.incrementAndGet();
        Book saved = new Book(id, b.title(), b.author(), b.year(), b.genre(),
                b.createdAt() != null ? b.createdAt() : Instant.now());
        store.put(id, saved);
        return saved;
    }

    public Book update(long id, Book b) {
        Book old = store.get(id);
        if (old == null) {
            throw new BookNotFoundException(id); // -> Вызывает 404 Not Found
        }
        Book updated = new Book(id, b.title(), b.author(), b.year(), b.genre(), old.createdAt());
        store.put(id, updated);
        return updated;
    }

    public void delete(long id) {
        if (store.remove(id) == null) {
            throw new BookNotFoundException(id); // Повторное удаление -> Вызывает 404 Not Found
        }
    }

    // =========================================================================
    // СТАРЫЕ МЕТОДЫ (Сохранены из прошлых лаб)
    // =========================================================================

    @Audited(action = "CATALOG_LIST", logArguments = true)
    public List<String> findAll(int limit) {
        sleep(300);                             // a deliberately "slow" method
        return IntStream.rangeClosed(1, limit)
                .mapToObj(i -> "Item no. " + i)
                .toList();
    }

    @Audited(action = "CATALOG_REMOVE")
    public String remove(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid identifier: " + id);
        }
        return "Removed item no. " + id;
    }

    // BEFORE the fix: self-invocation through this -> bypasses the proxy
    public String removeTwice(long id) {
        String first  = remove(id);
        String second = remove(id + 1);
        return first + "; " + second;
    }

    // AFTER the fix: calls go through the proxy via the self reference
    public String removeTwiceFixed(long id) {
        String first  = self.remove(id);
        String second = self.remove(id + 1);
        return first + "; " + second;
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
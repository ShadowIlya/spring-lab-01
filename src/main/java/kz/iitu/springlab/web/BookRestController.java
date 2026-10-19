package kz.iitu.springlab.web;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// ЗАГЛУШЕНО для Лаб 7: закомментировали @RestController, чтобы пути не конфликтовали с BookApiController
// @RestController
@RequestMapping("/api/old-books")
public class BookRestController {

    private final CatalogService service;

    public BookRestController(CatalogService service) {
        this.service = service;
    }

    // Individual variant 8: GET /api/books with a language header
    // Читаем заголовок Accept-Language и меняем названия полей
    @GetMapping
    public List<Map<String, Object>> list(@RequestParam(required = false) String author,
                                          @RequestParam(defaultValue = "10") int limit,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "en") String language) {

        List<Book> books = service.findAll(author).stream().limit(limit).toList();

        return books.stream().map(book -> {
            Map<String, Object> localizedBook = new LinkedHashMap<>();

            // Если клиент просит русский язык (ru) — переводим названия полей
            if (language.toLowerCase().startsWith("ru")) {
                localizedBook.put("код", book.id());
                localizedBook.put("название", book.title());
                localizedBook.put("автор", book.author());
                localizedBook.put("год", book.year());
            } else {
                // По умолчанию (en) отдаем стандартные названия
                localizedBook.put("id", book.id());
                localizedBook.put("title", book.title());
                localizedBook.put("author", book.author());
                localizedBook.put("year", book.year());
            }
            return localizedBook;
        }).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> find(@PathVariable long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book book) {
        Book saved = service.create(book);
        return ResponseEntity
                .created(URI.create("/api/books/" + saved.id()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> replace(@PathVariable long id, @RequestBody Book book) {
        Book updated = service.update(id, book);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
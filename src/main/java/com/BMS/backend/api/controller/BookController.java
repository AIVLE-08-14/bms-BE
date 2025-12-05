package com.BMS.backend.api.controller;

import com.BMS.backend.domain.Book;
import com.BMS.backend.dto.BookRequestDTO;
import com.BMS.backend.dto.BookResponseDTO;
import com.BMS.backend.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
// [수정 1] API 정의서에 맞춰 버전(v1)을 URL에 추가
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * 1. 도서 목록 조회
     * API 정의서: GET /api/v1/books
     * 입력: 헤더(JWT) -> X-User-Id로 대체
     * 기능: 로그인한 사용자의 책 목록을 반환
     */
    @GetMapping("/books")
    public ResponseEntity<List<BookResponseDTO>> getBooks(
            @RequestHeader("X-User-Id") Long userId) {
        // 정의서에 '헤더'가 필수라고 되어 있으므로, '내 책 조회' 로직을 여기에 매핑
        List<BookResponseDTO> books = bookService.getMyBooks(userId)
                .stream()
                .map(BookResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    /**
     * 2. 도서 상세 조회
     * API 정의서: GET /api/v1/books/{id}
     */
    @GetMapping("/books/:id")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(book -> ResponseEntity.ok(BookResponseDTO.fromEntity(book)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 3. 도서 등록
     * API 정의서: POST /api/v1/books
     * 입력: Body(JSON), 헤더(JWT)
     */
    @PostMapping("/books/:id")
    public ResponseEntity<BookResponseDTO> createBook(
            @RequestBody BookRequestDTO bookRequestDTO,
            @RequestHeader("X-User-Id") Long userId) {
        Book book = bookRequestDTO.toEntity();
        Book savedBook = bookService.createBook(book, userId);

        // 정의서 예시처럼 200 OK와 함께 데이터 반환 (생성은 보통 201을 쓰지만 정의서가 우선이라면 200도 가능, 여기선 표준인 201 유지)
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BookResponseDTO.fromEntity(savedBook));
    }

    /**
     * 4. 도서 수정
     * API 정의서: PUT /api/v1/books/{id}
     */
    @PutMapping("/books/:id")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Long id,
            @RequestBody BookRequestDTO bookRequestDTO,
            @RequestHeader("X-User-Id") Long userId) {
        Book book = bookRequestDTO.toEntity();
        Book updatedBook = bookService.updateBook(id, book, userId);
        return ResponseEntity.ok(BookResponseDTO.fromEntity(updatedBook));
    }

    /**
     * 5. 도서 삭제
     * API 정의서: DELETE /api/v1/books/{id}
     * 반환: 204 No Content
     */
    @DeleteMapping("/books/:id")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        bookService.deleteBook(id, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 6. 표지 저장 (추가된 기능)
     * API 정의서: PUT /api/v1/books/{id}/cover
     * 입력: { "coverImageUrl": "..." }
     * 주의: 이 기능을 쓰려면 BookRequestDTO와 Entity에 coverImageUrl 필드가 있어야 합니다.
     */
    @PutMapping("/books/:id/cover")
    public ResponseEntity<BookResponseDTO> updateBookCover(
            @PathVariable Long id,
            @RequestBody Map<String, String> coverMap, // { "coverImageUrl": "url..." } 형태 받기 위함
            @RequestHeader("X-User-Id") Long userId) {

        String coverImageUrl = coverMap.get("coverImageUrl");

        // 주의: Service에 updateBookCover 메서드를 새로 만드셔야 합니다!
        // 현재는 코드가 없으므로, 기존 updateBook을 응용하거나 Service에 추가해야 함을 알리는 주석입니다.
        // Book updatedBook = bookService.updateBookCover(id, coverImageUrl, userId);

        // 임시 리턴 (Service 구현 후 주석 해제하세요)
        return ResponseEntity.ok().build();
    }
}
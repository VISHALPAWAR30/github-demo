package com.example.document_wallet.controller;

import com.example.document_wallet.dto.DocumentRequest;
import com.example.document_wallet.dto.DocumentUploadRequest;
import com.example.document_wallet.dto.TypeCountProjection;
import com.example.document_wallet.model.DocumentEntity;
import com.example.document_wallet.repository.DocumentRepository;
import com.example.document_wallet.security.SecurityUtil;

import com.example.document_wallet.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentRepository repository;
    private final DocumentService documentService;



    public DocumentController(DocumentRepository repository, DocumentService documentService) {
        this.repository = repository;
        this.documentService = documentService;
    }


    // user endpoint
    @PostMapping("/document")
    public Object createDocument(
            @RequestBody(required = false) DocumentRequest request,
            Authentication authentication
    ) {
        if (request == null) {
            return "No document found!!";
        }

        String userId = SecurityUtil.getCurrentUserId(authentication);

        DocumentEntity document = new DocumentEntity();
        document.setOwnerUserId(userId);
        document.setType(request.getType());
        document.setName(request.getName());
        document.setTags(request.getTags());
        document.setMetadata(request.getMetadata());

        return repository.save(document);
    }

    @GetMapping("/{id}")
    public DocumentEntity getDocumentById(
            @PathVariable String id,
            Authentication authentication
    ) {
        String userId = SecurityUtil.getCurrentUserId(authentication);

        return repository.findByIdAndOwnerUserIdAndDeletedFalse(id, userId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Not a Valid User"
                        )
                );
    }

    //user want see their own document

    @GetMapping
    public List<DocumentEntity> myDocuments(Authentication authentication) {
        String userId = SecurityUtil.getCurrentUserId(authentication);
        return repository.findByOwnerUserIdAndDeletedFalse(userId);
    }

    @GetMapping("/paged")
    public Page<DocumentEntity> myDocumentsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        String userId = SecurityUtil.getCurrentUserId(authentication);
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByOwnerUserIdAndDeletedFalse(userId, pageable);
    }

    @PutMapping("/{id}")
    public DocumentEntity updateDocument(
            @PathVariable String id,
            @RequestBody DocumentEntity update,
            Authentication authentication
    ) {
        String userId = SecurityUtil.getCurrentUserId(authentication);

        DocumentEntity existing = repository
                .findByIdAndOwnerUserIdAndDeletedFalse(id, userId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Not a Valid User"
                        )
                );

        existing.setName(update.getName());
        existing.setTags(update.getTags());
        existing.setMetadata(update.getMetadata());
        existing.setUpdatedAt(Instant.now());

        return repository.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDocument(
            @PathVariable String id,
            Authentication authentication
    ) {
        String userId = SecurityUtil.getCurrentUserId(authentication);

        DocumentEntity document = repository
                .findByIdAndOwnerUserIdAndDeletedFalse(id, userId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Not a Valid User"
                        )
                );

        document.setDeleted(true);
        document.setUpdatedAt(Instant.now());
        repository.save(document);
    }

    // admin endpoint here admin can Fetch documents of any user
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/documents")
    public List<DocumentEntity> getDocumentsByUserId(
            @RequestParam String userId
    ) {
        return repository.findByOwnerUserIdAndDeletedFalse(userId);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public List<DocumentEntity> getDocuments() {
        return repository.findByDeletedFalse();
    }



    // aggregation endpoint
    @GetMapping("/stats/type-count")
    public Map<String, Long> documentTypeCount(Authentication authentication) {

        String userId = SecurityUtil.getCurrentUserId(authentication);
        System.out.println("JWT USER ID = " + userId);


        List<TypeCountProjection> results =
                repository.countDocumentsByType(userId);

        if (results == null || results.isEmpty()) {
            return Map.of();
        }

        return results.stream()
                .filter(r -> r.getId() != null)
                .collect(Collectors.toMap(
                        TypeCountProjection::getId,
                        TypeCountProjection::getCount
                ));
    }

    @GetMapping("/stats/count")
    public Map<String, Long> documentCount(Authentication authentication) {

        String userId = SecurityUtil.getCurrentUserId(authentication);

        long count = repository
                .findByOwnerUserIdAndDeletedFalse(userId)
                .size();

        return Map.of("total", count);
    }


    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam String type,
            @RequestParam String name,
            Authentication authentication
    ) throws IOException {

        if (file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "File is empty"
            );
        }

        String userId = SecurityUtil.getCurrentUserId(authentication);

        documentService.upload(file, type, name, userId);

        return ResponseEntity.ok(
                Map.of("message", "Document uploaded successfully")
        );
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadDocument(
            @PathVariable String id,
            Authentication authentication
    ) {

        String userId = SecurityUtil.getCurrentUserId(authentication);

        DocumentEntity doc = repository
                .findByIdAndOwnerUserIdAndDeletedFalse(id, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Document not found"
                ));

        byte[] fileBytes = Base64.getDecoder()
                .decode(doc.getFileBase64());

        return ResponseEntity.ok()
                .header("Content-Type", doc.getFileType())
                .header("Content-Disposition",
                        "attachment; filename=\"" + doc.getFileName() + "\"")
                .body(fileBytes);
    }


}

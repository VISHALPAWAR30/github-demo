package com.example.document_wallet.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentUploadRequest {

    @NotBlank
    private String type;

    @NotBlank
    private String name;

    @NotBlank
    private String fileBase64;

    @NotBlank
    private String fileType;
}
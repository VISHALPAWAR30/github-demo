package com.example.document_wallet.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;
import java.util.Set;

public class DocumentRequest {  @NotBlank
private String type;

    @NotBlank
    private String name;

    private Set<String> tags;
    private Map<String, Object> metadata;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}

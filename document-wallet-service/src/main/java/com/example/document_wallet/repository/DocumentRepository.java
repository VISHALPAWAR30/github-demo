package com.example.document_wallet.repository;

import com.example.document_wallet.model.DocumentEntity;
import com.example.document_wallet.dto.TypeCountProjection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
public interface DocumentRepository
        extends MongoRepository<DocumentEntity, String> {

    List<DocumentEntity> findByOwnerUserIdAndDeletedFalse(String ownerUserId);

    Optional<DocumentEntity> findByIdAndOwnerUserIdAndDeletedFalse(
            String id,
            String ownerUserId
    );


    Page<DocumentEntity> findByOwnerUserIdAndDeletedFalse(
            String ownerUserId,
            Pageable pageable
    );

    List<DocumentEntity> findByDeletedFalse();



    @Aggregation(pipeline = {
            "{ $match: { ownerUserId: ?0, deleted: false } }",
            "{ $group: { _id: '$type', count: { $sum: 1 } } }",
            "{ $project: { id: '$_id', count: 1, _id: 0 } }"
    })
    List<TypeCountProjection> countDocumentsByType(String ownerUserId);
}

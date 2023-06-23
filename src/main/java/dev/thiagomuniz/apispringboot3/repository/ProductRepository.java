package dev.thiagomuniz.apispringboot3.repository;

import dev.thiagomuniz.apispringboot3.dto.ProductRecordDto;
import dev.thiagomuniz.apispringboot3.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
}

package dev.thiagomuniz.apispringboot3.controller;

import dev.thiagomuniz.apispringboot3.dto.ProductRecordDto;
import dev.thiagomuniz.apispringboot3.model.ProductModel;
import dev.thiagomuniz.apispringboot3.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/products")
    public ResponseEntity<ProductModel> createProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @GetMapping("/products")
    public ResponseEntity<?> listAllProducts() {
        List<ProductModel> productModelList = productRepository.findAll();
        if (!productModelList.isEmpty()) {
            for (ProductModel productModel : productModelList) {
                productModel.add(linkTo(methodOn(ProductController.class).listProductById(productModel.getId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> listProductById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findById(id));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProductById(@PathVariable UUID id, @RequestBody @Valid ProductRecordDto productRecordDto) {
        var productModel = productRepository.findById(id);
        if (productModel.isPresent()) {
            BeanUtils.copyProperties(productRecordDto, productModel.get(), "id");
            return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable UUID id) {
        var productModel = productRepository.findById(id);
        if (productModel.isPresent()) {
            productRepository.delete(productModel.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

}

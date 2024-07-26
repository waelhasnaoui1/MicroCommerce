package com.ecommerceMicro.product_service.controller.admin;


import com.ecommerceMicro.product_service.dto.ProductRequest;
import com.ecommerceMicro.product_service.dto.ProductResponse;
import com.ecommerceMicro.product_service.service.admin.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
       return productService.createProduct(productRequest);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {

        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductWithCategory(@PathVariable String id) {
        return productService.getProductWithCategory(id);
    }

    @GetMapping("/category/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getProductsByCategory(@PathVariable String categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping("/reduceQuantity")
    public ResponseEntity<Void> reduceQuantity(
            @RequestParam String productId,
            @RequestParam long quantity
    ){
        log.info("ProductController | reduceQuantity is called");
        log.info("ProductController | reduceQuantity | productId : " + productId);
        log.info("ProductController | reduceQuantity | quantity : " + quantity);

        productService.reduceQuantity(productId,quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(
            @PathVariable("id") String productId
    ){
        productService.deleteProductById(productId);
    }

}

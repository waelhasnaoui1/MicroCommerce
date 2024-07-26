package com.ecommerceMicro.product_service.service.admin.product;

import com.ecommerceMicro.product_service.dto.ProductRequest;
import com.ecommerceMicro.product_service.dto.ProductResponse;
import com.ecommerceMicro.product_service.exceptions.ProductServiceCustomException;
import com.ecommerceMicro.product_service.model.Product;
import com.ecommerceMicro.product_service.repository.CategoryRepository;
import com.ecommerceMicro.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository; // Inject CategoryRepository

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .categoryId(productRequest.categoryId()) // Set the category ID
                .quantity(productRequest.quantity())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());

        return new ProductResponse(product.getId(), product.getName(),
                product.getDescription(), product.getPrice(), product.getCategoryId(),product.getQuantity());
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getCategoryId(),
                        product.getQuantity()
                )) // Include category ID

                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductWithCategory(String id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            return new ProductResponse(product.getId(), product.getName(),
                    product.getDescription(), product.getPrice(), product.getCategoryId(), product.getQuantity());
        }
        return null;
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        if (!products.isEmpty()) {
            return products.stream()
                    .map(product -> new ProductResponse(
                            product.getId(),
                            product.getName(),
                            product.getDescription(),
                            product.getPrice(),
                            product.getCategoryId(),
                            product.getQuantity()
                    ))
                    .collect(Collectors.toList());
        } else {
            // Return an empty list if no products are associated with the category
            return Collections.emptyList();
        }
    }

    @Override
    public void reduceQuantity(String productId, long quantity) {

        log.info("Reduce Quantity {} for id: {}",quantity,productId);
        Product product = productRepository.findById(productId).orElseThrow(
                ()-> new ProductServiceCustomException(
                        "Product with given id not found",
                        "PRODUCT_NOT_FOUND"

                )
        );

        if (product.getQuantity() < quantity) {
            throw new ProductServiceCustomException(
                    "Product does not have sufficient Quantity",
                    "INSUFFICIENT_QUANTITY"
            );

        }

        product.setQuantity(product.getQuantity()-quantity);
        productRepository.save(product);
        log.info("Product Quantity updated Successfully");

    }

    @Override
    public void deleteProductById(String productId) {
        log.info("Product id: {}",productId);
        if (!productRepository.existsById(productId)){
            log.info("Im in this loop {}",!productRepository.existsById(productId));
            throw new ProductServiceCustomException(
                    "Product with given id not found",
                    "PRODUCT_NOT_FOUND"
            );
        }
        log.info("Deleting product with id {} :",productId);
        productRepository.deleteById(productId);
    }
}

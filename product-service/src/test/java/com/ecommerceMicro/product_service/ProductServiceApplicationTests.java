package com.ecommerceMicro.product_service;

import com.ecommerceMicro.product_service.controller.admin.ProductController;
import com.ecommerceMicro.product_service.dto.ProductRequest;
import com.ecommerceMicro.product_service.model.Product;
import com.ecommerceMicro.product_service.service.admin.product.ProductService;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@WebMvcTest(ProductService.class)
class ProductServiceApplicationTests {

    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.1");

    @LocalServerPort
    private Integer port;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        MockitoAnnotations.openMocks(this);
    }

    static {
        mongoDBContainer.start();
    }

    @Test
    void shouldCreateProduct() throws Exception {

        ProductRequest productRequest = getProductRequest();

        RestAssured.given()
                .contentType("application/json")
                .body(productRequest)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo(productRequest.name()))
                .body("description", Matchers.equalTo(productRequest.description()))
                .body("price", Matchers.equalTo(productRequest.price().intValueExact()))
                .body("categoryId", Matchers.equalTo(productRequest.categoryId()));

    }

    private ProductRequest getProductRequest() {
        return new ProductRequest(null, "iPhone13", "latest model of iphone", BigDecimal.valueOf(1200), "1");
    }

    @Test
    public void shouldCreateProductWithMockito() throws Exception{
        /// the test will fail because of the port
        ProductRequest productRequest = getProductRequest();

        mockMvc.perform(
                MockMvcRequestBuilders.post("http://localhost:"+port+"/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(productRequest))

        ).andExpect(MockMvcResultMatchers.status().isAccepted());
    }


}

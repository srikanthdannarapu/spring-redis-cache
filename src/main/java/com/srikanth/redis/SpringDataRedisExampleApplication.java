package com.srikanth.redis;

import com.srikanth.redis.entity.Product;
import com.srikanth.redis.respository.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/product")
@EnableCaching
public class SpringDataRedisExampleApplication {
    @Autowired
    private ProductDao dao;

    @PostMapping
    public Product save(@RequestBody Product product) {
        return dao.save(product);
    }

    @GetMapping
    @Cacheable(value = "Product")
    public List<Product> getAllProducts() {
        return dao.findAll();
    }

    @GetMapping("/{id}")
    //unless = "#result.price > 100" ===>>> if price < 100 save it cache it
    @Cacheable(key = "#id", value = "Product", unless = "#result.price > 100")
    public Product findProduct(@PathVariable int id) {
        return dao.findProductById(id);
    }
    @DeleteMapping("/{id}")
    //CacheEvict makes sure that if we delete a product  from redis db, it will also be removed from cache
    @CacheEvict(key = "#id", value = "Product")
    public String remove(@PathVariable int id)   {
    	return dao.deleteProduct(id);
	}


    public static void main(String[] args) {
        SpringApplication.run(SpringDataRedisExampleApplication.class, args);
    }

}

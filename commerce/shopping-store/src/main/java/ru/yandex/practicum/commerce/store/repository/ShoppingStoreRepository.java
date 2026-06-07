package ru.yandex.practicum.commerce.store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.commerce.store.model.Product;
import ru.yandex.practicum.commerce.utils.enums.ProductCategory;

import java.util.UUID;

public interface ShoppingStoreRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT p FROM Product p WHERE p.productCategory = :category")
    Page<Product> findByCategory(@Param("category") ProductCategory category, Pageable pageable);

    boolean deleteByProductId(UUID productId);
}

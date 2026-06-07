package ru.yandex.practicum.commerce.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.commerce.warehouse.model.Product;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT p.productId, p.quantity FROM Product p WHERE p.productId in :ids ")
    Map<UUID, Long> findProductDetails(@Param("ids") Set<UUID> ids);

    @Query("SELECT p FROM Product p WHERE p.productId in :ids ")
    List<Product> findProducts(@Param("ids") Set<UUID> ids);
}

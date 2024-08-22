package com.cloud.A2;

import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<ProductEntity, Integer> {
}

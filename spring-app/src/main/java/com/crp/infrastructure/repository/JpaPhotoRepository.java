package com.crp.infrastructure.repository;

import com.crp.domain.repository.PhotoRepository;
import com.crp.infrastructure.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPhotoRepository extends JpaRepository<PhotoEntity, Long>, PhotoRepository {
}

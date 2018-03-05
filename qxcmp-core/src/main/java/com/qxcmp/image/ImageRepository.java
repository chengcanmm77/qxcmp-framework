package com.qxcmp.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface ImageRepository extends JpaRepository<Image, String>, JpaSpecificationExecutor<Image> {
}

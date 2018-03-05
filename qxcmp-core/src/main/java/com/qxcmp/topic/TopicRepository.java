package com.qxcmp.topic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface TopicRepository extends JpaRepository<Topic, String>, JpaSpecificationExecutor<Topic> {
}

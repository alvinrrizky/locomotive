package com.locomotive.consume_service.repository;

import com.locomotive.consume_service.model.LocomotiveSensor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocomotiveSensorRepository extends MongoRepository<LocomotiveSensor, String> {
}

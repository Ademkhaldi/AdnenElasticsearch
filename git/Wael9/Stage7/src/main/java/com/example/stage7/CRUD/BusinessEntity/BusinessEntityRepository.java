package com.example.stage7.CRUD.BusinessEntity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessEntityRepository extends MongoRepository<BusinessEntity, String> {
}

package com.example.stage7.CRUD.Portlet.repository;
import com.example.stage7.CRUD.Portlet.entity.Portlet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortletRepository extends MongoRepository<Portlet, String> {

}

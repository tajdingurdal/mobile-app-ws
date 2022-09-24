package com.tajdingurdal.app.repository;

import com.tajdingurdal.app.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
	Boolean existsByEmail(String email);
	UserEntity findByUserId(String userId);
	UserEntity findByLastName(String lastName);
}

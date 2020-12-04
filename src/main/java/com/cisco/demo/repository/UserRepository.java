package com.cisco.demo.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cisco.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	Optional<User> findById(Long id);
	Optional<List<User>> findByName(String name, Pageable pageable);
	Optional<User> findByEmail(String email);
	Optional<List<User>> findAllByCreationDateBetween(Date creationDateStart, Date creationDateEnd, Pageable pageable);
}

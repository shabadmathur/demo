package com.cisco.demo.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cisco.demo.entity.Bookmark;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	Optional<Bookmark> findById(Long id);
	Optional<List<Bookmark>> findByName(String name, Pageable pageable);
	Optional<List<Bookmark>> findByType(String name, Pageable pageable);
	Optional<List<Bookmark>> findByUrl(String url, Pageable pageable);
	Optional<List<Bookmark>> findByCreationDate(String creationDate, Pageable pageable);
	Optional<List<Bookmark>> findAllByCreationDateBetween(Date creationDateStart, Date creationDateEnd, Pageable pageable);
}
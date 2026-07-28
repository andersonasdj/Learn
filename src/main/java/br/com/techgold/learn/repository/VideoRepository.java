package br.com.techgold.learn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techgold.learn.model.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {

	List<Video> findByAulaIdOrderByOrdemAsc(Long aulaId);

}

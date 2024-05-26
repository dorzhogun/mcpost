package ru.skillbox.mcpost.repository;

import ru.skillbox.mcpost.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostIdAndAuthorId(Long postId, UUID authorId);
}

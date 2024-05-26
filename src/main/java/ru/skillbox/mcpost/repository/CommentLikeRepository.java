package ru.skillbox.mcpost.repository;

import ru.skillbox.mcpost.model.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByAuthorIdAndCommentId(UUID authorId, Long commentId);
}

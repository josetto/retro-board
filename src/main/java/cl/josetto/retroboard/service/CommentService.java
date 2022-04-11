package cl.josetto.retroboard.service;

import cl.josetto.retroboard.model.Comment;
import cl.josetto.retroboard.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Comment> saveAll(List<Comment> comments) {
        return commentRepository.saveAll(comments);
    }

    public List<Comment> getAllCommentsForToday() {
        LocalDate localDate = LocalDate.now();
        return commentRepository
                .findByCreatedYearAndMonthAndDay(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
    }

}

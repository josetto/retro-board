package cl.josetto.retroboard.repository;

import cl.josetto.retroboard.model.Comment;
import cl.josetto.retroboard.model.CommentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void findByCreatedYearAndMonthAndDayShouldReturn1Comment() {
        //Given
        Comment comment = new Comment();
        comment.setComment("Test");
        comment.setCommentType(CommentType.PLUS);
        comment.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        commentRepository.save(comment);

        //when
        LocalDate now = LocalDate.now();
        List<Comment> comments =
                commentRepository.findByCreatedYearAndMonthAndDay(now.getYear(), now.getMonthValue(), now.getDayOfMonth());

        //then
        Assertions.assertThat(comments.size()).isEqualTo(1);
        Assertions.assertThat(comments.get(0)).hasFieldOrPropertyWithValue("comment", "Test");
    }

    @Test
    public void saveShouldSave1Comment() {
        //given
        Comment comment = new Comment();
        comment.setComment("Test");
        comment.setCommentType(CommentType.PLUS);
        comment.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        //when
        Comment saved = commentRepository.save(comment);

        //then
        Assertions.assertThat(commentRepository.findById(saved.getId()).get()).isEqualTo(saved);
    }
}

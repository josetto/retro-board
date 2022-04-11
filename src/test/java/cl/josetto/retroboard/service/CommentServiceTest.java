package cl.josetto.retroboard.service;

import cl.josetto.retroboard.model.Comment;
import cl.josetto.retroboard.model.CommentType;
import cl.josetto.retroboard.repository.CommentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class CommentServiceTest {

    @MockBean
    private CommentRepository commentRepository;

    private CommentService commentService;

    @BeforeEach
    public void setup() {
        commentService = new CommentService(commentRepository);
    }

    @Test
    public void getAllCommentsForTodayShouldReturn1Comment() {
        //given
        Comment comment = new Comment();
        comment.setComment("Test");
        comment.setCommentType(CommentType.PLUS);
        comment.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        List<Comment> comments = List.of(comment);
        LocalDate now = LocalDate.now();



        Mockito.when(commentRepository.findByCreatedYearAndMonthAndDay(now.getYear(), now.getMonthValue(), now.getDayOfMonth()))
                .thenReturn(comments);

        //when
        List<Comment> actualComments = commentService.getAllCommentsForToday();
        Mockito.verify(commentRepository, Mockito.times(1))
                .findByCreatedYearAndMonthAndDay(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        Assertions.assertThat(comments).isEqualTo(actualComments);
    }

    @Test
    public void saveAllShouldSave2Comments() {
        //given
        Comment comment = new Comment();
        comment.setComment("Test Plus");
        comment.setCommentType(CommentType.PLUS);
        comment.setCreatedBy("Joseto");
        comment.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        Comment comment2 = new Comment();
        comment.setComment("Test Star");
        comment.setCommentType(CommentType.STAR);
        comment.setCreatedBy("Leonel");
        comment.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        List<Comment> comments = List.of(comment, comment2);
        Mockito.when(commentRepository.saveAll(comments)).thenReturn(comments);

        //when
        List<Comment> savedComments = commentService.saveAll(comments);

        //then
        Assertions.assertThat(savedComments).isNotEmpty();
        Mockito.verify(commentRepository,Mockito.times(1)).saveAll(comments);


    }
}

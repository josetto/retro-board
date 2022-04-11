package cl.josetto.retroboard.controller;

import cl.josetto.retroboard.model.Comment;
import cl.josetto.retroboard.model.CommentType;
import cl.josetto.retroboard.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    public void saveCommentsShouldReturnStatus302() throws Exception{
        ResultActions resultActions = mockMvc.perform(
                post("/comment").with(csrf())
                        .with(user("Joseto").roles("USER")).param("plusComment", "Test Plus")
            );

        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        Mockito.verify(commentService, Mockito.times(1)).saveAll(anyList());
        Mockito.verifyNoMoreInteractions(commentService);
    }

    @Test
    public void getCommentsShouldReturnStatus200() throws Exception {
        Comment comment = new Comment();
        comment.setComment("Test Plus");
        comment.setCommentType(CommentType.PLUS);
        comment.setCreatedBy("Joseto");
        comment.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        Comment comment2 = new Comment();
        comment2.setComment("Test Star");
        comment2.setCommentType(CommentType.STAR);
        comment2.setCreatedBy("Leonel");
        comment2.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        List<Comment> comments = Arrays.asList(comment, comment2);
        Mockito.when(commentService.getAllCommentsForToday()).thenReturn(comments);

        ResultActions resultActions = mockMvc.perform(
                get("/").with(user("Joseto").roles("USER")));

        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("comment"))
                .andExpect(model().attribute("plusComments", hasSize(1)))
                .andExpect(model().attribute("plusComments", hasItem(allOf(
                        hasProperty("createdBy", is("Joseto")),
                        hasProperty("comment", is("Test Plus"))
                        )
                )))
                .andExpect(model().attribute("starComments", hasSize(1)))
                .andExpect(model().attribute("starComments", hasItem(allOf(
                        hasProperty("createdBy", is("Leonel")),
                        hasProperty("comment", is("Test Star"))
                        )
                )));

        Mockito.verify(commentService, Mockito.times(1)).getAllCommentsForToday();
        Mockito.verifyNoMoreInteractions(commentService);

    }
}

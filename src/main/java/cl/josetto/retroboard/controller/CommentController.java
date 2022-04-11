package cl.josetto.retroboard.controller;

import antlr.StringUtils;
import cl.josetto.retroboard.model.Comment;
import cl.josetto.retroboard.model.CommentType;
import cl.josetto.retroboard.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("time", new SimpleDateFormat("yyyy-MM-dddd HH:mm:ss").format(new Date()));
        List<Comment> allComments = commentService.getAllCommentsForToday();
        Map<CommentType, List<Comment>> groupedComments =
                allComments.stream().collect(Collectors.groupingBy(Comment::getCommentType));

        model.addAttribute("starComments", groupedComments.get(CommentType.STAR));
        model.addAttribute("plusComments", groupedComments.get(CommentType.PLUS));
        model.addAttribute("deltaComments", groupedComments.get(CommentType.DELTA));
        return "comment";
    }

    @PostMapping("/comment")
    public String createComment(@RequestParam(required = false) String plusComment,
                                @RequestParam(required = false) String deltaComment,
                                @RequestParam(required = false) String starComment) {

        List<Comment> comments = new ArrayList<>();

        if(plusComment != null && !(plusComment.isEmpty())) {
            comments.add(Comment.createCommentFrom(plusComment, CommentType.PLUS));
        }
        if(deltaComment != null && !(deltaComment.isEmpty())) {
            comments.add(Comment.createCommentFrom(deltaComment, CommentType.DELTA));
        }
        if(starComment != null && !(starComment.isEmpty())) {
            comments.add(Comment.createCommentFrom(starComment, CommentType.STAR));
        }

        if(!comments.isEmpty()) {
            commentService.saveAll(comments);
        }
        return "redirect:/";
    }




}

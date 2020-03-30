package es.kairosds.controller;

import com.google.gson.Gson;
import es.kairosds.article.Article;
import es.kairosds.article.ArticleService;
import es.kairosds.comment.Comment;
import es.kairosds.comment.CommentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "USER")
public class CommentControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private CommentService commentService;

    private Gson jsonParser = new Gson();

    /**
     *  "lechuguino" word is the test swearing word, which is going to be checked as invalid
     */

    @Before
    public void initialize() {
        Article article = new Article("Test", "Test", "Test1", "Test", "Test");
        Comment comment1 = new Comment("Test1", "Test1");
        Comment comment2 = new Comment("Test2", "Test2");
        article.addComment(comment1);
        article.addComment(comment2);

        given(articleService.findOne(1)).willReturn(Optional.of(article));
        given(articleService.exists(1)).willReturn(true);
        given(articleService.save(any(Article.class))).willReturn(article);

        given(commentService.findOne(1)).willReturn(Optional.of(comment1));
        given(commentService.findOne(2)).willReturn(Optional.of(comment2));
        given(commentService.findOne(3)).willReturn(Optional.empty());
        given(commentService.findAll()).willReturn(article.getComments());
        given(commentService.checkSwearingWords("lechuguino")).willReturn(false);
        given(commentService.checkSwearingWords("Test3")).willReturn(true);
        given(commentService.save(any(Comment.class))).willReturn(comment1);
    }

    @Test
    public void shouldGetComments() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/articles/1/comments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author", is("Test1")))
                .andExpect(jsonPath("$[1].author", is("Test2")));
    }

    @Test
    public void shouldGetComment() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/articles/1/comments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author", is("Test1")));
        ;
    }

    @Test
    public void shouldNotFoundComment() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/articles/1/comments/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateComment() throws Exception {
        Comment comment = new Comment("Test3", "Test3");

        mvc.perform(MockMvcRequestBuilders.post("/articles/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(comment)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldTryToCreateInvalidCommentAndReturnBadRequest() throws Exception {
        Comment comment = new Comment("Test3", "lechuguino");

        mvc.perform(MockMvcRequestBuilders.post("/articles/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(comment)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateComment() throws Exception {
        Comment comment = new Comment("Test", "Test3");

        mvc.perform(MockMvcRequestBuilders.put("/articles/1/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author", is("Test")))
                .andExpect(jsonPath("$.message", is("Test3")));
    }

    @Test
    public void shouldNotFoundCommentToUpdate() throws Exception {
        Comment comment = new Comment("Test", "Test3");

        mvc.perform(MockMvcRequestBuilders.put("/articles/1/comments/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(comment)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteComment() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/articles/1/comments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author", is("Test1")));
    }

    @Test
    public void shouldNotFoundCommentToDelete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/articles/1/comments/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}

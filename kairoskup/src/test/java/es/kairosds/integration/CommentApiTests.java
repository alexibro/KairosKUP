package es.kairosds.integration;

import com.google.gson.Gson;
import es.kairosds.article.Article;
import es.kairosds.article.ArticleService;
import es.kairosds.comment.Comment;
import es.kairosds.comment.CommentService;
import es.kairosds.swearingapi.SwearingApiMiddleware;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "USER")
public class CommentApiTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @MockBean
    private SwearingApiMiddleware swearingApiMiddleware;

    private static long notFoundId = 100;
    private long lastArticleInsertedId;
    private long lastCommentInsertedId;

    private Gson jsonParser = new Gson();

    /**
     *  "lechuguino" word is the test swearing word, which is going to be checked as invalid
     */

    @Before
    public void initialize() {
        Article article = new Article("Test", "Test", "Test1", "Test", "Test");
        Comment comment1 = new Comment("Test1", "Test1");
        Comment comment2 = new Comment("Test2", "Test2");

        Article articleInserted = articleService.save(article);
        lastArticleInsertedId = articleInserted.getId();

        commentService.save(comment1);
        Comment commenInserted = commentService.save(comment2);
        lastCommentInsertedId = commenInserted.getId();

        given(swearingApiMiddleware.checkSwearingWords("lechugino")).willReturn(false);
        given(swearingApiMiddleware.checkSwearingWords("Test3")).willReturn(true);
    }

    @Test
    public void shouldGetComments() throws Exception {
        mvc.perform(get("/articles/" + lastArticleInsertedId + "/comments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(2)));
    }

    @Test
    public void shouldGetComment() throws Exception {
        mvc.perform(get(
                "/articles/" + lastArticleInsertedId + "/comments/" + lastCommentInsertedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author", is("Test2")));
        ;
    }

    @Test
    public void shouldNotFoundComment() throws Exception {
        mvc.perform(get(
                "/articles/" + lastArticleInsertedId + "/comments/" + notFoundId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateComment() throws Exception {
        Comment comment = new Comment("Test3", "Test3");

        mvc.perform(post("/articles/" + lastArticleInsertedId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(comment)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldTryToCreateInvalidCommentAndReturnBadRequest() throws Exception {
        Comment comment = new Comment("Test3", "lechuguino");

        mvc.perform(post("/articles/" + lastArticleInsertedId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(comment)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateComment() throws Exception {
        Comment comment = new Comment("Test", "Test3");

        mvc.perform(put(
                "/articles/" + lastArticleInsertedId + "/comments/" + lastCommentInsertedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author", is("Test")))
                .andExpect(jsonPath("$.message", is("Test3")));
    }

    @Test
    public void shouldNotFoundCommentToUpdate() throws Exception {
        Comment comment = new Comment("Test", "Test3");

        mvc.perform(put(
                "/articles/" + lastArticleInsertedId + "/comments/" + notFoundId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(comment)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteComment() throws Exception {
        mvc.perform(delete(
                "/articles/" + lastArticleInsertedId + "/comments/" + lastCommentInsertedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotFoundCommentToDelete() throws Exception {
        mvc.perform(delete(
                "/articles/" + lastArticleInsertedId + "/comments/" + notFoundId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}

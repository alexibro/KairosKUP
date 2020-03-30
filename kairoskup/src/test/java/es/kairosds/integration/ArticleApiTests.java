package es.kairosds.integration;

import com.google.gson.Gson;
import es.kairosds.article.Article;
import es.kairosds.article.ArticleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "USER")
public class ArticleApiTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ArticleService articleService;

    private static long notFoundId = 100;
    private long lastArticleInsertedId;

    private Gson jsonParser = new Gson();

    @Before
    public void initialize() {
        Article article1 = new Article("Test", "Test", "Test1", "Test", "Test");
        Article articleInserted = articleService.save(article1);
        lastArticleInsertedId = articleInserted.getId();
    }

    @Test
    public void shouldGetArticles() throws Exception {
        mvc.perform(get("/articles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].authorName", is("Test")));
    }

    @Test
    public void shouldGetArticle() throws Exception {
        Iterable<Article> articles = articleService.findAll();
        mvc.perform(get("/articles/" + lastArticleInsertedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName", is("Test")));
        ;
    }

    @Test
    public void shouldNotFoundArticle() throws Exception {
        mvc.perform(get("/articles/" + notFoundId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateArticle() throws Exception {
        Article article = new Article("Test2", "Test2", "Test2", "Test2", "Test2");

        mvc.perform(post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(article)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldUpdateArticle() throws Exception {
        Article article = new Article("Test2", "Test2", "Test2", "Test2", "Test2");

        mvc.perform(put("/articles/" + lastArticleInsertedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(article)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName", is("Test2")));;
    }

    @Test
    public void shouldNotFoundArticleToUpdate() throws Exception {
        Article article = new Article("Test2", "Test2", "Test2", "Test2", "Test2");

        mvc.perform(put("/articles/" + notFoundId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(article)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteArticle() throws Exception {
        mvc.perform(delete("/articles/" + lastArticleInsertedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName", is("Test")));
    }

    @Test
    public void shouldNotFoundArticleToDelete() throws Exception {
        mvc.perform(delete("/articles/" + notFoundId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
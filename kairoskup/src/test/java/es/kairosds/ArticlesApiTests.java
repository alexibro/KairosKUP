package es.kairosds;

import com.google.gson.Gson;
import es.kairosds.article.Article;
import es.kairosds.article.ArticleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "USER")
public class ArticlesApiTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ArticleService articleService;

    private Gson jsonParser = new Gson();

    @Before
    public void initialize() {
        Article article1 = new Article("Test", "Test", "Test1", "Test", "Test");
        List<Article> articles = new ArrayList<>();
        articles.add(article1);

        given(articleService.findAll()).willReturn(articles);
        given(articleService.findOne(1)).willReturn(Optional.of(article1));
        given(articleService.findOne(2)).willReturn(Optional.empty());
        given(articleService.exists(1)).willReturn(true);
        given(articleService.exists(2)).willReturn(false);
    }

    @Test
    public void testGetArticles() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/articles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].authorName", is("Test")));
    }

    @Test
    public void testGetArticle() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/articles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName", is("Test")));
        ;
    }

    @Test
    public void testGetArticleNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/articles/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateArticle() throws Exception {
        Article article = new Article("Test2", "Test2", "Test2", "Test2", "Test2");

        mvc.perform(MockMvcRequestBuilders.post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(article)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateArticle() throws Exception {
        Article article = new Article("Test2", "Test2", "Test2", "Test2", "Test2");

        mvc.perform(MockMvcRequestBuilders.put("/articles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(article)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName", is("Test2")));;
    }

    @Test
    public void testUpdateArticleNotFound() throws Exception {
        Article article = new Article("Test2", "Test2", "Test2", "Test2", "Test2");

        mvc.perform(MockMvcRequestBuilders.put("/articles/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(article)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteArticle() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/articles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName", is("Test")));
    }

    @Test
    public void testDeleteArticleNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/articles/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}

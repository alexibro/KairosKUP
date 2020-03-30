package es.kairosds.persistance;

import es.kairosds.article.Article;
import es.kairosds.article.ArticleRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ArticleRepositoryTests {

    @Autowired
    private ArticleRepository articleRepository;

    private static long notFoundId = 100;

    private long lastArticleInsertedId;

    @Before
    public void initialize() {
        Article article1 = new Article("Test1", "Test1", "Test1", "Test1", "Test1");
        Article article2 = new Article("Test2", "Test2", "Test2", "Test2", "Test2");

        articleRepository.save(article1);
        Article article = articleRepository.save(article2);
        lastArticleInsertedId = article.getId();
    }

    @After
    public void finalize() {
        articleRepository.deleteAll();
    }


    @Test
    public void shouldFindAllArticles() {
        Iterable<Article> articles = articleRepository.findAll();

        assertThat(articles).hasSize(2);
    }

    @Test
    public void shouldFindArticleById() {
        Optional<Article> article = articleRepository.findById(lastArticleInsertedId);

        assertThat(article.isPresent()).isTrue();
        assertThat(article.get().getAuthorName()).isEqualTo("Test2");
    }

    @Test
    public void shouldNotFindArticleById() {
        Optional<Article> article = articleRepository.findById(notFoundId);

        assertThat(article.isPresent()).isFalse();
    }

    @Test
    public void shouldExistsArticleById() {
        Boolean exists = articleRepository.existsById(lastArticleInsertedId);

        assertThat(exists).isTrue();
    }

    @Test
    public void shouldNotExistsArticleById() {
        Boolean exists  = articleRepository.existsById(notFoundId);

        assertThat(exists).isFalse();
    }

    @Test
    public void shouldSaveArticle() {
        Article article = new Article("Test3", "Test3", "Test3", "Test3", "Test3");

        Article articleInserted = articleRepository.save(article);
        assertThat(articleInserted).isNotNull();
        assertThat(articleInserted.getId()).isGreaterThan(0L);
    }

    @Test
    public void shouldDeleteArticleById() {
        articleRepository.deleteById(lastArticleInsertedId);

        Iterable<Article> articles = articleRepository.findAll();
        assertThat(articles).hasSize(1);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void shouldNotDeleteArticleById() {
        articleRepository.deleteById(notFoundId);
    }

}

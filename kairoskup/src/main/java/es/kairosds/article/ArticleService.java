package es.kairosds.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Optional<Article> findOne(long id) {
        return articleRepository.findById(id);
    }

    public boolean exists(long id) {
        return articleRepository.existsById(id);
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public void delete(long id) {
        articleRepository.deleteById(id);
    }
}

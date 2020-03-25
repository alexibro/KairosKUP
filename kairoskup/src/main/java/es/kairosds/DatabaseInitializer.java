package es.kairosds;

import es.kairosds.article.Article;
import es.kairosds.article.ArticleRepository;
import es.kairosds.comment.Comment;
import es.kairosds.comment.CommentRepository;
import es.kairosds.user.User;
import es.kairosds.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    private String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
            "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";

    @PostConstruct
    public void init() {

        User user = new User("user", "pass", "USER");
        userRepository.save(user);

        Comment comment1 = new Comment("Manu", "Comentario cargado en la base de datos");

        Article article1 = new Article("Alejandro", "AleQS", "Cargando la base de datos",
                "Primer articulo de la base de datos", loremIpsum);

        article1.addComment(comment1);
        articleRepository.save(article1);

    }

}

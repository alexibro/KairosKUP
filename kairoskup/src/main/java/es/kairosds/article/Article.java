package es.kairosds.article;

import es.kairosds.comment.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String authorName;
    private String authorNick;
    private String title;
    private String summary;
    private String text;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Article() {
        comments = new ArrayList<>();
    }

    public Article(String authorName, String authorNick, String title, String summary, String text) {
        this();
        this.authorName = authorName;
        this.authorNick = authorNick;
        this.title = title;
        this.summary = summary;
        this.text = text;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void updateComments(Comment oldComment, Comment updatedComment) {
        this.comments.remove(oldComment);
        this.comments.add(updatedComment);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorNick() {
        return authorNick;
    }

    public void setAuthorNick(String authorNick) {
        this.authorNick = authorNick;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

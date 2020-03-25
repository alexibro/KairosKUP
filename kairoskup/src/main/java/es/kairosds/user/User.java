package es.kairosds.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @JsonIgnore
    private String passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public User() {
        this.roles = new ArrayList<>();
        this.roles.add("USER");
    }

    public User(String name, String password, String... roles) {
        this.name = name;
        this.passwordHash = new BCryptPasswordEncoder().encode(password);
        this.roles = new ArrayList<>(Arrays.asList(roles));
        this.roles.add("USER");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}

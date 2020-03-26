package es.kairosds.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(1)
public class RestSecurityConfiguration extends WebSecurityConfigurerAdapter {
   
    @Autowired
    protected UserRepositoryAuthProvider userAuthentication;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.antMatcher("/**");

		// User
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/login").permitAll();

        // Articles
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/articles").authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/articles/{id}").authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/articles/{id}").authenticated();

        // Comments
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/articles/{articleId}/comments/{id}").authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/articles/{articleId}/comments/{id}").authenticated();

        // Other URLs can be accessed without authentication
        http.authorizeRequests().anyRequest().permitAll();

        // Do not redirect when logout
		http.logout().logoutSuccessHandler((rq, rs, a) -> { });
		
		// Use HTTP basic authentication
		http.httpBasic();

        // Disable CSRF
        http.csrf().disable();

        http.logout().logoutUrl("/logout");
        
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthentication);
    }
    
}

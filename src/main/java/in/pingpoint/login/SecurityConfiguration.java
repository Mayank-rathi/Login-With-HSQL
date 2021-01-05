package in.pingpoint.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username,password,enabled "
                        + "from users "
                        + "where username = ?")
                .authoritiesByUsernameQuery("select username,authority "
                        + "from authorities "
                        + "where username = ?");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("ADMIN", "USER")
                .antMatchers("/").permitAll()
                .and().formLogin();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}


    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //This will give any one to use the web page
                .antMatchers("/admin").hasRole("ADMIN")
                //By using .hasAnyRole it will aces to use any detail as admin
                .antMatchers("/user").hasAnyRole("ADMIN","USER")
                .antMatchers("/").permitAll()
                .and()
                .formLogin();
        //To Store the data in memory we use .inMemoryAuthentication()
        auth.inMemoryAuthentication()
                .withUser("mayank")
                .password("mayank")
                .roles("USER")
                //By using and we can add multi user or new additional functionality
                .and()
                .withUser("foo")
                .password("foo")
                .roles("ADMIN");

        @Bean
        //To Encoding password(hashing) we use this method
        //For following NoOpPasswordEncoder it can not encode any password
        public PasswordEncoder getPasswordEncoder() {
            return NoOpPasswordEncoder.getInstance();
        }


     spring.datasource.url=jdbc:h2:file:C:/temp/test
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
springspring.datasource.password=""
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

     */



package org.origami.zull.security;

import org.origami.zull.conf.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MD5PasswordEncoder();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable().authorizeRequests()

                .antMatchers("/authenticate").permitAll()
                .antMatchers("/authenticateDesk").permitAll()

                .antMatchers("/servicios-docs/origami/docs/imagen/**").permitAll()


                .antMatchers("/servicios-archivos/**").permitAll()
                .antMatchers("/servicios/origami/api/documents/pdf/**").permitAll()
                .antMatchers("/servicios/origami/api/documents/grabar").permitAll()
                .antMatchers("/servicios-docs/origami/docs/documentoFirmado/guardar").permitAll() //PARA QUE SE GUARDEN DESDE LA APP DE ESCRITORIO

                .antMatchers("/servicios/origami/api/recuperar/usuario").permitAll()
                .antMatchers("/servicios/origami/api/menu/findAll").permitAll()
                .antMatchers("/servicios/origami/api/usuario/find").permitAll()
                .antMatchers("/servicios/origami/api/update/password/user").permitAll()
                .antMatchers("/servicios/origami/api/create/aclLogin").permitAll()
                .antMatchers("/servicios/origami/api/valor/code/**").permitAll()
                .antMatchers("/servicios-logs/**").permitAll()
                .anyRequest().authenticated().and()
                .httpBasic().realmName("OrigamiGT").and().requestCache().requestCache(new NullRequestCache());
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

}

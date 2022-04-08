package com.finalproject.user;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
  @Autowired
  private MySQLUser mySQLUserDetailsService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(mySQLUserDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors()
      .and()
      .csrf().disable()
      .authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/register").permitAll()
      .antMatchers(HttpMethod.GET, "/api/user", "/api/post").permitAll()
//      .antMatchers(HttpMethod.GET, "/api/post").permitAll()
      .anyRequest().authenticated()
      .and()
      .addFilter(new JWTAuthenFilter(authenticationManager()))
      .addFilter(new JWTAuthorizeFilter(authenticationManager()))
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.applyPermitDefaultValues();
    corsConfig.setExposedHeaders(Arrays.asList("Authorization"));
    source.registerCorsConfiguration("/**", corsConfig);
    return source;
  }
  
//  @Configuration
//  public class SecurityConfig extends WebSecurityConfigurerAdapter {
//	  @Override
//	  protected void configure(final HttpSecurity http) throws Exception {
//		  http.csrf().disable().antMatcher("/api/user/logout").logout();
//		  
//	  }
//  }
 
}
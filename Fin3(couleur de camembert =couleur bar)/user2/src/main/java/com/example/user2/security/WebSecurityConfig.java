/*package com.bezkoder.spring.jwt.mongodb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import AuthEntryPointJwt;
import AuthTokenFilter;
import UserDetailsServiceImpl;

@Configuration
//@EnableWebSecurity
@EnableMethodSecurity
//(securedEnabled = true,
//jsr250Enabled = true,
//prePostEnabled = true) // by default
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

//@Override
//public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//  authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//}

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

//@Bean
//@Override
//public AuthenticationManager authenticationManagerBean() throws Exception {
//  return super.authenticationManagerBean();
//}

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

//@Override
//protected void configure(HttpSecurity http) throws Exception {
//  http.cors().and().csrf().disable()
//    .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//    .authorizeRequests().antMatchers("/api/auth/**").permitAll()
//    .antMatchers("/api/test/**").permitAll()
//    .anyRequest().authenticated();
//
//  http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//}

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll().requestMatchers("/api/test/**")
            .permitAll().anyRequest().authenticated());

    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
*/

package com.example.user2.security;


import com.example.user2.security.jwt.AuthEntryPointJwt;
import com.example.user2.security.jwt.AuthTokenFilter;
import com.example.user2.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
/*La classe WebSecurityConfig est utilisée pour configurer la sécurité Spring dans une application utilisant JWT (JSON Web Tokens). Cette classe configure plusieurs aspects de la sécurité, y compris la gestion de l'authentification et de l'autorisation, ainsi que la configuration des filtres de sécurité. Voici une explication ligne par ligne :

 */

/*@Configuration : Indique que cette classe est une classe de configuration Spring.
@EnableWebSecurity : Active la sécurité web Spring.
@EnableMethodSecurity : Active la sécurité au niveau des méthodes.*/
public class WebSecurityConfig {

  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;
/*Injection de UserDetailsServiceImpl pour charger les détails de l'utilisateur.
Injection de AuthEntryPointJwt pour gérer les exceptions d'authentification.
*/
  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }
  /*Déclare un bean AuthTokenFilter pour filtrer les requêtes HTTP et vérifier les JWT.*/
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }


  /*Déclare un bean DaoAuthenticationProvider qui utilise UserDetailsServiceImpl pour charger les détails de l'utilisateur et BCryptPasswordEncoder pour encoder les mots de passe*/
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }
/*Déclare un bean AuthenticationManager en utilisant la configuration d'authentification.
 */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  /*Déclare un bean PasswordEncoder qui utilise BCryptPasswordEncoder pour encoder les mots de passe.
   */

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())/*Cross-Site Request Forgery*/
        /*Désactive la protection CSRF, car l'application utilise des JWT pour l'authentification, ce qui n'est pas vulnérable aux attaques CSRF de la même manière que les sessions basées sur les cookies*/

            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))/*Configure un point d'entrée d'authentification personnalisé (AuthEntryPointJwt) qui renvoie une réponse 401 (Unauthorized) en cas d'erreur d'authentification.
     */
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))/*Définit la politique de gestion des sessions sur STATELESS, ce qui signifie que l'application n'utilisera pas de session pour stocker l'état de l'utilisateur. Chaque requête sera traitée de manière indépendante et l'état sera entièrement géré par les JWT.
     */
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/test/**").permitAll()
                    .requestMatchers("/user2/**").permitAll()
                    .requestMatchers("/user2/**").permitAll()
                    .requestMatchers("/user2/**").permitAll()
/*Permet à toutes les requêtes vers /api/auth/**, /api/test/** et /user2/** d'être accessibles sans authentification.*/
                    .anyRequest().authenticated());

    http.authenticationProvider(authenticationProvider());
 /*Ajoute le DaoAuthenticationProvider à la configuration de sécurité, permettant l'authentification des utilisateurs en utilisant le service de détails utilisateur (UserDetailsServiceImpl) et l'encodeur de mots de passe (BCryptPasswordEncoder).
  */
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
/*Ajoute le filtre JWT (AuthTokenFilter) avant le filtre UsernamePasswordAuthenticationFilter, afin de valider le JWT pour chaque requête et de configurer le contexte de sécurité si le JWT est valide.
 */
    return http.build();

  /*Finalise la construction de la chaîne de filtres de sécurité et la retourne.
   */
  }
  /*Configure la chaîne de filtres de sécurité :
Désactive CSRF.
Configure le gestionnaire d'exception pour utiliser AuthEntryPointJwt pour gérer les erreurs d'authentification.*/
}


/*CSRF (Cross-Site Request Forgery) est une attaque de sécurité web où un attaquant trompe un utilisateur authentifié pour qu'il exécute une action non souhaitée sur une application web où il est déjà authentifié.*/
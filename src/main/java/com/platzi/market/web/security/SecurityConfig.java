package com.platzi.market.web.security;

import com.platzi.market.domain.service.PlatziUserDetailsService;
import com.platzi.market.web.security.filter.JWTFilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private PlatziUserDetailsService platziUserDetailsService;

    @Autowired
    private JWTFilterRequest jwtFilterRequest;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(platziUserDetailsService);
    }
    /*Indicar que queremos autorizar todas las peticiones que se hagan authenticate
    * lo que hace el método es:
    * suprimir lo que tiene por defecto
    * csrf().disable(): deshabilitar las peticiones cruzadas
    * authorizeRequest(): autorice las peticiones.
    * antMathers: incluir en el método que es lo que quiero permitir_todo lo que termine en authenticate
    * anyRquest()authenticated(): pero las peticiones que no cumplan con el patrón necesitan la autenticación
    *  */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/**/authenticate").permitAll()
                .anyRequest().authenticated().and().sessionManagement(). //usar el filtro sin tener en cuenta la sesión,
                sessionCreationPolicy(SessionCreationPolicy.STATELESS); // por que los jwt van a manejar la petición.


        http.addFilterBefore(jwtFilterRequest, UsernamePasswordAuthenticationFilter.class);
    }
    /*Incluir el authenticationManagerBean
    * super. para que sea Spring quien siga realizando la autenticación*/
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

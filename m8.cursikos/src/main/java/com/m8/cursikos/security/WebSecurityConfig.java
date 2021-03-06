package com.m8.cursikos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Con esta clase manejaremos la seguridad de la aplicacion
 * @author Nerffren
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	//eximir a este tipo de archivos para la seguridad
	String [] archivosValidos =  new String[]{ "/include/**","/css/**","/icons/**","/img/**","/js/**","/layer/**","/layer/**", "/templates/**"
			, "/html/**", "/webjars/**", "/h2-console/**", "/jsp/**"};
	
		@Autowired
		UserDetailsServiceImpl userDetailsService;
		
		@Bean
		public BCryptPasswordEncoder passwordEncode() {
			return new BCryptPasswordEncoder();
		}
		
		/**
		 * Comprueba la contraseña del usuario al hacer n
		 */
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncode());
		}
		
		/**
		 * Gestiona la seguridad http, en este caso el formLogin el logout y los permisos
		 * tanto para usuarios y tipos de usuarios, como para el tipo de archivos a los que
		 * la aplicacion puede acceder
		 */
	 	@Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	        	//Con este primer parametro indicamos que en las siguientes lineas configuraremos las request
	            .authorizeRequests()
	            //permitimos acceder a los archivos a todos
		        .antMatchers(archivosValidos ).permitAll()
		        .antMatchers("/","/usuario/*").permitAll()//Marcamos las Url a las que se puede entrar sin registro
		        .antMatchers("/admin*").access("hasRole('ADMIN')")//Marcamos que dentro de las rutras que empiece por admin solo podra entrar admin
		        .antMatchers("/usuario*").access("hasRole('USER') or hasRole('ADMIN')")//lo mismo pero para usuario correitne y admin
	                .anyRequest().authenticated()//Lo que no se contemple en las propiedades anteriores seran redirigidos a la pagina de login
	                .and()
	            .formLogin()
	            	.loginPage("/formLogin")
	            	.loginProcessingUrl("/login")
	                .permitAll()
	                .defaultSuccessUrl("/")
	                .failureUrl("/error?error=true")
	                .usernameParameter("username")
	                .passwordParameter("password")
	                .and()
	                .csrf().ignoringAntMatchers("/h2-console/**")
	        		.and()
	        		.headers().frameOptions().sameOrigin()
	        		.and()
//	                .loginPage("/formLogin")
//	                .permitAll()
//	                .defaultSuccessUrl("/")
//	                .loginProcessingUrl("/login")
//	                .failureUrl("/login?error=true")
//	                .usernameParameter("username")
//	                .passwordParameter("password")
//	                .and()
//	                .csrf().ignoringAntMatchers("/h2-console/**")
//            		.and()
//            		.headers().frameOptions().sameOrigin()
//	        		.and()
	            .logout()
	                .logoutUrl("/logout")
	                .logoutSuccessUrl("/");
            		
	        
	    }
}

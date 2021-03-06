package com.m8.cursikos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;

import com.m8.cursikos.entities.Usuario;
import com.m8.cursikos.repositorios.UsuarioRepositorio;

/**
 * Esta clase se encargara de gestionar las sessiones de los usuarios
 * @author Nerffren
 *
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService{
	/**
	 * Autocableamos el repositorio de Usuario  
	 */
	@Autowired
	UsuarioRepositorio repositorio;
	
	/**
	 * Este metodo se encargara de comprobar si el usuario se encuantra en la aplicacion
	 * y en el caso de que lo este le creara la session
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = repositorio.findByUsername(username);
		
		UserBuilder builder = null;
		
		if (usuario != null) {
			builder = User.withUsername(username);
			builder.disabled(false);
			builder.password(usuario.getEncPassword());
			builder.authorities(new SimpleGrantedAuthority("ROLE_USER"));
		} else {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
		
		return builder.build();
	}
}

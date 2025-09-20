package com.smartContacts.smartContactsManeger.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration // Marks this as a configuration class
public class SecurityConfig {

	@Autowired
	private UserDetailsServiceImp userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> auth
				
//	                .requestMatchers("/login", "/register").permitAll() // public
				.requestMatchers("/css/**", "/js/**", "/img/**").permitAll() // allow static resources

				.requestMatchers("/login", "/register").permitAll() // public
				.requestMatchers("/createOrder").permitAll() 

				.requestMatchers("/admin/**").hasAuthority("ADMIN") // only ADMIN
				.requestMatchers("/user/**").hasAuthority("USER") // USER

				.anyRequest().permitAll() // everything else requires login
		).formLogin(form -> form.loginPage("/login").loginProcessingUrl("/do_login") // Spring handles this
				.usernameParameter("email")      // <--- match form input name
                .passwordParameter("password")   // <--- match form input name
				.defaultSuccessUrl("/user/userDashboard", true) // redirect here after login
				.failureUrl("/login?error=true").permitAll())
				.logout(logout -> logout
					    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // now GET works
						.logoutSuccessUrl("/login?logout=true").permitAll());

		return http.build();

	}

}

package com.restapi.app.restapiapp;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.restapi.app.exception.CustomAccessDenied;
import com.restapi.app.filters.JwtAuthenticationFilter;
import com.restapi.app.utils.CustomAuthentication;
import com.restapi.app.utils.CustomDetails;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class RestApiAppConfig extends WebSecurityConfigurerAdapter {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	DataSource dataSource;
	@Autowired
	CustomDetails customerUserDetails;
	@Autowired
	private CustomAuthentication authEntryPoint;
	@Autowired
	private CustomAccessDenied customAccessDenied;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(customerUserDetails).passwordEncoder(passwordEncoder());

	}

	/*
	 * @Autowired private JwtAuthenticationEntryPoint unauthorizedHandler;
	 */
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
		return new JwtAuthenticationFilter();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()

				/* .antMatchers("/getCourses","/addCourse").hasRole("ADMIN") */
				.antMatchers("/admin/*","/student/*").permitAll().anyRequest().authenticated().and().exceptionHandling()
				.accessDeniedHandler(customAccessDenied)
				.authenticationEntryPoint(authEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
			

	}

}

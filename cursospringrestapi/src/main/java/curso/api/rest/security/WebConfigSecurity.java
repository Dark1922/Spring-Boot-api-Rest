package curso.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import curso.api.rest.service.ImplementacaoUserDetailsService;

//Mapea URLs , endereços, autoriza ou bloquea acessos a urls
@Configuration
@EnableWebSecurity //partde de segurança
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	
    @Override //configura as solicitações de acessos http
    protected void configure(HttpSecurity http) throws Exception {

    	//Ativando a proteção contra usuários que não estão validados por token
    	http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    	
    	//ativando a restrição / permissão para acesso a página inicial do sistema 
    	.disable().authorizeRequests().antMatchers("/").permitAll()
    	
    	//permite todos usuarios na index também
    	.antMatchers("/index").permitAll()
    	
    	//get consultar leitura put post usando a api delete etc varios uso da api para o user
    	.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
    	
    	//URL De lougout - Redirecionar após o user deslogar do sistema
    	.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
    	
    	//Mapeia url de logout e invalida o usuário
    	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
    	
    	//Filtrar requisições de login para autenticação
        .and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
        		UsernamePasswordAuthenticationFilter.class)   	
    	
    	//filtrar demais requisições para verificar a presenção do token jwt no headerr http
    	.addFilterBefore(new JwtApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);	
    }	
	
	
	
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	
		//service que ira consultar o usuário no banco de dados
		auth.userDetailsService(implementacaoUserDetailsService)
		
		//padrão de codificação de senha criptografia
		.passwordEncoder(new BCryptPasswordEncoder());
	}
}

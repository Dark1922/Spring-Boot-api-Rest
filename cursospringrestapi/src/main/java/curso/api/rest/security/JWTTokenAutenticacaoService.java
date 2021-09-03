package curso.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import curso.api.rest.ApplicationContextLoad;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {

	//tempo de validade do token coloquei o tempo de 1 dia em milisegundos
	 private static final long EXPIRATION_TIME = 86400000;
	 
	 //uma senha única para compor a autenticação e ajudar na segurança
	 private static final String SECRET = "*SenhaExtremamenteSecreta";
	 
	 //prefixo padrão de token
	 private static final String TOKEN_PREFIXO = "Bearer";
	
	 
	 private static final String HEADER_STRING = "Authorization";
	 
	 //gerando token de autenticação e adicionando ao cabeçalho e resposta http
	 public void addAuthentication(HttpServletResponse response,
			 String username)  throws IOException{
		 
		 //Montagem do token
		 
		//usuario que está sendo recebido por param e sua data de expiração
		 //pegando a data atual de agora mais o tempo de expiração
	 String JWT = Jwts.builder()//chama o gerenciador de token
	 .setSubject(username) //adiciona o usuario que ta fzd o login
	 .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //tempo de expiração
	.signWith(SignatureAlgorithm.HS512, SECRET).compact();  //compactação e algoritimo  e geração de senha 
		
	 //junta o token com o prefixo
	 String token = TOKEN_PREFIXO + " " + JWT; // bearer 1239e12e0-wqr0sadsadqweo12e12 exemplo
	 
	 //adiciona  no cabeçalho http
	 response.addHeader(HEADER_STRING, token); //Authorization: bearer 1239e12e0-wqr0sadsadqweo12e12
	 
	 //Escreve Token como resposta no no corpo http
	 response.getWriter().write("{\"Authorization\": \""+token+"\"}");
	 
	 }
	 //Retona o usuário validado com token ou caso não seja valido retorna null
	 public Authentication getAuthentication(HttpServletRequest request) {
		 
		 //pega o token enviado no cabeçalho http
		 String token = request.getHeader(HEADER_STRING);
		 
		 if(token != null ){
			 //faz a validação do token do usuário na requisição
			 
			 String tokenLimpo = token.replace(TOKEN_PREFIXO, "").trim();
			 
			// bearer 1239e12e0-wqr0sadsadqweo12e12 token está assim
			 String user = Jwts.parser().setSigningKey(SECRET) 
					 
					 //vem só o token sem o bearer / 1239e12e0-wqr0sadsadqweo12e12 so a numeração
					 .parseClaimsJws(tokenLimpo.replace(TOKEN_PREFIXO, ""))
					 
					 //discompacta tudo retorna só o usuario
					 .getBody().getSubject(); //"joao silva
			 
			 if(user != null) {
				 //pega os dados em memoria pega o usuariorepository validando o login
				 Usuario usuario = ApplicationContextLoad.getApplicationContext()
						 .getBean(UsuarioRepository.class).findByLogin(user);
				 
				 if(usuario != null) { //usuario é diferente de null
					 
					 if(tokenLimpo.equalsIgnoreCase(usuario.getToken())) {
						 //se o token for igual ao que está cadastrado no banco vai passar
					 
					 
					 return new UsernamePasswordAuthenticationToken(
							 usuario.getLogin(),
							 usuario.getPassword(),
							 usuario.getAuthorities());	
					 }
			      }
			 }
			 
		  }
		 
			 return null; //não autorizado
	 }
}

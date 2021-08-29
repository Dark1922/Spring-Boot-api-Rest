package curso.api.rest.security;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
			 String username) throws Exception{
		 
		 //Montagem do token
		 
		//usuario que está sendo recebido por param e sua data de expiração
		 //pegando a data atual de agora mais o tempo de expiração
		 //compactação e algoritimo e geração de senha 
		 String JWT = Jwts.builder().setSubject(username)
				 .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				 .signWith(SignatureAlgorithm.ES512, SECRET).compact(); 
		 
	 }
}

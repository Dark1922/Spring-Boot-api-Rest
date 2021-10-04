package curso.api.rest.service;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class ServiceEnviaEmail {
	
	private String userName = "recuperaapispring@gmail.com";
	private String senha = "94230955mv2";

	public void enviarEmail(String assunto, String emailDestino, String mensagem) throws Exception {
		
/* Propriedades para enviar para o gmail para apartir do meu email , enviar o email d
 * e recuperar senha para email do usuario*/ 
		Properties properties = new Properties();
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true"); /*Autorização*/
		properties.put("mail.smtp.starttls", "true");/*Autenticação*/
		properties.put("mail.smtp.host", "smtp.gmail.com");/*Servidor google gmail*/
		properties.put("mail.smtp.port", "465");/*porta servidor*/
		properties.put("mail.smtp.socketFactory.port", "465");/*Expecificar porta socket*/
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");/*Classe de conexão socket*/
		
		/*E-mail que vai ser ultilizado pra fazer a autenticaçao e mandar o conteudo pros usuarios*/
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, senha);
			}
			
		});
		  /*E-mail pra quem vai ser enviado*/
	  	 Address[] toUser = InternetAddress.parse(emailDestino);
	  	 
	  	 Message message = new MimeMessage(session);
	  	 message.setFrom(new InternetAddress(userName));/*Quem está enviando - Nós*/
	  	 message.setRecipients(Message.RecipientType.TO, toUser);/*Para quem vai o email- quem ira receber*/
	  	 message.setSubject(assunto);/*Assunto do e-mail*/
	  	 message.setText(mensagem); /*Conteudo*/
	  	 
	  	 Transport.send(message);/*Transporte de enviar mensagem do javax mail*/
  	}
	 
}

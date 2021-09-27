package curso.api.rest.service;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;

import javax.servlet.ServletContext;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class ServiceRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/*Método que dá uma rebosta de byte array , mostra o pdf direto*/
	public byte[] gerarRelatorio(String nomeRelatorio, ServletContext servletContext) throws Exception {
		
		/*Obter conexão com banco de dados*/
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		
		/*Carregar o caminho do arquivo Jasper nome por parametro e extenção do arquivo que é o jasper*/
		String caminhoJasper = servletContext.getRealPath("relatorios")
				+File.separator + nomeRelatorio + ".jasper";
		
		/*Gerar o Relatorio com os dados e conexão*/
		JasperPrint print = JasperFillManager.fillReport(caminhoJasper,
				new HashedMap(), connection);
		
		/*Exporta para byte Array o Pdf para fazer o dowload*/
		byte[] retorno = JasperExportManager.exportReportToPdf(print);
		
		connection.close(); //fecha a conexão para evitar algum problema com a conexão
		
		return retorno; //ai pega e retorna certinho o nosso pdf e fecha  a conexão q foi usada pelo relatorio
	}
}

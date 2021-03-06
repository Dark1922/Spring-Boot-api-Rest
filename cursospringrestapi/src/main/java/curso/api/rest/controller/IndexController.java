package curso.api.rest.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

import curso.api.rest.model.UserChart;
import curso.api.rest.model.Usuario;
import curso.api.rest.model.UsuarioDTO;
import curso.api.rest.model.userReport;
import curso.api.rest.repository.TelefoneRepository;
import curso.api.rest.repository.UsuarioRepository;
import curso.api.rest.service.ImplementacaoUserDetailsService;
import curso.api.rest.service.ServiceRelatorio;
import lombok.AllArgsConstructor;

@Controller // arquitetura rest
@RequestMapping("/usuario")
@AllArgsConstructor
@CrossOrigin //qlq requisição sera permitido / acessar
public class IndexController {

	// se fosse cdi @Inject
	
	private UsuarioRepository usuarioRepository;
	
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	private TelefoneRepository telefoneRepository;
	
	private ServiceRelatorio serviceRelatorio;
	
	private JdbcTemplate jdbcTemplate;
	

	// serviços restfull  Métodos Buscar por Todos
	
	@GetMapping()
	@CacheEvict(value="listausers" ,allEntries = true )  
	@CachePut("listausers")
	public ResponseEntity<Page<Usuario>> listarTodos() throws InterruptedException {

		//paginação de 5 em 5 ordenado por nome
		PageRequest page = PageRequest.of(0, 5, Sort.by("nome"));
		
		//find all retorna uma implementação de página ai passamos nosso page configurado
		Page<Usuario> list = usuarioRepository.findAll(page);
		
         //vai retorna uma página configurada do tipo usuários na lista
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
		
	} 
	
	@GetMapping("/page/{pagina}")
	@CacheEvict(value="listausersPage" ,allEntries = true )  
	@CachePut("listausersPage")
	public ResponseEntity<Page<Usuario>> usuarioPagina(@PathVariable int pagina) throws InterruptedException {

		//paginação de 5 em 5 ordenado por nome, passa a página como parametro que recebe as posição da pagina
		//de acordo com oque agente clica
		PageRequest page = PageRequest.of(pagina, 5, Sort.by("nome"));
		
		//find all retorna uma implementação de página ai passamos nosso page configurado
		Page<Usuario> list = usuarioRepository.findAll(page);
		
         //vai retorna uma página configurada do tipo usuários na lista
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
		
	}

	/*               Métodos de Get buscar por id             */
	
	@GetMapping(value = "/{id}", produces = "application/json")
	@CacheEvict(value="buscarusers" ,allEntries = true )  
	@CachePut("buscarusers")
	public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {

		// vai retorna a pessoa se der certo retorna um status ok , se n encontrar dá um
		// notofund
		return usuarioRepository.findById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}


		
	@GetMapping(value = "oi/{id}")
	public ResponseEntity<UsuarioDTO> poridAlex(@PathVariable Long id) {

		// retorna optional
		Optional<Usuario> usuario = usuarioRepository.findById(id);

		// o retorno seria um relatorio
		return new ResponseEntity<UsuarioDTO>(new UsuarioDTO(usuario.get()), HttpStatus.OK);
	}
	
	
	
	
	/*               Métodos de Post criação de Usuários             */

	@PostMapping()
	public ResponseEntity<Usuario> cadastrar(@Valid @RequestBody Usuario usuario) throws Exception {

		for(int pos = 0; pos < usuario.getTelefones().size(); pos++) {
     	   usuario.getTelefones().get(pos).setUsuario(usuario); 
        }//varrendo no get do telefone aonde está setando o usuario que 
        //veio com parametro pai ,size é o tamanho
		
		
		/*consumindo api publica externa 
		URL url = new URL("https://viacep.com.br/ws/"+usuario.getCep()+"/json/");
		URLConnection connection = url.openConnection(); //abre a conexão
		InputStream is = connection.getInputStream(); //vem os dados da requisição do cep passado
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8")); //prepara a leitura
		
		String cep = "";
		StringBuilder jsonCep = new StringBuilder();
		
		//se tem linha passa os dados para várivel cep
		while((cep = br.readLine()) != null) { //faz a leitura dos dados que vem do cep
			jsonCep.append(cep); //juntar / unir os dados do cep
		}
		
		//vai pegar a string e converte pra json logradouro etc dentro dos atributo do Usuario.class
		Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);
		
		usuario.setCep(userAux.getCep());
		usuario.setLogradouro(userAux.getLogradouro());
		usuario.setComplemento(userAux.getComplemento());
		usuario.setBairro(userAux.getBairro());
		usuario.setLocalidade(userAux.getLocalidade());
		usuario.setUf(userAux.getUf());
		*/
		
		//criptografia de senha
		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getPassword());
		usuario.setPassword(senhaCriptografada);
		 
		Usuario Usuariosalvo = usuarioRepository.save(usuario);
		
		implementacaoUserDetailsService.insereAcessoPadrão(Usuariosalvo.getId());

		return new ResponseEntity<Usuario>(Usuariosalvo, HttpStatus.CREATED);

	}
	
	
	
	/*               Métodos de Put Atualização de Usuários       */
	
	@PutMapping("/{id}")
	public ResponseEntity<Usuario>atualizar(@Valid @PathVariable Long id,
			@RequestBody Usuario usuario) throws Exception {
		
		if(!usuarioRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		
		usuario.setId(id); //força o usuario a ser atualizado na api
		
		for(int pos = 0; pos < usuario.getTelefones().size(); pos++) {
	     	   usuario.getTelefones().get(pos).setUsuario(usuario); 
	        }
		
		//comparando a senha do usuario pelo id que é um elemento que n pode ser mudado
		Usuario userTemporario = usuarioRepository.findById(usuario.getId()).get();
		
		//senhas diferente
		if(!userTemporario.getPassword().equals(usuario.getPassword())) {
			//se for diferente vai criptografar a senha nova do usuario
			String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getPassword());
			usuario.setPassword(senhaCriptografada);
		}
		
		
		// consumindo api publica externa 
				URL url = new URL("https://viacep.com.br/ws/"+usuario.getCep()+"/json/");
				URLConnection connection = url.openConnection(); //abre a conexão
				InputStream is = connection.getInputStream(); //vem os dados da requisição do cep passado
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8")); //prepara a leitura
				
				String cep = "";
				StringBuilder jsonCep = new StringBuilder();
				
				//se tem linha passa os dados para várivel cep
				while((cep = br.readLine()) != null) { //faz a leitura dos dados que vem do cep
					jsonCep.append(cep); //juntar / unir os dados do cep
				}
				
				//vai pegar a string e converte pra json logradouro etc dentro dos atributo do Usuario.class
				Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);
				
				usuario.setCep(userAux.getCep());
				usuario.setLogradouro(userAux.getLogradouro());
				usuario.setComplemento(userAux.getComplemento());
				usuario.setBairro(userAux.getBairro());
				usuario.setLocalidade(userAux.getLocalidade());
				usuario.setUf(userAux.getUf());

		Usuario UsuarioAtualizar= usuarioRepository.save(usuario);

		return 	ResponseEntity.ok(UsuarioAtualizar);
		//ou return new ResponseEntity<Usuario>(Usuariosalvo, HttpStatus.OK);
 

	}
	
	
	/*               Métodos de Deletar os usuarios       */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover (@PathVariable Long id) {
		
		if(!usuarioRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		
		usuarioRepository.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}
	
	/* END-POINT de consultar de usuário por nome */
	@GetMapping("/buscarPorNome/{nome}")
	@CacheEvict(value="listanome" ,allEntries = true )  
	@CachePut("listanome")
	public ResponseEntity<Page<Usuario>> buscarPorNome(@PathVariable("nome") String nome) throws InterruptedException {
		
		PageRequest pageRequest  = null;
		Page<Usuario> list = null;
				
		/* não informou o nome e deixou vazio o campo de pesquisa, continua na paginação*/
		if(nome == null || (nome != null && nome.trim().isEmpty())
			|| nome.equalsIgnoreCase("undefined")) {
			
			pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
			 list = usuarioRepository.findAll(pageRequest);
		
			/*Informou o nome e faz o método de consulta por paginação*/
		}else {
			pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
			list = usuarioRepository.findUserByNamePage(nome, pageRequest);
			
		}		
		//método de consulta sem paginação
		//List<Usuario> list = usuarioRepository.findByNome(nome.trim().toUpperCase());
		
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	/* END-POINT de consultar de usuário por nome */
	@GetMapping("/buscarPorNome/{nome}/page/{page}")
	@CacheEvict(value="listanome" ,allEntries = true )  
	@CachePut("listanome")
	public ResponseEntity<Page<Usuario>> buscarPorNomePage(@PathVariable("nome") String nome,
			@PathVariable("page") int page) throws InterruptedException {
			
		
		PageRequest pageRequest  = null;
		Page<Usuario> list = null;
				
		/* não informou o nome e deixou vazio o campo de pesquisa, continua na paginação*/
		if(nome == null || (nome != null && nome.trim().isEmpty())
			|| nome.equalsIgnoreCase("undefined")) {
			
			pageRequest = PageRequest.of(page, 5, Sort.by("nome"));
			 list = usuarioRepository.findAll(pageRequest);
		
			/*Informou o nome e faz o método de consulta por paginação*/
		}else {
			pageRequest = PageRequest.of(page, 5, Sort.by("nome"));
			list = usuarioRepository.findUserByNamePage(nome, pageRequest);
			
		}		
		//método de consulta sem paginação
		//List<Usuario> list = usuarioRepository.findByNome(nome.trim().toUpperCase());
		
		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}
	
	/* END-POINT de deletar telefones por id */
	@DeleteMapping("/removerTelefone/{id}")
	public ResponseEntity<Void> deleteTelefone(@PathVariable Long id) {
		
		if(!telefoneRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		
		telefoneRepository.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}
	
	/*Endpoint Relatório - dowload*/
	@GetMapping("/relatorio") //obter um relatório
	public ResponseEntity<String> dowloadRelatorio(HttpServletRequest request) throws Exception {
		
		/*nome dinâmico do relatorio que queremos , getServletContext pra carregar onde ele está contexto*/
		byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-usuario", new HashMap(),
				request.getServletContext());
		
		/*base 63 que fica pronta para ser impressa e processadaem qlq lugar*/
		String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);
		
		return new ResponseEntity<String>(base64Pdf,HttpStatus.OK);
		
	}
	
	@PostMapping("/relatorio/") //envio de dados
	public ResponseEntity<String> dowloadRelatorioParam(HttpServletRequest 
			request, @RequestBody userReport userReport) throws Exception {
		/*userReport e os dados que vai vir da tela data inicio e fim*/
		
		/*Formato que está vindo da tela */
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		/* converter para param pra buscar por parametro no relatório*/
		SimpleDateFormat dataFormatParam = new  SimpleDateFormat("yyyy-MM-dd");
		
		/*Faz a formatação da data*/
		String dataInicio = dataFormatParam.format(dateFormat.parse(userReport.getDataInicio()));
		
		String dataFim = dataFormatParam.format(dateFormat.parse(userReport.getDataFim()));
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		/*parametros do relátorio passando as data que veio pelo usuário*/
		params.put("DATA_INICIO", dataInicio);
		params.put("DATA_FIM", dataFim);
		
		/*nome dinâmico do relatorio que queremos , getServletContext pra carregar onde ele está contexto*/
		byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-usuario-param", params,
				request.getServletContext());
		
		/*base 63 que fica pronta para ser impressa e processadaem qlq lugar*/
		String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);
		
		return new ResponseEntity<String>(base64Pdf,HttpStatus.OK);
		
	}
	
	@GetMapping("/grafico")
	public ResponseEntity<UserChart> grafico () {
		UserChart userChart = new UserChart();
		
		/*Retorna duas lista 1 lista dos nome 2 lista com os salários */
    List<String> resultado = jdbcTemplate.queryForList("select array_agg(nome) from usuario where salario >"
			+ " 0 and nome <> '' union all select cast(array_agg(salario) as character varying[]) from usuario "
			+ " where salario > 0 and nome <> ''", String.class);
		
		if(!resultado.isEmpty()) {
			/*vai remover as chaves por vázios pq temos que ter uma array n um objeto na posição 0*/
			String nome = resultado.get(0).replaceAll("\\{", "").replaceAll("\\}", "");
			String salario = resultado.get(1).replaceAll("\\{", "").replaceAll("\\}", "");
			
			userChart.setNome(nome);
			userChart.setSalario(salario);
			
		}
				
		return new ResponseEntity<UserChart>(userChart, HttpStatus.OK);
	}
	
}

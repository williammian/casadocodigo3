package br.com.casadocodigo3.loja.controllers;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.env.Environment;
import org.springframework.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.casadocodigo3.loja.daos.ProdutoDAO;
import br.com.casadocodigo3.loja.infra.prod.FileSaver;
import br.com.casadocodigo3.loja.models.Produto;
import br.com.casadocodigo3.loja.models.TipoPreco;
import br.com.casadocodigo3.loja.validation.ProdutoValidation;

@Controller
@RequestMapping("/produtos")
public class ProdutosController {

	@Autowired
	private ProdutoDAO produtoDao;
	
	@Autowired
	private FileSaver fileSaver;
	
	@Autowired 
	private FileSaver fileSaverProd;
	
	@Autowired
	private Environment environment;

	private String path;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new ProdutoValidation());
	}
	
	@RequestMapping("form")
	public ModelAndView form(Produto produto) {
		ModelAndView modelAndView = new ModelAndView("produtos/form");
		modelAndView.addObject("tipos", TipoPreco.values());
		
		return modelAndView;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@CacheEvict(value="produtosHome",allEntries=true)
	public ModelAndView gravar(MultipartFile sumario, @Valid Produto produto, BindingResult result, 
			RedirectAttributes redirectAttributes) {
		
		if (result.hasErrors()) {
			return form(produto);
		}
		
		if (sumario != null && !sumario.getOriginalFilename().isEmpty()) {
			if(Arrays.stream(environment.getActiveProfiles()).anyMatch(
					   env -> (env.equalsIgnoreCase("dev")) )) {
				path = fileSaver.writeDev(sumario);				
			}
			else{
				path= fileSaverProd.writeProd(sumario);				
			}
			produto.setSumarioPath(path);
		}
		
		produtoDao.gravar(produto);
		
		redirectAttributes.addFlashAttribute("sucesso", "Produto cadastrado com sucesso!");
		return new ModelAndView("redirect:produtos");
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView listar() {
		List<Produto> produtos = produtoDao.listar();
		ModelAndView modelAndView = new ModelAndView("produtos/lista");
		modelAndView.addObject("produtos", produtos);
		
		return modelAndView;
	}
	
	@RequestMapping("/detalhe/{id}")
	public ModelAndView detalhe(@PathVariable("id") Integer id) {

		ModelAndView modelAndView = new ModelAndView("produtos/detalhe");
		Produto produto = produtoDao.find(id);
		modelAndView.addObject("produto", produto);
		if(Arrays.stream(environment.getActiveProfiles()).anyMatch(
				   env -> (env.equalsIgnoreCase("dev")) )) {
			modelAndView.addObject("ambiente", "dev");
		}else{
			modelAndView.addObject("ambiente", "prod");
		}
		return modelAndView;
	}
	
}










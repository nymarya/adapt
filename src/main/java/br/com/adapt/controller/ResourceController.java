package br.com.adapt.controller;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.adapt.model.Tag;
import br.com.adapt.model.Task;
import br.com.adapt.model.Resource;
import br.com.adapt.model.User;
import br.com.adapt.service.ResourceService;
import br.com.adapt.service.UserService;



/**
 * @author mayra
 *
 */

@Controller
public abstract class ResourceController<T> {

	
	private static final String MSG_SUCESS_INSERT = "Tarefa cadastrada com sucesso.";
	private static final String MSG_SUCESS_UPDATE = "Tarefa atualizada com sucesso.";
	private static final String MSG_SUCESS_DELETE = "Tarefa removida com sucesso.";
	private static final String MSG_ERROR = "Error.";

	@Autowired
	private UserService userService;
	
	private ResourceService resourceService;
	
	@GetMapping("/tasks")
	public String index(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmailAdress(auth.getName());
		List<Resource> all = user.getScheduler().getTasks();
		model.addAttribute("tasks", all);
        return "tasks/index";
    }
	
	@GetMapping("/tasks/create")
	public String taskCreate(Model m) {
		m.addAttribute("endDate", LocalTime.NOON);
		m.addAttribute("startDate", LocalTime.NOON);
        return "tasks/create";
    }
	
	/**
	 * Método para salvar um novo recurso no BD 
	 * @param entityTask Entidade com infos a ser salvas
	 */
	@PostMapping("/task/save")
	public abstract String store( @Valid @ModelAttribute T entityTask,BindingResult result, RedirectAttributes redirectAttributes);

	
	/**
	 * Método para atualizar recurso no BD 
	 * @param entityTask Entidade com infos a ser salvas
	 */
	@RequestMapping(value = "/tasks/{id}", method = RequestMethod.POST)
	public abstract String update(@Valid @ModelAttribute T entity, BindingResult result, @PathVariable("id") Integer id,RedirectAttributes redirectAttributes);
	
	
	
	
	
	@GetMapping("tasks/{id}/edit")
	public String update(Model model, @PathVariable("id") Integer id) {
		
		try {
			if (id != null) {
				
				Resource entityTask = resourceService.findById(id);
				model.addAttribute("task", entityTask);
				
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		return "tasks/edit";
	}

	
	@GetMapping("tasks/{id}")
	public String show(Model model, @PathVariable("id") Integer id) {
		
		try {
			if (id != null) {
				
				Resource entity = resourceService.findById(id);
				model.addAttribute("task", entity);
				
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		return "tasks/show";
	}
	
	@PostMapping("tasks/{id}/delete")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			if (id != null) {
				Resource entity = resourceService.findById(id);
				resourceService.delete(entity);
				redirectAttributes.addFlashAttribute("success", MSG_SUCESS_DELETE);
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", MSG_ERROR);
			throw new ServiceException(e.getMessage());
		}
		return "redirect:/tasks";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(LocalTime.class, new CustomDateEditor(new SimpleDateFormat("H:m"), true));
	}
}
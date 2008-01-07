package tutorial.spring25.ui.springmvc;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tutorial.spring25.dao.PersonDao;

@Controller
public class PersonController {
	
	private PersonDao personDao;
	
	@Autowired
	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	@RequestMapping("/personlist")
	public ModelMap list(){
		return new ModelMap(personDao.getAll());
	}
	
	@RequestMapping("/persondetail")
	public ModelMap detail(@RequestParam("person_id") Long personId){
		return new ModelMap(personDao.getById(personId));
	}
	
	@RequestMapping("/personuploadresult")
	public ModelMap uploadResult(final HttpSession session){
		final ModelMap modelMap = new ModelMap();

		if (session.getAttribute(Constants.PERSONUPLOAD_SUCCESS_KEY) != null) {

			modelMap.addAttribute(session
					.getAttribute(Constants.PERSONUPLOAD_SUCCESS_KEY));

			session.removeAttribute(Constants.PERSONUPLOAD_SUCCESS_KEY);
		}
		
		if (session.getAttribute(Constants.PERSONUPLOAD_ERRORS_KEY) != null) {

			modelMap.addAttribute(session
					.getAttribute(Constants.PERSONUPLOAD_ERRORS_KEY));

			session.removeAttribute(Constants.PERSONUPLOAD_ERRORS_KEY);
		}
		return modelMap;
	}
}

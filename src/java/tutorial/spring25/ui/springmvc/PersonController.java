package tutorial.spring25.ui.springmvc;

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
}

package tutorial.spring25.ui.springmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import tutorial.spring25.dao.PersonDao;
import tutorial.spring25.model.Person;
import tutorial.spring25.validator.PersonValidator;

@Controller
@RequestMapping("/personform")
public class PersonFormController {
	private static final String REDIRECT_PERSONLIST = "redirect:personlist";
	private PersonDao personDao;

	@Autowired
	public void setPersonDao(final PersonDao personDao) {
		this.personDao = personDao;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelMap displayForm(@RequestParam(value = "person_id", required = false) Long id) {
		Person person = personDao.getById(id);

		if (person == null)	person = new Person();

		return new ModelMap(person);
	}

	@RequestMapping(method = RequestMethod.POST)
	public String processForm(@ModelAttribute("person") Person person, BindingResult result, SessionStatus status) {
		new PersonValidator().validate(person, result);
		if (result.hasErrors()) {
			return "personform";
		} else {
			personDao.save(person);
			status.setComplete();
			return REDIRECT_PERSONLIST;
		}
	}
}

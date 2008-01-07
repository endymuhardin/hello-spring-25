package tutorial.spring25.ui.springmvc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import tutorial.spring25.common.ParseError;
import tutorial.spring25.dao.PersonDao;
import tutorial.spring25.helper.PersonCSVParser;
import tutorial.spring25.model.Person;

@Controller
@RequestMapping("/personuploadform")
public class PersonUploadController {
	private static final Log LOG = LogFactory.getLog(PersonUploadController.class);
	private static final String REDIRECT_PERSONUPLOADRESULT = "redirect:personuploadresult";
	private PersonCSVParser personDataParser;
	private PersonDao personDao;

	@Autowired(required=true)
	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}


	@Autowired(required=true)
	public void setPersonDataParser(PersonCSVParser personDataParser) {
		this.personDataParser = personDataParser;
	}

		
	@RequestMapping(method=RequestMethod.GET)
	public ModelMap displayForm(){
		return new ModelMap();
	}
	
	@RequestMapping(method=RequestMethod.POST) 
	public String processForm(@RequestParam("persondata") MultipartFile file, final HttpSession session) {
		LOG.debug("processing uploaded file");
		if(file == null) {
			LOG.debug("No file uploaded");
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("File uploaded : "+file.getName());
			}
			
			// parse file into list of strings
			List<String> contents = new ArrayList<String>();
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
				String content = reader.readLine();
				while(content != null) {
					LOG.debug(content);
					
					if("".equals(content)) {
						content = reader.readLine();
						continue;
					}
					contents.add(content);
					content = reader.readLine();
				}
				reader.close();
			} catch (IOException e) {
				LOG.warn(e.getMessage(), e);
			}
			
			
			// parse list of strings into list of Persons
			List<Person> persons = new ArrayList<Person>();
			List<ParseError> errors = new ArrayList<ParseError>();
			personDataParser.parse(contents, persons, errors);
			
			for (Person person : persons) {
				personDao.save(person);
			}
			
			session.setAttribute(Constants.PERSONUPLOAD_ERRORS_KEY, errors);
			session.setAttribute(Constants.PERSONUPLOAD_SUCCESS_KEY, persons);
		}
		return REDIRECT_PERSONUPLOADRESULT;
	}
}

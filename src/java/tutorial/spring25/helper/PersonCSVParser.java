package tutorial.spring25.helper;

import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;

import tutorial.spring25.common.ParseError;
import tutorial.spring25.model.Person;
import tutorial.spring25.validator.PersonValidator;

@Component
public class PersonCSVParser {
	private static final int PERSON_NUM_FIELDS = 2;
	private String delimiter = ",";
	private MessageSource messageSource;

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	@Autowired(required=true)
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * parse strings of data into list of Person.
	 * Check for : <ul>
	 * <li>malformed data</li>
	 * </ul>
	 * 
	 * @param data list of string to be parsed
	 * @param result list of person generated from data
	 * @param errors bad data information
	 * */
	@SuppressWarnings("unchecked")
	public void parse(List<String> data, List<Person> result, List<ParseError> errors) {
		int counter = 0;
		
		for(String text : data){
			counter++;
			StringTokenizer tokenizer = new StringTokenizer(text, delimiter);
			
			// check for malformed data
			if(tokenizer.countTokens() != PERSON_NUM_FIELDS) {
				errors.add(new ParseError(counter, text, messageSource.getMessage("parseerror.reason.malformed", new Object[]{2,tokenizer.countTokens()}, Locale.getDefault())));
				continue;
			}
			
			final Person person = new Person();
			person.setName(tokenizer.nextToken());
			person.setEmail(tokenizer.nextToken());
			
			// check using validator
			final PersonValidator personValidator = new PersonValidator();
			final BeanPropertyBindingResult bindErrors = new BeanPropertyBindingResult(person, "person");
			personValidator.validate(person, bindErrors);
			
			if(bindErrors.hasErrors()) {
				List<ObjectError> bindErrorContent = bindErrors.getAllErrors();
				for (ObjectError objectError : bindErrorContent) {
					errors.add(new ParseError(counter, text, messageSource.getMessage(objectError.getCode(), objectError.getArguments(), Locale.getDefault())));
				}
				continue;
			}
			
			result.add(person);
		}
	}
}

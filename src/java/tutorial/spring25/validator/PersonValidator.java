package tutorial.spring25.validator;


import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import tutorial.spring25.model.Person;

public class PersonValidator {
	private static final String FIELD_EMAIL = "email";
	private static final String FIELD_NAME = "name";
	private static final String PERSON_EMAIL_MALFORMED_KEY = "person.email.malformed";
	private static final String DEFAULT_ERROR_MESSAGE = "DEFAULT ERROR MESSAGE, PLEASE OVERRIDE";
	private static final String PERSON_NAME_REQUIRED_KEY = "person.name.required";
	private static final String EMAIL_FORMAT = ".*@.*\\.com";

	public void validate(Person person, Errors errors) {
		// field nama harus diisi
		if(!StringUtils.hasText(person.getName())) {
			errors.rejectValue(FIELD_NAME, PERSON_NAME_REQUIRED_KEY, DEFAULT_ERROR_MESSAGE);
		}

		// bila field email diisi, formatnya harus benar
		if (StringUtils.hasLength(person.getEmail()) && !person.getEmail().matches(EMAIL_FORMAT) ) {
			errors.rejectValue(FIELD_EMAIL, PERSON_EMAIL_MALFORMED_KEY, DEFAULT_ERROR_MESSAGE);
		}
	}
}

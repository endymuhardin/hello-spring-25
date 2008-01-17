package test.spring25.helper;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import tutorial.spring25.common.ParseError;
import tutorial.spring25.helper.PersonCSVParser;
import tutorial.spring25.model.Person;


public class PersonCSVParserTest {

	private static ApplicationContext applicationContext;


	@BeforeClass public static void init() {
		applicationContext = new FileSystemXmlApplicationContext(new String[]{"src/config/applicationContext.xml", "webapp/WEB-INF/tutorial-servlet.xml"});
	}
	
	@Test
	public void testParseNormal() throws Exception {
		final List<String> data = new ArrayList<String>();
		final List<Person> result = new ArrayList<Person>();
		final List<ParseError> errors = new ArrayList<ParseError>();
		
		final BufferedReader reader = new BufferedReader(new FileReader("fixtures/person.csv"));
		String content = reader.readLine();
		while(content != null) {			
			data.add(content);
			content = reader.readLine();
		}
		
		assertEquals(3, data.size());
		
		PersonCSVParser productDataParserCSV = (PersonCSVParser) applicationContext.getBean("personCSVParser");
		productDataParserCSV.parse(data, result, errors);
		
		assertEquals(3, result.size());
		assertEquals(0, errors.size());
	}
	
	@Test
	public void testParseWithMalformedEmails() throws Exception {
		final List<String> data = new ArrayList<String>();
		final List<Person> result = new ArrayList<Person>();
		final List<ParseError> errors = new ArrayList<ParseError>();
		
		final BufferedReader reader = new BufferedReader(new FileReader("fixtures/person-with-malformed-emails.csv"));
		String content = reader.readLine();
		while(content != null) {
			data.add(content);
			content = reader.readLine();
		}
		
		assertEquals(3, data.size());
		
		PersonCSVParser productDataParserCSV = (PersonCSVParser) applicationContext.getBean("personCSVParser");
		productDataParserCSV.parse(data, result, errors);
		
		assertEquals(1, result.size());
		assertEquals(2, errors.size());
	}
}

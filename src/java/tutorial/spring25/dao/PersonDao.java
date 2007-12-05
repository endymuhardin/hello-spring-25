package tutorial.spring25.dao;

import java.util.List;

import tutorial.spring25.model.Person;

public interface PersonDao {
	
	/**
	 * saves Person object into database. 
	 * If object is already exists (denoted by not-null ID field), 
	 * the existing record with the corresponding ID is updated. 
	 * If the object is new (denoted by null ID field), 
	 * new record is inserted.
	 * 
	 *  This method also set the ID field for new record.
	 * */
	public void save(Person person);
	
	/**
	 * fetch all person object in database.
	 * @return List of all person
	 * */
	public List<Person> getAll();
	
	/**
	 * fetch Person object with the speficied ID. 
	 * @param id identifier for person object
	 * @return Person object if there is record found for the speficied id, null otherwise
	 * */
	public Person getById(Long id);
}

package tutorial.spring25.dao.springjdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tutorial.spring25.dao.PersonDao;
import tutorial.spring25.model.Person;

@Repository("personDao")
@Transactional(readOnly = true)
public class PersonDaoSpringJdbc implements PersonDao {

	private static final Log LOG = LogFactory.getLog(PersonDaoSpringJdbc.class);

	private SimpleJdbcTemplate simpleJdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	private static final String SQL_GET_ALL = "select * from T_PERSON";
	private static final String SQL_GET_BY_ID = "select * from T_PERSON where id=?";
	private static final String SQL_UPDATE = "update T_PERSON set name=?, email=? where id=?";

	private static final class PersonMapper implements
			ParameterizedRowMapper<Person> {

		@Override
		public Person mapRow(final ResultSet rs, final int rowNum)
				throws SQLException {
			final Person result = new Person();
			result.setId(rs.getLong("id"));
			result.setName(rs.getString("name"));
			result.setEmail(rs.getString("email"));
			return result;
		}
	}

	@Autowired
	public void setDataSource(final DataSource dataSource) {
		this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
		this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("T_PERSON").usingGeneratedKeyColumns("id");
	}

	@Override
	public List<Person> getAll() {
		return simpleJdbcTemplate.query(SQL_GET_ALL, new PersonMapper(),
				new HashMap<String, String>());
	}

	@Override
	public Person getById(final Long id) {
		try {
			if(id == null) return null;
			return simpleJdbcTemplate.queryForObject(SQL_GET_BY_ID,
					new PersonMapper(), id);
		} catch (Exception e) {
			LOG.warn(e.getMessage(), e);
			return null;
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void save(final Person person) {
		if (person.getId() != null) {
			simpleJdbcTemplate.getJdbcOperations().update(SQL_UPDATE,
					new Object[] { person.getName(), person.getEmail(), person.getId()});
		} else {
			final SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(person);
			person.setId(simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue());
		}
	}
}

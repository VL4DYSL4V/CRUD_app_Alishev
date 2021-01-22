package dao.mapper;

import entity.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet resultSet, int i) throws SQLException {
        int personId = resultSet.getInt("id");
        String personName = resultSet.getString("person_name");
        String email = resultSet.getString("email");
        int age  = resultSet.getInt("age");
        return new Person(personId, personName, age, email);
    }
}

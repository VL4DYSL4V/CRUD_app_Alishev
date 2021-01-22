package dao;


import dao.mapper.PersonMapper;
import entity.Person;
import exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class SpringJdbcDAO implements PeopleDAO{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SpringJdbcDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Person getById(int id) throws FetchException {
        return jdbcTemplate.query("SELECT * FROM alishev_people WHERE id = ?",
                new Object[]{id}, new PersonMapper()).stream().findAny().orElseThrow(FetchException::new);
    }

    @Override
    public Collection<Person> getAllPeople() {
        return jdbcTemplate.query("SELECT * FROM alishev_people;",
                new PersonMapper());
    }

    @Override
    public void save(Person person) throws SaveException {
        jdbcTemplate.update("INSERT INTO alishev_people(id, person_name, email, age) VALUES(?, ?, ?, ?);",
                generateId(), person.getName(), person.getEmail(), person.getAge());
    }

    @Override
    public void update(int id, Person person) throws UpdateException {
        jdbcTemplate.update("UPDATE alishev_people SET person_name = ?, email = ?, age = ? WHERE id = ?; ",
                person.getName(), person.getEmail(), person.getAge(), id);
    }

    @Override
    public void delete(int id) throws DeleteException {
        jdbcTemplate.update("DELETE FROM alishev_people WHERE id = ?", id);
    }

    private int generateId(){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int out = random.nextInt();
        Collection<Integer> existingIds = jdbcTemplate.query("SELECT id FROM alishev_people", (resultSet, i) -> resultSet.getInt("id"));
        while(existingIds.contains(out)) {
            out = random.nextInt();
        }
        return out;
    }
}

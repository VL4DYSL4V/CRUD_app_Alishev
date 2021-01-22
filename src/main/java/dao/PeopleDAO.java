package dao;

import entity.Person;
import exception.DeleteException;
import exception.FetchException;
import exception.SaveException;
import exception.UpdateException;

import java.util.Collection;

public interface PeopleDAO {

    Person getById(int id) throws FetchException;

    Collection<Person> getAllPeople() throws FetchException;

    void save(Person person) throws SaveException;

    void update(int id, Person person) throws UpdateException;

    void delete(int id) throws DeleteException;

}

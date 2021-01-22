package dao;

import entity.Person;
import exception.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import service.ConnectionService;

import javax.annotation.Nullable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Scope(value = "singleton")
public class SqlPeopleDAO implements PeopleDAO {

    private final ConnectionService connectionService;

    public SqlPeopleDAO(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public Person getById(int id) throws FetchException{
        Person out = null;
        String sql = "SELECT id, person_name, email, age FROM alishev_people WHERE id = ?";
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int personId = resultSet.getInt("id");
                    String personName = resultSet.getString("person_name");
                    String email = resultSet.getString("email");
                    int age  = resultSet.getInt("age");
                    out = new Person(personId, personName, age, email);
                }
            }
        } catch (ConnectionCannotBeEstablishedException | SQLException e) {
            throw new FetchException(e);
        }
        return out;
    }

    @Override
    @Nullable
    public Collection<Person> getAllPeople() throws FetchException {
        Collection<Person> out = new LinkedList<>();
        String sql = "SELECT id, person_name, email, age FROM alishev_people";
        try (Connection connection = connectionService.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    int personId = resultSet.getInt("id");
                    String personName = resultSet.getString("person_name");
                    String email = resultSet.getString("email");
                    int age  = resultSet.getInt("age");
                    out.add(new Person(personId, personName, age, email));
                }
            }
        } catch (ConnectionCannotBeEstablishedException | SQLException e) {
            throw new FetchException(e);
        }
        return out;
    }

    @Override
    public void save(Person person) throws SaveException {
        String sql = "INSERT INTO alishev_people(id, person_name, email, age) VALUES (?, ?, ?, ?);";
        try (Connection connection = connectionService.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {
            int id = generateId();
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, person.getName());
            preparedStatement.setString(3, person.getEmail());
            preparedStatement.setInt(4, person.getAge());
            preparedStatement.execute();
        }catch (ConnectionCannotBeEstablishedException | SQLException ex){
            throw new SaveException(ex);
        }
    }

    @Override
    public void update(int id, Person person) throws UpdateException {
        String sql = "UPDATE alishev_people SET person_name = ?, email = ?, age = ? WHERE id = ?;";
        try (Connection connection = connectionService.getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, person.getName());
            preparedStatement.setString(2, person.getEmail());
            preparedStatement.setInt(3, person.getAge());
            preparedStatement.setInt(4, id);
            preparedStatement.execute();
        }catch (SQLException | ConnectionCannotBeEstablishedException ex){
            throw new UpdateException(ex);
        }
    }

    @Override
    public void delete(int id) throws DeleteException {
        StringBuilder sqlBuilder =
                new StringBuilder("DELETE FROM alishev_people ")
                        .append("WHERE id = ").append(id);
        try (Connection connection = connectionService.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlBuilder.toString());
        }catch (SQLException | ConnectionCannotBeEstablishedException ex){
            throw new DeleteException(ex);
        }
    }

    private int generateId() throws ConnectionCannotBeEstablishedException, SQLException {
        Collection<Integer> existingIds = new HashSet<>();
        String sql = "SELECT id FROM alishev_people";
        try(Connection connection = connectionService.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){
            while(resultSet.next()){
                existingIds.add(resultSet.getInt("id"));
            }
        }
        int out = 0;
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        while(existingIds.contains(out)){
            out = threadLocalRandom.nextInt();
        }
        return out;
    }

    private void addTestRecordsToDB() throws ConnectionCannotBeEstablishedException, SQLException {
        Collection<Person> people = new ArrayList<>();
        people.add(new Person(1, "Alex", 25, "Alex@gmail.com"));
        people.add(new Person(2, "Stepan", 19, "Stepan@gmail.com"));
        people.add(new Person(3, "Petr", 18, "Petr@gmail.com"));
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO alishev_people(id, person_name) VALUES");
        people.forEach(person -> {
            sqlBuilder.append("(").append(person.getId())
                    .append(",'").append(person.getName())
                    .append("')").append(",");
        });
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        try (Connection connection = connectionService.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlBuilder.toString());
        }
    }

    private void setupDB() throws ConnectionCannotBeEstablishedException, SQLException {
        String sql = "CREATE TABLE alishev_people(" +
                "id INT NOT NULL PRIMARY KEY, " +
                "person_name VARCHAR(30) NOT NULL), " +
                "age INT," +
                "email VARCHAR (255);";
        try (Connection connection = connectionService.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }
}

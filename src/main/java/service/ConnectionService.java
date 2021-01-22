package service;

import exception.ConnectionCannotBeEstablishedException;

import java.sql.Connection;

public interface ConnectionService {

    Connection getConnection() throws ConnectionCannotBeEstablishedException;
}

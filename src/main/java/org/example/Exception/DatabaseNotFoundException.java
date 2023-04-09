package org.example.Exception;

public class DatabaseNotFoundException extends Exception{


    public DatabaseNotFoundException() {

    }

    @Override
    public String toString() {
        return "Database Not Found!";
    }

}

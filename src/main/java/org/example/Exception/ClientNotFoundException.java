package org.example.Exception;

public class ClientNotFoundException extends Exception{

    public ClientNotFoundException() {

    }

    @Override
    public String toString() {
        return "Client Not Found!";
    }

}

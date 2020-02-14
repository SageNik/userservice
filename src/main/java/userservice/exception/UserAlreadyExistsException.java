package userservice.exception;

public class UserAlreadyExistsException extends Exception{
    public UserAlreadyExistsException() {
        super("Sorry, user with that username already exists");
    }
}

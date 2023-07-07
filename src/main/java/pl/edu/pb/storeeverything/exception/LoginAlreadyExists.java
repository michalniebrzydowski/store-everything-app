package pl.edu.pb.storeeverything.exception;

public class LoginAlreadyExists extends RuntimeException {

    public LoginAlreadyExists(String message) {
        super(message);
    }
}

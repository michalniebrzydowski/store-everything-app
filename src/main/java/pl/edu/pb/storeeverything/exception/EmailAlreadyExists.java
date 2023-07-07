package pl.edu.pb.storeeverything.exception;

public class EmailAlreadyExists extends RuntimeException {

    public EmailAlreadyExists(String message) {
        super(message);
    }
}

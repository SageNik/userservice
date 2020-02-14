package userservice.exception;

public class VerificationCodeExpiredException extends Exception {

    public VerificationCodeExpiredException() {
        super("Sorry, verification code is expired");
    }
}

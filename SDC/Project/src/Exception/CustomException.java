package Exception;

// This is a custom exception class that extends the built-in Exception class.
public class CustomException extends Exception
{
    // Constructor for CustomException class, which takes a message parameter.
    public CustomException(String message){

        // This sets the error message for the exception.
        super(message);
    }
}

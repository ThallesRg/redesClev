package aplication.exception;

public class FileFormatException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileFormatException(String msg){
        super(msg);
    }

}

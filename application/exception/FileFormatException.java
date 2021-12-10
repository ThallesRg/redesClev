package application.exception;

/**
* A classe FileFormatException é responsável por lançar uma exceção personalizada caso o arquivo esteja incorreto
* 
* @author Luiz Felipe Triques Moraes
* @author Renata Rona Garib
* @author Thalles Raphael Guimarães
* 
*/
public class FileFormatException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    /**
    * Construtor responsável por receber uma mensagem de erro e repassar para superclasse RuntimeException
    * @param msg variável responsável por receber a mensagem
    */
	public FileFormatException(String msg){
        super(msg);
    }
}

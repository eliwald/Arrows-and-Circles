package backend;

import java.lang.Exception;

@SuppressWarnings("serial")
public class InvalidDFSMException extends Exception {

	public InvalidDFSMException(String message) {
		super(message);
	}
}
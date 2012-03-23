package backend;

import java.lang.Exception;

public class InvalidDeterministicFSMException extends Exception {
	public InvalidDeterministicFSMException(String message) {
		super(message);
	}
}
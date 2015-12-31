/**
 * 
 */
package net.dougsale.chicagotrafficcameras.repository;

/**
 * @author dsale
 *
 */
public class RepositoryException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public RepositoryException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RepositoryException(String message, Throwable cause) {
		super(message, cause);
	}
}

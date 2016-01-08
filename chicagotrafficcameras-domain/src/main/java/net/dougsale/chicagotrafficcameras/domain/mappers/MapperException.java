/**
 * 
 */
package net.dougsale.chicagotrafficcameras.domain.mappers;

/**
 * @author dsale
 *
 */
public class MapperException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public MapperException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MapperException(String message, Throwable cause) {
		super(message, cause);
	}
}

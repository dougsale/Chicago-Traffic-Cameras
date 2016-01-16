/**
 * 
 */
package net.dougsale.chicagotrafficcameras.repository;

import net.dougsale.chicagotrafficcameras.ErrorCode;
import net.dougsale.chicagotrafficcameras.SystemException;

/**
 * @author dsale
 *
 */
public class RepositoryException extends SystemException {

	private static final long serialVersionUID = -5954062474338855705L;

	/**
	 * @param message
	 */
	public RepositoryException(ErrorCode errCode) {
		super(errCode);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RepositoryException(ErrorCode errCode, Throwable cause) {
		super(errCode, cause);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	@Override
	public RepositoryException withContext(String key, Object value) {
		super.withContext(key, value);
		return this;
	}
}

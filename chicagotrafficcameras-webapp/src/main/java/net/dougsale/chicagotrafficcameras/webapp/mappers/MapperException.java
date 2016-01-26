/**
 * 
 */
package net.dougsale.chicagotrafficcameras.webapp.mappers;

import net.dougsale.chicagotrafficcameras.ErrorCode;
import net.dougsale.chicagotrafficcameras.SystemException;

/**
 * @author dsale
 *
 */
public class MapperException extends SystemException {

	private static final long serialVersionUID = -4817171628928690341L;

	/**
	 * @param message
	 */
	public MapperException(ErrorCode errCode) {
		super(errCode);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MapperException(ErrorCode errCode, Throwable cause) {
		super(errCode, cause);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	@Override
	public MapperException withContext(String key, Object value) {
		super.withContext(key, value);
		return this;
	}
}


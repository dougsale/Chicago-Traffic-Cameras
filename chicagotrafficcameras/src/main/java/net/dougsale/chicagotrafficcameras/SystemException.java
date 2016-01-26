/**
 * 
 */
package net.dougsale.chicagotrafficcameras;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dsale
 *
 */
public class SystemException extends Exception {

	private static final long serialVersionUID = -4152774576857609649L;
	
	private final ErrorCode errCode;
	private final Map<String,Object> contextData = new HashMap<>();
	
	/**
	 * 
	 */
	public SystemException(ErrorCode errCode) {
		notNull(errCode);
		this.errCode = errCode;
	}

	/**
	 * @param message
	 */
	public SystemException(ErrorCode errCode, String message) {
		super(message);
		notNull(errCode);
		this.errCode = errCode;
	}

	/**
	 * @param cause
	 */
	public SystemException(ErrorCode errCode, Throwable cause) {
		super(cause);
		notNull(errCode);
		this.errCode = errCode;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SystemException(ErrorCode errCode, String message, Throwable cause) {
		super(message, cause);
		notNull(errCode);
		this.errCode = errCode;
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public SystemException(ErrorCode errCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		notNull(errCode);
		this.errCode = errCode;
	}
	
	/**
	 * 
	 * @return
	 */
	public ErrorCode getErrCode() {
		return errCode;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public SystemException withContext(String key, Object value) {
		notNull(key);
		contextData.put(key, value);
		return this;
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<String, Object> getContextData() {
		return Collections.unmodifiableMap(contextData);
	}
}

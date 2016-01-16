/**
 * 
 */
package net.dougsale.chicagotrafficcameras.repository;

import net.dougsale.chicagotrafficcameras.ErrorCode;

/**
 * @author dsale
 *
 */
public enum RepositoryCode implements ErrorCode {
	UNAVAILABLE(100),
	MISSING_CLASS(101),
	INVALID_FORMAT(102),
	CLOSE_FAILED(103),
	READ_FAILED(104);

	private final int code;

	private RepositoryCode(int code) {
	    this.code = code;
	}

	@Override
	public int getCode() {
		return code;
	}

}

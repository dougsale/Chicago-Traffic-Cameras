/**
 * 
 */
package net.dougsale.chicagotrafficcameras.webapp.mappers;

import net.dougsale.chicagotrafficcameras.ErrorCode;

/**
 * @author dsale
 *
 */
public enum MapperErrorCodes implements ErrorCode {
	MALFORMED_JSON(101),
	JSON_PROCESSING(102),
	GENERIC_IO(103);

	private final int code;

	private MapperErrorCodes(int code) {
	    this.code = code;
	}

	@Override
	public int getCode() {
		return code;
	}

}

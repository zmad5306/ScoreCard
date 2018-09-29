package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;

public class ApiError implements Serializable {

	private static final long serialVersionUID = -6959597226612659891L;
	
	private Integer status;
	private String errorCode;
	private String message;
	
	public ApiError() {
		super();
	}

	public ApiError(Integer status, String errorCode, String message) {
		super();
		this.status = status;
		this.errorCode = errorCode;
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}
	
}

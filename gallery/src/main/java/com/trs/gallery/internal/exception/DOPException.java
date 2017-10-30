package com.trs.gallery.internal.exception;


public class DOPException extends Exception {

	/**
	 * @Field @serialVersionUID : TODO(这里用一句话描述这个类的作用)
	 */
	private static final long serialVersionUID = 2218600185497325486L;
	
	/**
	 * 异常消息
	 */
	private String message;
	
	
	public DOPException(String message) {
		super(message);
		this.message = message;
	}
	
	public DOPException(String message, Exception exception) {
		super(message, exception);
		this.message = message;
	}


	/**
	 * @return the 异常消息
	 */
	public String getMessage() {
		return message;
	}
	

	/**
	 * @param message the 异常消息 to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}

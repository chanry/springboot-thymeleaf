package com.trs.gallery.internal.exception;
/**
 * @Description 
 * @author guojiahua
 * @version 1.0.0
 * @creatime 2017年1月16日上午9:37:14
 */
public class NullParamException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 异常消息
	 */
	private String message;

	public NullParamException(String attributeName) {
		super(attributeName + "不能为空");
		this.message = attributeName + "不能为空";
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

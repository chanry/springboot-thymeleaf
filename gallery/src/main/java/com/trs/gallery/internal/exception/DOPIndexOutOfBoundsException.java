package com.trs.gallery.internal.exception;
/**
 * @Description 
 * @author guojiahua
 * @version 1.0.0
 * @creatime 2017年1月18日上午11:26:13
 */
public class DOPIndexOutOfBoundsException extends DOPException{

	/**
	 * @Field @serialVersionUID : TODO(这里用一句话描述这个类的作用)
	 */
	private static final long serialVersionUID = -1079373018606385468L;

	public DOPIndexOutOfBoundsException(String message) {
		super("查无此对象：" + message);
	}

	
}

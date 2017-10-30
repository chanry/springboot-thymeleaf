package com.trs.gallery.entity.custom;

/**
 * 附件信息实体
 * @author chenli
 *
 */
public class AttachmentInfo {
	/**
	 * 文档ID
	 */
	private Integer docId;
	
	/**
	 * 附件真实名
	 */
	private String appFile;
	
	/**
	 * 附件原名
	 */
	private String srcFile;
	
	/**
	 * 附件类型
	 */
	private String fileExt;
	
	/**
	 * 附件绝对地址
	 */
	private String imgUrl;
	
	
	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public String getAppFile() {
		return appFile;
	}

	public void setAppFile(String appFile) {
		this.appFile = appFile;
	}

	public String getSrcFile() {
		return srcFile;
	}

	public void setSrcFile(String srcFile) {
		this.srcFile = srcFile;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getImgUrl() {
		this.imgUrl = buildImgUrl(appFile);
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	private String buildImgUrl(String appFile) {
		StringBuilder stringBuilder = new StringBuilder();
		//stringBuilder.append("/").append(appFile.substring(2, 8)).append("/").append(appFile);
		stringBuilder.append(appFile.substring(0, 8)).append("/").append(appFile.substring(0, 10)).append("/")
				.append(appFile);
		return stringBuilder.toString();
	}
}

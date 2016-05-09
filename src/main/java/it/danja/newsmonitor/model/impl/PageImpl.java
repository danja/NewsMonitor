package it.danja.newsmonitor.model.impl;

public class PageImpl extends PageBase {

	private String url = null;
	private String content = null;

	public PageImpl(String url) {
		this.url =url;
	}

	@Override
	public void setUrl(String url) {
		this.url  = url;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void setContent(String content) {
		this.content  = content;
	}

	@Override
	public String getContent() {
		return content;
	}
}

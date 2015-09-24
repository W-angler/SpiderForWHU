package com.wangle.spider.main;

import org.htmlparser.nodes.TagNode;

/**
 * 简直醉了，HTMLParser没有iframe这个标签
 * @author wangle
 * @date 2015.09.22
 *
 */

public class IframeTag extends TagNode {
	private static final long serialVersionUID = 1L;
	private static final String[] mIds = { "IFRAME" };

	public String[] getIds() {
		return mIds;
	}

	public String getIframeLocation() {
		String ret = getAttribute("SRC");
		if (null == ret)
			ret = "";
		else if (null != getPage()) {
			ret = getPage().getAbsoluteURL(ret);
		}
		return ret;
	}

	public void setIframeLocation(String url) {
		setAttribute("SRC", url);
	}

	public String getIframeName() {
		return getAttribute("NAME");
	}

	public String toString() {
		return "IFRAME TAG : Iframe " + getIframeName() + " at "
				+ getIframeLocation() + "; begins at : " + getStartPosition()
				+ "; ends at : " + getEndPosition();
	}
}

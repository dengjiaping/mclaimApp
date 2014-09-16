package com.sinosoftyingda.fastclaim.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史赔案信息返回类
 * @author haoyun 20130225
 *
 */
public class LeaveMsgQueryClaimResponse extends CommonResponse {
	
	private List<LeastMsgQueryClaimItem> leaveMsgClaims=new ArrayList<LeastMsgQueryClaimItem>();//理赔案件信息明细

	public LeaveMsgQueryClaimResponse() {
		super();
	}

	public LeaveMsgQueryClaimResponse(List<LeastMsgQueryClaimItem> leaveMsgClaims) {
		super();
		this.leaveMsgClaims = leaveMsgClaims;
	}

	public List<LeastMsgQueryClaimItem> getLeaveMsgClaims() {
		return leaveMsgClaims;
	}

	public void setLeaveMsgClaims(List<LeastMsgQueryClaimItem> leaveMsgClaims) {
		this.leaveMsgClaims = leaveMsgClaims;
	}

}

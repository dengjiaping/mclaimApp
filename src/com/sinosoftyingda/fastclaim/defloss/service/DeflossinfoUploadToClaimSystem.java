package com.sinosoftyingda.fastclaim.defloss.service;

import android.content.Context;

import com.sinosoftyingda.fastclaim.common.db.access.DeflossSynchroAccess;
import com.sinosoftyingda.fastclaim.common.model.VerifyLossSubmitRequest;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnSumbitDefloss;
import com.sinosoftyingda.fastclaim.defloss.util.UtilIsNotNull;

/**
 * 定损信息同步或提交
 * @author DengGuang
 */
public class DeflossinfoUploadToClaimSystem {

	private Context context;
	public DeflossinfoUploadToClaimSystem(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 提交操作
	 * @param registNo
	 * @param lossNo
	 * @param topBtnSumbitDefloss
	 * @return
	 */
	public boolean submit(String registNo, String lossNo, TopBtnSumbitDefloss topBtnSumbitDefloss){
		boolean isSubmit = false;
		// 判断是否有拍照
		boolean isArrived = UtilIsNotNull.checkIsArrived();
		if(isArrived){
			UtilIsNotNull utilIsNotNull = new UtilIsNotNull();
			// 互碰自赔的案件：三者车无损提交即0提交
			boolean isOCommit = utilIsNotNull.checkOCommitRule();
			// 一般案件：标的车不可以录入BZ险下的损失项，即BZ险0赔付
			boolean isBZCommit = utilIsNotNull.checkBZCommitRule();

			if (isOCommit || isBZCommit) {
				//synchOrSubmit("submit");
				isSubmit = true;
			} else {
				JYCommitService jycommitService = new JYCommitService();
				jycommitService.onEvalFinish(context, "005", topBtnSumbitDefloss);
				isSubmit = false;
			}
		}else{
			Toast.showToast(context, "定损同步或提交必须拍摄一张照片！");
			isSubmit = false;
		}
		
		return isSubmit;
	}
	
	/**
	 * 同步操作
	 * @param registNo
	 * @param lossNo
	 * @param topBtnSumbitDefloss
	 * @return
	 */
	public boolean synch(String registNo, String lossNo, TopBtnSumbitDefloss topBtnSumbitDefloss){
		boolean isSynch = false;
		// 判断是否有拍照
		boolean isArrived = UtilIsNotNull.checkIsArrived();
		if(isArrived){
			UtilIsNotNull utilIsNotNull = new UtilIsNotNull();
			// 互碰自赔的案件：三者车无损提交即0提交
			boolean isOCommit = utilIsNotNull.checkOCommitRule();
			// 一般案件：标的车不可以录入BZ险下的损失项，即BZ险0赔付
			boolean isBZCommit = utilIsNotNull.checkBZCommitRule();
			VerifyLossSubmitRequest verifyLossSubmitRequest = DeflossSynchroAccess.find(registNo, lossNo);
			if (verifyLossSubmitRequest.getLossFitInfo().size() > 0 || verifyLossSubmitRequest.getLossRepairInfo().size() > 0) {
				if (isOCommit || isBZCommit) {
					// synchOrSubmit("synch");
					isSynch = true;
				} else {
					JYCommitService jycommitService = new JYCommitService();
					jycommitService.onEvalFinish(context, "004", topBtnSumbitDefloss);
					isSynch = false;
				}
			} else {
				// synchOrSubmit("synch");
				isSynch = true;
			}
		}else{
			isSynch = false;
			Toast.showToast(context, "定损同步或提交必须拍摄一张照片！");
		}
		
		return isSynch;
	}
}

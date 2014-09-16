package com.sinosoftyingda.fastclaim.common.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sinosoftyingda.fastclaim.common.model.AppointModel;
import com.sinosoftyingda.fastclaim.common.model.PolicyMsgInsuranceType;
import com.sinosoftyingda.fastclaim.common.model.PolicyMsgResponse;

/**
 * 保单信息DB
 * 
 * @author haoyun 20130319
 * 
 */
public class TblPolicyMsg {
	private String id;
	private String insuredName;// 被保险人
	private String drivingLicenesOwner;// 行驶证车主
	private String licenseNo;// 号牌号码
	private String labelType;// 厂牌类型
	private String vidNo;// VIN码
	private String engineNo;// 发动机号
	private String carFirstregistration;// 车辆初次登记日期
	private String useYears;// 已使用年限
	private String newCarPurchaseprice;// 新车购置价
	private String vehicleuse;// 车辆使用性质
	private String qsPolicyNo;// 交强险保单号
	private String qsPolicyInsureDate;// 交强险期限
	private String busInessPolicyNo;// 商业险保单号
	private String busInessPolicyInsureDate;// 商业险期限
	private String basicClausetypes;// 基本条款类型
	private String policyComCode;// 归属部门
	private String busInessFlag;// 业务标识
	private String policyEndOrses;// 本保单批改次数
	private List<PolicyMsgInsuranceType> policyMsgInsuranceTypes = new ArrayList<PolicyMsgInsuranceType>();// 保单投保条款

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getDrivingLicenesOwner() {
		return drivingLicenesOwner;
	}

	public void setDrivingLicenesOwner(String drivingLicenesOwner) {
		this.drivingLicenesOwner = drivingLicenesOwner;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getLabelType() {
		return labelType;
	}

	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}

	public String getVidNo() {
		return vidNo;
	}

	public void setVidNo(String vidNo) {
		this.vidNo = vidNo;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getCarFirstregistration() {
		return carFirstregistration;
	}

	public void setCarFirstregistration(String carFirstregistration) {
		this.carFirstregistration = carFirstregistration;
	}

	public String getUseYears() {
		return useYears;
	}

	public void setUseYears(String useYears) {
		this.useYears = useYears;
	}

	public String getNewCarPurchaseprice() {
		return newCarPurchaseprice;
	}

	public void setNewCarPurchaseprice(String newCarPurchaseprice) {
		this.newCarPurchaseprice = newCarPurchaseprice;
	}

	public String getVehicleuse() {
		return vehicleuse;
	}

	public void setVehicleuse(String vehicleuse) {
		this.vehicleuse = vehicleuse;
	}

	public String getQsPolicyNo() {
		return qsPolicyNo;
	}

	public void setQsPolicyNo(String qsPolicyNo) {
		this.qsPolicyNo = qsPolicyNo;
	}

	public String getQsPolicyInsureDate() {
		return qsPolicyInsureDate;
	}

	public void setQsPolicyInsureDate(String qsPolicyInsureDate) {
		this.qsPolicyInsureDate = qsPolicyInsureDate;
	}

	public String getBusInessPolicyNo() {
		return busInessPolicyNo;
	}

	public void setBusInessPolicyNo(String busInessPolicyNo) {
		this.busInessPolicyNo = busInessPolicyNo;
	}

	public String getBusInessPolicyInsureDate() {
		return busInessPolicyInsureDate;
	}

	public void setBusInessPolicyInsureDate(String busInessPolicyInsureDate) {
		this.busInessPolicyInsureDate = busInessPolicyInsureDate;
	}

	public String getBasicClausetypes() {
		return basicClausetypes;
	}

	public void setBasicClausetypes(String basicClausetypes) {
		this.basicClausetypes = basicClausetypes;
	}

	public String getPolicyComCode() {
		return policyComCode;
	}

	public void setPolicyComCode(String policyComCode) {
		this.policyComCode = policyComCode;
	}

	public String getBusInessFlag() {
		return busInessFlag;
	}

	public void setBusInessFlag(String busInessFlag) {
		this.busInessFlag = busInessFlag;
	}

	public String getPolicyEndOrses() {
		return policyEndOrses;
	}

	public void setPolicyEndOrses(String policyEndOrses) {
		this.policyEndOrses = policyEndOrses;
	}

	public List<PolicyMsgInsuranceType> getPolicyMsgInsuranceTypes() {
		return policyMsgInsuranceTypes;
	}

	public void setPolicyMsgInsuranceTypes(
			List<PolicyMsgInsuranceType> policyMsgInsuranceTypes) {
		this.policyMsgInsuranceTypes = policyMsgInsuranceTypes;
	}

	/**
	 * 新增或者更新数据
	 * 
	 * @param db
	 * @param tasks
	 */
	public static void insertOrUpdate(SQLiteDatabase db,
			PolicyMsgResponse policy, String registNo) {
		ContentValues cv = null;
		try {
			db.beginTransaction();

			cv = new ContentValues();
			cv.put("REGISTNO", registNo);
			cv.put("INSURENAME", policy.getInsuredName());
			cv.put("DRIVINGLICENSEOWNER", policy.getDrivingLicenesOwner());
			cv.put("LICENSENO", policy.getLicenseNo());
			cv.put("LABELTYPE", policy.getLabelType());
			cv.put("VINNO", policy.getVidNo());
			cv.put("ENGINENO", policy.getEngineNo());
			cv.put("CARFIRSTREGISTRATION", policy.getCarFirstregistration());
			cv.put("USEYEARS", policy.getUseYears());
			cv.put("NEWCARPURCHASEPRICE", policy.getNewCarPurchaseprice());
			cv.put("VEHICLEUSE", policy.getVehicleuse());
			cv.put("QSPOLICYNO", policy.getQsPolicyNo());
			cv.put("QSPOLICYINSUREDATE", policy.getQsPolicyInsureDate());
			cv.put("BUSINESSPOLICYNO", policy.getBusInessPolicyNo());
			cv.put("BUSINESSPOLICYINSUREDATE",
					policy.getBusInessPolicyInsureDate());
			cv.put("BASICCLAUSETYPES", policy.getBasicClausetypes());
			cv.put("POLICYCOMCODE", policy.getPolicyComCode());
			cv.put("BUSINESSFLAG", policy.getBusInessFlag());
			cv.put("POLICYENDORSES", policy.getPolicyEndOrses());

			String where = "ENGINENO=?";
			String[] whereValue = { policy.getEngineNo() };
			String policyId = "";
			if (isExist(db, where, "POLICYINFO", whereValue)) {
				int id = db.update("POLICYINFO", cv, "ENGINENO=?",
						new String[] { policy.getEngineNo() });
				policyId = id + "";
			} else {
				long id = db.insert("POLICYINFO", "id", cv);
				policyId = id + "";
			}
			for (int i = 0; i < policy.getPolicyMsgInsuranceTypes().size(); i++) {
				insertOrUpdateItemKind(db, policy.getPolicyMsgInsuranceTypes()
						.get(i), policyId, cv);
			}
			for (int i = 0; i < policy.getAppointModels().size(); i++) {
				insertOrUpdateCenGage(db, policy.getAppointModels()
						.get(i), policyId, cv);
			}

			db.setTransactionSuccessful();
		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			db.endTransaction();
			cv.clear();
			db.close();
		}
	}

	/**
	 * 新增或者更新数据
	 * 
	 * @param db
	 * @param tasks
	 */
	private static void insertOrUpdateItemKind(SQLiteDatabase db,
			PolicyMsgInsuranceType policy, String policyId, ContentValues cv) {

		cv = new ContentValues();
		cv.put("ITEMKINDCODE", policy.getItemKindCode());
		cv.put("ITEMKINDAMOUNT", policy.getItemKindAmount());
		cv.put("POLICYID", policyId);
		String where = "ITEMKINDCODE=?";
		String[] whereValue = { policy.getItemKindCode() };

		if (isExist(db, where, "ITEMKIND", whereValue)) {
			int id = db.update("ITEMKIND", cv, "ITEMKINDCODE=?",
					new String[] { policy.getItemKindCode() });
			policyId = id + "";
		} else {
			long id = db.insert("ITEMKIND", "id", cv);
			policyId = id + "";
		}

	}
	/**
	 * 新增或者更新数据
	 * 
	 * @param db
	 * @param tasks
	 */
	private static void insertOrUpdateCenGage(SQLiteDatabase db,
			AppointModel appointModel, String policyId, ContentValues cv) {

		cv = new ContentValues();
		cv.put("SERIALNO", appointModel.getSerialNo());
		cv.put("CLAUSECODE", appointModel.getClauseCode());
		cv.put("CLAUSE", appointModel.getClause());
		cv.put("CLAUSEDETAIL",appointModel.getClauseDetall());
		String where = "CLAUSECODE=?";
		String[] whereValue = { appointModel.getClauseCode() };

		if (isExist(db, where, "CENGAGE", whereValue)) {
			int id = db.update("CENGAGE", cv, "CLAUSECODE=?",
					new String[] { appointModel.getClauseCode() });
			policyId = id + "";
		} else {
			long id = db.insert("CENGAGE", "id", cv);
			policyId = id + "";
		}

	}
	/**
	 * 根据报案号判断该任务是否存在
	 * 
	 * @param db
	 * @param registno
	 * @return
	 */
	private static boolean isExist(SQLiteDatabase db, String where,
			String tableName, String[] whereValue) {
		Cursor cursor = db.query(tableName, null, where, whereValue, null,
				null, null);
		boolean flag = false;
		if (cursor.moveToNext()) {
			flag = true;
		} else {
			flag = false;
		}
		cursor.close();
		return flag;
	}

	/**
	 * 获取查勘详细信息任务
	 * 
	 * @param db
	 * @return
	 */
	public static PolicyMsgResponse getPolicyMsg(SQLiteDatabase db,
			String registNo) {
		String selection = "REGISTNO=?";
		Cursor cursor = db.query("POLICYINFO", null, selection,
				new String[] { registNo }, null, null, null);

		return getPolicyMsg(db, cursor);

	}

	private static PolicyMsgResponse getPolicyMsg(SQLiteDatabase db,
			Cursor cursor) {
		PolicyMsgResponse policy = new PolicyMsgResponse();
		try {
			String policyId = "";
			while (cursor.moveToNext()) {
				policy.setInsuredName(cursor.getString(cursor
						.getColumnIndex("INSURENAME")));
				policy.setDrivingLicenesOwner(cursor.getString(cursor
						.getColumnIndex("DRIVINGLICENSEOWNER")));
				policy.setLicenseNo(cursor.getString(cursor
						.getColumnIndex("LICENSENO")));
				policy.setLabelType(cursor.getString(cursor
						.getColumnIndex("LABELTYPE")));
				policy.setVidNo(cursor.getString(cursor.getColumnIndex("VINNO")));
				policy.setEngineNo(cursor.getString(cursor
						.getColumnIndex("ENGINENO")));
				policy.setCarFirstregistration(cursor.getString(cursor
						.getColumnIndex("CARFIRSTREGISTRATION")));
				policy.setUseYears(cursor.getString(cursor
						.getColumnIndex("USEYEARS")));
				policy.setNewCarPurchaseprice(cursor.getString(cursor
						.getColumnIndex("NEWCARPURCHASEPRICE")));
				policy.setVehicleuse(cursor.getString(cursor
						.getColumnIndex("VEHICLEUSE")));
				policy.setQsPolicyNo(cursor.getString(cursor
						.getColumnIndex("QSPOLICYNO")));
				policy.setQsPolicyInsureDate(cursor.getString(cursor
						.getColumnIndex("QSPOLICYINSUREDATE")));
				policy.setBusInessPolicyNo(cursor.getString(cursor
						.getColumnIndex("BUSINESSPOLICYNO")));
				policy.setBusInessPolicyInsureDate(cursor.getString(cursor
						.getColumnIndex("BUSINESSPOLICYINSUREDATE")));
				policy.setBasicClausetypes(cursor.getString(cursor
						.getColumnIndex("BASICCLAUSETYPES")));
				policy.setPolicyComCode(cursor.getString(cursor
						.getColumnIndex("POLICYCOMCODE")));
				policy.setBusInessFlag(cursor.getString(cursor
						.getColumnIndex("BUSINESSFLAG")));
				policy.setPolicyEndOrses(cursor.getString(cursor
						.getColumnIndex("POLICYENDORSES")));

				policyId = cursor.getString(cursor.getColumnIndex("id"));
			}
			policy.setPolicyMsgInsuranceTypes(getItemKind(db, policyId));
			policy.setAppointModels(getAppointModel(db,policyId));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return policy;
	}

	/**
	 * 获取保单投保条款
	 * 
	 * @param db
	 * @param cursor
	 * @param id
	 * @return
	 */
	private static List<PolicyMsgInsuranceType> getItemKind(SQLiteDatabase db,
			String policyId) {
		String selection = "POLICYID=?";
		Cursor cursor = db.query("ITEMKIND", null, selection,
				new String[] { policyId }, null, null, null);

		List<PolicyMsgInsuranceType> polciy = new ArrayList<PolicyMsgInsuranceType>();
		while (cursor.moveToNext()) {
			polciy.add(toItemKind(cursor));
		}
		cursor.close();
		return polciy;
	}

	private static PolicyMsgInsuranceType toItemKind(Cursor cursor) {
		PolicyMsgInsuranceType policy = new PolicyMsgInsuranceType();

		policy.setItemKindCode(cursor.getString(cursor
				.getColumnIndex("ITEMKINDCODE")));
		policy.setItemKindAmount(cursor.getString(cursor
				.getColumnIndex("ITEMKINDAMOUNT")));

		return policy;
	}
	/**
	 * 
	 * 
	 * @param db
	 * @param cursor
	 * @param id
	 * @return
	 */
	private static List<AppointModel> getAppointModel(SQLiteDatabase db,
			String policyId) {
		String selection = "POLICYID=?";
		Cursor cursor = db.query("CENGAGE", null, selection,
				new String[] { policyId }, null, null, null);

		List<AppointModel> appointModel = new ArrayList<AppointModel>();
		while (cursor.moveToNext()) {
			appointModel.add(toAppointModel(cursor));
		}
		cursor.close();
		return appointModel;
	}

	private static AppointModel toAppointModel(Cursor cursor) {
		AppointModel policy = new AppointModel();

		policy.setClause(cursor.getString(cursor.getColumnIndex("CLAUSE")));
		policy.setClauseCode(cursor.getString(cursor.getColumnIndex("CLAUSECODE")));
		policy.setClauseDetall(cursor.getString(cursor.getColumnIndex("CLAUSEDETAIL")));
		policy.setSerialNo(cursor.getString(cursor.getColumnIndex("SERIALNO")));
		
		return policy;
	}
}

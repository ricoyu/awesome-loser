package com.loserico.jackson;

import java.io.Serializable;
import java.util.Date;

public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String creator;
	private Date createTime;
	private String modifier;
	private Date modifiyTime;
	//客户类型
	private String customerType;
	//Taidii学生ID
	private String taidiiUid;
	//用友学生ID
	private String yongyouUid;
	//学生姓名
	private String customerRef;
	//所属中心ID
	private Long centreId;
	private String centre;
	//学生NRIC/Passport
	private String nricPassport;
	//出生日期
	private Date dob;
	//性别
	private String gender;
	//入学日
	private Date admDate;
	//退学日
	private Date leaveDate;
	//政府补助金额
	private Double govSubVal;
	//政府补助状态
	private String govSubStatus;
	//政府补助到期日
	private Date govSubExpDate;
	//内部补助金额
	private Double intSubVal;
	//内部补助状态
	private String intSubStatus;
	//内部补助到期日
	private Date intSubExpDate;
	//StartupGrant金额
	private Double staGraVal;
	//StartupGrant状态
	private String staGraStatus;
	//StartupGrant到期日
	private Date staGraExpDate;
	//额外补助金额
	private Double addSubVal;
	//额外补助状态
	private String addSubStatus;
	//额外补助到期日
	private Date addSubExpDate;
	//FAS金额
	private Double fasVal;
	//FAS状态
	private String fasStatus;
	//FAS到期日
	private Date fasExpDate;
	//客户所属中心银行帐号
	private String bankAccount;
	private boolean deleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaidiiUid() {
		return taidiiUid;
	}

	public void setTaidiiUid(String taidiiUid) {
		this.taidiiUid = taidiiUid;
	}

	public String getYongyouUid() {
		return yongyouUid;
	}

	public void setYongyouUid(String yongyouUid) {
		this.yongyouUid = yongyouUid;
	}

	public String getCustomerRef() {
		return customerRef;
	}

	public void setCustomerRef(String customerRef) {
		this.customerRef = customerRef;
	}

	public String getNricPassport() {
		return nricPassport;
	}

	public void setNricPassport(String nricPassport) {
		this.nricPassport = nricPassport;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getAdmDate() {
		return admDate;
	}

	public void setAdmDate(Date admDate) {
		this.admDate = admDate;
	}

	public Date getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}

	public Double getGovSubVal() {
		return govSubVal;
	}

	public void setGovSubVal(Double govSubVal) {
		this.govSubVal = govSubVal;
	}

	public String getGovSubStatus() {
		return govSubStatus;
	}

	public void setGovSubStatus(String govSubStatus) {
		this.govSubStatus = govSubStatus;
	}

	public Date getGovSubExpDate() {
		return govSubExpDate;
	}

	public void setGovSubExpDate(Date govSubExpDate) {
		this.govSubExpDate = govSubExpDate;
	}

	public Double getIntSubVal() {
		return intSubVal;
	}

	public void setIntSubVal(Double intSubVal) {
		this.intSubVal = intSubVal;
	}

	public String getIntSubStatus() {
		return intSubStatus;
	}

	public void setIntSubStatus(String intSubStatus) {
		this.intSubStatus = intSubStatus;
	}

	public Date getIntSubExpDate() {
		return intSubExpDate;
	}

	public void setIntSubExpDate(Date intSubExpDate) {
		this.intSubExpDate = intSubExpDate;
	}

	public Double getStaGraVal() {
		return staGraVal;
	}

	public void setStaGraVal(Double staGraVal) {
		this.staGraVal = staGraVal;
	}

	public String getStaGraStatus() {
		return staGraStatus;
	}

	public void setStaGraStatus(String staGraStatus) {
		this.staGraStatus = staGraStatus;
	}

	public Date getStaGraExpDate() {
		return staGraExpDate;
	}

	public void setStaGraExpDate(Date staGraExpDate) {
		this.staGraExpDate = staGraExpDate;
	}

	public Double getAddSubVal() {
		return addSubVal;
	}

	public void setAddSubVal(Double addSubVal) {
		this.addSubVal = addSubVal;
	}

	public String getAddSubStatus() {
		return addSubStatus;
	}

	public void setAddSubStatus(String addSubStatus) {
		this.addSubStatus = addSubStatus;
	}

	public Date getAddSubExpDate() {
		return addSubExpDate;
	}

	public void setAddSubExpDate(Date addSubExpDate) {
		this.addSubExpDate = addSubExpDate;
	}

	public Double getFasVal() {
		return fasVal;
	}

	public void setFasVal(Double fasVal) {
		this.fasVal = fasVal;
	}

	public String getFasStatus() {
		return fasStatus;
	}

	public void setFasStatus(String fasStatus) {
		this.fasStatus = fasStatus;
	}

	public Date getFasExpDate() {
		return fasExpDate;
	}

	public void setFasExpDate(Date fasExpDate) {
		this.fasExpDate = fasExpDate;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String lastModifier) {
		this.modifier = lastModifier;
	}

	public Date getModifyTime() {
		return modifiyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifiyTime = modifyTime;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Long getCentreId() {
		return centreId;
	}

	public void setCentreId(Long centreId) {
		this.centreId = centreId;
	}

	public String getCentre() {
		return centre;
	}

	public void setCentre(String centre) {
		this.centre = centre;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
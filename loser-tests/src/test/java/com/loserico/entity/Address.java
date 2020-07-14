package com.loserico.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.MessageFormat;

/**
 * User的专属地址，一个User可以有多个Address
 * 一个地址专属与某个人，某人可以有多个地址，删掉人的时候相应的地址也没有用了，所以要一起删除，参User的Cascade设置
 * 
 * @author Loser
 * @since Jan 28, 2016
 * @version 2.0
 *
 */
@Entity
@Table(name = "ADDRESS")
@Cacheable
public class Address extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -8316711366622255045L;
	private String country;
	private String stateOrProvince;
	private String city;
	private String postalCode;
	private String district;//区
	private String street;
	private Long userId;

	@Column(name = "COUNTRY", length = 20, nullable = false)
	@NotNull
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "STATE_OR_PROVINCE", length = 20, nullable = false)
	@NotNull
	public String getStateOrProvince() {
		return stateOrProvince;
	}

	public void setStateOrProvince(String stateOrProvince) {
		this.stateOrProvince = stateOrProvince;
	}

	@Column(name = "CITY", length = 20, nullable = false)
	@NotNull
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "POSTAL_CODE", length = 10, nullable = false)
	@NotNull
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@Column(name = "DISTRICT", length = 200, nullable = false)
	@NotNull
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Column(name = "STREET", length = 2000, nullable = false)
	@NotNull
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return MessageFormat.format("country[${0}], stateOrProvince[${1}], city[${2}], district[${3}], street[${4}]",
				country,
				stateOrProvince, city, district, street);
	}

}

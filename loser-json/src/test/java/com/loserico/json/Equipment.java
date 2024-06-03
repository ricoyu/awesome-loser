package com.loserico.json;

import lombok.Data;
import org.jongo.marshall.jackson.oid.Id;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents the equipment table in the database.
 */
@Data
public class Equipment {
	
	/**
	 * Primary key: Equipment ID.
	 */
	@Id
	private String equipmentId;
	
	/**
	 * Name of the equipment.
	 */
	private String equipmentName;
	
	/**
	 * Type ID of the equipment.
	 */
	private String equipTypeId;
	
	/**
	 * Type name of the equipment.
	 */
	private String equipTypeName;
	
	/**
	 * IP address of the equipment.
	 */
	private String ipAddress;
	
	/**
	 * Port number.
	 */
	private String port;
	
	/**
	 * Indicates whether the equipment is stopped.
	 */
	private String isStop;
	
	/**
	 * Slot number (e.g., for small vehicles: 1, photoelectric: 1, others: 0).
	 */
	private Integer slot;
	
	/**
	 * Rack number.
	 */
	private Integer rack;
	
	/**
	 * Unit ID (e.g., for hoists: 8, others: 0).
	 */
	private Integer unitId;
	
	/**
	 * Tenant ID associated with the equipment.
	 */
	private String ddTenantId;
	
	/**
	 * Date of disuse.
	 */
	private Date wuyongDate;
	
	/**
	 * Timestamp of the last update.
	 */
	private Date lastUpdatedStamp;
	
	/**
	 * ID of the user who created the record.
	 */
	private String createdUserId;
	
	/**
	 * ID of the user who last updated the record.
	 */
	private String lastUpdatedUserId;
	
	/**
	 * Name of the user who created the record.
	 */
	private String createdUserName;
	
	/**
	 * Name of the user who last updated the record.
	 */
	private String lastUpdatedUserName;
	
	/**
	 * Transaction timestamp of the last update.
	 */
	private Date lastUpdatedTxStamp;
	
	/**
	 * Timestamp when the record was created.
	 */
	private Date createdStamp;
	
	/**
	 * Transaction timestamp when the record was created.
	 */
	private Date createdTxStamp;
	
	/**
	 * Name indicating the stop status.
	 */
	private String isStopName;
	
	/**
	 * ID of the operating user.
	 */
	private String operateUserId;
	
	/**
	 * Name of the operating user.
	 */
	private String operateUserName;
	
	/**
	 * Name of the bill type.
	 */
	private String billTypeName;
	
	/**
	 * ID of the bill type.
	 */
	private String billTypeId;
	
	/**
	 * ID of the storehouse.
	 */
	private String storehouseId;
	
	/**
	 * Name of the storehouse.
	 */
	private String storehouseName;
	
	/**
	 * Quantity for depalletizer.
	 */
	private BigDecimal depalletizerQty;
	
}
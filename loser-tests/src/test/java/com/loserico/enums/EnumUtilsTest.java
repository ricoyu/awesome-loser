package com.loserico.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.loserico.common.lang.utils.EnumUtils;
import org.junit.Test;

import java.util.Arrays;

public class EnumUtilsTest {

	@Test
	public void testEnumLookup() {
		//		PaymentMode paymentMode = (PaymentMode)com.loserico.orm.utils.EnumUtils.lookupEnum(PaymentMode.class, "6");
		//PaymentMode paymentMode = (PaymentMode)com.loserico.orm.utils.EnumUtils.lookupEnum(PaymentMode.class, value);
		PaymentMode paymentMode = (PaymentMode) EnumUtils.lookupEnum(PaymentMode.class, "6", "code");
		System.out.println(paymentMode);
	}

	public static enum PaymentMode {
		OVERPAYMENT("OverPayment", "超额支付", "payment.mode.overpayment", Constants.MODE_RECEIPT) {
			@Override
			public boolean isBillPaymentType() {
				return true;
			}
		},
		SUBSIDY_OVP("SubsidyOverpayment", "补助余额", "payment.mode.subsidy.overpayment", Constants.MODE_RECEIPT),
		SUBSIDY("Subsidy", "补助", "payment.mode.subsidy", Constants.MODE_RECEIPT) {
			@Override
			public boolean isBillPaymentType() {
				return true;
			}
		},
		ADVANCE_PAYMENT("AdvancePayment", "预收款", "payment.mode.advanced.payment", Constants.MODE_RECEIPT) {
			@Override
			public boolean isBillPaymentType() {
				return true;
			}
		},
		DEPOSIT("Deposit", "预付款", "payment.mode.deposit", Constants.MODE_RECEIPT) {
			@Override
			public boolean isBillPaymentType() {
				return true;
			}
		},
		CASH("Cash", "现金", "payment.mode.cash", Constants.MODE_REFUND | Constants.MODE_WALLET),
		NETS("NETS", "NETS", "payment.mode.nets", Constants.MODE_RECEIPT | Constants.MODE_WALLET),
		BANK_TRANSFER("BankTransfer", "Bank Transfer", "payment.mode.bank.transfer", Constants.MODE_RECEIPT | Constants.MODE_WALLET) {
			@Override
			public String toString() {
				return "BANK TRANSFER";
			}
		},
		GIRO("Giro", "GIRO/CDA", "payment.mode.giro", Constants.MODE_RECEIPT | Constants.MODE_WALLET),
		CHEQUE("Cheque", "支票", "payment.mode.cheque", Constants.MODE_RECEIPT | Constants.MODE_REFUND | Constants.MODE_WALLET),
		VOUCHER("Voucher", "优惠券", "payment.mode.voucher", Constants.MODE_RECEIPT | Constants.MODE_WALLET),
		//	    CREDIT_CARD("CreditCard", "信用卡", "payment.mode.credit.card", Constants.MODE_RECEIPT | Constants.MODE_WALLET),
		BAD_DEBT("BadDebt", "BADDEBT", "payment.mode.baddebt", Constants.MODE_RECEIPT),
		INTERNET_BANKING("Internet Banking", "INTERNET_BANKING", "payment.mode.internetbanking", Constants.MODE_REFUND),
		INTERNAL_TRANSFER(
				"Internal Transfer",
				"INTERNAL_TRANSFER",
				"payment.mode.internal.transfer",
				Constants.MODE_WALLET | Constants.MODE_REFUND),

		OTHERS("Others", "OTHERS", "payment.mode.others", 0);

		private final String code;
		private final String desc;
		private final String template; //国际化消息模版
		private final int useMode;

		private PaymentMode(String code, String desc, String template, int useMode) {
			this.code = code;
			this.desc = desc;
			this.template = template;
			this.useMode = useMode;
		}

		@JsonValue
		public String getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}

		public String getTemplate() {
			return template;
		}

		public int getUseMode() {
			return useMode;
		}

		public String getName() {
			return this.name();
		}

		/**
		 * 是否是单据支付类型
		 * 
		 * @param paymentModeCode
		 * @return
		 */
		public static boolean isBillPaymentType(String paymentModeCode) {
			return Arrays.asList(PaymentMode.values()).stream()
					.filter(t -> t.isBillPaymentType() && t.getCode().equals(paymentModeCode))
					.count() > 0;
		}

		/**
		 * 是否是单据支付类型
		 * 
		 * @return
		 */
		public boolean isBillPaymentType() {
			return false;
		}

		/**
		 * 是否支持Receipt
		 *
		 * @return
		 */
		public boolean isSupportReceipt() {
			return (useMode & Constants.MODE_RECEIPT) != 0;
		}

		/**
		 * 是否支持Refund
		 *
		 * @return
		 */
		public boolean isSupportRefund() {
			return (useMode & Constants.MODE_REFUND) != 0;
		}

		/**
		 * 是否支持使用常规支付方式，用于预付款、押金
		 *
		 * @return
		 */
		public boolean isSupportWallet() {
			return (useMode & Constants.MODE_WALLET) != 0;
		}

		public static PaymentMode getPaymentMode(String code) {
			return Arrays.asList(PaymentMode.values()).stream()
					.filter(t -> t.getCode().equalsIgnoreCase(code))
					.findFirst().orElse(null);
		}

		private static class Constants {
			// receipt可以使用
			public static final int MODE_RECEIPT = 1;
			// refund可以使用
			public static final int MODE_REFUND = 1 << 1;
			// 钱包支付，可用于AdvancePayment Deposit等
			public static final int MODE_WALLET = 1 << 1 << 1;
		}

	}
	
	@Test
	public void testAlias() {
		SettlementAuditState auditState = (SettlementAuditState)EnumUtils.lookupEnum(SettlementAuditState.class, "未审核", "desc", "alias");
		System.out.println(auditState);
		
		SettlementAuditState auditState2 = EnumUtils.toEnum(SettlementAuditState.class, "未审核", "desc", "alias");
		System.out.println(auditState2);
	}
	
	public enum SettlementAuditState{

		ALL(-1, "全部"),
		TO_AUDIT(101, "待审核", "未审核"),
		AUDITTING(102, "审核中"),
		PASS(103, "审核通过"),
		REJECT(104, "审核驳回");
		
	    private int code;
	    private String desc;
	    private String alias;

		private SettlementAuditState(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		
		private SettlementAuditState(int code, String desc, String alias) {
			this.code = code;
			this.desc = desc;
			this.alias = alias;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		@JsonValue
		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	    
		public String getAlias() {
			return alias;
		}

		public void setAlias(String alias) {
			this.alias = alias;
		}

		@Override
		public String toString() {
			return desc;
		}
		
	}

	@Test
	public void testLookupSpeed() {
		String speed = "2";
		Enum speed1 = EnumUtils.lookupEnum(Speed.class, speed, "speed");
		System.out.println(speed1);
	}

	public enum Speed {

		/**
		 * 速度
		 */
		HIGH(1, "高速"),

		MIDDLE(2, "中速"),

		LOW(3, "低速");

		private int speed;
		private String desc;

		private Speed(int speed, String desc) {
			this.speed = speed;
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}

		public int getSpeed() {
			return speed;
		}
	}
}

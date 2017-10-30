package com.sssoft.Yundian.utils;

public class Constant {
	public class Rc{
		public static final String SUCC = "00";//交易成功
		public static final String UNK = "X0";//交易未知
		public static final String FAILE = "XX";//交易失败
	}
	public class RespType{
		public static final int RESP01 = 11;//现金交易返回
		public static final int RESP02 = 12;//条码交易返回
		public static final int RESP03 = 13;//收单交易返回
		public static final int RESP06 = 14;//余额，重打印，结算返回
	}
	public class PayMode{
		public static final String BANK = "1";//银行卡
		public static final String MOBILE = "2";//移动支付
		public static final String CASH = "3";//现金
		public static final String YCARD = "4";//预付卡
	}
	public class ChannelId{
		public static final String BANK = "11";//银行卡
		public static final String ZFB = "6";//支付宝
		public static final String WX = "2";//微信
	}
	public static final String FileName = "cashier";
	public final static String APPSTR = "CommonApp";
	public final static String CurrencyCode = "156";
	public class TxnType{
		public static final String TXN_TYPE_SALE = "101";//消费交易
		public static final String TXN_TYPE_CHX = "102";//撤销交易
		public static final String TXN_TYPE_REF = "103";//退货交易
		public static final String TXN_TYPE_QUERY = "104";//交易查询
		public static final String TXN_TYPE_MAN = "105";//呼出管理程序
		public static final String TXN_TYPE_AUTH = "106";//预授权
		public static final String TXN_TYPE_AUTHVOID = "107";//预授权撤销
		public static final String TXN_TYPE_AUTHFIN = "108";//预授权完成
		public static final String TXN_TYPE_AUTHFINVOID = "109";//预授权完成撤销
		public static final String TXN_TYPE_JS= "110";//结算
		public static final String TXN_TYPE_REPRINT = "111";//重打印
		public static final String TXN_TYPE_BALANCE = "112";//余额查询
	}
	public  class InfType{
		public static final String BD = "bd";
		public static final String WJ = "wj";
	}
	public  class MoTxnType{
		public static final String SALE = "2";
		public static final String CANNEL = "3";
		public static final String REFUND = "4";
		public static final String SALEQUERY = "5";
		public static final String REFUNDQUERY = "6";
	}
	public static final String ALLDATE = "yyyy/MM/dd HH:mm:ss";
	public static final String DATE14 = "yyyyMMddHHmmss";
	
	public final static String DEFAULT_CHARSET = "GBK";
	public final static String RC = "txnRespCode";
	public final static String RC_DETAIL = "txnRespDesc";
	
//	public class Field{
//		public final static String SIGN = "Sign";
//		public final static String PARTNER = "PartnerID";
//	}
//	public class Trans{
//		public final static String ServerIP = "58.213.110.146";
//		public final static int ServerPort = 19966;
//		public final static int TcpTimeout = 60;
//	}
//	public static final String TRUST_STORE_FILE_NAME = "";
//	//受信任证书库密码
//	public static final String TRUST_STORE_PASSWORD = "";
//	//APP客户端证书库名称
//	public static final String KEY_STORE_FILE_NAME = "";
//	//APP客户端证书库密码
//	public static final String KEY_STORE_PASSWORD = "";
//	
//
//	public static final Integer CONN_TIMEOUT = 30;
//	public static final Integer SO_TIMEOUT = 30;
	
	//public static final String  SEND_URL="http://beta.langshiyu.com/system/rest/payment/paymentResponse/ShiJiPos";
}

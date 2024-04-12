package com.loserico.json;

/**
 * Json工具类
 * <p>
 * Copyright: Copyright (c) 2024-04-12 14:34
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public final class JSON {

    /**
     * 对JSON串做一些预处理, 会把多余的双引号和转义符\去掉, 比如有这样一个JSON原始串
     * <pre> {@code
     * {
     *   "methord": "saveTask",
     *   "billIds": "",
     *   "operateType": "49",
     *   "edStatus": "view",
     *   "billItemJson": "{\"KingdeeAccess\":[{\"FBillNo\":\"XSCKD049570\",\"FMaterialId\":\"01.01.01.16.051\",\"batchNo\":1734969,\"memo\":\" \",\"FModifyDate\":\"2024-04-11T23:52:02.583\",\"Flot\":\"2023120802927220036\",\"pz\":0.78,\"caseNo\":\" \",\"mz\":27.7,\"spec\":\"0.90\",\"customerName\":\"江阴南闸志国机电维修部\",\"jz\":26.92,\"itemId\":2549083,\"lotNo\":1718216,\"qty\":1,\"tenantId\":\"00000\",\"customerId\":\"C617797\",\"FMaterialName\":\"QZ-2/155\",\"FormId\":\"SAL_OUTSTOCK\",\"billTypeName\":\"金蝶接入\",\"FormName\":\"销售出库\",\"containerId\":\" \",\"printPage\":1,\"billTypeId\":\"KingdeeAccess\"}]}",
     *   "userName": "定位",
     *   "billJson": "{\"FBillNo\":\"XSCKD049570\",\"FMaterialId\":\"01.01.01.16.051\",\"batchNo\":1734969,\"memo\":\" \",\"FModifyDate\":\"2024-04-11T23:52:02.583\",\"Flot\":\"2023120802927220036\",\"pz\":0.78,\"caseNo\":\" \",\"mz\":27.7,\"spec\":\"0.90\",\"customerName\":\"江阴南闸志国机电维修部\",\"jz\":26.92,\"itemId\":2549083,\"lotNo\":1718216,\"qty\":1,\"tenantId\":\"00000\",\"customerId\":\"C617797\",\"FMaterialName\":\"QZ-2/155\",\"FormId\":\"SAL_OUTSTOCK\",\"billTypeName\":\"金蝶接入\",\"FormName\":\"销售出库\",\"containerId\":\" \",\"printPage\":1,\"billTypeId\":\"KingdeeAccess\"}",
     *   "userId": "location",
     *   "billId": "",
     *   "tenantId": "00000",
     *   "wareHouseId": "10000",
     *   "billTypeName": "金蝶接入",
     *   "key": "93785204",
     *   "billTypeId": "KingdeeAccess"
     * }
     * }</pre>
     * 处理后返回的JSON串如下
     * <pre> {@code
     * {
     *   "methord": "saveTask",
     *   "billIds": "",
     *   "operateType": "49",
     *   "edStatus": "view",
     *   "billItemJson": {"KingdeeAccess":[{"FBillNo":"XSCKD049570","FMaterialId":"01.01.01.16.051","batchNo":1734969,"memo":" ","FModifyDate":"2024-04-11T23:52:02.583","Flot":"2023120802927220036","pz":0.78,"caseNo":" ","mz":27.7,"spec":"0.90","customerName":"江阴南闸志国机电维修部","jz":26.92,"itemId":2549083,"lotNo":1718216,"qty":1,"tenantId":"00000","customerId":"C617797","FMaterialName":"QZ-2/155","FormId":"SAL_OUTSTOCK","billTypeName":"金蝶接入","FormName":"销售出库","containerId":" ","printPage":1,"billTypeId":"KingdeeAccess"}]},
     *   "userName": "定位",
     *   "billJson": {"FBillNo":"XSCKD049570","FMaterialId":"01.01.01.16.051","batchNo":1734969,"memo":" ","FModifyDate":"2024-04-11T23:52:02.583","Flot":"2023120802927220036","pz":0.78,"caseNo":" ","mz":27.7,"spec":"0.90","customerName":"江阴南闸志国机电维修部","jz":26.92,"itemId":2549083,"lotNo":1718216,"qty":1,"tenantId":"00000","customerId":"C617797","FMaterialName":"QZ-2/155","FormId":"SAL_OUTSTOCK","billTypeName":"金蝶接入","FormName":"销售出库","containerId":" ","printPage":1,"billTypeId":"KingdeeAccess"},
     *   "userId": "location",
     *   "billId": "",
     *   "tenantId": "00000",
     *   "wareHouseId": "10000",
     *   "billTypeName": "金蝶接入",
     *   "key": "93785204",
     *   "billTypeId": "KingdeeAccess"
     * }
     * }</pre>
     * @param json
     * @return
     */
    public static String cleanup(String json) {
        json = json.replace("\\\"", "\""); //去掉转义符\
        json = json.replace("\"{", "{"); //去掉左大括号左边的双引号"{
        json = json.replace("}\"", "}"); //去掉右大括号右边的双引号}"
        return json;
    }
}

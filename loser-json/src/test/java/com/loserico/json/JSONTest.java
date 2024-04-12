package com.loserico.json;

import com.loserico.common.lang.utils.IOUtils;
import org.junit.Test;

import static com.loserico.json.JSON.cleanup;
import static org.junit.Assert.assertEquals;

public class JSONTest {

    @Test
    public void testCleanup() {
        String msgData = IOUtils.readFileAsString("d:/msg_data.json");
        String json = cleanup(msgData);
        System.out.println(json);
    }

    @Test
    public void testEmptyString() {
        String input = "";
        String expected = "";
        assertEquals(expected, cleanup(input));
    }

    @Test
    public void testValidJsonString() {
        String input = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        String expected = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        assertEquals(expected, cleanup(input));
    }

    @Test
    public void testJsonStringWithEscapedCharacters() {
        String input = "{\"name\":\"John\",\"message\":\"\\\"Hello\\\"\"}";
        String expected = "{\"name\":\"John\",\"message\":\"\"Hello\"\"}";
        assertEquals(expected, cleanup(input));
    }

    @Test
    public void testJsonStringWithExtraQuotes() {
        String input = "\"{\"name\":\"John\",\"age\":30}\"";
        String expected = "{\"name\":\"John\",\"age\":30}";
        assertEquals(expected, cleanup(input));
    }

    @Test
    public void testJsonStringWithBracketsInKeyValue() {
        String input = "{\"name\":\"{John}\",\"age\":30}";
        String expected = "{\"name\":{John},\"age\":30}";
        assertEquals(expected, cleanup(input));
    }

    @Test
    public void testJsonStringWithNestedBrackets() {
        String input = "{\"name\":\"{John}\",\"details\":{\"city\":\"{New York}\",\"zipcode\":10001}}";
        String expected = "{\"name\":{John},\"details\":{\"city\":{New York},\"zipcode\":10001}}";
        assertEquals(expected, cleanup(input));
    }

    @Test
    public void test() {
        String json = IOUtils.readFileAsString("d:/msg_data2.json");
        System.out.println(json);
        json = cleanup(json);
        System.out.println(json);
    }

    @Test
    public void testCleanup1() {
        String originalJson = "{\"methord\": \"saveTask\",\"billIds\": \"\",\"operateType\": \"49\",\"edStatus\": \"view\",\"billItemJson\": \"\\\"{\\\"KingdeeAccess\\\":[{\\\"FBillNo\\\":\\\"XSCKD049570\\\",\\\"FMaterialId\\\":\\\"01.01.01.16.051\\\",\\\"batchNo\\\":1734969,\\\"memo\\\":\\\" \\\",\\\"FModifyDate\\\":\\\"2024-04-11T23:52:02.583\\\",\\\"Flot\\\":\\\"2023120802927220036\\\",\\\"pz\\\":0.78,\\\"caseNo\\\":\\\" \\\",\\\"mz\\\":27.7,\\\"spec\\\":\\\"0.90\\\",\\\"customerName\\\":\\\"江阴南闸志国机电维修部\\\",\\\"jz\\\":26.92,\\\"itemId\\\":2549083,\\\"lotNo\\\":1718216,\\\"qty\\\":1,\\\"tenantId\\\":\\\"00000\\\",\\\"customerId\\\":\\\"C617797\\\",\\\"FMaterialName\\\":\\\"QZ-2/155\\\",\\\"FormId\\\":\\\"SAL_OUTSTOCK\\\",\\\"billTypeName\\\":\\\"金蝶接入\\\",\\\"FormName\\\":\\\"销售出库\\\",\\\"containerId\\\":\\\" \\\",\\\"printPage\\\":1,\\\"billTypeId\\\":\\\"KingdeeAccess\\\"}]}\\\",\"userName\": \"定位\",\"billJson\": \"\\\"{\\\"FBillNo\\\":\\\"XSCKD049570\\\",\\\"FMaterialId\\\":\\\"01.01.01.16.051\\\",\\\"batchNo\\\":1734969,\\\"memo\\\":\\\" \\\",\\\"FModifyDate\\\":\\\"2024-04-11T23:52:02.583\\\",\\\"Flot\\\":\\\"2023120802927220036\\\",\\\"pz\\\":0.78,\\\"caseNo\\\":\\\" \\\",\\\"mz\\\":27.7,\\\"spec\\\":\\\"0.90\\\",\\\"customerName\\\":\\\"江阴南闸志国机电维修部\\\",\\\"jz\\\":26.92,\\\"itemId\\\":2549083,\\\"lotNo\\\":1718216,\\\"qty\\\":1,\\\"tenantId\\\":\\\"00000\\\",\\\"customerId\\\":\\\"C617797\\\",\\\"FMaterialName\\\":\\\"QZ-2/155\\\",\\\"FormId\\\":\\\"SAL_OUTSTOCK\\\",\\\"billTypeName\\\":\\\"金蝶接入\\\",\\\"FormName\\\":\\\"销售出库\\\",\\\"containerId\\\":\\\" \\\",\\\"printPage\\\":1,\\\"billTypeId\\\":\\\"KingdeeAccess\\\"}\\\",\"userId\": \"location\",\"billId\": \"\",\"tenantId\": \"00000\",\"wareHouseId\": \"10000\",\"billTypeName\": \"金蝶接入\",\"key\": \"93785204\",\"billTypeId\": \"KingdeeAccess\"}";
        String expectedJson = "{\"methord\": \"saveTask\",\"billIds\": \"\",\"operateType\": \"49\",\"edStatus\": \"view\",\"billItemJson\": {\"KingdeeAccess\":[{\"FBillNo\":\"XSCKD049570\",\"FMaterialId\":\"01.01.01.16.051\",\"batchNo\":1734969,\"memo\":\" \",\"FModifyDate\":\"2024-04-11T23:52:02.583\",\"Flot\":\"2023120802927220036\",\"pz\":0.78,\"caseNo\":\" \",\"mz\":27.7,\"spec\":\"0.90\",\"customerName\":\"江阴南闸志国机电维修部\",\"jz\":26.92,\"itemId\":2549083,\"lotNo\":1718216,\"qty\":1,\"tenantId\":\"00000\",\"customerId\":\"C617797\",\"FMaterialName\":\"QZ-2/155\",\"FormId\":\"SAL_OUTSTOCK\",\"billTypeName\":\"金蝶接入\",\"FormName\":\"销售出库\",\"containerId\":\" \",\"printPage\":1,\"billTypeId\":\"KingdeeAccess\"}]},\"userName\": \"定位\",\"billJson\": {\"FBillNo\":\"XSCKD049570\",\"FMaterialId\":\"01.01.01.16.051\",\"batchNo\":1734969,\"memo\":\" \",\"FModifyDate\":\"2024-04-11T23:52:02.583\",\"Flot\":\"2023120802927220036\",\"pz\":0.78,\"caseNo\":\" \",\"mz\":27.7,\"spec\":\"0.90\",\"customerName\":\"江阴南闸志国机电维修部\",\"jz\":26.92,\"itemId\":2549083,\"lotNo\":1718216,\"qty\":1,\"tenantId\":\"00000\",\"customerId\":\"C617797\",\"FMaterialName\":\"QZ-2/155\",\"FormId\":\"SAL_OUTSTOCK\",\"billTypeName\":\"金蝶接入\",\"FormName\":\"销售出库\",\"containerId\":\" \",\"printPage\":1,\"billTypeId\":\"KingdeeAccess\"},\"userId\": \"location\",\"billId\": \"\",\"tenantId\": \"00000\",\"wareHouseId\": \"10000\",\"billTypeName\": \"金蝶接入\",\"key\": \"93785204\",\"billTypeId\": \"KingdeeAccess\"}";
        String cleanedJson = JSON.cleanup(originalJson);

        assertEquals(expectedJson, cleanedJson);
    }
}

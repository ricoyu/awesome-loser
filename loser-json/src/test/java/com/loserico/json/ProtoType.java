package com.loserico.json;

import com.fasterxml.jackson.annotation.JsonValue;
import com.loserico.common.lang.i18n.I18N;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ProtoType {

    HTTP("http"),

    HTTP_DATA("http-data"),

    TLS("tls"),

    SSH("ssh"),

    DNS("dns"),

    ICMP("icmp"),

    DHCP("dhcp"),

    FTP("ftp"),

    FTP_DATA("ftp-data"),

    SMTP("smtp"),

    SMTP_DATA("smtp-data"),

    IMAP("imap"),

    IMAP_DATA("imap-data"),

    POP3("pop3"),

    POP3_DATA("pop3-data"),

    SMB("smb"),

    SMB_DATA("smb-data"),

    NFS("nfs"),

    NFS_DATA("nfs-data"),

    TELNET("telnet"),

    MODBUS("modbus"),

    RDP("rdp"),

    LDAP("ldap"),

    /**
     * 这个只有网络日志有, 事件是没有的
     */
    FLOW_TCP("tcp(flow)"),

    FLOW_ICMP("icmp(flow)"),

    FLOW_UDP("udp(flow)"),


    ORACLE("oracle"),

    SQL_SERVER("sqlserver"),

    SYBASE("sybase"),

    DB2("db2"),

    TERADATA("teradata"),

    MYSQL("mysql"),

    POSTGRE("postgre"),

    DAMENG("dameng"),

    KINGBASE("kingbase"),

    CACHEDB("cachedb"),

    OSCAR("oscar"),

    HBASE("hbase"),

    MONGODB("mongodb"),

    HIVE("hive"),

    REDIS("redis"),

    HANA("hana"),

    MEMORY_CACHE("memorycache");

    @JsonValue
    private String key;

    private ProtoType(String key) {
        this.key = key;
    }

    public boolean isEqualTo(String protoType) {
        return this.key.equalsIgnoreCase(protoType);
    }

    public String getKey() {
        return key;
    }

    /**
     * 处理枚举
     *
     * @return
     */
    public static Map<String, String> handlerEnum() {

        Map<String, String> map = new HashMap<>();
        for (ProtoType protoType : ProtoType.values()) {
            String key = protoType.getKey();
            map.put(key, I18N.i18nMessage(key));
        }
        return map;
    }

    /**
     * 获取所有protoType
     *
     * @return
     */
    public static List<String> getAllProtoTypes() {
        List<String> protoTypes = new ArrayList<>();
        for (ProtoType value : ProtoType.values()) {
            protoTypes.add(value.getKey());
        }
        return protoTypes;
    }

    /**
     * 处理枚举
     *
     * @return
     */
    public static Map<String, String> handlerEventEnum() {
        Map<String, String> map = handlerEnum();
        return map;
    }

    public static Map<String, String> handlerEventEnumForNetLog() {
        Map<String, String> map = new HashMap<>();
        for (ProtoType value : ProtoType.values()) {
            map.put(value.getKey(), value.getKey().toLowerCase());
        }
        return map;
    }
}

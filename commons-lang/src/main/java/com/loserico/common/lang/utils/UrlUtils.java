package com.loserico.common.lang.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.*;

@Slf4j
public class UrlUtils {

    private static final String ENCODING = "UTF-8";

    /**
     * 对完整URL中的URI部分进行编码，并返回包含编码后URI部分的完整URL
     *
     * @param urlOrUri 需要编码的完整URL
     * @return 包含编码后URI部分的完整URL
     * @throws URISyntaxException           URI语法异常
     * @throws UnsupportedEncodingException 编码不支持异常
     */
    public static String encodeUrl(String urlOrUri) {
        if (isValidUrl(urlOrUri)) {
        try {
            URL u = new URL(urlOrUri);
            String query = u.getQuery() != null ? "?" + URLEncoder.encode(u.getQuery(), ENCODING).replaceAll("\\+", "%20").replaceAll("%3D", "=").replaceAll("%26", "&") : "";
            return u.getProtocol() + "://" + u.getHost() + (u.getPort() != -1 ? ":" + u.getPort() : "") + u.getPath() + query;
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        } else {
            try {
                return URLEncoder.encode(urlOrUri, ENCODING)
                        .replaceAll("\\+", "%20")
                        .replaceAll("%3D", "=")
                        .replaceAll("%26", "&");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 检查字符串是否是有效的URL
     *
     * @param url 要检查的字符串
     * @return 如果是有效的URL则返回true，否则返回false
     */
    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}

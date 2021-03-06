package com.andy.pay.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目常用工具
 *
 * @author Leone
 * @since 2018-05-10
 **/
@Slf4j
public class AppUtils {

    public static String urlEncoder(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String urlDecoder(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 匹配ip是否合法
     *
     * @param ip
     * @return
     */
    public static Boolean isIp(String ip) {
        String re = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * 生成md5签名的方法
     *
     * @author Leone
     * @params: [charset, params, apiKey]
     * @return: java.lang.String
     * @since 2018/6/3 14:59
     **/
    public static String createSign(Map params, String apiKey) {
        StringBuffer sb = new StringBuffer();
        Set set = params.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + apiKey);
        return MD5(sb.toString()).toUpperCase();
    }

    /**
     * 生成MD5摘要算法
     *
     * @author Leone
     * @params: [message, charset]
     * @return: java.lang.String
     * @since 2018/6/3 15:00
     **/
    public static String MD5(String content) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] hashCode = messageDigest.digest(content.getBytes("UTF-8"));
            HexBinaryAdapter hexBinaryAdapter = new HexBinaryAdapter();
            return hexBinaryAdapter.marshal(hashCode).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成 HMAC_SHA256
     *
     * @author Leone
     * @params: [data, key]
     * @return: java.lang.String
     * @since 2018/6/3 15:01
     **/
    public static String HMAC_SHA256(String content, String api_key) throws Exception {
//        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
//        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
//        sha256_HMAC.init(secret_key);
//        byte[] array = sha256_HMAC.doFinal(content.getBytes("UTF-8"));
//        StringBuilder sb = new StringBuilder();
//        for (byte item : array) {
//            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
//        }
//        return sb.toString().toUpperCase();
        try {
            KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = generator.generateKey();
            byte[] key = secretKey.getEncoded();
            SecretKey secretKeySpec = new SecretKeySpec(api_key.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance(secretKeySpec.getAlgorithm());
            mac.init(secretKeySpec);
            byte[] digest = mac.doFinal(content.getBytes());
            return new HexBinaryAdapter().marshal(digest).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * XML格式字符串转换为Map
     *
     * @author Leone
     * @params: [xmlStr]
     * @return: java.util.Map
     * @since 2018/6/3 15:02
     **/
    public static Map xmlToMap(String xmlStr) {
        try (InputStream inputStream = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"))) {
            Map<String, String> data = new HashMap<>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            return data;
        } catch (Exception ex) {
            log.warn("xml转换map异常,message: {}. XML content: {}", ex.getMessage(), xmlStr);
        }
        return null;
    }


    /**
     * map转换为xml字符串
     *
     * @author Leone
     * @params: [params]
     * @return: java.lang.String
     * @since 2018/6/3 15:02
     **/
    public static String mapToXml(Map params) {
        StringBuilder sb = new StringBuilder();
        Set set = params.entrySet();
        Iterator it = set.iterator();
        sb.append("<xml>\n");
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            sb.append("<" + k + ">").append(v).append("</" + k + ">\n");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 生成32位随机数字
     *
     * @author Leone
     * @params: []
     * @return: java.lang.String
     * @since 2018/6/3 15:02
     **/
    public static String genNonceStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取当前时间戳，单位秒(10位)
     *
     * @author Leone
     * @params: []
     * @return: java.lang.String
     * @since 2018/6/3 15:02
     **/
    public static long getTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 生成32位字符串
     *
     * @author Leone
     * @params: []
     * @return: java.lang.String
     * @since 2018/6/3 15:02
     **/
    public static String generateNum(int length) {
        StringBuffer result = new StringBuffer();
        final String sources = "0123456789";
        if (length < 0 && length > 512) {
            return null;
        }
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            result.append(sources.charAt(rand.nextInt(9)) + "");
        }
        return result.toString();
    }

    public static void main(String[] args) throws Exception {
        System.out.println(genNonceStr());
        System.out.println(getTimestamp());
        System.out.println(generateNum(6));
        System.out.println(MD5("hello"));
    }

    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (null != ip && "".equals(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (null != ip && "".equals(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }


}

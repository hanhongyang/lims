package com.gmlimsqi.sap.util;

import java.io.*;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpUtils {
    
    public static String HttpPost(String username, String password, String url, String data) throws IOException {
        getAuthenticator(username, password);
        HttpURLConnection con = getHttpURLConnection(url, data);
        StringBuilder response = getStringBuilder(con);
        return response.toString();
    }

    public static String HttpPost(String username, String password, String url, Map<String, String> header,
                                  String data) throws IOException {
        getAuthenticator(username, password);
        HttpURLConnection con = getHttpURLConnection(url, header, data);
        StringBuilder response = getStringBuilder(con);
        return response.toString();
    }
    
    private static StringBuilder getStringBuilder(HttpURLConnection con) throws IOException {
        String contentType = con.getContentType();
        String charset = "UTF-8"; // 默认字符编码为 UTF-8
        if (contentType != null && contentType.contains("charset=")) {
            charset = contentType.split("charset=")[1];
        }
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),charset))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response;
    }
    
    private static HttpURLConnection getHttpURLConnection(String url, Map<String, String> header, String data) throws IOException {
        String contentType = "application/json;charset=utf-8";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", contentType);
        con.setDoOutput(true);
        
        for (Map.Entry<String, String> entry : header.entrySet()) {
            con.setRequestProperty(entry.getKey(), entry.getValue());
        }
        
        if (data != null && !data.isEmpty()) {
            OutputStream outputStream = null;
            outputStream = con.getOutputStream();
            BufferedWriter writer = null;
            //将字节流转换为字符流
            writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            //往连接中写入参数，body可以是name=lmf&age=23键值对拼接形式，也可以是json字符串形式
            writer.write(data);
            //必须刷新流空间的数据
            writer.flush();
//            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
//                wr.writeBytes(data);
//                wr.flush();
//            }
        }
        return con;
    }
    
    private static HttpURLConnection getHttpURLConnection(String url, String data) throws IOException {
        String contentType = "application/json;charset=utf-8";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", contentType);
        con.setDoOutput(true);
        
        if (data != null && !data.isEmpty()) {
            OutputStream outputStream = null;
            outputStream = con.getOutputStream();
            BufferedWriter writer = null;
            //将字节流转换为字符流
            writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            //往连接中写入参数，body可以是name=lmf&age=23键值对拼接形式，也可以是json字符串形式
            writer.write(data);
            //必须刷新流空间的数据
            writer.flush();
//            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
//                wr.writeBytes(data);
//                wr.flush();
//            }
        }
        return con;
    }
    
    private static void getAuthenticator(String username, String password) {
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
    }
    
    
}

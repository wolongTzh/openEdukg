package com.edukg.open.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http通信工具类
 *
 * @author chen
 */
public class HttpUtil {


    /**
     * post请求传输map数据
     *
     * @param url url地址
     * @param map map数据
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendPostDataByMap(String url, Map<String, Object> map) throws ClientProtocolException, IOException {
        String result = "";
        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        // 装填参数
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
            }
        }

        // 设置参数到请求对象中
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));

        // 设置header信息
        // 指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        // 执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = httpClient.execute(httpPost);
        // 获取结果实体
        // 判断网络连接状态码是否正常(0--200都是正常)
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(), "utf-8");
        }
        // 释放连接
        response.close();

        return result;
    }

    /**
     * post请求传输json数据
     *
     * @param url  url地址
     * @param json json数据
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendPostDataByJson(String url, String json) throws ClientProtocolException, IOException {
        String result = "";

        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        // 设置参数到请求对象中
        StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        stringEntity.setContentEncoding("utf-8");
        httpPost.setEntity(stringEntity);

        // 执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = httpClient.execute(httpPost);

        // 获取结果实体
        // 判断网络连接状态码是否正常(0--200都是正常)
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(), "utf-8");
        }
        // 释放链接
        response.close();

        return result;
    }

    /**
     * post请求传输json数据
     *
     * @param url  url地址
     * @param json json数据
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendPostDataByJsonWithFile(String url, JSONObject json) throws ClientProtocolException, IOException {
        String result = "";

        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        // 设置参数到请求对象中
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(StandardCharsets.UTF_8);
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (Map.Entry<String, Object> entry: json.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.equals("file")) {
                File file = File.createTempFile("temp", "pdf");
                MultipartFile multipartFile = (MultipartFile)value;
                multipartFile.transferTo(file);
                builder.addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, ((MultipartFile)value).getOriginalFilename());
            }
            else {
                builder.addPart(key, new StringBody("" + value , ContentType.create("text/plain", Consts.UTF_8)));
            }
        }
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);

        // 执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = httpClient.execute(httpPost);

        // 获取结果实体
        // 判断网络连接状态码是否正常(0--200都是正常)
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(), "utf-8");
        }
        // 释放链接
        response.close();

        return result;
    }

    /**
     * get请求传输数据
     *
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendGetData(String url) throws ClientProtocolException, IOException {
        String result = "";

        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建get方式请求对象
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-type", "application/json");
        // 通过请求对象获取响应对象
        CloseableHttpResponse response = httpClient.execute(httpGet);

        // 获取结果实体
        // 判断网络连接状态码是否正常(0--200都是正常)
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(), "utf-8");
        }
        // 释放链接
        response.close();
        return result;
    }

    /**
     * get请求传输数据
     *
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendGetFile(String url, ServletOutputStream outputStream) throws ClientProtocolException, IOException {
        String result = "success";

        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建get方式请求对象
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-type", "application/json");
        // 通过请求对象获取响应对象
        CloseableHttpResponse response = httpClient.execute(httpGet);
        // 获取结果实体
        // 判断网络连接状态码是否正常(0--200都是正常)
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            InputStream inputStream = response.getEntity().getContent();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        // 释放链接
        response.close();
        return result;
    }

}


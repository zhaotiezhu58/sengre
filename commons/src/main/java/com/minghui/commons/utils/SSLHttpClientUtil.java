package com.minghui.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class SSLHttpClientUtil {
    public static final Logger log = LoggerFactory.getLogger(SSLHttpClientUtil.class);

    private static CloseableHttpClient httpClient = null;
    private static HttpClientContext context = null;
    private static CookieStore cookieStore = null;
    private static RequestConfig requestConfig = null;

    static {
        httpClient = createSSLInsecureClient();
    }

    public static CloseableHttpClient createSSLInsecureClient() {
        context = HttpClientContext.create();
        cookieStore = new BasicCookieStore();
        // 配置超时时间（连接服务端超时1秒，请求数据返回超时2秒）   
        requestConfig = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(60000)
                .setConnectionRequestTimeout(60000).build();

        try {
            SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, new TrustStrategy() {
                        //信任�?�?
                        public boolean isTrusted(X509Certificate[] chain,
                                                 String authType) {
                            return true;
                        }
                    }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                    .setRedirectStrategy(new DefaultRedirectStrategy())
                    .setDefaultRequestConfig(requestConfig)
                    .setDefaultCookieStore(cookieStore)
                    .build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    public static Map<String, Object> postReturnCookie(String reqURL, Map<String, String> hearder, Map params, String encodeCharset) {
        CloseableHttpResponse response = null;
        Map<String, Object> map = new HashMap<String, Object>();
        String reseContent = "通信失败";
        HttpPost httpPost = new HttpPost(reqURL);
        //由于下面使用的是new StringEntity(....),�?以默认发出去的请求报文头中CONTENT_TYPE值为text/plain; charset=ISO-8859-1   
        //这就有可能会导致服务端接收不到POST过去的参�?,比如运行在Tomcat6.0.36中的Servlet,�?以我们手工指定CONTENT_TYPE头消�?   
        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=" + encodeCharset);
        if (hearder != null) {
            for (String key : hearder.keySet()) {
                httpPost.setHeader(key, hearder.get(key));
            }
        }
        try {
            UrlEncodedFormEntity formEntity = null;
            try {
                if (encodeCharset == null || StringUtils.isEmpty(encodeCharset)) {
                    formEntity = new UrlEncodedFormEntity(getParamsList(params));
                } else {
                    formEntity = new UrlEncodedFormEntity(getParamsList(params), encodeCharset);
                }
            } catch (UnsupportedEncodingException e) {
                throw new Exception("不支持的编码�?", e);
            }
            httpPost.setEntity(formEntity);
            response = httpClient.execute(httpPost, context);
            CookieStore cookieStore = context.getCookieStore();
            List<Cookie> cookies = cookieStore.getCookies();
            map.put("cookies", cookies);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                reseContent = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
                EntityUtils.consume(entity);
            }
            map.put("result", reseContent);
        } catch (ConnectTimeoutException cte) {
            log.error("请求通信[" + reqURL + "]时连接超�?,堆栈轨迹如下", cte);
        } catch (SocketTimeoutException ste) {
            log.error("请求通信[" + reqURL + "]时读取超�?,堆栈轨迹如下", ste);
        } catch (Exception e) {
            log.error("请求通信[" + reqURL + "]时偶遇异�?,堆栈轨迹如下", e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static String postWithCookie(String reqURL, Map<String, String> hearder, Map params, String encodeCharset, String cookie) {
        String reseContent = "通信失败";
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost(reqURL);
        //由于下面使用的是new StringEntity(....),�?以默认发出去的请求报文头中CONTENT_TYPE值为text/plain; charset=ISO-8859-1   
        //这就有可能会导致服务端接收不到POST过去的参�?,比如运行在Tomcat6.0.36中的Servlet,�?以我们手工指定CONTENT_TYPE头消�?   
        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=" + encodeCharset);
        try {
            UrlEncodedFormEntity formEntity = null;
            try {
                formEntity = new UrlEncodedFormEntity(getParamsList(params), encodeCharset);
            } catch (UnsupportedEncodingException e) {
                throw new Exception("不支持的编码�?", e);
            }

            httpPost.setEntity(formEntity);
            httpPost.setHeader("Cookie", cookie);
            if (hearder != null) {
                for (String key : hearder.keySet()) {
                    httpPost.setHeader(key, hearder.get(key));
                }
            }
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                reseContent = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
                EntityUtils.consume(entity);
            }
        } catch (ConnectTimeoutException cte) {
            log.error("请求通信[" + reqURL + "]时连接超�?,堆栈轨迹如下", cte);
        } catch (SocketTimeoutException ste) {
            log.error("请求通信[" + reqURL + "]时读取超�?,堆栈轨迹如下", ste);
        } catch (Exception e) {
            log.error("请求通信[" + reqURL + "]时偶遇异�?,堆栈轨迹如下", e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reseContent;
    }

    public static String get(String url, Map<String, String> hearder, String encode) throws Exception {
        log.info("请求地址:{}", url);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("accept", "*/*");
        httpGet.setHeader("connection", "Keep-Alive");
        httpGet.setHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        if (hearder != null) {
            for (String key : hearder.keySet()) {
                httpGet.setHeader(key, hearder.get(key));
            }
        }
        HttpResponse httpResponse = httpClient.execute(httpGet);
        String result = EntityUtils.toString(httpResponse.getEntity());
        log.info("请求响应:{}",result);
        return result;
    }

    public static String post(String url, Map<String, String> param, Map<String, String> hearder, String charset) {
        String response = "";
        CloseableHttpResponse httpResponse = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("accept", "*/*");
        httpPost.setHeader("connection", "Keep-Alive");
        httpPost.setHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        if (hearder != null) {
            for (String key : hearder.keySet()) {
                httpPost.setHeader(key, hearder.get(key));
            }
        }

        try {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(getParamsList(param), charset);
            httpPost.setEntity(formEntity);
            httpResponse = httpClient.execute(httpPost);
            response = EntityUtils.toString(httpResponse.getEntity());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

//      public static String postFile(String reqURL,Map<String,String> hearder, Map<String,Object> params, String encodeCharset,String cookie,File file) throws ClientProtocolException, IOException {
//            FileBody fileBody = null;
//            HttpPost httppost = new HttpPost(reqURL);
//            httppost.setHeader("Cookie",cookie);
//            if(hearder!=null){
//                for(String key:hearder.keySet()){
//                    httppost.setHeader(key,hearder.get(key));
//                }
//            }
//
//            if (file != null) {
//                fileBody = new FileBody(file);
//            }
//
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//            if(params!=null){
//                for(String paramKey:params.keySet()){
//                    StringBody keyBody = new StringBody(params.get(paramKey).toString(), ContentType.MULTIPART_FORM_DATA);
//                    builder.addPart(paramKey,keyBody);
//                }
//            }
//
//            builder.addPart("file", fileBody);
//            HttpEntity entity = builder.build();
//            httppost.setEntity(entity);
//
//            System.out.println("执行: " + httppost.getRequestLine());
//            CloseableHttpResponse response = httpClient.execute(httppost);
//            System.out.println("response:"+response);
//            System.out.println("statusCode is "
//                    + response.getStatusLine().getStatusCode());
//            HttpEntity resEntity = response.getEntity();
//            System.out.println("----------------------------------------");
//            System.out.println(response.getStatusLine());
//            if (resEntity != null) {
//                System.out.println("返回长度: " + resEntity.getContentLength());
//                System.out.println("返回类型: " + resEntity.getContentType());
//                InputStream in = resEntity.getContent();
//                String returnValue = inputStream2String(in);
//                System.out.println("returnValue:" + returnValue);
//                //上传到文件服务器后，删除本地的文�?
//                file.deleteOnExit();
//                response.close();
//                return returnValue;
//            }
//            return null;
//    }

    public static String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    public static String postRestful(String url, Map<String, String> hearder, String json) {
        CloseableHttpResponse httpResponse = null;
        String response = "";
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("content-type", "application/json");
        httpPost.addHeader("Accept", "application/json");
        if (hearder != null) {
            for (String key : hearder.keySet()) {
                httpPost.addHeader(key, hearder.get(key));
            }
        }

        try {
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpResponse = httpClient.execute(httpPost);
            response = EntityUtils.toString(httpResponse.getEntity());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    private static List getParamsList(Map<String, String> paramsMap) {
        if (paramsMap == null || paramsMap.size() == 0) {
            return null;
        }
        List params = new ArrayList();
        for (Map.Entry<String, String> map : paramsMap.entrySet()) {
            params.add(new BasicNameValuePair(map.getKey(), map.getValue()));
        }
        return params;
    }


    public static void main(String[] args) throws Exception {
        String s = SSLHttpClientUtil.get("http://www.6q2ibs.com?orderType=0&privateKey=236eb2b4309df3e8b8ab30c6ef15fa2b&amount=1&tokenId=202&chainId=256&checksum=5b74d3e1f7c2edb64eafe5968caa0eb6", null, null);
        System.out.println(s);
    }
}

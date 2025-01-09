package ssss;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.HashMap;
import java.util.stream.Collectors;
public class HttpTest {

      /**

     */
    public static String[] doGet(String httpUrl, ArrayList keys, Map<String,String> header) {

        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuffer result = new StringBuffer();
        try {
            System.out.println(httpUrl);
//            System.out.println("header");

            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            if(header!=null){
                for (Object key : keys) {
//                	System.out.println(key.toString()+":"+header.get(key.toString()));
                    connection.setRequestProperty(key.toString(),header.get(key.toString()));
                }

            }
            Map<String, List<String>> map = connection.getRequestProperties();
            for (Object key : keys) {

                List<String> l = map.get(key.toString());
                System.out.println(key.toString()+":"+l.get(0));
            }

            connection.setRequestMethod("GET");

            connection.setReadTimeout(15000);

            connection.connect();

            System.out.println("connection.getResponseCode():"+connection.getResponseCode());
            System.out.println("connection.getResponseMessage():"+connection.getResponseMessage());

            if (connection.getResponseCode() == 200) {

                is = connection.getInputStream();
                if (null != is) {
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String temp = null;
                    while (null != (temp = br.readLine())) {
                        result.append(temp);
                    }
                }
            }else {
                is = connection.getErrorStream();
                if (null != is) {
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String temp = null;
                    while (null != (temp = br.readLine())) {
                        result.append(temp);
                    }
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connection.disconnect();
        }
        String r[] = new String[2];
        r[0] = result.toString();
        try {
            r[1] = connection.getResponseCode()+"";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }

    public  static String[] getData(String[] args) {
        String TIME_STAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        String version = "2021-05-13";
        String signVersion = "V1.0";
        String signMethod = "HmacSHA1";

        //appId,accessKey,secretKey需要替换为实际应用所对应的三个值
        String appId = "d87d334f-88d5-445c-a439-f4d9efc30d40";
        String accessKey = "ba3c1b99-5d8c-4fd9-b6a6-f43fb99dc543";
        String secretKey = "0ac982f4-656c-4a78-b584-a286f3296adc";

        SimpleDateFormat sdf = new SimpleDateFormat(TIME_STAMP_FORMAT);

        HashMap params = new HashMap();
        params.put("AppId", appId);
        params.put("Version", version);
        params.put("AccessKey", accessKey);
        String timestamp = sdf.format(new Date());

        params.put("Timestamp", timestamp);
        params.put("SignatureMethod", signMethod);
        params.put("SignatureVersion", signVersion);
        String signNonce = UUID.randomUUID().toString();

        params.put("SignatureNonce", signNonce);

        String httpUrl = args[0];//"https://flow-portrait-api-wuxi.cmecloud.cn:7003/crowdFlow/administrativeRegion/grid/resident/month?districtCode=450205&queryDate=202306&residentType=USUAL&portraitType=AGE&coorsType=WGS84";
        //以下参数需替换为单次请求对应的参数信息,需根据实际情况进行增加、修改、删除
        System.out.println("arg:"+httpUrl);
        Map<String,String> httpGetParams = new HashMap<String, String>();
        String url = httpUrl;
        String[] paramArray = url.split("\\?")[1].split("&");
        for (String param : paramArray) {
            String[] keyValue = param.split("=");
            String paramName = keyValue[0];
            String paramValue = keyValue[1];
            httpGetParams.put(paramName, paramValue);
//		    System.out.println(paramName+"："+paramValue);
        }
//        httpGetParams.put("districtCode", "450205");
//        httpGetParams.put("queryDate", "202306");
//        httpGetParams.put("residentType", "USUAL");
//        httpGetParams.put("portraitType", "AGE");
//        httpGetParams.put("coorsType", "WGS84");

        for(String key:httpGetParams.keySet()) {
            params.put(key,httpGetParams.get(key));
        }

        ArrayList keys = (ArrayList) params.keySet().stream().sorted().collect(Collectors.toList());
        StringBuffer sb = new StringBuffer();
        for (Object key : keys) {

            sb.append(key.toString()).append("=").append(params.get(key)).append("&");
        }
        String canonData = sb.toString().substring(0, sb.length() - 1);

        String toSign = new StringBuffer("GET").append("&").append("/").append("&").append(canonData).toString();
        System.out.println("toSign:"+toSign);
        String digestValue = null;
        try {
            digestValue = URLEncoder.encode(toSign, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] serverSignBytes = HmacUtils.hmacSha1(secretKey, digestValue);
        String signValue = Base64.encodeBase64String(serverSignBytes);
//        vars.put("timestamp", timestamp);
//        vars.put("signNonce", signNonce);
//        vars.put("sign", signValue);
//        System.out.println("timestamp:"+timestamp);
//        System.out.println("signValue:"+signValue);
//        System.out.println("signNonce:"+signNonce);

//        String ppp = (toSign+="&Signature="+signValue).replaceAll("&","\n").replaceAll("=",":");
//        System.out.println(ppp);

        StringBuffer getUrl = new StringBuffer();
        Map<String,String> headerMap = new HashMap<String, String>();
        for (Object key : keys) {
            headerMap.put(key.toString(),params.get(key).toString());
        }
        keys.add("Signature");
        headerMap.put("Signature",signValue);


        String result[] = doGet(httpUrl,keys,headerMap);
//        System.out.println("result:"+result);
        return result;

    }

    public static void main(String[] args) {
       String data[] = getData(args);
       System.out.println("responseCode:"+data[1]);
       System.out.println("result:"+data[0]);
    }


}

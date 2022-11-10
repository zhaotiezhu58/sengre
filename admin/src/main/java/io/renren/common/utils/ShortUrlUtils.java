package io.renren.common.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ShortUrlUtils {

    public static String getShortUrl(String url) {
        try {
            Map<String, Object> p = new HashMap<>();
            p.put("url", url);
            HttpRequest request = HttpUtil.createPost("https://api.reurl.cc/shorten")
                    .header("Content-Type", "application/json")
                    .header("reurl-api-key", "4070ff49d794e33c14563b663c974755ecd3b334929b04df8a38b58d65165567c4f5d6")
                    .body(JSONUtil.toJsonStr(p));
            HttpResponse response = request.execute();
            log.info("响应报文:{}", response.body());
            JSONObject jsonObject = JSONUtil.parseObj(response.body());
            String shortUrl = jsonObject.getStr("short_url");
            return StringUtils.isNotBlank(shortUrl) ? shortUrl : url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
}

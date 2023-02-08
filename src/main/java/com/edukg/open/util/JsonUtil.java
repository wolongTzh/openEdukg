package com.edukg.open.util;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public class JsonUtil {

    /**
     * 将Json字符串转成Map
     *
     * @param jsonString
     * @return map
     */
    public static Map parseJsonToMap(String jsonString) {
        Map map = JSON.parseObject(jsonString, Map.class);
        System.err.println("Json转Map:");
        for (Object obj : map.keySet()) {
            System.err.print(obj + "-" + map.get(obj));
        }
        System.err.println();
        return map;
    }

    /**
     * 将Map转换成Json
     *
     * @param map
     * @return
     */
    public static String parseMapToJson(Map<String, Object> map) {
        String json = JSON.toJSONString(map);
        System.err.println("Map转Json:");
        System.err.println(json);
        return json;
    }


}

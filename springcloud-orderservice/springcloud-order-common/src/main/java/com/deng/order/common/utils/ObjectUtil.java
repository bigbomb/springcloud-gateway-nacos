package com.deng.order.common.utils;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cglib.beans.BeanMap;
//

/** 

* @author 作者 lujunjie: 

* @version 创建时间：Nov 3, 2019 11:20:21 AM 

* 类说明 

*/
public class ObjectUtil {

    public static String mapToString(Map<String, String[]> paramMap) throws JsonProcessingException {

        if (paramMap == null) {
            return "";
        }
        Map<String, Object> params = new HashMap<>(16);
        for (Map.Entry<String, String[]> param : paramMap.entrySet()) {

            String key = param.getKey();
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.put(key, paramValue);
        }
        ObjectMapper mapper = new ObjectMapper();
        return  mapper.writeValueAsString(params);
    }

    public static String mapToStringAll(Map<String, String[]> paramMap) throws JsonProcessingException {

        if (paramMap == null) {
            return "";
        }
        Map<String, Object> params = new HashMap<>(16);
        for (Map.Entry<String, String[]> param : paramMap.entrySet()) {

            String key = param.getKey();
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.put(key, paramValue);
        }
        ObjectMapper mapper = new ObjectMapper();
        return  mapper.writeValueAsString(params);
    }

    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }
}

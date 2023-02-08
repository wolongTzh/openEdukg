package com.edukg.open.queue;

import com.alibaba.fastjson.JSONArray;
import com.edukg.open.base.Response;
import com.edukg.open.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RelatedSubjectTask extends Thread {
    @Autowired
    private RequestQueue queue;
    @Value("${base.url}")
    private String baseUrl;

    private boolean running = true;

    @Override
    public void run() {
        while (running) {
            try {
                RelatedSubjectVo vo = queue.getRsQueue().take();
                System.out.println("开始处理请求");

                String course = vo.getCourse();
                String subjectName = vo.getSubjectName();
                String apiPath = "/relatedsubject";
                Map<String, Object> requestMap = new HashMap<>();
                requestMap.put("course", course);
                requestMap.put("subjectName", subjectName);
                String body = HttpUtil.sendPostDataByMap(baseUrl + ":8888" + apiPath, requestMap);
                try {
                    JSONArray re = JSONArray.parseArray(body);
                    vo.getResult().setResult(Response.success(re));
                } catch (Exception e) {
                    e.printStackTrace();
                    vo.getResult().setResult(Response.fail(-1, "请求异常"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }

        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}

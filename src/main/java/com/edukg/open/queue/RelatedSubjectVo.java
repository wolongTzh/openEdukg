package com.edukg.open.queue;

import com.alibaba.fastjson.JSONArray;
import com.edukg.open.base.Response;
import org.springframework.web.context.request.async.DeferredResult;

public class RelatedSubjectVo {
    private String subjectName;
    private String course;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public DeferredResult<Response<JSONArray>> getResult() {
        return result;
    }

    public void setResult(DeferredResult<Response<JSONArray>> result) {
        this.result = result;
    }

    private DeferredResult<Response<JSONArray>> result;

}

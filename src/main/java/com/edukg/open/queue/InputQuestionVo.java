package com.edukg.open.queue;

import com.alibaba.fastjson.JSONArray;
import com.edukg.open.base.Response;
import org.springframework.web.context.request.async.DeferredResult;

public class InputQuestionVo {
    private String course;
    private String inputQuestion;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getInputQuestion() {
        return inputQuestion;
    }

    public void setInputQuestion(String inputQuestion) {
        this.inputQuestion = inputQuestion;
    }

    public DeferredResult<Response<JSONArray>> getResult() {
        return result;
    }

    public void setResult(DeferredResult<Response<JSONArray>> result) {
        this.result = result;
    }

    private DeferredResult<Response<JSONArray>> result;

}

package com.edukg.open.queue;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class RequestQueue {
    private BlockingQueue<InputQuestionVo> qaQueue = new LinkedBlockingQueue<>(20);
    private BlockingQueue<RelatedSubjectVo> rsQueue = new LinkedBlockingQueue<>(20);

    public BlockingQueue<InputQuestionVo> getQaQueue() {
        return qaQueue;
    }
    public BlockingQueue<RelatedSubjectVo> getRsQueue() {
        return rsQueue;
    }
}

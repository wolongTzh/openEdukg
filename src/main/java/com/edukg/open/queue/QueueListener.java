package com.edukg.open.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class QueueListener {
    @Autowired
    private InputQuestionTask inputQuestionTask;
    @Autowired
    private RelatedSubjectTask relatedSubjectTask;

    /**
     * 初始化时启动监听请求队列
     */
    @PostConstruct
    public void init() {
        inputQuestionTask.start();
        relatedSubjectTask.start();
    }

    /**
     * 销毁容器时停止监听任务
     */
    @PreDestroy
    public void destory() {
        inputQuestionTask.setRunning(false);
        relatedSubjectTask.setRunning(false);
    }

}


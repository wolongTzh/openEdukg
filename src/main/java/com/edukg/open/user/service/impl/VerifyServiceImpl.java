package com.edukg.open.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edukg.open.user.entity.UserVerify;
import com.edukg.open.user.mapper.UserVerifyMapper;
import com.edukg.open.user.service.IVerifyService;
import com.edukg.open.util.DateUtil;
import com.edukg.open.util.Md5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 信息表 服务实现类
 * </p>
 *
 * @author wangjh
 * @since 2021-05-12
 */
@Service
public class VerifyServiceImpl extends ServiceImpl<UserVerifyMapper, UserVerify> implements IVerifyService {
    private static Logger log = LoggerFactory.getLogger(VerifyServiceImpl.class);

    @Override
    public Boolean validate(String appId, String secretKey, String host,
                            String phone, String smsCode) {
        try {
            // 当前时间
            Date currentDate = new Date();
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = df.format(currentDate);
            // 时间戳
            String timestamp = DateUtil.toString(currentDate, "yyyyMMddHHmmss");
            // 签名
            String sign = Md5.md5((appId + secretKey + timestamp).getBytes());
            // 消息内容
            String content = "【基础教育知识服务平台】欢迎使用平台服务，您的验证码是：" + smsCode + "，有效时间五分钟。如非本人操作请忽略。";
            String res="";
            StringBuffer buffer = new StringBuffer();
            String requestUrl = host + "/inter/sms/mt" + "?appKey=" + appId +
                    "&sign=" + sign +
                    "&timestamp=" + timestamp +
                    "&mobiles=" + phone +
                    "&content=" + URLEncoder.encode(content, "utf-8");
            System.out.println("---------" + requestUrl);
            URL url = new URL(requestUrl);
            HttpURLConnection urlCon= (HttpURLConnection)url.openConnection();
            urlCon.connect();
            if (200==urlCon.getResponseCode()) {
                InputStream is = urlCon.getInputStream();
                InputStreamReader isr = new InputStreamReader(is,"utf-8");
                BufferedReader br = new BufferedReader(isr);

                String str = null;
                while((str = br.readLine())!=null){
                    buffer.append(str);
                }
                br.close();
                isr.close();
                is.close();
                res = buffer.toString();
                JSONObject jsonObject = JSONObject.parseObject(res);
                String code = jsonObject.get("code").toString();
                if ("SUCCESS".equals(code)) {
                    UserVerify record = new UserVerify();
                    record.setCode(smsCode);
                    record.setPhone(phone);
                    record.setStatus("0");
                    record.setCreatTime(currentTime);
                    record.setUpdateTime(currentTime);
                    save(record);
                    return true;
                } else {
                    log.error("-------手机号：" + phone + "获取验证码失败。code = " + code);
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

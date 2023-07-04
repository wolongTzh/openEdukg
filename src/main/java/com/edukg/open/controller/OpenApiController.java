package com.edukg.open.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.edukg.open.base.BusinessException;
import com.edukg.open.base.Response;
import com.edukg.open.config.LimitRequest;
import com.edukg.open.config.LimitRequestForQa;
import com.edukg.open.config.SystemControllerLog;
import com.edukg.open.queue.InputQuestionVo;
import com.edukg.open.queue.RelatedSubjectVo;
import com.edukg.open.queue.RequestQueue;
import com.edukg.open.user.service.ISysUserLogService;
import com.edukg.open.util.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.*;


/**
 * <p>
 * 开放api
 * </p>
 *
 * @author liufeifei
 * @since 2021-05-12
 */
@RestController
@Controller
@RequestMapping("/api/open")
@Api(tags = "开放api相关接口")
public class OpenApiController {


    private static final Logger LOG = LoggerFactory.getLogger(OpenApiController.class);

    @Autowired
    private RequestQueue queue;

    private void checkSession(HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            System.out.println("OpenApiController!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            throw new BusinessException(-1, "请先登录");
        }
    }

    @Value("${base.url}")
    private String baseUrl;
    @Resource
    private ISysUserLogService sysUserLogService;
    /**
     * 1.实体搜索接口  http://39.100.31.203:8008/instanceList
     *
     * @param request
     * @param searchKey
     * @param subject
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "实体搜索接口", notes = "实体搜索接口", httpMethod = "GET")
    @RequestMapping(value = "instanceList", method = RequestMethod.GET)
    @SystemControllerLog(description = "实体搜索接口")
    @LimitRequest()
    public Response<JSONArray> instanceList(HttpServletRequest request,
                                            @ApiParam(value = "请输入关键字", required = true) @RequestParam("searchKey") String searchKey,
                                            @ApiParam(value = "请选择学科", defaultValue = "chinese", required = true, allowableValues = "chinese,english,math,physics,chemistry,biology,politics,geo,history") @RequestParam("course") String subject) throws IOException {
//        checkSession(request);
        LOG.info("请求接口记录 - /instanceList -");
        LOG.info(new Date().toString());
        String apiPath = "/instanceList?searchKey=" + searchKey + "&subject=" + subject;
        String body = HttpUtil.sendGetData(baseUrl + ":8008" + apiPath);
        try {
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (jsonObject.get("data") != null) {
                return Response.success(jsonObject.getJSONArray("data"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }

    /**
     * 2. 实体详情接口 http://39.100.31.203:8001/api/wiki/infoByInstanceName
     *
     * @param request
     * @param name
     * @param subject
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "实体详情接口", notes = "实体详情接口", httpMethod = "GET")
    @RequestMapping(value = "infoByInstanceName", method = RequestMethod.GET)
    @SystemControllerLog(description = "实体详情接口")
    @LimitRequest()
    public Response<JSONObject> infoByInstanceName(HttpServletRequest request,
                                                   @RequestParam("name") String name,
                                                   String uri,
                                                   @ApiParam(value = "请选择学科", required = false, defaultValue = "chinese", allowableValues = "chinese,english,math,physics,chemistry,biology,politics,geo,history") @RequestParam("course") String subject) throws IOException {
        checkSession(request);
        LOG.info("请求接口记录 - /infoByInstanceName -");
        LOG.info(new Date().toString());
        if (subject.equals("math")) {
            JSONArray content = new JSONArray();
            JSONArray property = new JSONArray();
            String label = "";
            HttpPost httpPost = new HttpPost("http://39.97.172.123:28090" + "/server/getInstGraph");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("uri", uri));
            try {
                CloseableHttpClient client = HttpClients.createDefault();
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf8"));
                CloseableHttpResponse response = client.execute(httpPost);
                try {
                    if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                        HttpEntity resEntity = response.getEntity();
                        content = JSONArray.parseObject(EntityUtils.toString(resEntity)).getJSONArray("content");
                    }
                } finally {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String apiPath = "/api/wiki/infoByInstanceName?name=" + name + "&subject=" + subject;
            String body = HttpUtil.sendGetData(baseUrl + ":8001" + apiPath);
//        String body = HttpUtil.sendGetData(serverPath8001 + apiPath);
            try {
                JSONObject jsonObject = JSONObject.parseObject(body);
                if (jsonObject.get("data") != null) {
                   property = jsonObject.getJSONObject("data").getJSONArray("property");
                   label = jsonObject.getJSONObject("data").getString("label");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("content", content);
            jsonObject.put("property", property);
            jsonObject.put("label", label);
            return Response.success(jsonObject);
        }
        String apiPath = "/api/wiki/infoByInstanceName?name=" + name + "&subject=" + subject;
        String body = HttpUtil.sendGetData(baseUrl + ":8001" + apiPath);
//        String body = HttpUtil.sendGetData(serverPath8001 + apiPath);
        try {
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (jsonObject.get("data") != null) {
                return Response.success(jsonObject.getJSONObject("data"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }


    /**
     * 3. 实体相关习题接口 http://39.100.31.203:8001/api/wiki/questionListByUriName
     *
     * @param request
     * @param uriName
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "实体相关习题接口", notes = "实体相关习题接口", httpMethod = "GET")
    @RequestMapping(value = "questionListByUriName", method = RequestMethod.GET)
    @SystemControllerLog(description = "实体详情接口")
    @LimitRequest()
    public Response<Object> questionListByUriName(HttpServletRequest request,
                                                  @RequestParam("uriName") String uriName) throws IOException {
        checkSession(request);
        LOG.info("请求接口记录 - /questionListByUriName -");
        LOG.info(new Date().toString());
        String apiPath = "/api/wiki/questionListByUriName?uriName=" + uriName;
        String body = HttpUtil.sendGetData(baseUrl + ":8001" + apiPath);
//        String body = HttpUtil.sendGetData(serverPath8001 + apiPath);
        try {
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (jsonObject.get("data") != null) {
                return Response.success(jsonObject.get("data"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }


    /**
     * 4. 问答接口 http://39.100.31.203:8888/course/inputQuestion
     *
     * @param request
     * @param course
     * @param inputQuestion
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "问答接口", notes = "问答接口", httpMethod = "POST")
    @RequestMapping(value = "/inputQuestion", method = RequestMethod.POST)
    @SystemControllerLog(description = "问答接口")
    @LimitRequestForQa()
    @LimitRequest()
    public DeferredResult<Response<JSONArray>> inputQuestion(HttpServletRequest request,
                                             @ApiParam(value = "请选择学科", required = false, defaultValue = "chinese", allowableValues = "chinese,english,math,physics,chemistry,biology,politics,geo,history") @RequestParam(required = false) String course,
                                             @ApiParam(value = "请输入问题", required = true) @RequestParam(required = true) String inputQuestion) throws IOException {
        checkSession(request);
        LOG.info("请求接口记录 - /inputQuestion -");
        LOG.info(new Date().toString());
        InputQuestionVo vo = new InputQuestionVo();
        vo.setCourse(course);
        vo.setInputQuestion(inputQuestion);
        DeferredResult<Response<JSONArray>> result = new DeferredResult<>();
        vo.setResult(result);
        try {
            queue.getQaQueue().put(vo);
            return result;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 5. 知识链接接口  http://39.100.31.203:8077/linkInstance
     *
     * @param request
     * @param course
     * @param context
     * @return
     * @throws IOException
     */
    @LimitRequest()
    @ApiOperation(value = "知识链接接口", notes = "知识链接接口", httpMethod = "POST")
    @RequestMapping(value = "/linkInstance", method = RequestMethod.POST)
    @SystemControllerLog(description = "知识链接接口")
    public Response<JSONObject> linkInstance(HttpServletRequest request,
                                             @ApiParam(value = "请选择学科", defaultValue = "chinese", allowableValues = "chinese,english,math,physics,chemistry,biology,politics,geo,history") @RequestParam(required = true) String course,
                                             @ApiParam(value = "请输入文本", required = true) @RequestParam(required = true) String context) throws IOException {
        checkSession(request);
        LOG.info("请求接口记录 - /linkInstance -");
        LOG.info(new Date().toString());
        String apiPath = "/linkInstance";
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("course", course);
        requestMap.put("context", context);
        String body = HttpUtil.sendPostDataByMap(baseUrl + ":8077" + apiPath, requestMap);
//        String body = HttpUtil.sendPostDataByMap(serverPath8077 + apiPath, requestMap);
        try {
            return Response.success(JSONObject.parseObject(body));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }


    //

    /**
     * 6. 知识链接属性详情接口 http://39.100.31.203:8007/getKnowledgeCard
     *
     * @param request
     * @param course
     * @param uri
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "知识链接属性详情接口", notes = "知识链接属性详情接口", httpMethod = "POST")
    @RequestMapping(value = "/getKnowledgeCard", method = RequestMethod.POST)
    @SystemControllerLog(description = "知识链接属性详情接口")
    @LimitRequest()
    public Response<JSONObject> getKnowledgeCard(HttpServletRequest request,
                                                 @ApiParam(value = "请选择学科", defaultValue = "chinese", allowableValues = "chinese,english,math,physics,chemistry,biology,politics,geo,history") @RequestParam(required = true) String course,
                                                 @ApiParam(value = "请输入uri", required = true) @RequestParam(required = true) String uri) throws IOException {
        checkSession(request);
        LOG.info("请求接口记录 - /getKnowledgeCard -");
        LOG.info(new Date().toString());
        String apiPath = "/getKnowledgeCard";
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("course", course);
        requestMap.put("uri", uri);
        String body = HttpUtil.sendPostDataByMap(baseUrl + ":8007" + apiPath, requestMap);
//        String body = HttpUtil.sendPostDataByMap(serverPath8007 + apiPath, requestMap);
        try {
            JSONObject jsonObject = JSONObject.parseObject(body);
            return Response.success(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }


    /**
     * 7. 知识关联详情接口 http://39.100.31.203:8888/relatedsubject
     *
     * @param request
     * @param subjectName
     * @param course
     * @return
     * @throws IOException
     */

    @ApiOperation(value = "知识关联详情接口", notes = "知识关联详情接口", httpMethod = "POST")
    @RequestMapping(value = "/relatedsubject", method = RequestMethod.POST)
    @SystemControllerLog(description = "知识关联详情接口")
    @LimitRequestForQa()
    @LimitRequest()
    public DeferredResult<Response<JSONArray>> relatedsubject(HttpServletRequest request,
                                              @ApiParam(value = "请输入需要查询的相关词条", required = true) @RequestParam(required = true) String subjectName,
                                              @ApiParam(value = "请选择学科", required = true, defaultValue = "chinese", allowableValues = "chinese,english,physics,chemistry,biology,politics,geo,history") @RequestParam(required = true) String course) throws IOException {
        checkSession(request);
        LOG.info("请求接口记录 - /relatedsubject -");
        LOG.info(new Date().toString());
        RelatedSubjectVo vo = new RelatedSubjectVo();
        vo.setCourse(course);
        vo.setSubjectName(subjectName);
        DeferredResult<Response<JSONArray>> result = new DeferredResult<>();
        vo.setResult(result);
        try {
            queue.getRsQueue().put(vo);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}

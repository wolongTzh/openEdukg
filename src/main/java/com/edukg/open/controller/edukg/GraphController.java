package com.edukg.open.controller.edukg;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.edukg.open.base.Response;
import com.edukg.open.config.LimitRequest;
import com.edukg.open.config.SystemControllerLog;
import com.edukg.open.util.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * edukg api
 * </p>
 *
 * @author tanzheng
 * @since 2023-02-27
 */
@RestController
@Controller
@Slf4j
@RequestMapping("/api/graph")
@Api(tags = "edukg 后端服务转发")
public class GraphController {

    @Value("${base.url}")
    private String baseUrl;

    /**
     * 1.实体名称搜索接口  http://39.97.172.123:8081/api/graph/findInstanceByName
     *
     * @param request
     * @param searchText
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "实体名称搜索接口", notes = "实体名称搜索接口", httpMethod = "GET")
    @RequestMapping(value = "findInstanceByName", method = RequestMethod.GET)
    @SystemControllerLog(description = "实体名称搜索接口")
    @LimitRequest()
    public Response<JSONArray> findInstanceByName(HttpServletRequest request,
                                            @ApiParam(value = "请输入关键字", required = true) @RequestParam("searchText") String searchText) throws IOException {
//        checkSession(request);
        log.info("请求接口记录 - /findInstanceByName -");
        log.info(new Date().toString());
        String apiPath = "/api/graph/findInstanceByName?searchText=" + searchText;
        String body = HttpUtil.sendGetData(baseUrl + ":8081" + apiPath);
        try {
            JSONObject jsonObject = JSONObject.parseObject(body);
            return Response.success(jsonObject.getJSONArray("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }

    /**
     * 2.实体详情获取接口  http://39.97.172.123:8081/api/graph/getInstanceInfo
     *
     * @param request
     * @param uri
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "实体详情获取接口", notes = "实体详情获取接口", httpMethod = "GET")
    @RequestMapping(value = "getInstanceInfo", method = RequestMethod.GET)
    @SystemControllerLog(description = "实体详情获取接口")
    @LimitRequest()
    public Response<JSONObject> getInstanceInfo(HttpServletRequest request,
                                            @ApiParam(value = "请输入uri", required = true) @RequestParam("uri") String uri) throws IOException {
//        checkSession(request);
        log.info("请求接口记录 - /getInstanceInfo -");
        log.info(new Date().toString());
        String apiPath = "/api/graph/getInstanceInfo?uri=" + uri.replace("#", "%23");
        String body = HttpUtil.sendGetData(baseUrl + ":8081" + apiPath);
        try {
            JSONObject jsonObject = JSONObject.parseObject(body);
            return Response.success(jsonObject.getJSONObject("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }

    /**
     * 3.知识链接（实体识别）接口  http://39.97.172.123:8081/api/graph/instanceLinking
     *
     * @param request
     * @param searchText
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "知识链接（实体识别）接口", notes = "知识链接（实体识别）接口", httpMethod = "POST")
    @RequestMapping(value = "instanceLinking", method = RequestMethod.POST)
    @SystemControllerLog(description = "知识链接（实体识别）接口")
    @LimitRequest()
    public Response<JSONArray> instanceLinking(HttpServletRequest request,
                                               @ApiParam(value = "请输入文本段", required = true) @RequestParam("searchText") String searchText) throws IOException {
//        checkSession(request);
        log.info("请求接口记录 - /instanceLinking -");
        log.info(new Date().toString());
        String apiPath = "/api/graph/instanceLinking";
        JSONObject json = new JSONObject();
        json.put("searchText", searchText);
        String body = HttpUtil.sendPostDataByJson(baseUrl + ":8081" + apiPath, JSONObject.toJSONString(json));
        try {
            JSONObject jsonObject = JSONObject.parseObject(body);
            return Response.success(jsonObject.getJSONArray("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }

    /**
     * 4.问答接口  http://39.97.172.123:8081/api/graph/qa
     *
     * @param request
     * @param question
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "问答接口", notes = "问答接口", httpMethod = "POST")
    @RequestMapping(value = "qa", method = RequestMethod.POST)
    @SystemControllerLog(description = "问答接口")
    @LimitRequest()
    public Response<JSONObject> qa(HttpServletRequest request,
                                               @ApiParam(value = "请输入问题", required = true) @RequestParam("question") String question) throws IOException {
//        checkSession(request);
        log.info("请求接口记录 - /qa -");
        log.info(new Date().toString());
        String apiPath = "/api/graph/qa";
        JSONObject json = new JSONObject();
        json.put("question", question);
        String body = HttpUtil.sendPostDataByJson(baseUrl + ":8081" + apiPath, JSONObject.toJSONString(json));
        try {
            JSONObject jsonObject = JSONObject.parseObject(body);
            return Response.success(jsonObject.getJSONObject("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }

    /**
     * 5.最短路径查询接口  http://39.97.172.123:8081/api/graph/findPath
     *
     * @param request
     * @param instanceList
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "最短路径查询接口", notes = "最短路径查询接口", httpMethod = "POST")
    @RequestMapping(value = "findPath", method = RequestMethod.POST)
    @SystemControllerLog(description = "最短路径查询接口")
    @LimitRequest()
    public Response<JSONArray> findPath(HttpServletRequest request,
                                  @ApiParam(value = "请输入问题", required = true) @RequestParam("instanceList") List<String> instanceList) throws IOException {
//        checkSession(request);
        log.info("请求接口记录 - /findPath -");
        log.info(new Date().toString());
        String apiPath = "/api/graph/findPath";
        JSONObject json = new JSONObject();
        json.put("instanceList", instanceList);
        String body = HttpUtil.sendPostDataByJson(baseUrl + ":8081" + apiPath , JSONObject.toJSONString(json));
        try {
            JSONObject jsonObject = JSONObject.parseObject(body);
            return Response.success(jsonObject.getJSONArray("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }
}

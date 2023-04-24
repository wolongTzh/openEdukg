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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@RequestMapping("/api/resource")
@Api(tags = "edukg 后端服务转发")
public class ResourceController {

    @Value("${base.url}")
    private String baseUrl;

    /**
     * 1.试题资源列表获取接口  http://39.97.172.123:8081/api/resource/findQuestion
     *
     * @param request
     * @param searchText
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "试题资源列表获取接口", notes = "试题资源列表获取接口", httpMethod = "GET")
    @RequestMapping(value = "findQuestion", method = RequestMethod.GET)
    @SystemControllerLog(description = "试题资源列表获取接口")
    @LimitRequest(loginPrompt = "登录以查看更多试题资源")
    public Response<JSONObject> findQuestion(HttpServletRequest request,
                                                  @ApiParam(value = "请输入关键字", required = true) @RequestParam("searchText") String searchText,
                                                  @ApiParam(value = "请输入searchText的类型", required = true) @RequestParam("type") String type,
                                                  @ApiParam(value = "请输入页码", required = true) @RequestParam("pageNo") Integer pageNo,
                                                  @ApiParam(value = "请输入页容量", required = true) @RequestParam("pageSize") String pageSize) throws IOException {
//        checkSession(request);
        log.info("请求接口记录 - /findQuestion -");
        log.info(new Date().toString());
        String apiPath = "/api/resource/findQuestion?searchText=" + searchText + "&type=" + type + "&pageNo=" + pageNo + "&pageSize=" + pageSize;
        String body = HttpUtil.sendGetData(baseUrl + ":8081" + apiPath);
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
     * 2.电子书资源列表获取接口  http://39.97.172.123:8081/api/resource/findBook
     *
     * @param request
     * @param searchText
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "电子书资源列表获取接口", notes = "电子书资源列表获取接口", httpMethod = "GET")
    @RequestMapping(value = "findBook", method = RequestMethod.GET)
    @SystemControllerLog(description = "电子书资源列表获取接口")
    @LimitRequest(loginPrompt = "登录以查看更多教材资源")
    public Response<JSONObject> findBook(HttpServletRequest request,
                                                  @ApiParam(value = "请输入关键字", required = true) @RequestParam("searchText") String searchText,
                                                  @ApiParam(value = "请输入searchText的类型", required = true) @RequestParam("type") String type,
                                                  @ApiParam(value = "请输入页码", required = true) @RequestParam("pageNo") Integer pageNo,
                                                  @ApiParam(value = "请输入页容量", required = true) @RequestParam("pageSize") String pageSize) throws IOException {
//        checkSession(request);
        log.info("请求接口记录 - /findBook -");
        log.info(new Date().toString());
        String apiPath = "/api/resource/findBook?searchText=" + searchText + "&type=" + type + "&pageNo=" + pageNo + "&pageSize=" + pageSize;
        String body = HttpUtil.sendGetData(baseUrl + ":8081" + apiPath);
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
     * 3.电子书内容获取接口  http://39.97.172.123:8081/api/resource/getBookData
     *
     * @param request
     * @param chapterId
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "电子书内容获取接口", notes = "电子书内容获取接口", httpMethod = "GET")
    @RequestMapping(value = "getBookData", method = RequestMethod.GET)
    @SystemControllerLog(description = "电子书内容获取接口")
    @LimitRequest()
    public Response<JSONObject> getBookData(HttpServletRequest request,
                                        @ApiParam(value = "请输入章节id", required = true) @RequestParam("chapterId") String chapterId) throws IOException {
//        checkSession(request);
        log.info("请求接口记录 - /getBookData -");
        log.info(new Date().toString());
        String apiPath = "/api/resource/getBookData?chapterId=" + chapterId;
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
     * 4.课程资源实体匹配接口  http://39.97.172.123:8081/api/resource/findCourse
     *
     * @param request
     * @param uri
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "课程资源实体匹配接口", notes = "课程资源实体匹配接口", httpMethod = "GET")
    @RequestMapping(value = "findCourse", method = RequestMethod.GET)
    @SystemControllerLog(description = "课程资源实体匹配接口")
    @LimitRequest()
    public Response<JSONArray> findCourse(HttpServletRequest request,
                                        @ApiParam(value = "请输入uri", required = true) @RequestParam("uri") String uri) throws IOException {
//        checkSession(request);
        log.info("请求接口记录 - /findCourse -");
        log.info(new Date().toString());
        String apiPath = "/api/resource/findCourse?uri=" + uri.replace("#", "%23");
        String body = HttpUtil.sendGetData(baseUrl + ":8081" + apiPath);
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
}

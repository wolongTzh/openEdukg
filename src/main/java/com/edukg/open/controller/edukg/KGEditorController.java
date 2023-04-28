package com.edukg.open.controller.edukg;

import com.alibaba.fastjson.JSONObject;
import com.edukg.open.base.Response;
import com.edukg.open.config.LimitRequest;
import com.edukg.open.config.SystemControllerLog;
import com.edukg.open.model.param.StartExtractionParam;
import com.edukg.open.model.param.savePartitionParam;
import com.edukg.open.model.param.SaveGraphParam;
import com.edukg.open.util.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * <p>
 * kgeditor api
 * </p>
 *
 * @author tanzheng
 * @since 2023-02-27
 */
@RestController
@Controller
@Slf4j
@RequestMapping("/kgeditor")
@Api(tags = "edukg 后端服务转发")
public class KGEditorController {

    @Value("${base.kgeditor}")
    private String baseUrl;

    /**
     * 1.创建任务接口  http://39.97.172.123:8001/extract/create_task/
     *
     * @param request
     * @param file
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "创建任务接口", notes = "创建任务接口", httpMethod = "POST")
    @RequestMapping(value = "createTask", method = RequestMethod.POST)
    @SystemControllerLog(description = "创建任务接口")
//    @LimitRequest()
    public Response<JSONObject> createTask(HttpServletRequest request,
                                           @ApiParam(value = "请输入用户id", required = true) @RequestParam("userId") String userId,
                                               @ApiParam(value = "请上传文件", required = true) @RequestParam("file") MultipartFile file,
                                          @ApiParam(value = "请输入文件名称", required = true) @RequestParam("name") String name,
                                          @ApiParam(value = "请目录起始页码", required = true) @RequestParam("catalogBeginPage") int catalogBeginPage,
                                          @ApiParam(value = "请目录终止页码", required = true) @RequestParam("catalogEndPage") int catalogEndPage) throws IOException {
//        checkSession(request);
        log.info("请求接口记录 - /createTask -");
        log.info(new Date().toString());
        String apiPath = "/extract/create_task/";
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("userId", userId);
        json.put("catalogBeginPage", catalogBeginPage);
        json.put("catalogEndPage", catalogEndPage);
        log.info("json = " + JSONObject.toJSONString(json));
        json.put("file", file);
        String body = HttpUtil.sendPostDataByJsonWithFile(baseUrl + ":8001" + apiPath, json);
        log.info("body = " + body);
        try {
            JSONObject jsonObject = JSONObject.parseObject(body);
            return Response.success(jsonObject.getJSONObject("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }

    /**
     * 2.获取任务列表  http://39.97.172.123:8001/extract/get_task_list/
     *
     * @param request
     * @param userId
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "获取任务列表", notes = "获取任务列表", httpMethod = "GET")
    @RequestMapping(value = "getTaskList", method = RequestMethod.GET)
    @SystemControllerLog(description = "获取任务列表")
//    @LimitRequest()
    public Response<JSONObject> getTaskList(HttpServletRequest request,
                                           @ApiParam(value = "请输入用户id", required = true) @RequestParam("userId") String userId) throws IOException {
//        checkSession(request);
        log.info("请求接口记录 - /getTaskList -");
        log.info(new Date().toString());
        String apiPath = "/extract/get_task_list?userId=" + userId;
        log.info("apiPath : " + apiPath);
        String body = HttpUtil.sendGetData(baseUrl + ":8001" + apiPath);
        log.info("body = " + body);
        try {
            JSONObject jsonObject = JSONObject.parseObject(body);
            return Response.success(jsonObject.getJSONObject("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }

    /**
     * 3.获取课本解析结果  http://39.97.172.123:8001/extract/get_partition/
     *
     * @param request
     * @param userId
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "获取课本解析结果", notes = "获取课本解析结果", httpMethod = "GET")
    @RequestMapping(value = "getPartition", method = RequestMethod.GET)
    @SystemControllerLog(description = "获取课本解析结果")
//    @LimitRequest()
    public Response<JSONObject> getPartition(HttpServletRequest request,
                                            @ApiParam(value = "请输入用户id", required = true) @RequestParam("userId") String userId,
                                             @ApiParam(value = "请输入任务id", required = true) @RequestParam("taskId") String id) throws IOException {
//        checkSession(request);
        log.info("请求接口记录 - /getPartition -");
        log.info(new Date().toString());
        String apiPath = "/extract/get_partition?userId=" + userId + "&id=" + id;
        log.info("apiPath : " + apiPath);
        String body = HttpUtil.sendGetData(baseUrl + ":8001" + apiPath);
//        log.info("body = " + body);
        try {
            JSONObject jsonObject = JSONObject.parseObject(body);
            return Response.success(jsonObject.getJSONObject("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }

    /**
     * 4.保存课本解析结果，用于校对  http://39.97.172.123:8001/extract/save_partition/
     *
     * @param request
     * @param param
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "保存课本解析结果，用于校对", notes = "保存课本解析结果，用于校对", httpMethod = "POST")
    @RequestMapping(value = "savePartition", method = RequestMethod.POST)
    @SystemControllerLog(description = "保存课本解析结果，用于校对")
//    @LimitRequest()
    public Response<JSONObject> savePartition(HttpServletRequest request,
                                              @ApiParam(value = "请输入用户id", required = true) @RequestBody savePartitionParam param) throws IOException {
//        checkSession(request);
        log.info("请求接口记录 - /savePartition -");
        log.info(new Date().toString());
        String apiPath = "/extract/save_partition/";
        JSONObject json = new JSONObject();
        json.put("userId", param.getUserId());
        json.put("id", param.getTaskId());
        json.put("partition", param.getPartition());
//        log.info("json = " + JSONObject.toJSONString(json));
        String body = HttpUtil.sendPostDataByJson(baseUrl + ":8001" + apiPath, JSONObject.toJSONString(json));
        log.info("body = " + body);
        try {
            JSONObject jsonObject = JSONObject.parseObject(body);
            return Response.success(jsonObject.getJSONObject("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }

    /**
     * 5.开始抽取  http://39.97.172.123:8001/extract/start_extraction/
     *
     * @param request
     * @param param
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "开始抽取", notes = "开始抽取", httpMethod = "POST")
    @RequestMapping(value = "startExtraction", method = RequestMethod.POST)
    @SystemControllerLog(description = "开始抽取")
//    @LimitRequest()
    public Response<String> startExtraction(HttpServletRequest request,
                                            @ApiParam(value = "请输入用户id", required = true) @RequestBody StartExtractionParam param) throws IOException {
//        checkSession(request);
        log.info("请求接口记录 - /startExtraction -");
        log.info(new Date().toString());
        String apiPath = "/extract/start_extraction/";
        JSONObject json = new JSONObject();
        json.put("userId", param.getUserId());
        json.put("id", param.getTaskId());
        log.info("json = " + JSONObject.toJSONString(json));
        String body = HttpUtil.sendPostDataByJson(baseUrl + ":8001" + apiPath, JSONObject.toJSONString(json));
        log.info("body = " + body);
        try {
            JSONObject jsonObject = JSONObject.parseObject(body);
            return Response.success((String) jsonObject.get("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }

    /**
     * 6.获取构建的图谱  http://39.97.172.123:8001/extract/get_graph/
     *
     * @param request
     * @param userId
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "获取构建的图谱", notes = "获取构建的图谱", httpMethod = "GET")
    @RequestMapping(value = "getGraph", method = RequestMethod.GET)
    @SystemControllerLog(description = "获取构建的图谱")
//    @LimitRequest()
    public Response<JSONObject> getGraph(HttpServletRequest request,
                                             @ApiParam(value = "请输入用户id", required = true) @RequestParam("userId") String userId,
                                             @ApiParam(value = "请输入任务id", required = true) @RequestParam("taskId") String id) throws IOException {
//        checkSession(request);
        log.info("请求接口记录 - /getGraph -");
        log.info(new Date().toString());
        String apiPath = "/extract/get_graph?userId=" + userId + "&id=" + id;
        log.info("apiPath : " + apiPath);
        String body = HttpUtil.sendGetData(baseUrl + ":8001" + apiPath);
//        log.info("body = " + body);
        try {
            JSONObject jsonObject = JSONObject.parseObject(body);
            return Response.success(jsonObject.getJSONObject("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }

    /**
     * 7.保存图谱，用于校对  http://39.97.172.123:8001/extract/save_graph/
     *
     * @param request
     * @param param
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "保存图谱，用于校对", notes = "保存图谱，用于校对", httpMethod = "POST")
    @RequestMapping(value = "saveGraph", method = RequestMethod.POST)
    @SystemControllerLog(description = "保存图谱，用于校对")
//    @LimitRequest()
    public Response<String> saveGraph(HttpServletRequest request,
                                      @ApiParam(value = "请输入用户id", required = true) @RequestBody SaveGraphParam param) throws IOException {
//        checkSession(request);
        log.info("请求接口记录 - /saveGraph -");
        log.info(new Date().toString());
        String apiPath = "/extract/save_graph/";
        JSONObject json = new JSONObject();
        json.put("userId", param.getUserId());
        json.put("id", param.getTaskId());
        json.put("termdef", param.getTermdef());
//        log.info("json = " + JSONObject.toJSONString(json));
        String body = HttpUtil.sendPostDataByJson(baseUrl + ":8001" + apiPath, JSONObject.toJSONString(json));
        log.info("body = " + body);
        try {
            JSONObject jsonObject = JSONObject.parseObject(body);
            return Response.success((String) jsonObject.get("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.fail(-1, "请求异常");
    }
}

package com.edukg.open.controller;

import com.edukg.open.base.BusinessException;
import com.edukg.open.base.Response;
import com.edukg.open.shiro.ShiroRealm;
import com.edukg.open.user.entity.UserInfo;
import com.edukg.open.user.service.IUserService;
import com.edukg.open.user.service.IVerifyService;
import com.edukg.open.util.Md5Util;
import com.edukg.open.util.SmsValidateCodeGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author liufeifei
 * @since 2021-05-11
 */
@RestController
@Controller
@RequestMapping("/api/user")
@Api(tags = "用户相关接口")
public class UserController {

    @Value("${emay.appId}")
    private String appId;
    @Value("${emay.secretKey}")
    private String secretKey;
    @Value("${emay.host}")
    private String host;
    @Resource
    private IUserService userService;
    @Resource
    private IVerifyService verifyService;
    @Resource
    private ShiroRealm shiroRealm;


    /**
     * 1-1 注册 post /api/user/register
     *
     * @param email
     * @param password
     * @return
     */
    @ApiOperation(value = "注册用户", notes = "注册用户", httpMethod = "POST")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Response<String> register(
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("userType") String userType,
            @RequestParam("userName") String userName,
            @RequestParam("validateCode") String validateCode,
            HttpServletResponse response) {
        UserInfo user = userService.register(phone, email, password, userType, userName, validateCode);

        if (user != null) {
            Cookie c1 = new Cookie("userName", user.getUserName());
            c1.setMaxAge(3600);
            response.addCookie(c1);
            Cookie c2 = new Cookie("phone", user.getPhone());
            //设置生命周期为1小时，秒为单位
            c2.setMaxAge(3600);
            response.addCookie(c2);
            return Response.success("注册成功");
        } else {
            return Response.error("-1", "注册失败");
        }
    }

    /**
     * 1-2 登录 post /api/user/login
     *
     * @param phone
     * @param password
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response<String> login(@RequestParam("phone") String phone,
                                  @RequestParam("password") String password,
                                  HttpServletRequest request) {
        System.out.println("phone = " + phone);
        System.out.println("password = " + password);
        //添加用户认证信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                phone,
                Md5Util.getMD5String(phone + password));

        //进行验证，这里可以捕获异常，然后返回对应信息
        System.out.println("tag 1");
        subject.login(usernamePasswordToken);
        System.out.println("tag 2");
        //设置session
        HttpSession session = request.getSession();
        session.setAttribute("user", userService.findByPhone(phone));
        session.setMaxInactiveInterval(60 * 60 * 24 * 3);//以秒为单位
        return Response.success("登录成功");


    }

    /**
     * 1-3 确认登录状态 get /api/user/getUserInfo
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "请求用户个人信息", notes = "请求用户个人信息", httpMethod = "GET")
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public Response<UserInfo> getUserInfo(HttpServletRequest request) {
        UserInfo thisUser = checkSession(request);
        if (thisUser != null) {
            return Response.success(thisUser);
        } else {
            return Response.error("-1", "请求不到");
        }

    }

    /**
     * 1-4 登出 get /api/user/logout
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "登出", notes = "登出", httpMethod = "POST")
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public Response<Boolean> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return Response.success(true);

    }


    private UserInfo checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserInfo thisUser = (UserInfo) session.getAttribute("user");
        if (thisUser == null) {
            System.out.println("UserController!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            throw new BusinessException(-1, "请先登录");
        }
        return thisUser;
    }

    /**
     * 1-5 用户邮箱、手机重复校验
     *
     * @param type  ‘email’ || ‘phone'
     * @param value
     * @return
     */
    @ApiOperation(value = "用户邮箱、手机重复校验", notes = "用户邮箱、手机重复校验", httpMethod = "GET")
    @RequestMapping(value = "/checkUser", method = RequestMethod.GET)
    public Response<Boolean> checkUser(@RequestParam("type") String type,
                                       @RequestParam("value") String value) {
        switch (type) {
            case "email":
                return Response.success(userService.checkEmail(value));

            case "phone":
                return Response.success(userService.checkPhone(value));

            default:
                break;
        }
        return Response.error("-1", "异常参数值");

    }


    /**
     * 1-7 用户修改密码 post /api/user/editpw
     *
     * @param phone
     * @return
     */

    @ApiOperation(value = "用户修改密码", notes = "用户修改密码", httpMethod = "POST")
    @RequestMapping(value = "/editpw", method = RequestMethod.POST)
    public Response<Boolean> editpw(
            @RequestParam("phone") String phone,
            @RequestParam("newpw") String newpw,
            @RequestParam("validateCode") String validateCode
    ) {
        return Response.success(userService.editpw(phone, newpw, validateCode));
    }
    /**
     * 用户申请验证码
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "用户申请验证码", notes = "用户申请验证码", httpMethod = "GET")
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    public Response<Boolean> validate(@RequestParam("phone") String phone) {
        // 验证码生成
        String smsCode = SmsValidateCodeGenerator.generate();

        if (verifyService.validate(appId, secretKey, host, phone, smsCode)) {
            return Response.success(true, "申请验证码成功");
        } else {
            return Response.success(false, "申请验证码失败，请重试");
        }
    }
}

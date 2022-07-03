package com.example.emos.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.example.emos.api.common.util.R;
import com.example.emos.api.controller.form.*;
import com.example.emos.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@RestController
@RequestMapping("/user")
@Tag(name = "UserController", description = "用户Web接口")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 生成登陆二维码的字符串
     */
    @GetMapping("/createQrCode")
    @Operation(summary = "生成二维码Base64格式的字符串")
    public R createQrCode() {
        HashMap map = userService.createQrCode();
        return R.ok(map);
    }

    /**
     * 检测登陆验证码
     *
     * @param form
     * @return
     */
    @PostMapping("/checkQrCode")
    @Operation(summary = "检测登陆验证码")
    public R checkQrCode(@Valid @RequestBody CheckQrCodeForm form) {
        boolean bool = userService.checkQrCode(form.getCode(), form.getUuid());
        return R.ok().put("result", bool);
    }

    @PostMapping("/wechatLogin")
    @Operation(summary = "微信小程序登陆")
    public R wechatLogin(@Valid @RequestBody WechatLoginForm form) {
        HashMap map = userService.wechatLogin(form.getUuid());
        boolean result = (boolean) map.get("result");
        if (result) {
            int userId = (int) map.get("userId");
            StpUtil.setLoginId(userId);
            Set<String> permissions = userService.searchUserPermissions(userId);
            map.remove("userId");
            map.put("permissions", permissions);
        }
        return R.ok(map);
    }

    /**
     * 登陆成功后加载用户的基本信息
     */
    @GetMapping("/loadUserInfo")
    @Operation(summary = "登陆成功后加载用户的基本信息")
    @SaCheckLogin
    public R loadUserInfo() {
        int userId = StpUtil.getLoginIdAsInt();
        HashMap summary = userService.searchUserSummary(userId);
        return R.ok(summary);
    }

    @PostMapping("/searchById")
    @Operation(summary = "根据ID查找用户")
    @SaCheckPermission(value = {"ROOT", "USER:SELECT"}, mode = SaMode.OR)
    public R searchById(@Valid @RequestBody SearchUserByIdForm form) {
        HashMap map = userService.searchById(form.getUserId());
        return R.ok(map);
    }

    @GetMapping("/searchAllUser")
    @Operation(summary = "查询所有用户")
    @SaCheckLogin
    public R searchAllUser() {
        ArrayList<HashMap> list = userService.searchAllUser();
        return R.ok().put("list", list);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录接口")
    public R login(@Valid @RequestBody LoginForm loginForm){
        HashMap param = JSONUtil.parse(loginForm).toBean(HashMap.class);
        Integer loginId = userService.login(param);
        R r = R.ok().put("result",loginId != null ? true : false);
        if(loginId != null){
            StpUtil.setLoginId(loginId);
            Set<String> permissions = userService.searchUserPermissions(loginId);
            r.put("permissions",permissions);
        }
        return r;
    }

    /**
     * 用户修改密码
     * @param updatePasswordForm 修改密码验证FORM
     * @return 消息模型
     */
    @PostMapping("/updatePassWord")
    @SaCheckLogin
    @Operation(summary = "修改密码")
    public R updatePassWord(@Valid @RequestBody UpdatePasswordForm updatePasswordForm){
        HashMap param = new HashMap(){{
            put("userid",StpUtil.getLoginIdAsInt());
            put("password",updatePasswordForm.getPassword());
        }};
        int rows =  userService.updatePassWord(param);
        return R.ok().put("rows",rows);
    }

    /**
     * 用户推出登录
     * @return 消息模型
     */
    @GetMapping("/logOut")
    @Operation(summary = "退出登录")
    public  R logOut(){
        StpUtil.logout();
        return R.ok();
    }
}

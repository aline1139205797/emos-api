package com.example.emos.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.temp.SaTempUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.common.util.R;
import com.example.emos.api.controller.form.*;
import com.example.emos.api.service.UserService;
import com.example.emos.api.service.db.pojo.TbUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
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

    /**
     * 用户登录
     *
     * @param loginForm 用户登录表单
     * @return 消息模型（附带token）
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录接口")
    public R login(@Valid @RequestBody LoginForm loginForm) {
        HashMap param = JSONUtil.parse(loginForm).toBean(HashMap.class);
        Integer loginId = userService.login(param);
        R r = R.ok().put("result", loginId != null ? true : false);
        if (loginId != null) {
            StpUtil.setLoginId(loginId);
            Set<String> permissions = userService.searchUserPermissions(loginId);
            String token = StpUtil.getTokenInfo().getTokenValue();
            r.put("permissions", permissions).put("token", token);
        }
        return r;
    }

    /**
     * 用户修改密码
     *
     * @param updatePasswordForm 修改密码验证FORM
     * @return 消息模型
     */
    @PostMapping("/updatePassWord")
    @SaCheckLogin
    @Operation(summary = "修改密码")
    public R updatePassWord(@Valid @RequestBody UpdatePasswordForm updatePasswordForm) {
        HashMap param = new HashMap() {{
            put("userid", StpUtil.getLoginIdAsInt());
            put("password", updatePasswordForm.getPassword());
        }};
        int rows = userService.updatePassWord(param);
        return R.ok().put("rows", rows);
    }

    /**
     * 用户退出登录
     *
     * @return 消息模型
     */
    @GetMapping("/logOut")
    @Operation(summary = "退出登录")
    public R logOut() {
        StpUtil.logout();
        return R.ok();
    }

    @PostMapping("/getUserPage")
    @Operation(summary = "用户数据分页查询")
    @SaCheckPermission(value = {"ROOT", "USER:SELECT"}, mode = SaMode.OR)
    public R searchUserByPage(@Valid @RequestBody SearchUserByPageForm form) {
        int page = form.getPageIndex();
        int length = form.getPageSize();
        int start = (page - 1) * length;
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("start", start);
        PageUtils pageUtils = userService.searchUserByPage(param);
        return R.ok().put("page", pageUtils);
    }

    /**
     * 新增用户
     *
     * @param insertUserForm 用户表单对象
     * @return 插入数量
     */
    @PostMapping("/addUser")
    @Operation(summary = "新增用户")
    @SaCheckPermission(value = {"ROOT", "USER:INSERT"}, mode = SaMode.OR)
    public R addUser(@Valid @RequestBody InsertUserForm insertUserForm) {
        TbUser user = JSONUtil.parse(insertUserForm).toBean(TbUser.class);
        user.setStatus((byte) 1);
        user.setRole(JSONUtil.parseArray(insertUserForm.getRole()).toString());
        user.setCreateTime(new Date());
        int num = userService.addUser(user);
        return R.ok().put("num", num);
    }

    @PostMapping("/updateUser")
    @Operation(summary = "修改用户")
    @SaCheckPermission(value = {"ROOT", "USER:UPDATE"}, mode = SaMode.OR)
    public R updateUser(@Valid @RequestBody UpdateUserForm form) {
        TbUser user = JSONUtil.parse(form).toBean(TbUser.class);
        user.setRole(JSONUtil.parseArray(form.getRole()).toString());
        int num = userService.updateUser(user);
        if (num == 1) {
            StpUtil.logoutByLoginId(form.getId());
        }
        return R.ok().put("num", num);
    }

    @PostMapping("/delete")
    @Operation(summary = "批量删除用户")
    @SaCheckPermission(value = {"ROOT", "USER:DELETE"}, mode = SaMode.OR)
    public R deleteUsers(@Valid @RequestBody DeleteUserByIdsForm form) {
        Integer loginId = StpUtil.getLoginIdAsInt();
        if (ArrayUtil.contains(form.getIds(), loginId)) {
            return R.error("您不能删除自己的用户");
        }
        int num = userService.deleteUsers(form.getIds());
        //离线被删除的用户
        if (num > 0) {
            for (Integer id : form.getIds()) {
                StpUtil.logoutByLoginId(id);
            }
        }
        return R.ok().put("num", num);
    }
}

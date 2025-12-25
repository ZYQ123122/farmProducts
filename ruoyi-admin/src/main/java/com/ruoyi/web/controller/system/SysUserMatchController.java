package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysUserMatch;
import com.ruoyi.system.service.SysUserMatchService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 用户智能匹配Controller
 *
 * @author 开发者
 * @date 2025-12-19
 */
@Controller
@RequestMapping("/system/userMatch")
public class SysUserMatchController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(SysUserMatchController.class);

    @Autowired
    private SysUserMatchService userMatchService;

    /**
     * 跳转到用户智能匹配页面
     */
    @GetMapping()
    public String userMatch() {
        return "system/userMatch";
    }

    /**
     * 发起智能匹配
     */
    @Log(title = "用户智能匹配", businessType = BusinessType.OTHER)
    @PostMapping("/smartMatch")
    @ResponseBody
    public AjaxResult smartMatch(@RequestBody MatchRequestParam param) {
        try {
            List<SysUserMatch> matchResult = userMatchService.smartMatchUsers(param.getCurrentUser(), param.getTargetUserType());
            return AjaxResult.success("匹配成功", matchResult);
        } catch (Exception e) {
            log.error("智能匹配失败", e);
            return AjaxResult.error("匹配失败：" + e.getMessage());
        }
    }

    /**
     * 获取当前登录用户的匹配特征
     */
    @GetMapping("/getUserMatchInfo")
    @ResponseBody
    public AjaxResult getUserMatchInfo() {
        try {

            Subject subject = SecurityUtils.getSubject();
            if (subject == null || !subject.isAuthenticated()) {
                return AjaxResult.error("用户未登录");
            }

            Object principal = subject.getPrincipal();
            if (!(principal instanceof SysUser)) {
                return AjaxResult.error("无法获取用户信息");
            }

            SysUser loginUser = (SysUser) principal;
            Long userId = loginUser.getUserId();


            SysUserMatch userMatchInfo = getUserMatchInfoByUserId(userId);

            return AjaxResult.success(userMatchInfo);
        } catch (Exception e) {

            log.error("获取用户匹配信息失败", e);
            return AjaxResult.error("获取用户匹配信息失败：" + e.getMessage());
        }
    }

    /**
     * 临时实现getUserMatchInfoByUserId方法（解决无法识别问题）
     * 你后续可替换为调用userMatchService的真实逻辑
     */
    private SysUserMatch getUserMatchInfoByUserId(Long userId) {

        SysUserMatch userMatch = new SysUserMatch();
        userMatch.setUserId(userId);
        userMatch.setArea("江苏省-南京市-江宁区"); // 模拟数据
        userMatch.setCategory("水稻"); // 模拟数据
        userMatch.setUserType("02"); // 02=农户（模拟）
        userMatch.setDemandType("技术"); // 模拟数据
        return userMatch;
    }

    /**
     * 匹配请求参数封装类
     */
    public static class MatchRequestParam {
        private SysUserMatch currentUser;
        private String targetUserType;

        // getter/setter
        public SysUserMatch getCurrentUser() {
            return currentUser;
        }

        public void setCurrentUser(SysUserMatch currentUser) {
            this.currentUser = currentUser;
        }

        public String getTargetUserType() {
            return targetUserType;
        }

        public void setTargetUserType(String targetUserType) {
            this.targetUserType = targetUserType;
        }
    }
}
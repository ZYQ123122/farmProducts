package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysUserMatch;
import com.ruoyi.system.service.SysUserMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户智能匹配Controller
 *
 * @author 开发者
 * @date 2025-12-19
 */
@RestController
@RequestMapping("/system/userMatch")
public class SysUserMatchController extends BaseController {
    @Autowired
    private SysUserMatchService userMatchService;

    /**
     * 发起智能匹配
     */
    @Log(title = "用户智能匹配", businessType = BusinessType.OTHER)
    @PostMapping("/smartMatch")
    public AjaxResult smartMatch(@RequestBody MatchRequestParam param) {
        // 调用匹配算法
        List<SysUserMatch> matchResult = userMatchService.smartMatchUsers(param.getCurrentUser(), param.getTargetUserType());
        return AjaxResult.success("匹配成功", matchResult);
    }

    /**
     * 匹配请求参数封装类
     */
    public static class MatchRequestParam {
        /** 当前发起匹配的用户 */
        private SysUserMatch currentUser;

        /** 目标用户类型（02-农户/03-专家/04-银行/05-买家） */
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
package com.ruoyi.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.ExpertInfo;
import com.ruoyi.system.service.IExpertInfoService;

/**
 * 专家入驻申请Controller
 *
 * @author ruoyi
 * @date 2025-11-26
 */
@Controller
@RequestMapping("/farmer/expert")
public class ExpertApplyController extends BaseController
{
    @Autowired
    private IExpertInfoService expertInfoService;

    /**
     * 专家入驻申请页面
     */
    @GetMapping("/apply")
    public String apply()
    {
        return "farmer/expert/apply";
    }

    /**
     * 查询当前用户的专家信息
     */
    @GetMapping("/myInfo")
    @ResponseBody
    public AjaxResult getMyExpertInfo()
    {
        Long userId = ShiroUtils.getUserId();
        ExpertInfo expertInfo = expertInfoService.selectExpertInfoByUserId(userId);
        return success(expertInfo);
    }

    /**
     * 提交专家入驻申请
     */
    @Log(title = "专家入驻申请", businessType = BusinessType.INSERT)
    @PostMapping("/apply")
    @ResponseBody
    public AjaxResult submitApply(ExpertInfo expertInfo)
    {
        Long userId = ShiroUtils.getUserId();
        expertInfo.setUserId(userId);
        expertInfo.setCreateBy(ShiroUtils.getLoginName());

        int result = expertInfoService.applyExpert(expertInfo);
        if (result > 0) {
            return success("申请提交成功，请等待平台审核！");
        } else {
            return error("申请提交失败，请重试！");
        }
    }

    /**
     * 更新专家信息（已通过审核的专家可以更新自己的信息）
     */
    @Log(title = "更新专家信息", businessType = BusinessType.UPDATE)
    @PostMapping("/updateMyInfo")
    @ResponseBody
    public AjaxResult updateMyInfo(ExpertInfo expertInfo)
    {
        Long userId = ShiroUtils.getUserId();
        ExpertInfo existing = expertInfoService.selectExpertInfoByUserId(userId);

        if (existing == null) {
            return error("您还不是专家，请先申请入驻！");
        }

        // 只有审核通过的专家才能更新信息
        if (!"1".equals(existing.getAuditStatus())) {
            return error("您的专家申请尚未通过审核，无法更新信息！");
        }

        expertInfo.setId(existing.getId());
        expertInfo.setUserId(userId);
        expertInfo.setUpdateBy(ShiroUtils.getLoginName());
        // 保持审核状态不变
        expertInfo.setAuditStatus(existing.getAuditStatus());

        int result = expertInfoService.updateExpertInfo(expertInfo);
        return toAjax(result);
    }
}
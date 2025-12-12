package com.ruoyi.web.controller.system;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.ExpertInfo;
import com.ruoyi.system.service.IExpertInfoService;

/**
 * 专家信息Controller（管理员端）
 *
 * @author ruoyi
 * @date 2025-11-26
 */
@Controller
@RequestMapping("/system/expert")
public class ExpertInfoController extends BaseController
{
    private String prefix = "system/expert";

    @Autowired
    private IExpertInfoService expertInfoService;

    @GetMapping()
    public String expert()
    {
        return prefix + "/expert";
    }

    /**
     * 查询专家信息列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ExpertInfo expertInfo)
    {
        startPage();
        List<ExpertInfo> list = expertInfoService.selectExpertInfoList(expertInfo);
        return getDataTable(list);
    }

    /**
     * 审核专家申请列表页面
     */
    @GetMapping("/audit")
    public String auditList()
    {
        return prefix + "/auditList";
    }

    /**
     * 查询待审核专家列表
     */
    @PostMapping("/audit/list")
    @ResponseBody
    public TableDataInfo auditListData()
    {
        ExpertInfo expertInfo = new ExpertInfo();
        expertInfo.setAuditStatus("0"); // 待审核
        startPage();
        List<ExpertInfo> list = expertInfoService.selectExpertInfoList(expertInfo);
        return getDataTable(list);
    }

    /**
     * 审核专家申请页面
     */
    @GetMapping("/audit/{id}")
    public String audit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ExpertInfo expertInfo = expertInfoService.selectExpertInfoById(id);
        mmap.put("expertInfo", expertInfo);
        return prefix + "/audit";
    }

    /**
     * 审核专家申请
     */
    @Log(title = "专家审核", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    @ResponseBody
    public AjaxResult auditSave(Long id, String auditStatus, String auditRemark)
    {
        int result = expertInfoService.auditExpert(id, auditStatus, auditRemark);
        if (result > 0) {
            if ("1".equals(auditStatus)) {
                return success("审核通过！");
            } else {
                return success("已拒绝该申请！");
            }
        } else {
            return error("审核失败！");
        }
    }

    /**
     * 删除专家信息
     */
    @Log(title = "专家信息", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(expertInfoService.deleteExpertInfoByIds(com.ruoyi.common.core.text.Convert.toLongArray(ids)));
    }
}
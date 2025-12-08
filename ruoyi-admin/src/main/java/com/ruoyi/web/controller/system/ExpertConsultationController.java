package com.ruoyi.web.controller.system;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
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
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.ExpertConsultation;
import com.ruoyi.system.domain.ExpertInfo;
import com.ruoyi.system.service.IExpertConsultationService;
import com.ruoyi.system.service.IExpertInfoService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 专家咨询Controller
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/consultation")
public class ExpertConsultationController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(ExpertConsultationController.class);
    private String prefix = "system/consultation";

    @Autowired
    private IExpertConsultationService expertConsultationService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private IExpertInfoService expertInfoService;

    @RequiresPermissions("system:consultation:view")
    @GetMapping()
    public String consultation()
    {
        return prefix + "/consultation";
    }

    /**
     * 查询专家咨询列表
     */
    @RequiresPermissions("system:consultation:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ExpertConsultation consultation)
    {
        startPage();
        List<ExpertConsultation> list = expertConsultationService.selectExpertConsultationList(consultation);
        return getDataTable(list);
    }

    /**
     * 查询我的咨询列表（用户视角）
     */
    @PostMapping("/myList")
    @ResponseBody
    public TableDataInfo myList(String status)
    {
        Long userId = ShiroUtils.getUserId();
        startPage();
        List<ExpertConsultation> list = expertConsultationService.selectConsultationListByUserId(userId, status);
        return getDataTable(list);
    }

    /**
     * 查询待回复咨询列表（专家视角）
     */
    @PostMapping("/expertList")
    @ResponseBody
    public TableDataInfo expertList(String status)
    {
        Long expertId = ShiroUtils.getUserId();
        startPage();
        List<ExpertConsultation> list = expertConsultationService.selectConsultationListByExpertId(expertId, status);
        return getDataTable(list);
    }

    /**
     * 新增专家咨询
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存专家咨询
     */
    @Log(title = "专家咨询", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated ExpertConsultation consultation)
    {
        consultation.setUserId(ShiroUtils.getUserId());
        return toAjax(expertConsultationService.insertExpertConsultation(consultation));
    }

    /**
     * 修改专家咨询
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ExpertConsultation consultation = expertConsultationService.selectExpertConsultationById(id);
        mmap.put("consultation", consultation);
        return prefix + "/edit";
    }

    /**
     * 修改保存专家咨询
     */
    @Log(title = "专家咨询", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated ExpertConsultation consultation)
    {
        Long userId = ShiroUtils.getUserId();
        // 验证权限：只能修改自己的咨询
        ExpertConsultation existing = expertConsultationService.selectExpertConsultationByIdAndUserId(
            consultation.getId(), userId);
        if (existing == null)
        {
            return error("无权修改此咨询");
        }
        return toAjax(expertConsultationService.updateExpertConsultation(consultation));
    }

    /**
     * 查看咨询详情（返回JSON，用于农户端）
     */
    @GetMapping("/detail/{id}")
    @ResponseBody
    public AjaxResult getDetail(@PathVariable("id") Long id)
    {
        try {
            Long userId = ShiroUtils.getUserId();
            log.info("查询咨询详情: id={}, userId={}", id, userId);
            
            ExpertConsultation consultation = expertConsultationService.selectExpertConsultationById(id);
            if (consultation == null)
            {
                log.warn("咨询不存在: id={}", id);
                return error("咨询不存在");
            }
            
            // 验证权限：只能查看自己的咨询
            if (!consultation.getUserId().equals(userId))
            {
                log.warn("无权查看咨询: id={}, consultation.userId={}, current.userId={}", 
                    id, consultation.getUserId(), userId);
                return error("无权查看此咨询");
            }
            
            // 确保 createTime 字段有值（从 createdAt 复制）
            if (consultation.getCreateTime() == null && consultation.getCreatedAt() != null) {
                consultation.setCreateTime(consultation.getCreatedAt());
            }
            
            log.info("成功返回咨询详情: id={}, title={}", id, consultation.getTitle());
            return success(consultation);
        } catch (Exception e) {
            log.error("查询咨询详情异常: id=" + id, e);
            return error("查询咨询详情失败：" + e.getMessage());
        }
    }

    /**
     * 查看咨询详情页面（管理员端）
     */
    @GetMapping("/detailPage/{id}")
    public String detailPage(@PathVariable("id") Long id, ModelMap mmap)
    {
        ExpertConsultation consultation = expertConsultationService.selectExpertConsultationById(id);
        mmap.put("consultation", consultation);
        return prefix + "/detail";
    }

    /**
     * 回复咨询页面
     */
    @GetMapping("/reply")
    public String reply(Long id, ModelMap mmap)
    {
        ExpertConsultation consultation = expertConsultationService.selectExpertConsultationById(id);
        mmap.put("consultation", consultation);
        return prefix + "/reply";
    }

    /**
     * 专家回复咨询
     * 注意：不检查权限注解，因为业务逻辑层已经验证了专家只能回复分配给自己的咨询
     */
    @Log(title = "专家咨询", businessType = BusinessType.UPDATE)
    @PostMapping("/reply")
    @ResponseBody
    public AjaxResult replySave(Long id, String reply)
    {
        Long expertId = ShiroUtils.getUserId();
        return toAjax(expertConsultationService.replyConsultation(id, expertId, reply));
    }

    /**
     * 删除专家咨询
     */
    @Log(title = "专家咨询", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        Long userId = ShiroUtils.getUserId();
        Long[] idArray = com.ruoyi.common.core.text.Convert.toLongArray(ids);
        for (Long id : idArray)
        {
            expertConsultationService.softDeleteExpertConsultation(id, userId);
        }
        return success();
    }

    /**
     * 修改咨询状态
     */
    @Log(title = "专家咨询", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(Long id, String status)
    {
        return toAjax(expertConsultationService.changeConsultationStatus(id, status));
    }

    /**
     * 获取专家列表（统一接口，用于下拉选择、预约专家、知识库等）
     * 返回格式：包含userId, userName, loginName, phonenumber, remark等信息
     * 只返回审核通过的专家，排除银行用户
     */
    @GetMapping("/getExpertList")
    @ResponseBody
    public AjaxResult getExpertList()
    {
        try {
            // 查找银行角色（role_key为"bank"的角色），用于排除
            SysRole bankRoleQuery = new SysRole();
            bankRoleQuery.setRoleKey("bank");
            List<SysRole> bankRoles = roleService.selectRoleList(bankRoleQuery);
            Long bankRoleId = null;
            if (bankRoles != null && !bankRoles.isEmpty()) {
                bankRoleId = bankRoles.get(0).getRoleId();
                log.info("找到银行角色ID: {}", bankRoleId);
            }

            // 直接从 expert_info 表中查找已审核通过且状态正常的专家
            ExpertInfo query = new ExpertInfo();
            query.setAuditStatus("1"); // 已通过
            query.setStatus("0");      // 正常
            List<ExpertInfo> expertInfos = expertInfoService.selectExpertInfoList(query);
            log.info("expert_info 中审核通过的专家数量: {}", expertInfos != null ? expertInfos.size() : 0);

            List<SysUser> approvedExperts = new java.util.ArrayList<>();
            if (expertInfos != null) {
                for (ExpertInfo expertInfo : expertInfos) {
                    Long userId = expertInfo.getUserId();
                    if (userId == null) {
                        continue;
                    }

                    SysUser user = userService.selectUserById(userId);
                    if (user == null) {
                        log.warn("用户不存在，跳过: user_id={}", userId);
                        continue;
                    }

                    // 只要正常状态的用户
                    if (!"0".equals(user.getStatus())) {
                        log.info("用户状态非正常，跳过: user_id={}, status={}", userId, user.getStatus());
                        continue;
                    }

                    // 注意：不再排除银行用户，因为只要在 expert_info 表中是已审核的专家，就应该显示
                    // 如果将来需要排除银行用户，可以在这里添加过滤逻辑

                    approvedExperts.add(user);
                    log.info("✓ 添加审核通过的专家用户: user_id={}, user_name={}", userId, user.getUserName());
                }
            }

            log.info("最终返回的审核通过专家数量: {}", approvedExperts.size());
            return success(approvedExperts);
        } catch (Exception e) {
            log.error("获取专家列表失败", e);
            return error("获取专家列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取专家详细信息列表（用于预约专家页面展示）
     * 返回格式化的专家信息，包含专业领域、经验等
     */
    @GetMapping("/getExpertDetailList")
    @ResponseBody
    public AjaxResult getExpertDetailList(String keyword)
    {
        try {
            log.info("getExpertDetailList 被调用，keyword={}", keyword);
            // 先获取专家用户列表（已经过滤了审核通过的专家）
            AjaxResult result = getExpertList();
            if (!result.isSuccess()) {
                log.warn("getExpertList 返回失败: {}", result.get("msg"));
                return result;
            }
            
            @SuppressWarnings("unchecked")
            List<SysUser> expertUserList = (List<SysUser>) result.get(AjaxResult.DATA_TAG);
            log.info("getExpertList 返回专家数量: {}", expertUserList != null ? expertUserList.size() : 0);
            
            if (expertUserList == null || expertUserList.isEmpty()) {
                log.warn("专家列表为空，返回空列表");
                return success(new java.util.ArrayList<>());
            }
            
            // 转换为前端需要的格式，并从expert_info表获取详细信息
            List<java.util.Map<String, Object>> expertDetailList = new java.util.ArrayList<>();
            for (SysUser expertUser : expertUserList) {
                log.info("处理专家: user_id={}, user_name={}", expertUser.getUserId(), expertUser.getUserName());
                
                // 从expert_info表获取详细信息
                ExpertInfo expertInfo = expertInfoService.selectExpertInfoByUserId(expertUser.getUserId());
                log.info("查询专家信息: user_id={}, expertInfo={}, audit_status={}", 
                    expertUser.getUserId(), 
                    expertInfo != null ? "存在" : "不存在",
                    expertInfo != null ? expertInfo.getAuditStatus() : "N/A");
                
                // getExpertList()已经过滤了审核通过的专家，这里只需要检查expertInfo是否存在
                // 如果expertInfo为null，说明数据不一致，跳过
                if (expertInfo == null) {
                    log.warn("专家信息记录不存在，跳过: user_id={}", expertUser.getUserId());
                    continue;
                }
                
                // 再次确认审核状态（双重保险）
                String auditStatus = expertInfo.getAuditStatus();
                log.info("检查审核状态: user_id={}, audit_status={}, 等于'1'? {}", 
                    expertUser.getUserId(), auditStatus, "1".equals(auditStatus));
                if (auditStatus == null || !"1".equals(auditStatus)) {
                    log.warn("专家审核状态不是'1'，跳过: user_id={}, audit_status={}", 
                        expertUser.getUserId(), auditStatus);
                    continue;
                }
                
                // 如果有关键词，进行过滤（包含专业领域、姓名等）
                if (keyword != null && !keyword.trim().isEmpty()) {
                    String searchText = (expertUser.getUserName() != null ? expertUser.getUserName() : "") +
                                       (expertUser.getLoginName() != null ? expertUser.getLoginName() : "") +
                                       (expertInfo.getSpecialty() != null ? expertInfo.getSpecialty() : "");
                    if (!searchText.toLowerCase().contains(keyword.toLowerCase())) {
                        log.info("关键词过滤：跳过专家 user_id={}, searchText={}", expertUser.getUserId(), searchText);
                        continue;
                    }
                }
                
                java.util.Map<String, Object> expertMap = new java.util.HashMap<>();
                expertMap.put("id", expertUser.getUserId());
                expertMap.put("userId", expertUser.getUserId());
                expertMap.put("name", expertUser.getUserName() != null ? expertUser.getUserName() : expertUser.getLoginName());
                expertMap.put("loginName", expertUser.getLoginName());
                expertMap.put("phone", expertUser.getPhonenumber());
                expertMap.put("phonenumber", expertUser.getPhonenumber());
                
                // 从expert_info表获取专业信息
                expertMap.put("specialty", expertInfo.getSpecialty() != null ? expertInfo.getSpecialty() : "农业专家");
                expertMap.put("experience", expertInfo.getExperience() != null ? expertInfo.getExperience() : "");
                expertMap.put("description", expertInfo.getDescription() != null ? expertInfo.getDescription() : "");
                expertMap.put("expertise", expertInfo.getExpertise() != null ? expertInfo.getExpertise() : "");
                expertMap.put("avatar", expertInfo.getAvatar() != null ? expertInfo.getAvatar() : "");
                
                expertDetailList.add(expertMap);
                log.info("✓ 添加到专家详情列表: user_id={}, name={}", expertUser.getUserId(), expertMap.get("name"));
            }
            
            log.info("getExpertDetailList 最终返回专家数量: {}", expertDetailList.size());
            return success(expertDetailList);
        } catch (Exception e) {
            log.error("getExpertDetailList 异常", e);
            return error("获取专家列表失败：" + e.getMessage());
        }
    }
}



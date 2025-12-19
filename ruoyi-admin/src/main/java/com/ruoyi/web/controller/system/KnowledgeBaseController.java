package com.ruoyi.web.controller.system;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.KnowledgeBase;
import com.ruoyi.system.service.IKnowledgeBaseService;

/**
 * 知识库Controller
 *
 * @author ruoyi
 * @date 2025-11-26
 */
@Controller
@RequestMapping("/system/knowledge")
public class KnowledgeBaseController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseController.class);

    @Autowired
    private IKnowledgeBaseService knowledgeBaseService;

    /**
     * 查询知识库列表（用于农户端展示）
     */
    @GetMapping("/list")
    @ResponseBody
    public AjaxResult list(String category, String keyword)
    {
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.setStatus("0"); // 只查询正常状态的知识
        if (category != null && !category.isEmpty() && !"all".equals(category)) {
            knowledgeBase.setCategory(category);
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            knowledgeBase.setTitle(keyword);
        }
        List<KnowledgeBase> list = knowledgeBaseService.selectKnowledgeBaseList(knowledgeBase);
        return success(list);
    }

    /**
     * 查询知识库详情
     */
    @GetMapping("/detail/{id}")
    @ResponseBody
    public AjaxResult getDetail(@PathVariable("id") Long id)
    {
        KnowledgeBase knowledge = knowledgeBaseService.selectKnowledgeBaseById(id);
        if (knowledge == null) {
            return error("知识不存在");
        }
        // 增加浏览次数
        knowledgeBaseService.incrementViewCount(id);
        return success(knowledge);
    }

    /**
     * 查询当前专家自己的知识列表
     */
    @GetMapping("/myList")
    @ResponseBody
    public AjaxResult myList(String category, String keyword)
    {
        Long authorId = ShiroUtils.getUserId();
        log.info("查询专家知识列表: authorId={}, category={}, keyword={}", authorId, category, keyword);

        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.setAuthorId(authorId);
        // 专家端查看自己的知识，不限制状态（可以看到所有状态的知识）
        // knowledgeBase.setStatus("0"); // 不设置状态，查询所有状态

        if (category != null && !category.isEmpty() && !"all".equals(category)) {
            knowledgeBase.setCategory(category);
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            knowledgeBase.setTitle(keyword);
        }
        List<KnowledgeBase> list = knowledgeBaseService.selectKnowledgeBaseList(knowledgeBase);
        log.info("查询结果: 找到{}条知识", list != null ? list.size() : 0);
        return success(list);
    }

    /**
     * 新增或修改知识（专家端）
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxResult save(@Validated KnowledgeBase knowledgeBase)
    {
        Long userId = ShiroUtils.getUserId();
        String loginName = ShiroUtils.getLoginName();
        String userName = ShiroUtils.getSysUser().getUserName();

        if (knowledgeBase.getId() == null)
        {
            // 新增知识
            knowledgeBase.setAuthorId(userId);
            knowledgeBase.setAuthorName(userName);
            knowledgeBase.setCreateBy(loginName);
            if (knowledgeBase.getStatus() == null) {
                knowledgeBase.setStatus("0");
            }
            if (knowledgeBase.getIsTop() == null) {
                knowledgeBase.setIsTop("0");
            }
            if (knowledgeBase.getPublishTime() == null) {
                knowledgeBase.setPublishTime(new Date());
            }
            return toAjax(knowledgeBaseService.insertKnowledgeBase(knowledgeBase));
        }
        else
        {
            // 编辑知识：验证权限，只能编辑自己的知识
            KnowledgeBase existing = knowledgeBaseService.selectKnowledgeBaseById(knowledgeBase.getId());
            if (existing == null) {
                return error("知识不存在");
            }
            if (!userId.equals(existing.getAuthorId())) {
                return error("只能编辑自己的知识");
            }
            // 保持原有的作者信息不变
            knowledgeBase.setAuthorId(existing.getAuthorId());
            knowledgeBase.setAuthorName(existing.getAuthorName());
            knowledgeBase.setUpdateBy(loginName);
            return toAjax(knowledgeBaseService.updateKnowledgeBase(knowledgeBase));
        }
    }

    /**
     * 删除知识（专家端，只能删自己的）
     */
    @PostMapping("/delete")
    @ResponseBody
    public AjaxResult delete(Long id)
    {
        if (id == null)
        {
            return error("参数错误");
        }
        KnowledgeBase knowledge = knowledgeBaseService.selectKnowledgeBaseById(id);
        if (knowledge == null)
        {
            return error("知识不存在");
        }
        if (!ShiroUtils.getUserId().equals(knowledge.getAuthorId()))
        {
            return error("只能删除自己的知识");
        }
        return toAjax(knowledgeBaseService.deleteKnowledgeBaseById(id));
    }
}
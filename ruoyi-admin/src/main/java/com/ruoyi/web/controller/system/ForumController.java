package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.ForumCategory;
import com.ruoyi.system.domain.ForumComment;
import com.ruoyi.system.domain.ForumTopic;
import com.ruoyi.system.service.IForumCategoryService;
import com.ruoyi.system.service.IForumCommentService;
import com.ruoyi.system.service.IForumLikeService;
import com.ruoyi.system.service.IForumTopicService;

/**
 * 社区交流控制器
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/farmer/forum")
public class ForumController extends BaseController
{
    @Autowired
    private IForumTopicService topicService;

    @Autowired
    private IForumCommentService commentService;

    @Autowired
    private IForumCategoryService categoryService;

    @Autowired
    private IForumLikeService likeService;

    /**
     * 社区首页（主题列表）
     */
    @GetMapping("/index")
    public String index(ModelMap mmap, HttpServletRequest request)
    {
        SysUser user = getSysUser();
        if (user == null)
        {
            return "redirect:/login";
        }

        // 获取分类列表
        List<ForumCategory> categories = categoryService.selectAllCategories();
        mmap.put("categories", categories);

        // 获取主题列表
        ForumTopic queryTopic = new ForumTopic();
        List<ForumTopic> topics = topicService.selectTopicList(queryTopic);
        mmap.put("topics", topics);
        mmap.put("user", user);

        return "farmer/community/index";
    }

    /**
     * 主题详情页
     */
    @GetMapping("/detail")
    public String detail(@RequestParam Long id, ModelMap mmap, HttpServletRequest request)
    {
        SysUser user = getSysUser();
        if (user == null)
        {
            return "redirect:/login";
        }

        // 增加浏览次数
        topicService.incrementViewCount(id);

        // 获取主题详情
        ForumTopic topic = topicService.selectTopicById(id);
        if (topic == null)
        {
            return "redirect:/farmer/forum/index";
        }

        // 获取评论列表
        List<ForumComment> comments = commentService.selectCommentListByTopicId(id);
        
        // 标记每个评论是否已点赞
        Long userId = user.getId();
        if (userId != null)
        {
            for (ForumComment comment : comments)
            {
                if (comment.getId() != null)
                {
                    boolean isLiked = likeService.isLiked(comment.getId(), userId);
                    comment.setIsLiked(isLiked);
                }
            }
        }

        mmap.put("topic", topic);
        mmap.put("comments", comments);
        mmap.put("user", user);

        return "farmer/community/detail";
    }

    /**
     * 发帖页面
     */
    @GetMapping("/add")
    public String add(ModelMap mmap, HttpServletRequest request)
    {
        SysUser user = getSysUser();
        if (user == null)
        {
            return "redirect:/login";
        }

        // 获取分类列表
        List<ForumCategory> categories = categoryService.selectAllCategories();
        mmap.put("categories", categories);
        mmap.put("user", user);

        return "farmer/community/add";
    }

    /**
     * 保存主题
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxResult save(ForumTopic topic)
    {
        SysUser user = getSysUser();
        if (user == null || user.getId() == null)
        {
            return error("请先登录");
        }

        topic.setFarmerId(user.getId());
        int result = topicService.insertTopic(topic);
        return toAjax(result);
    }

    /**
     * 保存评论
     */
    @PostMapping("/comment/save")
    @ResponseBody
    public AjaxResult saveComment(ForumComment comment)
    {
        SysUser user = getSysUser();
        if (user == null || user.getId() == null)
        {
            return error("请先登录");
        }

        comment.setFarmerId(user.getId());
        int result = commentService.insertComment(comment);
        return toAjax(result);
    }

    /**
     * 点赞/取消点赞
     */
    @PostMapping("/comment/like")
    @ResponseBody
    public AjaxResult toggleLike(@RequestParam Long commentId)
    {
        SysUser user = getSysUser();
        if (user == null || user.getId() == null)
        {
            return error("请先登录");
        }

        boolean isLiked = likeService.toggleLike(commentId, user.getId());
        return success(isLiked ? "点赞成功" : "取消点赞成功");
    }

    /**
     * 删除主题
     */
    @PostMapping("/delete")
    @ResponseBody
    public AjaxResult delete(@RequestParam Long id)
    {
        SysUser user = getSysUser();
        if (user == null || user.getId() == null)
        {
            return error("请先登录");
        }

        ForumTopic topic = topicService.selectTopicById(id);
        if (topic == null)
        {
            return error("主题不存在");
        }

        // 只能删除自己的主题
        if (topic.getFarmerId() == null || !topic.getFarmerId().equals(user.getId()))
        {
            return error("无权删除此主题");
        }

        int result = topicService.deleteTopicById(id);
        return toAjax(result);
    }

    /**
     * 删除评论
     */
    @PostMapping("/comment/delete")
    @ResponseBody
    public AjaxResult deleteComment(@RequestParam Long id)
    {
        SysUser user = getSysUser();
        if (user == null || user.getId() == null)
        {
            return error("请先登录");
        }

        ForumComment comment = commentService.selectCommentById(id);
        if (comment == null)
        {
            return error("评论不存在");
        }

        // 只能删除自己的评论
        if (comment.getFarmerId() == null || !comment.getFarmerId().equals(user.getId()))
        {
            return error("无权删除此评论");
        }

        int result = commentService.deleteCommentById(id);
        return toAjax(result);
    }
}


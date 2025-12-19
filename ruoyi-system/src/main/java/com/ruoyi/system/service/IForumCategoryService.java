package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.ForumCategory;

/**
 * 论坛分类业务接口
 * 
 * @author ruoyi
 */
public interface IForumCategoryService
{
    /**
     * 查询所有分类
     */
    List<ForumCategory> selectAllCategories();

    /**
     * 根据ID查询分类
     */
    ForumCategory selectCategoryById(Long id);
}


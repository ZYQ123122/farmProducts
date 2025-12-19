package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.ForumCategory;

/**
 * 论坛分类 Mapper
 * 
 * @author ruoyi
 */
public interface ForumCategoryMapper
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


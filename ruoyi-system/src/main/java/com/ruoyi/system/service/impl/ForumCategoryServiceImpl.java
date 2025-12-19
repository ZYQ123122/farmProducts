package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.ForumCategory;
import com.ruoyi.system.mapper.ForumCategoryMapper;
import com.ruoyi.system.service.IForumCategoryService;

/**
 * 论坛分类业务实现
 * 
 * @author ruoyi
 */
@Service
public class ForumCategoryServiceImpl implements IForumCategoryService
{
    @Autowired
    private ForumCategoryMapper categoryMapper;

    @Override
    public List<ForumCategory> selectAllCategories()
    {
        return categoryMapper.selectAllCategories();
    }

    @Override
    public ForumCategory selectCategoryById(Long id)
    {
        return categoryMapper.selectCategoryById(id);
    }
}


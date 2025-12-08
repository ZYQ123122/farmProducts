package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.KnowledgeBaseMapper;
import com.ruoyi.system.domain.KnowledgeBase;
import com.ruoyi.system.service.IKnowledgeBaseService;

/**
 * 知识库Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
@Service
public class KnowledgeBaseServiceImpl implements IKnowledgeBaseService 
{
    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;

    /**
     * 查询知识库
     * 
     * @param id 知识库主键
     * @return 知识库
     */
    @Override
    public KnowledgeBase selectKnowledgeBaseById(Long id)
    {
        return knowledgeBaseMapper.selectKnowledgeBaseById(id);
    }

    /**
     * 查询知识库列表
     * 
     * @param knowledgeBase 知识库
     * @return 知识库
     */
    @Override
    public List<KnowledgeBase> selectKnowledgeBaseList(KnowledgeBase knowledgeBase)
    {
        return knowledgeBaseMapper.selectKnowledgeBaseList(knowledgeBase);
    }

    /**
     * 新增知识库
     * 
     * @param knowledgeBase 知识库
     * @return 结果
     */
    @Override
    public int insertKnowledgeBase(KnowledgeBase knowledgeBase)
    {
        return knowledgeBaseMapper.insertKnowledgeBase(knowledgeBase);
    }

    /**
     * 修改知识库
     * 
     * @param knowledgeBase 知识库
     * @return 结果
     */
    @Override
    public int updateKnowledgeBase(KnowledgeBase knowledgeBase)
    {
        return knowledgeBaseMapper.updateKnowledgeBase(knowledgeBase);
    }

    /**
     * 批量删除知识库
     * 
     * @param ids 需要删除的知识库主键
     * @return 结果
     */
    @Override
    public int deleteKnowledgeBaseByIds(Long[] ids)
    {
        return knowledgeBaseMapper.deleteKnowledgeBaseByIds(ids);
    }

    /**
     * 删除知识库信息
     * 
     * @param id 知识库主键
     * @return 结果
     */
    @Override
    public int deleteKnowledgeBaseById(Long id)
    {
        return knowledgeBaseMapper.deleteKnowledgeBaseById(id);
    }

    /**
     * 增加浏览次数
     * 
     * @param id 知识ID
     * @return 结果
     */
    @Override
    public int incrementViewCount(Long id)
    {
        return knowledgeBaseMapper.incrementViewCount(id);
    }
}


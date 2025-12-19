package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.ForumAttachment;

/**
 * 附件 Mapper
 * 
 * @author ruoyi
 */
public interface ForumAttachmentMapper
{
    /**
     * 查询附件列表
     */
    List<ForumAttachment> selectAttachmentList(@Param("relatedId") Long relatedId, @Param("relatedType") String relatedType);

    /**
     * 新增附件
     */
    int insertAttachment(ForumAttachment attachment);

    /**
     * 删除附件
     */
    int deleteAttachmentById(Long id);

    /**
     * 根据关联ID和类型删除附件
     */
    int deleteAttachmentsByRelated(@Param("relatedId") Long relatedId, @Param("relatedType") String relatedType);
}


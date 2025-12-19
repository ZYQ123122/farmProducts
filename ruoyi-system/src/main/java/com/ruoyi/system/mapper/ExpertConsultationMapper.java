package com.ruoyi.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.system.domain.ExpertConsultation;

/**
 * 专家咨询 Mapper接口
 */
public interface ExpertConsultationMapper
{
    /**
     * 查询专家咨询列表
     *
     * @param consultation 专家咨询
     * @return 专家咨询集合
     */
    List<ExpertConsultation> selectExpertConsultationList(ExpertConsultation consultation);

    /**
     * 根据用户ID查询咨询列表
     *
     * @param userId 用户ID
     * @param status 状态（可选）
     * @return 专家咨询集合
     */
    List<ExpertConsultation> selectConsultationListByUserId(@Param("userId") Long userId, @Param("status") String status);

    /**
     * 根据专家ID查询咨询列表
     *
     * @param expertId 专家ID
     * @param status 状态（可选）
     * @return 专家咨询集合
     */
    List<ExpertConsultation> selectConsultationListByExpertId(@Param("expertId") Long expertId, @Param("status") String status);

    /**
     * 根据ID查询专家咨询
     *
     * @param id 咨询ID
     * @return 专家咨询
     */
    ExpertConsultation selectExpertConsultationById(Long id);

    /**
     * 根据ID和用户ID查询专家咨询（权限校验）
     *
     * @param id 咨询ID
     * @param userId 用户ID
     * @return 专家咨询
     */
    ExpertConsultation selectExpertConsultationByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 新增专家咨询
     *
     * @param consultation 专家咨询
     * @return 结果
     */
    int insertExpertConsultation(ExpertConsultation consultation);

    /**
     * 修改专家咨询
     *
     * @param consultation 专家咨询
     * @return 结果
     */
    int updateExpertConsultation(ExpertConsultation consultation);

    /**
     * 专家回复咨询
     *
     * @param id 咨询ID
     * @param expertId 专家ID
     * @param reply 回复内容
     * @return 结果
     */
    int replyConsultation(@Param("id") Long id, @Param("expertId") Long expertId, @Param("reply") String reply);

    /**
     * 软删除专家咨询
     *
     * @param id 咨询ID
     * @param userId 用户ID
     * @return 结果
     */
    int softDeleteExpertConsultation(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 修改咨询状态
     *
     * @param id 咨询ID
     * @param status 状态
     * @return 结果
     */
    int changeConsultationStatus(@Param("id") Long id, @Param("status") String status);
}

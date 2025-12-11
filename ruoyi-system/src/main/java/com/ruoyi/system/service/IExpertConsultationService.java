package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.ExpertConsultation;

/**
 * 专家咨询业务接口
 */
public interface IExpertConsultationService
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
    List<ExpertConsultation> selectConsultationListByUserId(Long userId, String status);

    /**
     * 根据专家ID查询咨询列表
     *
     * @param expertId 专家ID
     * @param status 状态（可选）
     * @return 专家咨询集合
     */
    List<ExpertConsultation> selectConsultationListByExpertId(Long expertId, String status);

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
    ExpertConsultation selectExpertConsultationByIdAndUserId(Long id, Long userId);

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
    int replyConsultation(Long id, Long expertId, String reply);

    /**
     * 软删除专家咨询
     *
     * @param id 咨询ID
     * @param userId 用户ID
     * @return 结果
     */
    int softDeleteExpertConsultation(Long id, Long userId);

    /**
     * 修改咨询状态
     *
     * @param id 咨询ID
     * @param status 状态
     * @return 结果
     */
    int changeConsultationStatus(Long id, String status);
}
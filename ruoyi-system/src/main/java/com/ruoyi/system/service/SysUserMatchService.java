package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysUserMatch;
import java.util.List;

/**
 * 用户智能匹配Service接口
 *
 * @author 开发者
 * @date 2025-12-19
 */
public interface SysUserMatchService {
    /**
     * 智能匹配目标用户
     * @param currentUser 当前发起匹配的用户
     * @param targetUserType 目标用户类型（02-农户/03-专家/04-银行/05-买家）
     * @return 按匹配分数降序排列的有效匹配结果
     */
    List<SysUserMatch> smartMatchUsers(SysUserMatch currentUser, String targetUserType);
}
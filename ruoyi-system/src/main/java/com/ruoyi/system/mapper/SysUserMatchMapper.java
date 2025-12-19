package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysUserMatch;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 用户匹配特征Mapper接口
 *
 * @author 开发者
 * @date 2025-12-19
 */
@Mapper
public interface SysUserMatchMapper {
    /**
     * 查询指定类型的所有用户（用于匹配）
     * @param targetUserType 目标用户类型（02/03/04/05）
     * @return 目标用户列表
     */
    List<SysUserMatch> selectTargetUserList(@Param("targetUserType") String targetUserType);

    /**
     * 根据用户ID查询匹配特征
     * @param userId 用户ID
     * @return 用户匹配特征
     */
    SysUserMatch selectUserMatchByUserId(@Param("userId") Long userId);

    /**
     * 新增用户匹配特征
     * @param userMatch 用户匹配特征
     * @return 结果
     */
    int insertUserMatch(SysUserMatch userMatch);

    /**
     * 修改用户匹配特征
     * @param userMatch 用户匹配特征
     * @return 结果
     */
    int updateUserMatch(SysUserMatch userMatch);
}
package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysUserMatch;
import com.ruoyi.system.mapper.SysUserMatchMapper;
import com.ruoyi.system.service.SysUserMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class SysUserMatchServiceImpl implements SysUserMatchService {
    // 匹配维度权重配置（可后续迁移到若依系统配置表sys_config）
    private static final double AREA_WEIGHT = 0.4; // 地区权重40%
    private static final double CATEGORY_WEIGHT = 0.3; // 品类权重30%
    private static final double DEMAND_TYPE_WEIGHT = 0.2; // 需求类型权重20%
    private static final double SCALE_WEIGHT = 0.1; // 规模权重10%
    private static final double MATCH_THRESHOLD = 60.0; // 最低匹配分数（≥60分才有效）

    @Autowired
    private SysUserMatchMapper userMatchMapper;

    @Override
    public List<SysUserMatch> smartMatchUsers(SysUserMatch currentUser, String targetUserType) {
        // 1. 空值校验
        if (currentUser == null || StringUtils.isEmpty(targetUserType)) {
            return new ArrayList<>();
        }

        // 2. 查询所有目标类型的用户（从数据库获取）
        List<SysUserMatch> allTargetUsers = userMatchMapper.selectTargetUserList(targetUserType);
        if (allTargetUsers.isEmpty()) {
            return new ArrayList<>();
        }

        // 3. 遍历计算每个目标用户的匹配分
        List<SysUserMatch> matchResultList = new ArrayList<>();
        for (SysUserMatch targetUser : allTargetUsers) {
            // 计算单维度匹配分（0-100分）
            double areaScore = calculateAreaMatchScore(currentUser.getArea(), targetUser.getArea());
            double categoryScore = calculateCategoryMatchScore(currentUser.getCategory(), targetUser.getCategory());
            double demandTypeScore = calculateDemandTypeMatchScore(currentUser, targetUser);
            double scaleScore = calculateScaleMatchScore(currentUser.getScale(), targetUser.getScale());

            // 加权计算总匹配分
            double totalScore = areaScore * AREA_WEIGHT
                    + categoryScore * CATEGORY_WEIGHT
                    + demandTypeScore * DEMAND_TYPE_WEIGHT
                    + scaleScore * SCALE_WEIGHT;

            // 仅保留阈值以上的匹配结果
            if (totalScore >= MATCH_THRESHOLD) {
                targetUser.setMatchScore(totalScore);
                targetUser.setMatchUserId(currentUser.getUserId());
                targetUser.setMatchUserName(currentUser.getUserName());
                matchResultList.add(targetUser);
            }
        }

        // 4. 按匹配分数降序排序
        return matchResultList.stream()
                .sorted((u1, u2) -> Double.compare(u2.getMatchScore(), u1.getMatchScore()))
                .collect(Collectors.toList());
    }

    /**
     * 计算地区匹配分（0-100）：完全一致100分，市级一致80分，省级一致50分，否则0分
     */
    private double calculateAreaMatchScore(String currentArea, String targetArea) {
        if (StringUtils.isEmpty(currentArea) || StringUtils.isEmpty(targetArea)) {
            return 0;
        }
        String[] currentAreaArr = currentArea.split("-");
        String[] targetAreaArr = targetArea.split("-");

        // 完全一致（省-市-区都匹配）
        if (currentArea.equals(targetArea)) {
            return 100;
        }
        // 市级一致（省+市匹配）
        if (currentAreaArr.length >= 2 && targetAreaArr.length >= 2
                && currentAreaArr[0].equals(targetAreaArr[0])
                && currentAreaArr[1].equals(targetAreaArr[1])) {
            return 80;
        }
        // 省级一致
        if (currentAreaArr.length >= 1 && targetAreaArr.length >= 1
                && currentAreaArr[0].equals(targetAreaArr[0])) {
            return 50;
        }
        return 0;
    }

    /**
     * 计算品类匹配分（0-100）：完全一致100分，大类一致80分，否则0分
     */
    private double calculateCategoryMatchScore(String currentCategory, String targetCategory) {
        if (StringUtils.isEmpty(currentCategory) || StringUtils.isEmpty(targetCategory)) {
            return 0;
        }
        // 完全一致
        if (currentCategory.equals(targetCategory)) {
            return 100;
        }
        // 大类匹配（可根据业务扩展更多品类组）
        Map<String, List<String>> categoryGroup = new HashMap<>();
        categoryGroup.put("粮食", Arrays.asList("水稻", "小麦", "玉米"));
        categoryGroup.put("果蔬", Arrays.asList("苹果", "橙子", "西红柿"));
        categoryGroup.put("畜禽", Arrays.asList("生猪", "肉鸡", "奶牛"));

        // 判断是否属于同一大类
        for (Map.Entry<String, List<String>> entry : categoryGroup.entrySet()) {
            if (entry.getValue().contains(currentCategory) && entry.getValue().contains(targetCategory)) {
                return 80;
            }
        }
        return 0;
    }

    /**
     * 计算需求类型匹配分（0-100）：根据用户类型匹配对应供需场景
     */
    private double calculateDemandTypeMatchScore(SysUserMatch currentUser, SysUserMatch targetUser) {
        String currentUserType = currentUser.getUserType();
        String targetUserType = targetUser.getUserType();
        String currentDemandType = currentUser.getDemandType();

        // 场景1：农户（02）→ 银行（04）（融资需求）
        if ("02".equals(currentUserType) && "04".equals(targetUserType) && "融资".equals(currentDemandType)) {
            return targetUser.getBankLoanType() != null && targetUser.getBankLoanType().contains(currentUser.getCategory()) ? 100 : 0;
        }
        // 场景2：农户（02）→ 专家（03）（技术需求）
        else if ("02".equals(currentUserType) && "03".equals(targetUserType) && "技术".equals(currentDemandType)) {
            return targetUser.getExpertField() != null && targetUser.getExpertField().contains(currentUser.getCategory()) ? 100 : 0;
        }
        // 场景3：买家（05）→ 农户（02）（采购需求）
        else if ("05".equals(currentUserType) && "02".equals(targetUserType) && "采购".equals(currentDemandType)) {
            return targetUser.getCategory().equals(currentUser.getCategory()) ? 100 : 0;
        }
        // 其他场景可扩展（如专家→农户、银行→农户）
        return 0;
    }

    /**
     * 计算规模匹配分（0-100）：供需规模差值越小，分数越高
     */
    private double calculateScaleMatchScore(Integer currentScale, Integer targetScale) {
        if (currentScale == null || targetScale == null || currentScale == 0 || targetScale == 0) {
            return 0;
        }
        // 计算规模差值比例（差值越小，分数越高）
        double diffRatio = Math.abs(currentScale - targetScale) / (double) Math.max(currentScale, targetScale);
        return 100 * (1 - diffRatio);
    }
}
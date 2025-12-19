package com.ruoyi.framework.web.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * RuoYi首创 html调用 thymeleaf 实现字典读取
 * 简化版本：不再使用字典表
 * 
 * @author ruoyi
 */
@Service("dict")
public class DictService
{
    /**
     * 根据字典类型查询字典数据信息
     * 
     * @param dictType 字典类型
     * @return 参数键值
     */
    public List<?> getType(String dictType)
    {
        // 返回空列表，不再使用字典表
        return new ArrayList<>();
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     * 
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    public String getLabel(String dictType, String dictValue)
    {
        // 返回原值，不再使用字典表
        return dictValue;
    }
}

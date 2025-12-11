package com.ruoyi.web.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 角色信息Controller
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/role")
public class SysRoleController extends BaseController
{
    private String prefix = "system/role";

    @GetMapping()
    public String role()
    {
        return prefix + "/role";
    }

    /**
     * 查询角色列表
     * 注意：如果项目中没有角色服务，此方法会返回空列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list()
    {
        // 暂时返回空列表，如果项目中有角色服务，可以在这里添加
        return getDataTable(new java.util.ArrayList<>());
    }
}


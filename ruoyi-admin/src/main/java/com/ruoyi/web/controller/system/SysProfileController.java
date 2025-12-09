package com.ruoyi.web.controller.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.framework.shiro.service.SysPasswordService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 个人信息 业务处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping({"/system/user/profile", "/system/user"})
public class SysProfileController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(SysProfileController.class);

    private String prefix = "system/user/profile";

    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private SysPasswordService passwordService;

    /**
     * 个人信息
     */
    @GetMapping()
    public String profile(ModelMap mmap)
    {
        SysUser user = getSysUser();
        mmap.put("user", user);
        // 新表结构：不再有角色组和岗位组
        mmap.put("roleGroup", user.getRole() != null ? user.getRole() : "");
        mmap.put("postGroup", "");
        return prefix + "/profile";
    }

    @GetMapping("/checkPassword")
    @ResponseBody
    public boolean checkPassword(String password)
    {
        SysUser user = getSysUser();
        return passwordService.matches(user, password);
    }

    @GetMapping("/resetPwd")
    public String resetPwd(ModelMap mmap)
    {
        SysUser user = getSysUser();
        mmap.put("user", userService.selectUserById(user.getUserId()));
        return prefix + "/resetPwd";
    }

    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwd(String oldPassword, String newPassword)
    {
        SysUser user = getSysUser();
        if (!passwordService.matches(user, oldPassword))
        {
            return error("修改密码失败，旧密码错误");
        }
        if (passwordService.matches(user, newPassword))
        {
            return error("新密码不能与旧密码相同");
        }
        // 新表结构：不再使用salt，直接加密密码
        String encryptedPassword = passwordService.encryptPassword(user.getUsername(), newPassword, "");
        user.setPasswordHash(encryptedPassword);
        if (userService.resetUserPwd(user) > 0)
        {
            setSysUser(userService.selectUserById(user.getUserId()));
            return success();
        }
        return error("修改密码异常，请联系管理员");
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit")
    public String edit(ModelMap mmap)
    {
        SysUser user = getSysUser();
        mmap.put("user", userService.selectUserById(user.getUserId()));
        return prefix + "/edit";
    }

    /**
     * 修改头像
     */
    @GetMapping("/avatar")
    public String avatar(ModelMap mmap)
    {
        SysUser user = getSysUser();
        mmap.put("user", userService.selectUserById(user.getUserId()));
        return prefix + "/avatar";
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public AjaxResult update(SysUser user)
    {
        try
        {
            SysUser currentUser = getSysUser();
            if (currentUser == null)
            {
                return error("用户未登录，请重新登录");
            }
            
            // 更新用户名（如果提供了新用户名且与当前用户名不同）
            if (StringUtils.isNotEmpty(user.getUsername()) && !user.getUsername().equals(currentUser.getUsername()))
            {
                // 检查用户名是否唯一（排除当前用户）
                SysUser checkUser = new SysUser();
                checkUser.setId(currentUser.getId());
                checkUser.setUsername(user.getUsername());
                if (!userService.checkLoginNameUnique(checkUser))
                {
                    return error("修改失败，用户名已存在");
                }
                currentUser.setUsername(user.getUsername());
            }
            
            // 更新姓名（允许为空）
            currentUser.setName(StringUtils.isNotEmpty(user.getName()) ? user.getName() : null);
            
            // 更新邮箱（允许为空，但如果提供了且与当前邮箱不同，需要检查唯一性）
            if (StringUtils.isNotEmpty(user.getEmail()))
            {
                // 如果邮箱没有改变，不需要检查唯一性
                String currentEmail = currentUser.getEmail();
                if (currentEmail == null || !user.getEmail().equals(currentEmail))
                {
                    currentUser.setEmail(user.getEmail());
                    if (!userService.checkEmailUnique(currentUser))
                    {
                        return error("修改失败，邮箱已存在");
                    }
                }
            }
            else
            {
                currentUser.setEmail(null);
            }
            
            // 更新手机号（允许为空，但如果提供了且与当前手机号不同，需要检查唯一性）
            if (StringUtils.isNotEmpty(user.getPhone()))
            {
                // 如果手机号没有改变，不需要检查唯一性
                String currentPhone = currentUser.getPhone();
                if (currentPhone == null || !user.getPhone().equals(currentPhone))
                {
                    currentUser.setPhone(user.getPhone());
                    if (!userService.checkPhoneUnique(currentUser))
                    {
                        return error("修改失败，手机号码已存在");
                    }
                }
            }
            else
            {
                currentUser.setPhone(null);
            }
            
            // 更新用户信息到数据库
            int result = userService.updateUserInfo(currentUser);
            if (result > 0)
            {
                // 重新获取更新后的用户信息并更新session
                SysUser updatedUser = userService.selectUserById(currentUser.getUserId());
                if (updatedUser != null)
                {
                    setSysUser(updatedUser);
                    return success();
                }
                else
                {
                    return error("修改成功，但获取更新后的用户信息失败");
                }
            }
            else if (result == 0)
            {
                // 没有数据被更新，可能是数据没有变化
                return success("数据未发生变化");
            }
            else
            {
                return error("修改失败，数据库更新异常");
            }
        }
        catch (Exception e)
        {
            log.error("修改个人信息失败", e);
            return error("修改失败：" + (e.getMessage() != null ? e.getMessage() : "服务器错误，请联系管理员"));
        }
    }

    /**
     * 保存头像
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping("/updateAvatar")
    @ResponseBody
    public AjaxResult updateAvatar(@RequestParam("avatarfile") MultipartFile file)
    {
        try
        {
            if (!file.isEmpty())
            {
                SysUser currentUser = getSysUser();
                String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION, true);
                if (userService.updateUserAvatar(currentUser.getUserId(), avatar))
                {
                // 新表结构：不再有avatar字段，但保留上传功能用于其他用途
                // String oldAvatar = currentUser.getAvatar();
                // if (StringUtils.isNotEmpty(oldAvatar))
                // {
                //     FileUtils.deleteFile(RuoYiConfig.getProfile() + FileUtils.stripPrefix(oldAvatar));
                // }
                // currentUser.setAvatar(avatar);
                    setSysUser(currentUser);
                    return success();
                }
            }
            return error();
        }
        catch (Exception e)
        {
            log.error("修改头像失败！", e);
            return error(e.getMessage());
        }
    }

    /**
     * 校验用户名是否唯一（用于前端验证）
     */
    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public boolean checkLoginNameUnique(@RequestParam(required = false) Long userId, @RequestParam(required = false) String loginName)
    {
        try
        {
            if (StringUtils.isEmpty(loginName))
            {
                return true; // 空值认为是唯一的
            }
            SysUser checkUser = new SysUser();
            checkUser.setId(userId != null ? userId : -1L);
            checkUser.setUsername(loginName);
            return userService.checkLoginNameUnique(checkUser);
        }
        catch (Exception e)
        {
            log.error("检查用户名唯一性失败", e);
            return false;
        }
    }

    /**
     * 校验邮箱是否唯一（用于前端验证）
     */
    @PostMapping("/checkEmailUnique")
    @ResponseBody
    public boolean checkEmailUnique(@RequestParam(required = false) Long userId, @RequestParam(required = false) String email)
    {
        try
        {
            if (StringUtils.isEmpty(email))
            {
                return true; // 空值认为是唯一的
            }
            SysUser checkUser = new SysUser();
            checkUser.setId(userId != null ? userId : -1L);
            checkUser.setEmail(email);
            return userService.checkEmailUnique(checkUser);
        }
        catch (Exception e)
        {
            log.error("检查邮箱唯一性失败", e);
            return false;
        }
    }

    /**
     * 校验手机号是否唯一（用于前端验证）
     */
    @PostMapping("/checkPhoneUnique")
    @ResponseBody
    public boolean checkPhoneUnique(@RequestParam(required = false) Long userId, @RequestParam(required = false) String phonenumber)
    {
        try
        {
            if (StringUtils.isEmpty(phonenumber))
            {
                return true; // 空值认为是唯一的
            }
            SysUser checkUser = new SysUser();
            checkUser.setId(userId != null ? userId : -1L);
            checkUser.setPhone(phonenumber);
            return userService.checkPhoneUnique(checkUser);
        }
        catch (Exception e)
        {
            log.error("检查手机号唯一性失败", e);
            return false;
        }
    }
}

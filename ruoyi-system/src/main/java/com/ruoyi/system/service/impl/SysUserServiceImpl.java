package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.IBankInfoService;
import com.ruoyi.system.domain.BankInfo;

/**
 * 用户 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl implements ISysUserService
{
    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    protected Validator validator;

    @Autowired
    private IBankInfoService bankInfoService;

    /**
     * 加密密码（新表结构：使用MD5加密 username + password）
     * 注意：为了与SysPasswordService保持一致，应该使用相同的加密方法
     * 但这里保留作为备用，实际注册时应该使用SysPasswordService.encryptPassword()
     */
    private String encryptPassword(String username, String password)
    {
        // 使用与SysPasswordService相同的加密方式：Md5Hash
        return new Md5Hash(username + password).toHex();
    }

    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<SysUser> selectUserList(SysUser user)
    {
        return userMapper.selectUserList(user);
    }

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByLoginName(String userName)
    {
        return userMapper.selectUserByLoginName(userName);
    }

    /**
     * 通过手机号码查询用户
     * 
     * @param phoneNumber 手机号码
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByPhoneNumber(String phoneNumber)
    {
        return userMapper.selectUserByPhoneNumber(phoneNumber);
    }

    /**
     * 通过邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByEmail(String email)
    {
        return userMapper.selectUserByEmail(email);
    }

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId)
    {
        return userMapper.selectUserById(userId);
    }

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId)
    {
        checkUserAllowed(new SysUser(userId));
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(String ids)
    {
        Long[] userIds = Convert.toLongArray(ids);
        for (Long userId : userIds)
        {
            checkUserAllowed(new SysUser(userId));
        }
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 新增保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user)
    {
        // 加密密码
        if (StringUtils.isNotEmpty(user.getPassword()))
        {
            String username = StringUtils.isNotEmpty(user.getUsername()) ? user.getUsername() : user.getLoginName();
            user.setPasswordHash(encryptPassword(username, user.getPassword()));
        }
        return userMapper.insertUser(user);
    }

    /**
     * 注册用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public boolean registerUser(SysUser user)
    {
        // 密码已经在SysRegisterService中加密过了，这里不再重复加密
        // 如果passwordHash为空，说明可能是直接调用此方法，需要加密
        if (StringUtils.isEmpty(user.getPasswordHash()) && StringUtils.isNotEmpty(user.getPassword()))
        {
            String username = StringUtils.isNotEmpty(user.getUsername()) ? user.getUsername() : user.getLoginName();
            user.setPasswordHash(encryptPassword(username, user.getPassword()));
        }
        
        // 插入用户
        int result = userMapper.insertUser(user);
        if (result <= 0)
        {
            return false;
        }
        
        // 如果是银行用户，自动创建银行信息记录
        if ("bank".equals(user.getRole()) && bankInfoService != null)
        {
            // 检查是否已存在银行信息（防止重复创建）
            BankInfo existingBank = bankInfoService.selectBankInfoByUserId(user.getId());
            if (existingBank == null)
            {
                BankInfo bankInfo = new BankInfo();
                // 生成银行代码：使用用户ID确保唯一性，格式为 BANK + 用户ID
                String bankCode = "BANK" + String.format("%06d", user.getId());
                bankInfo.setBankCode(bankCode);
                // 使用用户名作为银行名称，如果没有则使用默认值
                String username = StringUtils.isNotEmpty(user.getUsername()) ? user.getUsername() : user.getLoginName();
                bankInfo.setBankName(StringUtils.isNotEmpty(user.getName()) ? user.getName() + "银行" : username + "银行");
                bankInfo.setUserId(user.getId());
                // 使用用户信息填充联系信息
                bankInfo.setContactPerson(user.getName());
                bankInfo.setContactPhone(user.getPhone());
                bankInfo.setContactEmail(user.getEmail());
                bankInfo.setAddress(user.getContact());
                bankInfo.setStatus("0"); // 默认正常状态
                bankInfo.setCreateBy(username);
                
                // 创建银行信息记录
                bankInfoService.insertBankInfo(bankInfo);
            }
        }
        
        return true;
    }

    /**
     * 修改保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user)
    {
        // 如果密码不为空，则加密密码
        if (StringUtils.isNotEmpty(user.getPassword()))
        {
            String username = StringUtils.isNotEmpty(user.getUsername()) ? user.getUsername() : user.getLoginName();
            user.setPasswordHash(encryptPassword(username, user.getPassword()));
        }
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户个人详细信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserInfo(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     * 
     * @param userId 用户ID
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(Long userId, String avatar)
    {
        return userMapper.updateUserAvatar(userId, avatar) > 0;
    }

    /**
     * 更新用户登录信息（IP和登录时间）
     * 
     * @param userId 用户ID
     * @param loginIp 登录IP地址
     * @param loginDate 登录时间
     * @return 结果
     */
    @Override
    public void updateLoginInfo(Long userId, String loginIp, Date loginDate)
    {
        userMapper.updateLoginInfo(userId, loginIp, loginDate);
    }

    /**
     * 修改用户密码
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetUserPwd(SysUser user)
    {
        // 加密新密码
        String username = StringUtils.isNotEmpty(user.getUsername()) ? user.getUsername() : user.getLoginName();
        String encryptedPassword = encryptPassword(username, user.getPassword());
        user.setPasswordHash(encryptedPassword);
        // Mapper方法需要userId和password参数
        return userMapper.resetUserPwd(user.getId(), encryptedPassword, "");
    }

    /**
     * 校验用户名称是否唯一
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkLoginNameUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        SysUser info = userMapper.checkLoginNameUnique(user.getUsername());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkPhoneUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhone());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkEmailUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     * 
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user)
    {
        if (StringUtils.isNotNull(user.getId()) && user.isAdmin())
        {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 用户状态修改
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int changeStatus(SysUser user)
    {
        // 新表结构：使用isVerified字段
        return userMapper.updateUserStatus(user.getId(), user.getIsVerified() != null && user.getIsVerified() == 1 ? "1" : "0");
    }
}

package com.ruoyi.framework.shiro.service;

import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PostConstruct;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.ShiroConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.exception.user.UserPasswordRetryLimitExceedException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;

/**
 * 登录密码方法
 * 
 * @author ruoyi
 */
@Component
public class SysPasswordService
{
    @Autowired
    private CacheManager cacheManager;

    private Cache<String, AtomicInteger> loginRecordCache;

    @Value(value = "${user.password.maxRetryCount}")
    private String maxRetryCount;

    @PostConstruct
    public void init()
    {
        loginRecordCache = cacheManager.getCache(ShiroConstants.LOGIN_RECORD_CACHE);
    }

    public void validate(SysUser user, String password)
    {
        String loginName = user.getUsername();

        AtomicInteger retryCount = loginRecordCache.get(loginName);

        if (retryCount == null)
        {
            retryCount = new AtomicInteger(0);
            loginRecordCache.put(loginName, retryCount);
        }
        if (retryCount.incrementAndGet() > Integer.valueOf(maxRetryCount).intValue())
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginName, Constants.LOGIN_FAIL, MessageUtils.message("user.password.retry.limit.exceed", maxRetryCount)));
            throw new UserPasswordRetryLimitExceedException(Integer.valueOf(maxRetryCount).intValue());
        }

        if (!matches(user, password))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginName, Constants.LOGIN_FAIL, MessageUtils.message("user.password.retry.limit.count", retryCount)));
            loginRecordCache.put(loginName, retryCount);
            throw new UserPasswordNotMatchException();
        }
        else
        {
            clearLoginRecordCache(loginName);
        }
    }

    public boolean matches(SysUser user, String newPassword)
    {
        if (user.getPasswordHash() == null)
        {
            return false;
        }
        
        // 新表结构：直接比较password_hash
        // 使用MD5加密：username + password
        String encryptedPassword = encryptPassword(user.getUsername(), newPassword, "");
        
        // 首先尝试标准加密方式（新注册的用户）
        if (user.getPasswordHash().equals(encryptedPassword))
        {
            return true;
        }
        
        // 向后兼容：尝试双重加密方式（修复前注册的用户）
        // 双重加密：MD5(username + MD5(username + password))
        String doubleEncryption = encryptPassword(user.getUsername(), encryptedPassword, "");
        if (user.getPasswordHash().equals(doubleEncryption))
        {
            // 如果匹配双重加密，在内存中升级为单次加密（不更新数据库，避免循环依赖）
            // 用户登录成功后可以在个人中心修改密码，或者管理员可以重置密码
            user.setPasswordHash(encryptedPassword);
            return true;
        }
        
        return false;
    }

    public void clearLoginRecordCache(String loginName)
    {
        loginRecordCache.remove(loginName);
    }

    public String encryptPassword(String loginName, String password, String salt)
    {
        // 新表结构：使用MD5加密 username + password
        return new Md5Hash(loginName + password).toHex();
    }
}

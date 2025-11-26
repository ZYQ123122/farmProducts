package com.ruoyi.framework.shiro.rememberMe;

import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.CookieRememberMeManager;

/**
 * 自定义CookieRememberMeManager（简化版本）
 *
 * @author ruoyi
 */
public class CustomCookieRememberMeManager extends CookieRememberMeManager
{
    /**
     * 记住我时简化处理
     */
    @Override
    protected void rememberIdentity(Subject subject, PrincipalCollection principalCollection)
    {
        // 新表结构：不再需要处理角色权限
        byte[] bytes = convertPrincipalsToBytes(principalCollection);
        rememberSerializedIdentity(subject, bytes);
    }

    /**
     * 取记住我身份时简化处理
     */
    @Override
    public PrincipalCollection getRememberedPrincipals(SubjectContext subjectContext)
    {
        // 新表结构：不再需要恢复角色权限
        return super.getRememberedPrincipals(subjectContext);
    }
}

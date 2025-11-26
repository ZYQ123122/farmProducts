package com.ruoyi.framework.shiro.service;

import java.io.Serializable;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;
import com.ruoyi.framework.shiro.session.OnlineSession;

/**
 * 会话db操作处理（简化版本）
 * 
 * @author ruoyi
 */
@Component
public class SysShiroService
{
    /**
     * 删除会话
     *
     * @param onlineSession 会话信息
     */
    public void deleteSession(OnlineSession onlineSession)
    {
        // 简化：不再使用在线用户服务
    }

    /**
     * 获取会话信息
     *
     * @param sessionId
     * @return
     */
    public Session getSession(Serializable sessionId)
    {
        // 简化：返回null，不再使用在线用户服务
        return null;
    }

    public Session createSession(Object userOnline)
    {
        // 简化：返回空会话
        return new OnlineSession();
    }
}

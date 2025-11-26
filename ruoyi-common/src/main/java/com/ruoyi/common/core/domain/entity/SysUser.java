package com.ruoyi.common.core.domain.entity;

import java.util.Date;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.annotation.Excel.Type;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
public class SysUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户序号", type = Type.EXPORT, cellType = ColumnType.NUMERIC, prompt = "用户编号")
    private Long id;

    /** 用户名（手机号或邮箱） */
    @Excel(name = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 0, max = 50, message = "用户名长度不能超过50个字符")
    private String username;

    /** 密码哈希值 */
    @JsonIgnore
    @NotBlank(message = "密码不能为空")
    private String passwordHash;

    /** 用户角色（农户/专家/银行/买家/管理员） */
    @Excel(name = "用户角色", readConverterExp = "farmer=农户,expert=专家,bank=银行,buyer=买家,admin=管理员")
    @NotBlank(message = "用户角色不能为空")
    private String role;

    /** 用户姓名 */
    @Excel(name = "用户姓名")
    @Size(min = 0, max = 100, message = "用户姓名长度不能超过100个字符")
    private String name;

    /** 联系方式 */
    @Excel(name = "联系方式")
    @Size(min = 0, max = 100, message = "联系方式长度不能超过100个字符")
    private String contact;

    /** 邮箱（可选） */
    @Excel(name = "用户邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /** 手机号（可选） */
    @Excel(name = "手机号码", cellType = ColumnType.TEXT)
    @Size(min = 0, max = 20, message = "手机号长度不能超过20个字符")
    private String phone;

    /** 是否通过资质验证（0:未通过,1:已通过） */
    @Excel(name = "是否验证", readConverterExp = "0=未通过,1=已通过")
    private Integer isVerified;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date updatedAt;

    public SysUser()
    {
    }

    public SysUser(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    // 为了兼容旧代码，保留getUserId和setUserId方法
    public Long getUserId()
    {
        return id;
    }

    public void setUserId(Long userId)
    {
        this.id = userId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    // 为了兼容旧代码，保留getLoginName和setLoginName方法
    public String getLoginName()
    {
        return username;
    }

    public void setLoginName(String loginName)
    {
        this.username = loginName;
    }

    @JsonIgnore
    public String getPasswordHash()
    {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash)
    {
        this.passwordHash = passwordHash;
    }

    // 为了兼容旧代码，保留getPassword和setPassword方法
    @JsonIgnore
    public String getPassword()
    {
        return passwordHash;
    }

    public void setPassword(String password)
    {
        this.passwordHash = password;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    // 为了兼容旧代码，保留getUserType和setUserType方法
    public String getUserType()
    {
        return role;
    }

    public void setUserType(String userType)
    {
        this.role = userType;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    // 为了兼容旧代码，保留getUserName和setUserName方法
    public String getUserName()
    {
        return name;
    }

    public void setUserName(String userName)
    {
        this.name = userName;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    // 为了兼容旧代码，保留getPhonenumber和setPhonenumber方法
    public String getPhonenumber()
    {
        return phone;
    }

    public void setPhonenumber(String phonenumber)
    {
        this.phone = phonenumber;
    }

    // 为了兼容旧代码，保留getAvatar和setAvatar方法（新表结构中没有avatar字段）
    public String getAvatar()
    {
        return null;
    }

    public void setAvatar(String avatar)
    {
        // 新表结构中没有avatar字段，此方法为空实现
    }

    public Integer getIsVerified()
    {
        return isVerified;
    }

    public void setIsVerified(Integer isVerified)
    {
        this.isVerified = isVerified;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    // 为了兼容BaseEntity，保留createTime和updateTime的getter/setter
    @Override
    public Date getCreateTime()
    {
        return createdAt;
    }

    @Override
    public void setCreateTime(Date createTime)
    {
        this.createdAt = createTime;
    }

    @Override
    public Date getUpdateTime()
    {
        return updatedAt;
    }

    @Override
    public void setUpdateTime(Date updateTime)
    {
        this.updatedAt = updateTime;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("username", getUsername())
                .append("role", getRole())
                .append("name", getName())
                .append("contact", getContact())
                .append("email", getEmail())
                .append("phone", getPhone())
                .append("isVerified", getIsVerified())
                .append("createdAt", getCreatedAt())
                .append("updatedAt", getUpdatedAt())
                .toString();
    }
}

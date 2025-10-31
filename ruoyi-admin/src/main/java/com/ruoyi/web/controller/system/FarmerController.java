package com.ruoyi.web.controller.system;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.ShiroConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysConfigService;

@Controller
@RequestMapping("/farmer")
public class FarmerController extends BaseController {

    @Autowired
    private ISysConfigService configService;

    @GetMapping("/index")
    public String index(ModelMap mmap, HttpServletRequest request) {
        SysUser user = getSysUser();
        mmap.put("user", user);
        mmap.put("sideTheme", configService.selectConfigByKey("sys.index.sideTheme"));
        mmap.put("skinName", configService.selectConfigByKey("sys.index.skinName"));
        Boolean footer = Convert.toBool(configService.selectConfigByKey("sys.index.footer"), true);
        Boolean tagsView = Convert.toBool(configService.selectConfigByKey("sys.index.tagsView"), true);
        mmap.put("footer", footer);
        mmap.put("tagsView", tagsView);
        mmap.put("mainClass", contentMainClass(footer, tagsView));
        mmap.put("copyrightYear", RuoYiConfig.getCopyrightYear());
        mmap.put("demoEnabled", RuoYiConfig.isDemoEnabled());
        mmap.put("isDefaultModifyPwd", initPasswordIsModify(user.getPwdUpdateDate()));
        mmap.put("isPasswordExpired", passwordIsExpiration(user.getPwdUpdateDate()));
        boolean isMobile = ServletUtils.checkAgentIsMobile(ServletUtils.getRequest().getHeader("User-Agent"));
        mmap.put("isMobile", isMobile);
        request.getSession().setAttribute(ShiroConstants.CSRF_TOKEN, ServletUtils.generateToken());
        return "farmer/index";
    }

    @GetMapping("/main")
    public String main(ModelMap mmap) {
        mmap.put("version", RuoYiConfig.getVersion());
        return "farmer/main";
    }

    @GetMapping("/product/info")
    public String productInfo() {
        return "farmer/product/info";
    }

    @GetMapping("/product/manage")
    public String productManage() {
        return "farmer/product/manage";
    }

    @GetMapping("/product/demand")
    public String productDemand() {
        return "farmer/product/demand";
    }

    @GetMapping("/product/contact")
    public String productContact() {
        return "farmer/product/contact";
    }

    @GetMapping("/expert/appointment")
    public String expertAppointment() {
        return "farmer/expert/appointment";
    }

    @GetMapping("/expert/inquiry")
    public String expertInquiry() {
        return "farmer/expert/inquiry";
    }

    @GetMapping("/expert/knowledge")
    public String expertKnowledge() {
        return "farmer/expert/knowledge";
    }

    @GetMapping("/finance/match")
    public String financeMatch() {
        return "farmer/finance/match";
    }

    @GetMapping("/finance/apply")
    public String financeApply() {
        return "farmer/finance/apply";
    }

    @GetMapping("/community/index")
    public String communityIndex() {
        return "farmer/community/index";
    }

    private String contentMainClass(Boolean footer, Boolean tagsView) {
        if (!footer && !tagsView) {
            return "tagsview-footer-hide";
        } else if (!footer) {
            return "footer-hide";
        } else if (!tagsView) {
            return "tagsview-hide";
        }
        return StringUtils.EMPTY;
    }

    private boolean initPasswordIsModify(Date pwdUpdateDate) {
        Integer initPasswordModify = Convert.toInt(configService.selectConfigByKey("sys.account.initPasswordModify"));
        return initPasswordModify != null && initPasswordModify == 1 && pwdUpdateDate == null;
    }

    private boolean passwordIsExpiration(Date pwdUpdateDate) {
        Integer passwordValidateDays = Convert.toInt(configService.selectConfigByKey("sys.account.passwordValidateDays"));
        if (passwordValidateDays != null && passwordValidateDays > 0) {
            if (StringUtils.isNull(pwdUpdateDate)) {
                return true;
            }
            Date nowDate = DateUtils.getNowDate();
            return DateUtils.differentDaysByMillisecond(nowDate, pwdUpdateDate) > passwordValidateDays;
        }
        return false;
    }
}
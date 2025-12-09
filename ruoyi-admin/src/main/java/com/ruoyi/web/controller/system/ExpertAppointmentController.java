package com.ruoyi.web.controller.system;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.ExpertAppointment;
import com.ruoyi.system.service.IExpertAppointmentService;

/**
 * 专家预约Controller（农户 + 专家端）
 */
@Controller
@RequestMapping("/system/appointment")
public class ExpertAppointmentController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(ExpertAppointmentController.class);

    @Autowired
    private IExpertAppointmentService appointmentService;

    private static final SimpleDateFormat DATE_TIME_FMT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    /**
     * 农户提交预约
     */
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(Long expertId, String appointmentTime, String note, String contactPhone)
    {
        if (expertId == null)
        {
            return error("请选择专家");
        }
        if (appointmentTime == null || appointmentTime.isEmpty())
        {
            return error("请选择预约时间");
        }
        ExpertAppointment appointment = new ExpertAppointment();
        appointment.setUserId(ShiroUtils.getUserId());
        appointment.setExpertId(expertId);
        try
        {
            appointment.setAppointmentTime(DATE_TIME_FMT.parse(appointmentTime));
        }
        catch (ParseException e)
        {
            throw new ServiceException("预约时间格式不正确");
        }
        appointment.setNote(note);
        appointment.setContactPhone(contactPhone);
        appointment.setStatus("pending");
        return toAjax(appointmentService.insertAppointment(appointment));
    }

    /**
     * 农户查看自己的预约列表
     */
    @PostMapping("/myList")
    @ResponseBody
    public AjaxResult myList(String status)
    {
        Long userId = ShiroUtils.getUserId();
        List<ExpertAppointment> list = appointmentService.selectAppointmentListByUserId(userId, status);
        return success(list);
    }

    /**
     * 专家查看自己的预约列表
     */
    @PostMapping("/expertList")
    @ResponseBody
    public AjaxResult expertList(String status)
    {
        Long expertId = ShiroUtils.getUserId();
        log.info("查询专家预约列表: expertId={}, status={}", expertId, status);
        List<ExpertAppointment> list = appointmentService.selectAppointmentListByExpertId(expertId, status);
        log.info("查询结果: 找到{}条预约记录", list != null ? list.size() : 0);
        return success(list);
    }

    /**
     * 专家处理预约（接受/拒绝）
     */
    @PostMapping("/handle")
    @ResponseBody
    public AjaxResult handle(Long id, String action)
    {
        if (id == null)
        {
            return error("参数错误");
        }
        if (!"accept".equals(action) && !"reject".equals(action))
        {
            return error("不支持的操作类型");
        }
        ExpertAppointment appointment = appointmentService.selectAppointmentById(id);
        if (appointment == null)
        {
            return error("预约不存在");
        }
        if (!ShiroUtils.getUserId().equals(appointment.getExpertId()))
        {
            return error("无权处理该预约");
        }
        String status = "accepted";
        if ("reject".equals(action))
        {
            status = "rejected";
        }
        return toAjax(appointmentService.updateAppointmentStatus(id, status));
    }
}
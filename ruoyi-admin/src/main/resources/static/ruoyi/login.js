
$(function() {
    validateKickout();
    validateRule();
    $('.imgcode').click(function() {
        var url = ctx + "captcha/captchaImage?type=" + captchaType + "&s=" + Math.random();
        $(".imgcode").attr("src", url);
    });
});

function login() {
    var username = $.common.trim($("input[name='username']").val());
    var password = $.common.trim($("input[name='password']").val());
    var validateCode = $("input[name='validateCode']").val();
    var rememberMe = $("input[name='rememberme']").is(':checked');
    var role = $.common.trim($("select[name='role']").val()); // 获取 role 参数
    if ($.common.isEmpty(username)) {
        $.modal.msg("请输入用户名");
        return false;
    }
    if ($.common.isEmpty(password)) {
        $.modal.msg("请输入密码");
        return false;
    }
    if ($.common.isEmpty(role)) {
        $.modal.msg("请选择身份");
        return false;
    }
    if($.common.isEmpty(validateCode) && captchaEnabled) {
        $.modal.msg("请输入验证码");
        return false;
    }
    $.ajax({
        type: "post",
        url: ctx + "login",
        data: {
            "username": username,
            "password": password,
            "validateCode": validateCode,
            "rememberMe": rememberMe,
            "role": role // 添加 role 参数
        },
        beforeSend: function () {
            $.modal.loading($("#btnSubmit").data("loading"));
        },
        success: function(r) {
            if (r.code == web_status.SUCCESS) {
                location.href = r.redirect || (ctx + 'index');  // 使用后端返回的 redirect 字段
            } else {
                $('.imgcode').click();
                $(".code").val("");
                $.modal.msg(r.msg);
            }
        },
        error: function(xhr, status, error) {
            $.modal.closeLoading();
            var errorMsg = "请求失败";
            
            // 优先尝试从响应JSON中获取错误信息
            if (xhr.responseJSON) {
                if (xhr.responseJSON.msg) {
                    errorMsg = xhr.responseJSON.msg;
                } else if (xhr.responseJSON.message) {
                    errorMsg = xhr.responseJSON.message;
                }
            } 
            // 尝试解析响应文本
            else if (xhr.responseText) {
                try {
                    var response = JSON.parse(xhr.responseText);
                    if (response.msg) {
                        errorMsg = response.msg;
                    } else if (response.message) {
                        errorMsg = response.message;
                    }
                } catch (e) {
                    // 如果不是JSON，可能是HTML错误页面
                    if (xhr.status === 500) {
                        errorMsg = "服务器内部错误，请检查后端日志";
                    } else if (xhr.status === 404) {
                        errorMsg = "请求的接口不存在";
                    } else if (xhr.status === 403) {
                        errorMsg = "没有权限访问";
                    } else {
                        errorMsg = "请求失败：" + (xhr.statusText || error || "未知错误");
                    }
                }
            } 
            // 网络错误或其他情况
            else {
                if (xhr.status === 0) {
                    errorMsg = "网络连接失败，请检查网络或服务器是否运行";
                } else if (xhr.status === 500) {
                    errorMsg = "服务器内部错误 (500)";
                } else if (xhr.status === 404) {
                    errorMsg = "接口不存在 (404)";
                } else if (xhr.status === 403) {
                    errorMsg = "没有权限访问 (403)";
                } else {
                    errorMsg = "请求失败：" + (xhr.statusText || error || "未知错误") + " (状态码: " + (xhr.status || "未知") + ")";
                }
            }
            
            // 显示详细错误信息（开发环境）
            if (xhr.status) {
                console.error("登录请求失败:", {
                    status: xhr.status,
                    statusText: xhr.statusText,
                    responseText: xhr.responseText,
                    error: error
                });
            }
            
            $.modal.msg(errorMsg);
            $('.imgcode').click();
        }
    });
}

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            },
            role: {
                required: true // 添加 role 验证
            },
            validateCode: {
                required: function() { return captchaEnabled; }
            }
        },
        messages: {
            username: {
                required: icon + "请输入您的用户名",
            },
            password: {
                required: icon + "请输入您的密码",
            },
            role: {
                required: icon + "请选择身份"
            },
            validateCode: {
                required: icon + "请输入验证码"
            }
        },
        submitHandler: function(form) {
            login();
        }
    })
}

function validateKickout() {
    if (getParam("kickout") == 1) {
        layer.alert("<font color='red'>您已在别处登录，请您修改密码或重新登录</font>", {
            icon: 0,
            title: "系统提示"
        },
        function(index) {
            //关闭弹窗
            layer.close(index);
            if (top != self) {
                top.location = self.location;
            } else {
                var url = location.search;
                if (url) {
                    var oldUrl = window.location.href;
                    var newUrl = oldUrl.substring(0, oldUrl.indexOf('?'));
                    self.location = newUrl;
                }
            }
        });
    }
}

function getParam(paramName) {
    var reg = new RegExp("(^|&)" + paramName + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return null;
}
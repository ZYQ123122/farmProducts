
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
        error: function(xhr) {
            $.modal.closeLoading();
            $.modal.msg("请求失败：" + xhr.statusText);
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
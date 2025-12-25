/**
 * 用户智能匹配页面逻辑
 * 适配若依一体版的jQuery+Bootstrap生态
 */
$(function() {
    // 全局变量
    var currentUserType = ''; // 当前登录用户类型（可从后端获取）
    var userId = [[${@permissionService.getUserId()}]]; // Thymeleaf获取当前用户ID
    var userName = [[${@permissionService.getUserName()}]]; // Thymeleaf获取当前用户名

    // 1. 初始化：获取当前用户信息（可选）
    initUserInfo();

    // 2. 监听当前用户类型（模拟：实际可从后端接口获取后赋值）
    // 示例：如果当前用户是专家，显示专家字段；是银行显示银行字段
    $('#targetUserType').change(function() {
        var targetType = $(this).val();
        // 隐藏所有专属字段
        $('#expertFieldGroup, #bankLoanTypeGroup, #expertFieldTh, #bankLoanTypeTh').hide();
        // 根据目标类型显示对应字段
        if (targetType === '03') {
            $('#expertFieldGroup, #expertFieldTh').show();
        } else if (targetType === '04') {
            $('#bankLoanTypeGroup, #bankLoanTypeTh').show();
        }
    });

    // 3. 发起匹配按钮点击事件
    $('#submitMatch').click(function() {
        // 表单验证
        if (!validateForm()) {
            return;
        }
        // 构建请求参数
        var params = {
            targetUserType: $('#targetUserType').val(),
            currentUser: {
                userId: userId,
                userName: userName,
                userType: currentUserType,
                area: $('#area').val(),
                category: $('#category').val(),
                demandType: $('#demandType').val(),
                scale: $('#scale').val(),
                expertField: $('#expertField').val(),
                bankLoanType: $('#bankLoanType').val()
            }
        };
        // 显示加载中
        $.modal.loading("正在匹配，请稍候...");
        // 调用后端接口
        $.ajax({
            url: ctx + "system/userMatch/smartMatch",
            type: "post",
            data: JSON.stringify(params),
            contentType: "application/json;charset=utf-8",
            success: function(res) {
                $.modal.closeLoading();
                if (res.code === 200) {
                    // 展示结果区域
                    $('#resultBox').show();
                    // 渲染匹配结果
                    renderMatchResult(res.data);
                    $.modal.msgSuccess("匹配成功！");
                } else {
                    $.modal.msgError(res.msg);
                }
            },
            error: function() {
                $.modal.closeLoading();
                $.modal.msgError("匹配失败，请重试！");
            }
        });
    });

    // 4. 重置按钮点击事件
    $('#resetForm').click(function() {
        $('#matchForm')[0].reset();
        $('#resultBox').hide();
        $('#expertFieldGroup, #bankLoanTypeGroup, #expertFieldTh, #bankLoanTypeTh').hide();
        $('#matchResultBody').empty();
        $('#emptyResult').hide();
    });

    /**
     * 表单验证
     */
    function validateForm() {
        var targetType = $('#targetUserType').val();
        var area = $('#area').val();
        var category = $('#category').val();

        if (isEmpty(targetType)) {
            $.modal.msgError("请选择匹配目标类型！");
            return false;
        }
        if (isEmpty(area)) {
            $.modal.msgError("请输入所在地区！");
            return false;
        }
        if (isEmpty(category)) {
            $.modal.msgError("请输入经营/采购品类！");
            return false;
        }
        return true;
    }

    /**
     * 初始化当前用户信息
     */
    function initUserInfo() {
        $.ajax({
            url: ctx + "system/userMatch/getUserMatchInfo",
            type: "get",
            success: function(res) {
                if (res.code === 200) {
                    // 回显用户信息到表单
                    $('#area').val(res.data.area);
                    $('#category').val(res.data.category);
                    $('#demandType').val(res.data.demandType);
                    $('#scale').val(res.data.scale);
                    // 记录当前用户类型
                    currentUserType = res.data.userType;
                }
            }
        });
    }

    /**
     * 渲染匹配结果表格
     */
    function renderMatchResult(data) {
        var $tbody = $('#matchResultBody');
        $tbody.empty();
        // 无结果
        if (data.length === 0) {
            $('#emptyResult').show();
            return;
        }
        $('#emptyResult').hide();
        // 遍历结果生成行
        $.each(data, function(i, item) {
            var tr = $('<tr></tr>');
            // 用户名
            tr.append('<td class="text-center">' + item.matchUserName + '</td>');
            // 用户类型（转换为中文）
            var userTypeText = '';
            switch (item.userType) {
                case '02': userTypeText = '农户'; break;
                case '03': userTypeText = '专家'; break;
                case '04': userTypeText = '银行'; break;
                case '05': userTypeText = '买家'; break;
                default: userTypeText = '未知';
            }
            tr.append('<td class="text-center">' + userTypeText + '</td>');
            // 地区
            tr.append('<td class="text-center">' + (item.area || '') + '</td>');
            // 品类
            tr.append('<td class="text-center">' + (item.category || '') + '</td>');
            // 匹配度（保留2位小数）
            tr.append('<td class="text-center">' + (item.matchScore ? item.matchScore.toFixed(2) : 0) + '</td>');
            // 规模
            tr.append('<td class="text-center">' + (item.scale || '') + '</td>');
            // 专家/银行专属列
            if ($('#expertFieldTh').is(':visible')) {
                tr.append('<td class="text-center">' + (item.expertField || '') + '</td>');
            } else if ($('#bankLoanTypeTh').is(':visible')) {
                tr.append('<td class="text-center">' + (item.bankLoanType || '') + '</td>');
            } else {
                // 占位（保持列数一致）
                tr.append('<td class="text-center"></td>');
            }
            $tbody.append(tr);
        });
    }

    /**
     * 判空工具函数（复用若依逻辑）
     */
    function isEmpty(value) {
        return value === undefined || value === null || value === '';
    }
});
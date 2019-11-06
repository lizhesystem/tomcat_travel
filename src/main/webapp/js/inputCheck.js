// 验证用户名
checkusername = function () {
    var username = $('#username').val();
    var flag = /^\w{4,10}$/.test(username);
    if (flag) {
        $('#username').css("border", "")
    } else {
        $('#username').css("border", "1px solid red")
    }
    return flag;
};

// 验证密码不能为空
checkpassword = function () {
    var flag = $('#password').val().length !== 0;
    if (flag) {
        $('#password').css("border", "")
    } else {
        $('#password').css("border", "1px solid red");
    }

    return flag;
};

// 验证邮箱
checkemail = function () {
    var flag = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/.test($('#email').val());
    if (flag) {
        $('#email').css("border", "")
    } else {
        $('#email').css("border", "1px solid red");
    }
    return flag;
};

// 验证手机号
checktelephone = function () {
    var flag = /^1[35678]\d{9}$/.test($("#telephone").val());
    if (flag) {
        $('#telephone').css("border", "")
    } else {
        $('#telephone').css("border", "1px solid red")
    }
    return flag;
};


$(function () {
    // 入口函数,页面加载完之后执行
    $('#registerForm').submit(function () {
        // 如果条件都是true代表都满足条件,使用serialize封装数据ajax发送请求
        if (checkusername() && checkpassword() && checkemail() && checktelephone()) {
            $.post('user/regist', $(this).serialize(), function (data) {
                if (data.flag) {
                    window.location.href = 'register_ok.html'
                } else {
                    $('.magerr').html(data.errorMsg);
                    $('#img').attr('src', "checkCode?" + new Date().getTime());
                }
            })
        }

        return false;
    });

    // 焦点离开输入框触发函数效验
    $('#username').blur(checkusername);
    $('#password').blur(checkpassword);
    $('#email').blur(checkemail);
    $('#telephone').blur(checktelephone);

});

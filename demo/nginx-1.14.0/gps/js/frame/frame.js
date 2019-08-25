$(document).ready(function() {
    $('.city-select').change(function(){
        var cityCode = $(this).find('option:selected').val();
        changeCity(cityCode);
    });
});
/**
 * 注销
 */
function logout() {
    $("#dialog_logout").dialog({
        height : 200,
        width : 300,
        title : "系统提示",
        modal : true,
        buttons : [{
                text : '确定',
                handler : function() {
                     FXC.AJAXText(_webApp+'/login/logout.do',null,function(result){
                        if(result == 'yes'){
                            location.href = host + "/index.html";
                        }else{
                            alert("退出失败");
                        }
                    });
                    $("#dialog_logout").dialog("close");
                }
            },{
                text : '取消',
                handler : function() {
                    $("#dialog_logout").dialog("close");
                }
            }]
    });
}

/**
*修改密码
*/
function updatePassword() {
    $("#dialog_updatePassword").dialog({
        height : 200,
        width : 300,
        title : "修改密码",
        modal : true,
        buttons : [{
                text : '确定',
                handler : function() {
                    var oldpas = $("#dialog_updatePassword").find('input[name="oldPassword"]').val();
                    var pas1 = $("#dialog_updatePassword").find('input[name="password1"]').val();
                    var pas2 = $("#dialog_updatePassword").find('input[name="password2"]').val();
                    if(pas1!='' &&  pas2 != '' && oldpas!= ''){
                        if(pas1 != pas2){
                            alert("两次密码不一致");
                            return;
                        }
                    }else{
                        alert("密码不能为空");
                        return;
                    }
                    FXC.AJAX(_webApp+'/system/user/updatePassword.do',{oldpas:oldpas,pas:pas1},function(result){
                        if(result && result.success){
                            $("#dialog_updatePassword").dialog("close");
                            alert(result.msg);
                        }else{
                            alert(result.msg?result.msg:"修改失败");
                        }
                    });
                }
            },{
                text : '取消',
                handler : function() {
                    $("#dialog_updatePassword").dialog("close");
                }
            }]
    });
}
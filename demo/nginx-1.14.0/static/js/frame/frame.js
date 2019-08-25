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
        buttons : {
            "确定" : function() {
                location.href = _webApp + "/logout";
                $(this).dialog("close");
            },
            "取消" : function() {
                $(this).dialog("close");
            }
        }
    });
}

function changeCity(cityCode) {
    var url = _webApp + "/"+cityCode;
    window.location.href = url;
}
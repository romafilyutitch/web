$(document).ready(function() {
    $("#loginBtn").click(function() {
        var userName = $("#login").val();
        var password = $("#password").val();
        alert("userName " + userName + ", password " + password);
        // $.ajax({
        //     type : "POST",
        //     url : "controller",
        //     data : {"command" : "login", "login" : userName, "password" : password},
        //     success : function () {
        //         var url = "index.jsp";
        //         $(location).attr("href", url);
        //     }
        // })
    })
})
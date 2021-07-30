$(document).ready(function (){
    const changeLoginForm = document.querySelector("#changeLoginForm");
    const changePasswordForm = document.querySelector("#changePasswordForm");
    changeLoginForm.addEventListener("submit", function (event) {
        event.preventDefault();
        event.stopPropagation();
        if (changeLoginForm.checkValidity()) {
            const formValues = $("#changeLoginForm").serialize();
            $.ajax({
                url:"controller",
                method:"post",
                data: formValues,
                success:function (response) {
                    $("body").html(response);
                }
            })
        }
        changeLoginForm.classList.add("was-validated");
    });
    changePasswordForm.addEventListener("submit", function (event) {
        event.preventDefault();
        event.stopPropagation();
        if(changePasswordForm.checkValidity()) {
            const formValues = $("#changePasswordForm").serialize();
            $.ajax({
                url:"controller",
                method:"post",
                data:formValues,
                success:function (response) {
                    $("body").html(response);
                }
            })
        }
        changePasswordForm.classList.add("was-validated");
    });
})
function deleteAccount(userId) {
    $.ajax({
        url:"controller",
        method:"post",
        data:{command:"delete_account", id:userId},
        success:function(response) {
            $("body").html(response);
        }
    })
}
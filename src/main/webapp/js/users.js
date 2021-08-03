(function () {
    const forms = document.querySelectorAll('.needs-validation')
    Array.from(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                event.preventDefault();
                event.stopPropagation();
                if (form.checkValidity()) {
                    const formValues = $(this).serialize();
                    $.ajax({
                        url:"controller",
                        method:"post",
                        data:formValues,
                        success:function (response) {
                            $("body").html(response);
                        }
                    })
                }
                form.classList.add('was-validated')
            })
        });
})()
$(document).ready(function(){
    const currentLocale = $("#locale").val();
    const datepicker = $(".datepicker");
    datepicker.datepicker({
        language:currentLocale,
        locale:currentLocale,
        format:'dd.mm.yyyy'
    })
})
function promoteUserRole(userId){
    $.ajax({
        url:"controller",
        method:"post",
        data:{command:"promote_role", id:userId},
        success:function (response) {
            $("body").html(response);
        }
    })
}
function demoteUserRole(userId) {
    $.ajax({
        url:"controller",
        method:"post",
        data:{command:"demote_role", id:userId},
        success:function (response) {
            $("body").html(response);
        }
    })
}
function findPage(currentPage) {
    $.ajax({
        url:"controller",
        method:"post",
        data:{command:"show_users",page:currentPage},
        success:function (response) {
            $("body").html(response);
        }
    })
}
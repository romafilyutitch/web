$(document).ready(function () {
    const form = document.querySelector("#registerForm");
    form.addEventListener("submit", function (event) {
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
        form.classList.add("was-validated");
    })
})
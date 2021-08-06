$(document).ready(function(){
    const addNewBookForm = document.querySelector("#addNewBookForm");
    addNewBookForm.addEventListener("submit", function(event){
        event.preventDefault();
        event.stopPropagation();
        if(addNewBookForm.checkValidity()) {
            const formValues = $(this).serialize();
            $.ajax({
                url:"controller",
                method:"post",
                data:formValues,
                success:function(response) {
                    $("body").html(response);
                }
            });
        }
        addNewBookForm.classList.add("was-validated");
    });
});
function addOneCopyOfBook(bookId) {
    $.ajax({
        url:"controller",
        method:"post",
        data:{command:"add_copy", id:bookId},
        success:function(response) {
            $("body").html(response);
        }
    });
}
function removeOneCopyOfBook(bookId) {
    $.ajax({
        url:"controller",
        method:"post",
        data:{command:"remove_copy", id:bookId},
        success:function (response) {
            $("body").html(response);
        }
    });
}
function deleteBook(bookId) {
    $.ajax({
        url:"controller",
        method:"post",
        data:{command:"delete_book", id:bookId},
        success:function (response) {
            $("body").html(response);
        }
    });
}
function findPage(currentPage) {
    $.ajax({
        url:"controller",
        method:"get",
        data:{command:"show_books", page:currentPage},
        success:function (response) {
            $("body").html(response);
        }
    });
}
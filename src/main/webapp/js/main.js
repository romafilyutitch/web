$(document).ready(function(){
    const form = document.querySelector("#findBookByNameForm");
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
    const addCommentForms = document.querySelectorAll(".addCommentForm");
    Array.from(addCommentForms).forEach(function (form) {
        form.addEventListener("submit", function(event) {
            event.preventDefault();
            event.stopPropagation();
            if (form.checkValidity()) {
                const formValues = $(this).serialize();
                $.ajax({
                    url:"controller",
                    method:"post",
                    data:formValues,
                    success:function (response) {
                        $("body").html(response)
                    }
                })
            }
            form.classList.add("was-validated")
        })
    })
})
function findByGenre(genre) {
    $.ajax({
        url:"controller",
        method:"get",
        data:{command:"find_book_by_genre", genre:genre},
        success:function(response) {
            $("body").html(response);
        }
    })
}

function findAllBooks() {
    $.ajax({
        url:"controller",
        method:"get",
        data:{command:"main"},
        success:function(response) {
            $("body").html(response);
        }
    })
}
function findPage(pageNumber) {
    $.ajax({
        url:"controller",
        method:"get",
        data:{command:"main", page : pageNumber},
        success:function(response) {
            $("body").html(response);
        }
    })
}
function setLanguage(currentLanguage) {
    $.ajax({
        url:"controller",
        method:"get",
        data:{command:"set_language", language:currentLanguage},
        success:function (response) {
            $("body").html(response);
        }
    })
}
function addLike(bookId) {
    $.ajax({
        url:"controller",
        method:"post",
        data:{command:"add_like", id:bookId},
        success:function (response) {
            $("body").html(response);
        }
    })
}
function deleteComment(commentId) {
    $.ajax({
        url:"controller",
        method:"post",
        data:{command:"delete_comment", id:commentId},
        success:function (response) {
            $("body").html(response);
        }
    })
}
function orderBook(bookId) {
    $.ajax({
        url:"controller",
        method:"post",
        data:{command:"order_book", id:bookId},
        success:function (response) {
            $("body").html(response);
        }
    })
}
function logout() {
    $.ajax({
        url:"controller",
        method:"post",
        data:{command:"logout"},
        success:function (response) {
            $("body").html(response);
        }
    })
}
function sortBooksByName() {
    $.ajax({
        url:"controller",
        method:"get",
        data:{command:"sort_book_by_name"},
        success:function(response) {
            $("body").html(response);
        }
    })
}

function sortBooksByLikes() {
    $.ajax({
        url:"controller",
        method:"get",
        data:{command:"sort_book_by_likes"},
        success:function(response) {
            $("body").html(response);
        }
    })
}

function sortBooksByComments() {
    $.ajax({
        url:"controller",
        method:"get",
        data:{command:"sort_book_by_comments"},
        success:function (response) {
            $("body").html(response);
        }
    })
}


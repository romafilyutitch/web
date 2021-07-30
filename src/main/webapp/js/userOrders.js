function returnOrder(orderId) {
    $.ajax({
        url:"controller",
        method:"post",
        data:{command:"return_book", id:orderId},
        success:function (response) {
            $("body").html(response);
        }
    })
}
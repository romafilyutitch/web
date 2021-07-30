function approveOrder(orderId){
    $.ajax({
        url:"controller",
        method:"post",
        data:{command:"approve_order", id:orderId},
        success:function (response) {
            $("body").html(response);
        }
    });
}
function deleteOrder(orderId) {
    $.ajax({
        url:"controller",
        method:"post",
        data:{command:"delete_order", id:orderId},
        success:function (response) {
            $("body").html(response);
        }
    });
}

function findPage(currentPage) {
    $.ajax({
        url:"controller",
        method:"get",
        data:{command:"show_orders", page:currentPage},
        success:function (response) {
            $("body").html(response);
        }
    });
}
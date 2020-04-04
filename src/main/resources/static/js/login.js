function loginIn(){
    var formObject = {};
    var formArray =$("#userInfo").serializeArray();
    $.each(formArray,function(i,item){
        formObject[item.name] = item.value;
    });
    $.ajax({
        url:"/login",
        type:"POST",
        contentType: "application/json",
        data: JSON.stringify(formObject),
        dataType: "json",
        success:function(response){
            if (response.code == 200) {
                window.location.href="/";
            }else {
                $('#error_tag').html(response.message).show();
            }

        },
        error:function(e){
            console.log(e);
        }
    });
}
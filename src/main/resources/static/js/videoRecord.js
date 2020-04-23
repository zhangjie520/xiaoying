function saveNote() {
    var videoId=$('#videoId').val();
    var recordContent=$('#recordContent').val();
    if (!recordContent) {
        alert("笔记不能为空~~");
        return;
    }
    $.ajax({
        method: "POST",
        url: "/record",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({ "videoId": videoId, "recordContent": recordContent})
    }).done(function (response) {
        if (response.code == 200) {
            alert("保存成功");
        } else {
            if (response.code == 2003) {
                var isAccepted = confirm(response.message);
                if (isAccepted) {
                    window.open("/login");
                    window.localStorage.setItem("closable", "true");
                }
            }else {
                alert(response.message);
            }
        }
    });
    
}
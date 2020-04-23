/**
 * 提交回复
 */
function post() {
    var videoId = $("#video-id").val();
    var valueContent = $('#comment-content').val();
    comment2taget(videoId,valueContent,1);

}
function comment2taget(parentId, content, type) {
    if (!content) {
        alert("内容不能为空~~");
        return;
    }
    $.ajax({
        method: "POST",
        url: "/comment",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({"parentId": parentId, "content": content, "type": type})
    }).done(function (response) {
        if (response.code == 200) {
            $('#comment-section').hide();
            window.location.reload();
        } else {
            if (response.code == 2003) {
                var isAccepted = confirm(response.message);
                if (isAccepted) {
                    window.open("/login");
                    window.localStorage.setItem("closable", "true");
                }
            }
        }
    });
}
function comment(e) {
    var commentId = e.getAttribute('data-id');
    var content = $('#input-'+commentId).val();
    comment2taget(commentId,content,2);
}

/**
 * 展开二级评论
 * @param commentId
 */
function collapseComments(e) {
    // debugger;
    var commentId = e.getAttribute("data-id");
    var collapse = e.getAttribute("data-collapse");

    if (collapse) {
        e.classList.remove("active");
        e.removeAttribute("data-collapse");
    } else {
        var subCommentContainer=$('#comment-'+commentId);
        if(subCommentContainer.children().length!=1){
            e.classList.add("active");
            e.setAttribute("data-collapse", "in");
        }else {
            $.getJSON( "/comment/"+commentId, function( data ) {
                // console.log(data);
                var items = [];
                $.each( data.data, function(index,comment ) {
                    var html="<div class=\"media\"><div class=\"media-left \"><span><img class=\"media-object\" src=\""+comment.user.avatarUrl+"\" alt=\"...\"></span></div><div class=\"media-body\"><h5><span>"+comment.user.name+"</span></h5><div >"+comment.content+"</div><div class=\"menu\"><span class=\"pull-right\">"+moment(comment.gmtGreate).format('YYYY-MM-DD')+"</span></div></div></div>";
                    items.push(html );
                });
                subCommentContainer.prepend(items);
            });
            e.classList.add("active");
            e.setAttribute("data-collapse", "in");
        }

    }

}
/**
 *like video or comment
 */
function likeComment(e) {
    var commentId = e.getAttribute("data-id");
    var like_comment_count='#like_comment_count_'+commentId;
    var like_count = $(like_comment_count).html();
    $.getJSON( "/likeComment/"+commentId, function( data ) {
        console.log(data);
        $(like_comment_count).html(Number(like_count)+Number(1));

    });
}
function likeVideo(e) {
    var videoId = e.getAttribute("data-id");
    var like_count = $('#like_video_count').html();
    $.getJSON( "/likeVideo/"+videoId, function( data ) {
        console.log(data);
        $('#like_video_count').html(Number(like_count)+Number(1));

    });
}
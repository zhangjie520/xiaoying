// function uploadVideo(video) {
//     var fileObj = video.files[0]; // 获取文件对象
//
//     if (fileObj != undefined) {
//
//         if (fileObj.name) {
//             console.log(fileObj.name)
//         } else {
//             alert("请选择文件");
//         }
//
//         var size = fileObj.size;
//         var type = fileObj.type;
//
//         //校验格式
//         if (type.indexOf('mp4') == -1) {
//             alert("请上传MP4格式")
//             return false;
//         }
//         //校验大小
//         if (size / 1024 / 1024 > 500) {
//             alert("请上传500Mbp之内的视频")
//             return false;
//         }
//
//         var form = new FormData(); // FormData 对象
//         form.append("file", fileObj);
//         $.ajax({
//             url: "/videoUpload",
//             contentType: false,//false 传输对象
//             processData: false,
//             type: "POST",
//             data: form,
//             success: function (ret) {
//                 if(ret.resCode != undefined){
//                     if(ret.resCode == '1'){
//                         alert("上传成功")
//                     }
//                     else if(ret.resCode == '0'){
//                         alert("上传失败")
//                     }
//                 }
//             }
//         })
//
//     }
// }
var xhr;//异步请求对象
var ot; //时间
var oloaded;//大小
//上传文件方法
function UploadVideo() {
    var fileObj = document.getElementById("file").files[0]; // js 获取文件对象
    if(fileObj.name){
        $(".el-upload-list").css("display","block");
        $(".el-upload-list li").css("border","1px solid #20a0ff");
        $("#videoName").text(fileObj.name);
    }else{
        alert("请选择文件");
    }
}
/*点击取消*/
function del(){
    $("#file").val('');
    $(".el-upload-list").css("display","none");
}
/*点击提交*/
function sub(){
    var fileObj = document.getElementById("file").files[0]; // js 获取文件对象
    if(fileObj==undefined||fileObj==""){
        alert("请上传视频文件");
        return false;
    };
    var title = $.trim($("#title").val());
    if(title==''){
        alert("请填写视频标题");
        return false;
    }
    var video_bio = $.trim($("#video_bio").val());
    if(video_bio==''){
        alert("请填写视频简介");
        return false;
    }
    var video_tag = $.trim($("#video_tag").val());
    if(video_tag==''){
        alert("请填写标签");
        return false;
    }
    var video_cover_url = $.trim($("#video_cover_url").val());
    if(video_cover_url==''){
        alert("请上传封面");
        return false;
    }
    var url = "/videoUpload"; // 接收上传文件的后台地址
    var form = new FormData(); // FormData 对象
    form.append("file", fileObj); // 文件对象
    form.append("title", title); // 标题
    form.append("video_bio", video_bio); // 简介
    form.append("video_cover_url", video_cover_url); // 封面
    form.append("video_tag", video_tag); // 标签
    xhr = new XMLHttpRequest(); // XMLHttpRequest 对象
    xhr.open("post", url, true); //post方式，url为服务器请求地址，true 该参数规定请求是否异步处理。
    xhr.onload = uploadComplete; //请求完成
    xhr.onerror = uploadFailed; //请求失败
    xhr.upload.onprogress = progressFunction; //【上传进度调用方法实现】
    xhr.upload.onloadstart = function() { //上传开始执行方法
        ot = new Date().getTime(); //设置上传开始时间
        oloaded = 0; //设置上传开始时，以上传的文件大小为0
    };
    xhr.send(form); //开始上传，发送form数据
}

//上传进度实现方法，上传过程中会频繁调用该方法
function progressFunction(evt) {
    // event.total是需要传输的总字节，event.loaded是已经传输的字节。如果event.lengthComputable不为真，则event.total等于0
    if(evt.lengthComputable) {
        // $(".el-progress--line").css("display","inline");
        /*进度条显示进度*/
        $(".el-progress-bar__inner").css("width", Math.round(evt.loaded / evt.total * 100) + "%");
        $(".el-progress__text").html("     上传进度："+Math.round(evt.loaded / evt.total * 100)+"%");
    }

    var time = document.getElementById("time");
    var nt = new Date().getTime(); //获取当前时间
    var pertime = (nt - ot) / 1000; //计算出上次调用该方法时到现在的时间差，单位为s
    ot = new Date().getTime(); //重新赋值时间，用于下次计算

    var perload = evt.loaded - oloaded; //计算该分段上传的文件大小，单位b
    oloaded = evt.loaded; //重新赋值已上传文件大小，用以下次计算

    //上传速度计算
    var speed = perload / pertime; //单位b/s
    var bspeed = speed;
    var units = 'b/s'; //单位名称
    if(speed / 1024 > 1) {
        speed = speed / 1024;
        units = 'k/s';
    }
    if(speed / 1024 > 1) {
        speed = speed / 1024;
        units = 'M/s';
    }
    speed = speed.toFixed(1);
    //剩余时间
    var resttime = ((evt.total - evt.loaded) / bspeed).toFixed(1);
    time.innerHTML = '上传速度：' + speed + units + ',剩余时间：' + resttime + 's';
    if(bspeed == 0)
        time.innerHTML = '上传已取消';
}
//上传成功响应
function uploadComplete(evt) {
    //服务断接收完文件返回的结果  注意返回的字符串要去掉双引号
    var response=JSON.parse(evt.target.responseText);
    if(response['code']==200){
        var str = response.data.videoUrl;
        alert("上传成功！");
        window.location.href="/";
        // $(".preview").append("<video  controls='' autoplay='' name='media'><source src="+str+" type='video/mp4'></video>");
    }else{
        alert("上传失败"+response['message']);
    }
}
//上传失败
function uploadFailed(evt) {
    alert("上传失败！");
}

//上传封面
function UploadCover(video) {
    var fileObj = video.files[0]; // 获取文件对象

    if (fileObj != undefined) {

        if (fileObj.name) {
            $("#coverName").text(fileObj.name);
        } else {
            alert("请选择文件");
        }

        //验证图片
        if (verifyImageInfo(this,['jpg','png'],0,{'width':0,'height':0})){
            alert("图片格式应为jpg、png");
            return;
        }

        var form = new FormData(); // FormData 对象
        form.append("file", fileObj);
        $.ajax({
            url: "/uploadCover",
            contentType: false,//false 传输对象
            processData: false,
            type: "POST",
            data: form,
            success: function (response) {
                if (response.code == 200) {
                    $('#video_cover_url').val(response.data['coverUrl']);
                }else {
                    alert(response.message);
                }
            }
        })

    }
}
//图片验证（大小，尺寸，类型）
function verifyImageInfo(that,allow_type_arr,allow_space,allow_size_obj){
    if(verificationPicType(that,allow_type_arr)
        && verificationPicSpace(that,allow_space)){
        //尺寸验证不能在判断条件。
        //因为内部图片加载是异步的，函数的返回值不是基于图片尺寸大小判断的结果。只能利用错误提示
        verificationPicSize(that,allow_size_obj);
        return true;
    }
    return false;
}
/**
 * 图片类型验证
 * @allow_type_arr ['jpg','png'],如果数组为空则表示不限
 */
function verificationPicType(that,allow_type_arr) {

    if(allow_type_arr.length==0) return true;

    var fileTypes = allow_type_arr;
    var filePath  = that.value;
    //当括号里面的值为0、空字符、false 、null 、undefined的时候就相当于false
    if(filePath){
        var isNext = false;
        var fileEnd = filePath.substring(filePath.indexOf(".")+1).toLowerCase();
        // console.log(fileEnd);
        for (var i = 0; i < fileTypes.length; i++) {
            if (fileTypes[i] == fileEnd) {
                isNext = true;
                break;
            }
        }
        if (!isNext){
            alert('不接受此文件类型');
            that.value = "";
            return false;
        }
        return true;
    }else {
        return false;
    }
}
/**
 * 图片大小验证
 * @allow_space 400,如果值为0则表示不限
 */
function verificationPicSpace(that,allow_space) {

    if(allow_space==0) return true;

    var fileSize = 0;
    var fileMaxSize = allow_space;
    var filePath = that.value;
    if(filePath){
        fileSize =that.files[0].size;
        var size = fileSize / 1024;//单位b转换kb
        // console.log(size);
        if (size > fileMaxSize) {
            alert("文件大小超出限制！");
            that.value = "";
            return false;
        }else if (size <= 0) {
            alert("文件大小不能为0！");
            that.value = "";
            return false;
        }
        return true;
    }else{
        return false;
    }
}
/**
 * 图片尺寸验证
 * @allow_size_obj {'width':123,"height":345},如果值为0则表示不限
 */
function verificationPicSize(that,allow_size_obj) {
    var filePath = that.value;
    if(filePath){
        //读取图片数据
        var filePic = that.files[0];
        var reader = new FileReader();
        reader.onload = function (e) {
            var data = e.target.result;
            //加载图片获取图片真实宽度和高度
            var image = new Image();
            image.onload=function(){
                var width = image.width;
                var height = image.height;
                if(width!=allow_size_obj['width'] && allow_size_obj['width']!=0){
                    alert("文件宽度"+width+"不符合要求");
                    that.value = "";
                    return false;
                }
                if(height!=allow_size_obj['height'] && allow_size_obj['height']!=0){
                    alert("文件高度"+height+"不符合要求");
                    that.value = "";
                    return false;
                }
                return true;
            };
            image.src= data;
        };
        reader.readAsDataURL(filePic);
    }else{
        return false;
    }
}

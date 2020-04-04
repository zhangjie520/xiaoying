function pass(e) {
    var videoId = e.getAttribute("data-id");
    $.getJSON( "/checkPass/"+videoId, function( data ) {
        if (data.code == 200) {
            window.location.href="/?tag=check";
        }else {
            alert(data.message);
        }
    });
}
function fail(e) {
    var videoId = e.getAttribute("data-id");
    $.getJSON( "/checkFail/"+videoId, function( data ) {
        // console.log(data);
        if (data.code == 200) {
            window.location.href="/?tag=check";
        }else {
            alert(data.message);
        }

    });
}
$(document).ready(function() {
    var loggedInUser = Cookies.get('loggedInUser');
    if (loggedInUser != null) {
        $("#current-user").prepend(loggedInUser);
    } else {
        window.location.replace("welcome.html");
    }

    $("#logout").click(function (e) {
        e.preventDefault();
        Cookies.remove('loggedInUser');
        location.reload();
    });

    $("#pgn-submit").submit(function(e) {
        alert("Submitted!");
        e.preventDefault();
        var pgnBody = $("#pgn-body").val();
        var postRequest = $.post("chessgear/api/games/import/" + loggedInUser, JSON.stringify({pgn : pgnBody}), function(data) {
            alert("Import success!");
        });
        postRequest.fail(function (data) {
            alert("Import failed: " + jQuery.parseJSON(data.responseText).why);
        });
    });
});
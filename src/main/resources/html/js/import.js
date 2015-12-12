$(document).ready(function() {
    var loggedInUser = getLoggedInUser();
    setUserHeader(loggedInUser);

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
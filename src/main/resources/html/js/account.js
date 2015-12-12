$(document).ready(function() {

    var loggedInUser = getLoggedInUser();
    setUserHeader(loggedInUser);

    $("#change-password").click(function (e) {
        e.preventDefault();
        var newPassword = prompt("New password:", "");
        var postRequest = $.post("/chessgear/api/account", JSON.stringify({user : loggedInUser, password : newPassword}), function(data) {
            alert("Password changed successfully!");
        });
        postRequest.fail(function (data) {
            alert("Request failed: " + jQuery.parseJSON(data.responseText).why);
        });
    });

    var gamesListRequest = $.get("chessgear/api/games/list/" + loggedInUser, function(data) {
        var parsedRequest = $.parseJSON(data);
        parsedRequest.games.forEach(function (entry) {
            $("#games-list").append("<li><a href=\"chessgear/api/games/" + loggedInUser + "/" + entry.id + "\">" + entry.name + "</a></li>");
        });
    });

});


$("#logout").click(function (e) {
    e.preventDefault();
    Cookies.remove('loggedInUser');
    location.reload();
});

function getLoggedInUser() {
    var loggedInUser = Cookies.get('loggedInUser');
    return loggedInUser;
}

function setUserHeader(loggedInUser) {
    if (loggedInUser != null) {
        $("#current-user").prepend(loggedInUser);
    } else {
        window.location.replace("welcome.html");
    }
}
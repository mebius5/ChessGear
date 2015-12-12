function getLoggedInUser() {
    var loggedInUser = Cookies.get('loggedInUser');
    return loggedInUser;
}
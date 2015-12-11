$(document).ready(function() {
    var loggedInUser = Cookies.get('loggedInUser');
    if (loggedInUser != null) {
        $("#current-user").prepend(loggedInUser);
    } else {
        $("#current-user").html("Not logged in");
    }

    $("#logout").click(function (e) {
        e.preventDefault();
        Cookies.remove('loggedInUser');
        location.reload();
    });

    $("#login").submit(function (e) {
        e.preventDefault();
        
        var loginEmail = $("#username").val();
        var loginPassword = $("#password").val();
        var postRequest = jQuery.post("/chessgear/api/login", JSON.stringify({user : loginEmail, password : loginPassword}), function (data, status) {
            alert("Login success!");
            Cookies.set('loggedInUser', loginEmail);
            window.location.replace("index.html");
        });
        postRequest.fail(function (data) {
            alert("Login failed: " + jQuery.parseJSON(data.responseText).why);
        });
    });

    $("#register-button").click(function (e) {
        e.preventDefault();
        alert("Registering user");
        var loginEmail = $("#username").val();
        var loginPassword = $("#password").val();
        var postRequest = jQuery.post("/chessgear/api/register", JSON.stringify({user : loginEmail, password : loginPassword}), function (data, status) {
            alert("Registration success!");
        });
        postRequest.fail(function (data) {
            alert("Registration failed: " + jQuery.parseJSON(data.responseText).why);
        });
    });
    
});
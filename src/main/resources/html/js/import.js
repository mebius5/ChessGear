$(document).ready(function() {
    var loggedInUser = getLoggedInUser();
    setUserHeader(loggedInUser);

    $("#pgn-submit").submit(function (e) {
        alert("Import in progress!");
        e.preventDefault();
        var pgnBody = $("#pgn-body").val();
        var postRequest = $.post("chessgear/api/games/import/" + loggedInUser, JSON.stringify({pgn : pgnBody}), function(data) {
            alert("Import success!");
        });
        postRequest.fail(function (data) {
            alert("Import failed: " + jQuery.parseJSON(data.responseText).why);
        });
    });

    $("#pgn-file-upload > input:file").change(function() {
        $("#pgn-file-upload").submit();
    });

    $("#pgn-file-upload").submit(function (e) {
        e.preventDefault();

        var file = $("#pgn-file")[0].files[0];
        var formData = new FormData();
        formData.append("file", file);

        $.ajax({
            url: '/chessgear/api/games/importfile/' + loggedInUser,
                   //Ajax events
                   beforeSend: function(e) {
                     alert('File upload in progress');
                   },
                   success: function (e) {
                     alert('Upload completed');
                   },
                   error: function (e) {
                     alert('error ' + e.message);
                   },
                   // Form data
                   data: formData,
                   type: 'POST',
                   //Options to tell jQuery not to process data or worry about content-type.
                   cache: false,
                   contentType: false,
                   processData: false
        });
    });
});
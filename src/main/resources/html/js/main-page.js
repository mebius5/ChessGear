var toggle = 1;
var back = 0;

$(document).ready(function() {

    var loggedInUser = getLoggedInUser();
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

    var nodeId = getUrlParameter("node");
    if (nodeId === undefined) {
        nodeId = 0;
    }
    getNodeAndDisplayBoard(nodeId, loggedInUser);

});

$("#navigate-root").click(function (e) {
    window.location.href="./?node=0";
});

$("#navigate-back").click(function (e) {
    if (back != null)
    window.location.href="./?node=" + back;
})

$("#flipButton").click(function (e) {
    rotateBoard();
});

// Rotates the board
function rotateBoard() {
    if (toggle == 1) {
        $("#board-wrapper").css({transform : 'rotate(180deg)', transition : 'transform 1s linear'});
        $(".board-square > img").each(function(index) {
            $(this).css({transform : 'rotate(-180deg)', transition : 'transform 1s linear'});
        });
    } else {
        $("#board-wrapper").css({transform : 'rotate(0deg)', transition : 'transform 1s linear'});
        $(".board-square > img").each(function(index) {
            $(this).css({transform : 'rotate(0deg)', transition : 'transform 1s linear'});
        });
        
    }
    toggle = -toggle;
    
}

function getNodeAndDisplayBoard(id, username) {
    var boardStateRequest = $.get("chessgear/api/games/tree/" + username + "/" + id, function(data) {
        var parsedResponse = jQuery.parseJSON(data);
        back = parsedResponse.previousNodeId;
        displayBoard(parsedResponse.boardstate);
        displayChildren(parsedResponse.children);
    });
}

function displayBoard(boardState) {
    var splitIndex = boardState.indexOf(" ");
    boardState = boardState.substring(0, splitIndex);
    var tokenizedRows = boardState.split("/");

    for (var c = 0; c < 8; c++) {

        // Current row.
        var currentRow = 8 - c;
        var currentFile = 0;

        var currentRowFEN = tokenizedRows[c];
        // For each character in the current row's FEN
        for (var d = 0; d < currentRowFEN.length; d++) {


            var currentChar = currentRowFEN.charAt(d);
            var numSpaces = parseInt(currentChar);
            // If numeric
            if (isNaN(numSpaces)) {
                var fileChar = numberToFile(currentFile);
                var imgSrc = getPieceFromChar(currentChar);
                var square = "#square-" + fileChar + currentRow;
                var img = "<img src=\"img/" + imgSrc +"\">";
                $(square).html(img);
                currentFile++;
            } else {
                for (var e = 0; e < numSpaces; e++) {
                    var fileChar = numberToFile(currentFile);
                    var square = "#square-" + fileChar + currentRow;
                    $(square).html("");
                    currentFile++;
                }
            }

        }
    }
}

function displayChildren(children) {
    // Clears the list of children first.
    $("#children-list").html("");
    for (var c = 0; c < children.length; c++) {
        $("#children-list").append("<li><a href='" + children[c].id + "' id='child-" + children[c].id + "'>" + children[c].name + "</a> - Eval : " + children[c].eval + "</li>");
        $("#child-" + children[c].id).click(function (e) {
            e.preventDefault();
            getNodeAndDisplayBoard($(this).attr("href"), getLoggedInUser());
        });
    }
}

/**
 * 0=a, 1=b, etc.
 */
function numberToFile(number) {
    return String.fromCharCode(97 + number);
}

function getPieceFromChar(piece) {

    switch (piece) {
        case "p":
            return "black_pawn.png";
        case "r":
            return "black_rook.png";
        case "n":
            return "black_knight.png";
        case "b":
            return "black_bishop.png";
        case "q":
            return "black_queen.png";
        case "k":
            return "black_king.png";
        case "P":
            return "white_pawn.png";
        case "R":
            return "white_rook.png";
        case "N":
            return "white_knight.png";
        case "B":
            return "white_bishop.png";
        case "Q":
            return "white_queen.png";
        case "K":
            return "white_king.png";

    }

    return null;

}

function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
}
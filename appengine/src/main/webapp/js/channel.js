initChannel = function (params) {

    function onMessage(message) {
        var operation = JSON.parse(message.data);
        params.onMessage(operation);
    }

    function onOpened() {
        console.log("Channel is opened");
        params.onOpen();
    }

    function openChannel(token) {
        var channel = new goog.appengine.Channel(token);
        var handler = {
            'onopen': onOpened,
            'onmessage': onMessage,
            'onerror': function () {
                //TODO define error handler here
            },
            'onclose': function () {
                //TODO re-create token here
            }
        };
        channel.open(handler);
    }

    function getToken() {
        var request = new XMLHttpRequest();
        request.open("GET", "/channel/token?clientId=" + params.clientId, true);
        request.onreadystatechange = function () {
            if (request.readyState == 4) {
                if (request.status == 200) {
                    console.log("Token received " + request.responseText);
                    openChannel(request.responseText);
                } else {
                    console.log("Can't get token: " + request.responseText); //TODO handle errors here
                }
            }
        };
        request.send();
    }

    getToken();
}
var initEditor = function (documentId, clientId, view) {

    var version;
    var sentOperation = null;
    var pendingOperations = [];
    var incomingOperationsQueue = [];

    var isMine = function (operation) {
        return operation.initiator === clientId;
    }

    var sendOperation = function (operation) {
        var r = new XMLHttpRequest();
        r.open("POST", "/document/update", true);
        r.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        var message =
            "op=" + operation.op +
                "&position=" + operation.position +
                "&value=" + operation.value +
                "&documentId=" + documentId +
                "&initiator=" + clientId +
                "&version=" + version;
        console.log("Sending operation " + message);
        r.send(message);
        r.onreadystatechange = function () {
            if (r.readyState == 4) {
                if (r.status == 204) {
                    console.log("Operation was sent " + message);
                } else {
                    console.error("Error during sending operation: " + message);
                }
            }
        };
    }

    var trySendPendingOperation = function () {
        if (incomingOperationsQueue.length > 0 || sentOperation !== null) {
            return;
        }
        if (pendingOperations.length > 0) {
            sentOperation = pendingOperations[0]
            pendingOperations = pendingOperations.slice(1)
            sendOperation(sentOperation);
        } else {
            view.showDocumentIsSaved();
        }
    }

    var applyForeignOperation = function (operation) {
        var operationToApply = operation;
        //transform against sent
        if (sentOperation !== null) {
            var transformed = transformer.transform(operationToApply, sentOperation);
            operationToApply = transformed.first;
            sentOperation = transformed.second;
        }

        //transform against pending
        for (var i = 0; i < pendingOperations.length; i++) {
            var transformed = transformer.transform(operationToApply, pendingOperations[i]);
            operationToApply = transformed.first;
            pendingOperations[i] = transformed.second;
        }
        view.applyOperation(operationToApply);
    }

    var processIncomingOperation = function () {
        if (!view.isBlocked() && incomingOperationsQueue.length !== 0) {
            var operation = incomingOperationsQueue[0];
            incomingOperationsQueue.splice(0, 1);
            version++;
            console.log('Version was incremented ' + version)
            if (isMine(operation)) {
                sentOperation = null;
            } else {
                applyForeignOperation(operation);
            }
            trySendPendingOperation();
        }
        setTimeout(processIncomingOperation, 100);
    }

    setTimeout(processIncomingOperation, 100);

    var onIncomingOperation = function (operation) {
        console.log("Operation was received " + JSON.stringify(operation))
        incomingOperationsQueue.push(operation);
    }

    var initEditor = function () {
        console.log("Fetching current document version");
        var request = new XMLHttpRequest();
        request.open("GET", "/document?documentId=" + documentId + "&clientId=" + clientId, true);
        request.onreadystatechange = function () {
            if (request.readyState == 4) {
                if (request.status == 200) {
                    console.log("Document received " + request.responseText);
                    var receivedDocument = JSON.parse(request.responseText);
                    version = receivedDocument.documentVersion;
                    view.showDocument(receivedDocument.documentText, receivedDocument.documentTitle);
                } else {
                    console.error("Can't get document: " + request.responseText);
                }
            }
        };
        request.send();
    }

    var initialize = function () {
        initChannel({
                onOpen: initEditor,
                onMessage: onIncomingOperation,
                clientId: clientId
            }
        );
    }

    initialize();

    return {

        onUserOperations: function (operations) {
            if (operations.length > 0) {
                view.showDocumentIsSaving()
                pendingOperations = pendingOperations.concat(operations)
                if (sentOperation === null) {
                    trySendPendingOperation();
                }
            }
        }
    }
};




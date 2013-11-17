var initView = function () {

    var presenter;
    var content;
    var textArea = document.getElementById('editor');
    var blocked = false;

    /**
     * TODO refactor it!
     */
    var calculateOperations = function () {
        var oldval = content;
        var newval = textArea.value;
        console.log('Calculating diff newval: ' + newval)
        console.log('Calculating diff oldval: ' + oldval)

        var commonEnd, commonStart;
        if (oldval === newval) return [];
        commonStart = 0;
        while (oldval.charAt(commonStart) === newval.charAt(commonStart)) {
            commonStart++;
        }
        commonEnd = 0;
        while (oldval.charAt(oldval.length - 1 - commonEnd) === newval.charAt(newval.length - 1 - commonEnd) && commonEnd + commonStart < oldval.length && commonEnd + commonStart < newval.length) {
            commonEnd++;
        }
        var calculatedOperations = [];
        if (oldval.length !== commonStart + commonEnd) {
            for (var i = oldval.length - commonEnd - 1; i >= commonStart; i--) {
                calculatedOperations.push(operations.delete(oldval.charAt(i), i))
            }
        }
        if (newval.length !== commonStart + commonEnd) {
            for (var i = commonStart; i < newval.length - commonEnd; i++) {
                calculatedOperations.push(operations.insert(newval.charAt(i), i));
            }
        }

        content = newval;
        return calculatedOperations;
    }

    textArea.onkeydown = function () {
        console.log('Block is installed');
        blocked = true;
    }

    textArea.onkeyup = function () {
        console.log("Key press on position " + JSON.stringify(getUserSelection()))
        var operations = calculateOperations();
        console.log('Generated operations ' + JSON.stringify(operations))
        presenter.onUserOperations(operations);
        blocked = false;
        console.log('Block is released')
    }

    var getUserSelection = function () {
        return {
            start: textArea.selectionStart,
            end: textArea.selectionEnd
        };
    }

    var setUserSelection = function (userSelection) {
        console.log('Set new selection ' + JSON.stringify(userSelection))
        textArea.selectionStart = userSelection.start;
        textArea.selectionEnd = userSelection.end;
    };

    var applyInsert = function (operation) {
        console.log("Applying inserting " + JSON.stringify(operation))
        var userSelection = getUserSelection();
        content = content.substr(0, operation.position) + operation.value + content.substr(operation.position);
        textArea.value = content;
        if (operation.position <= userSelection.start) {
            console.log('Adjust cursor + 1');
            userSelection.start = userSelection.start + 1;
            userSelection.end = userSelection.end + 1;
        }
        setUserSelection(userSelection);
    };

    var applyDelete = function (operation) {
        console.log("Applying deletion " + JSON.stringify(operation))
        var userSelection = getUserSelection();
        content = content.substr(0, operation.position) + content.substr(operation.position + 1);
        textArea.value = content;
        if (operation.position <= userSelection.start) {
            userSelection.start = userSelection.start - 1;
            userSelection.end = userSelection.end - 1;
        }
        setUserSelection(userSelection);
    };

    return {

        isBlocked: function () {
            return blocked;
        },

        initPresenter: function (p) {
            presenter = p
        },

        showDocument: function (text, title) {
            content = text;
            textArea.value = text;
            document.getElementById('title').innerHTML = title;
        },

        showDocumentIsSaved: function () {
            document.getElementById('status').innerHTML = 'Saved';
        },

        showDocumentIsSaving: function () {
            document.getElementById('status').innerHTML = 'Saving...';
        },

        applyOperation: function (operation) {
            if (operation.op === 'insert') {
                applyInsert(operation);
                return;
            }
            if (operation.op === 'delete') {
                applyDelete(operation);
                return;
            }
        }
    }
}
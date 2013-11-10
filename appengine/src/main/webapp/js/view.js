var initView = function () {

    var presenter;
    var content;
    var textArea = document.getElementById('editor');

    /**
     * TODO refactor it!
     */
    var calculateOperations = function () {
        var oldval = content.trim();
        var newval = textArea.value.trim();

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

    textArea.onkeyup = function () {
        var operations = calculateOperations();
        presenter.onUserOperations(operations);
    }

    var getUserSelection = function () {
        return {
            start: textArea.selectionStart,
            end: textArea.selectionEnd
        };
    }

    var setUserSelection = function (userSelection) {
        textArea.selectionStart = userSelection.start;
        textArea.selectionEnd = userSelection.end;
    };

    var applyInsert = function (operation) {
        content = content.substr(0, operation.position) + operation.value + content.substr(operation.position);
        var userSelection = getUserSelection();
        textArea.value = content;
        if (operation.position <= userSelection.start) {
            userSelection.start = userSelection.start + 1;
            userSelection.end = userSelection.end + 1;
        }
        setUserSelection(userSelection);
    };

    var applyDelete = function (operation) {
        content = content.substr(0, operation.position) + content.substr(operation.position + 1);
        var userSelection = getUserSelection();
        textArea.value = content;
        if (operation.position <= userSelection.start) {
            userSelection.start = userSelection.start - 1;
            userSelection.end = userSelection.end - 1;
        }
        setUserSelection(userSelection);
    };

    return {

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
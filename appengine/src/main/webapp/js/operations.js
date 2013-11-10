var operations = function () {

    return {
        empty: function () {
            return {
                op: "empty"
            }
        },

        insert: function (value, position) {
            return {
                op: "insert",
                position: position,
                value: value
            }
        },

        delete: function (value, position) {
            return {
                op: "delete",
                position: position,
                value: value
            }
        }
    }
}();
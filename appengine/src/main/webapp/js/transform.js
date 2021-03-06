/**
 * Contains Operational Transformation functions.
 *
 * Usage:
 * transformedPair = transformer.transform(o1,o2)
 */
var transformer = function () {
    var transformationMatrix = {};

    var identicalTransformation = function (o1, o2) {
        return {
            first: o1,
            second: o2
        }
    }

    transformationMatrix['insert,insert'] = function (o1, o2) {
        if (o1.position <= o2.position) {
            return {
                first: operations.insert(o1.value, o1.position),
                second: operations.insert(o2.value, o2.position + 1)
            }
        } else {
            return {
                first: operations.insert(o1.value, o1.position + 1),
                second: operations.insert(o2.value, o2.position)
            }
        }
    }

    transformationMatrix['delete,insert'] = function (o1, o2) {
        if (o1.position <= o2.position) {
            return {
                first: operations.delete(o1.value, o1.position),
                second: operations.insert(o2.value, o2.position - 1)
            }
        } else {
            return {
                first: operations.delete(o1.value, o1.position + 1),
                second: operations.insert(o2.value, o2.position)
            }
        }
    }

    transformationMatrix['insert,delete'] = function (o1, o2) {
        if (o1.position <= o2.position) {
            return {
                first: operations.insert(o1.value, o1.position),
                second: operations.delete(o2.value, o2.position + 1)
            }
        } else {
            return {
                first: operations.insert(o1.value, o1.position - 1),
                second: operations.delete(o2.value, o2.position)
            }
        }
    }

    transformationMatrix['delete,delete'] = function (o1, o2) {
        if (o1.position === o2.position) {
            return {
                first: operations.empty(),
                second: operations.empty()
            }
        }
        if (o1.position < o2.position) {
            return {
                first: operations.delete(o1.value, o1.position),
                second: operations.delete(o2.value, o2.position - 1)
            }
        } else {
            return {
                first: operations.delete(o1.value, o1.position - 1),
                second: operations.delete(o2.value, o2.position)
            }
        }
    }

    transformationMatrix['empty,delete'] = identicalTransformation
    transformationMatrix['empty,insert'] = identicalTransformation
    transformationMatrix['insert,empty'] = identicalTransformation
    transformationMatrix['delete,empty'] = identicalTransformation
    transformationMatrix['empty,empty'] = identicalTransformation

    return {

        transform: function (o1, o2) {
            console.log("Transforming " + JSON.stringify(o1) + " | " + JSON.stringify(o2))
            var transformed = transformationMatrix[o1.op + ',' + o2.op].apply(null, [o1, o2]);
            console.log("Transformed " + JSON.stringify(transformed))
            return  transformed;
        }
    }
}();
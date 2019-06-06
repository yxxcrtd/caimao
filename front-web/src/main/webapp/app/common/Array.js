define([
    "dojo/_base/lang",
    "dojo/_base/array"
], function(lang, array){
    // module:
    //       myModule/array
    // just add new  method for dojo/array module
    array.uniq = function(arr){
        var uniqArr = [];
        return array.filter(arr, function(item){
            if(array.indexOf(uniqArr, item) == -1){
                uniqArr.push(item);
                return true;
            }
        })
    };


    array.union = function(){
        return array.uniq(Array.prototype.concat.apply(Array.prototype, arguments));
    };

    array.intersection = function(arr){
        var arrs = Array.prototype.slice.call(arguments, 1);
        return array.filter(array.uniq(arr), function (item) {
            return array.every(arrs, function (otherArr) {
                return array.indexOf(otherArr, item) != -1;
            });
        });
    };

    array.difference = function(arr){
        var rest = Array.prototype.concat.apply(Array.prototype, Array.prototype.slice.call(arguments, 1));
        return array.filter(array.uniq(arr), function (item) { return array.indexOf(rest, item) == -1; });
    };

    return array;
});
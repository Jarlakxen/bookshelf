// ----------------------------------
//          Utils
// ----------------------------------

function $ext_array(array){

    if (arguments.length == 0) {
        array = [];
    }

    array.remove = function(value){
        this.splice( this.indexOf(value), 1 );
    }

    return array;
}
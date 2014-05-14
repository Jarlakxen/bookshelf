'use strict';

// ----- EXTENDS ----- //

if (!Array.prototype.indexOf) { 
    Array.prototype.indexOf = function(obj, start) {
         for (var i = (start || 0), j = this.length; i < j; i++) {
             if (this[i] === obj) { return i; }
         }
         return -1;
    }
}

Array.prototype.remove = function(value) {
	this.splice( this.indexOf(value), 1 );
};

String.prototype.startsWith = function (str){
	return this.indexOf(str) == 0;
};
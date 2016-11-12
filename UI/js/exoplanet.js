function setup(){

}

function addRow(){

}

class SystemObject{
  constructor(var propAmount){
    var info = createArray(5, propAmount + 1)
    info[0][0] = 'Property';
    info[0][1] = 'Current';
    info[0][2] = 'Nasa';
    info[0][3] = 'exoplanet';
    info[0][4] = 'Result';
  }
}

// Dispalys the correct row for the table to permute
function showRow(){
  $('#row').ready(function(){
  });
}

function commitChanges(){
  var text = document.getElementById("commit-message").value;
  window.alert("Your Changes Have Been Made: "+ text);
}

// Helper func
function createArray(length) {
    var arr = new Array(length || 0),
        i = length;

    if (arguments.length > 1) {
        var args = Array.prototype.slice.call(arguments, 1);
        while(i--) arr[length-1 - i] = createArray.apply(this, args);
    }

    return arr;
}

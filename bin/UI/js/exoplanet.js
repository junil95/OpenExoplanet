function SystemObject(name, propAmount, child, type){
    this.name = name;
    this.propAmount = propAmount;
    this.child = child || null;

    this.info = createArray(propAmount + 1, 5);

    var i = 0;
    for(i; i < 5; i++){
      var g = 1;
      for(g; g < propAmount + 1; g++){
          this.info[g][i] = 'N/A';
      }
    }

    this.info[0][0] = 'Property';
    this.info[0][1] = 'Current';
    this.info[0][2] = 'Nasa';
    this.info[0][3] = 'exoplanet';
    this.info[0][4] = 'Result';

    this.type = type || "system";
}

var systemObjs = [new SystemObject("First Test", 3), new SystemObject("Second Test", 12), new SystemObject("Root Test", 6, new SystemObject("Root Test-2", 7))];

// Dispalys the correct row for the table to permute
function selectRow(systemObjNum){
  // Saves the current row
  var rowNum = $(".selected > label").first().prop("for").substring(8);
  var i = 1;
  for(i; i < systemObjs[rowNum].propAmount + 1; i++){
    systemObjs[rowNum].info[i][4] = $("#info" + i).val();
  }

  // Changes the selected row class
  $('.selected').removeClass('selected');
  $('#checkbox' + systemObjNum).parent().parent().addClass('selected');

  //Clears the html
  var systemObj = systemObjs[systemObjNum];
  $('#current-title').text('Changes for: ' + systemObj.name);

  $("#info-table").html("");
  $("#info-table").append("<tr><th>Property</th><th>Current</th><th>Nasa</th><th>Exoplanet</th><th>Result</th></tr>");

  var i = 1
  // Looping with + 1
  for(i; i < systemObj.info.length; i++){
    $("#info-table").append(generateRowHTML(systemObj.info[i], i));
  }
}

function generateRowHTML(infoArray, num){
  var result = '';

  result += '<tr>' +
  '<td>' + infoArray[0] +  '</td>' +
  '<td>' + infoArray[1] +  '</td>' +
  '<td>' + infoArray[2] +  '</td>' +
  '<td>' + infoArray[3] +  '</td>' +
  '<td>' +
  '<div class="form-group">' +
  '<input type="text" id="info' + num + '"value="' + infoArray[4] + '" placeholder="Wanted Value" class="form-control">' +
  '</div>' +
  '</td>' +
  '</tr>';
  return result;
}

function commitChanges(){
  var text = document.getElementById("commit-message").value;
  window.alert("Your Changes Have Been Made: "+ text);
}

function checkAll(){
  if($( "#checkboxall" ).prop( "checked" )){
    // Unchecking all boxes
    $( ".custom-checkbox" ).prop( "checked", true);
  }
  else{
    $( ".custom-checkbox" ).prop( "checked", false);
  }
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

// Helper func
function findSystemObj(wanted, wantedType){
    for (x in systemObjs) {
      if(x.name === wanted && x.type == wantedType){
        return x;
      }
    }
}

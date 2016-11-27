// Global var for all system objs
var systemObjs = [];

function SystemObject(name, child, type){
    this.name = name;
    this.propAmount = 0;
    this.child = child || null;

    this.info = createArray(5, 1);

    this.info[0][0] = 'Property';
    this.info[1][0] = 'Current';
    this.info[2][0] = 'Nasa';
    this.info[3][0] = 'EU';
    this.info[4][0] = 'Result';

    this.type = type || "system";
    this.conflict = false;
}

SystemObject.prototype.addRow = function() {
  var i = 0;
  for(i; i < 5; i++){
    this.info[i].push('N/A');
  }
  this.propAmount += 1;
};

SystemObject.prototype.getChild = function(indent) {
  // Setting a temp var for how to go down in
  var curr = this;
  var i = 0;
  while(i < indent && curr != null){
    curr = curr.child;
    i++;
  }
  return curr;
};

SystemObject.prototype.addAttribute = function(column_num, column_name, column_value){
  //console.log(column_name + "   " + this.type);
  //console.log(this.info[0]);
  var rowNum = this.info[0].indexOf(column_name);
  // If it doesn't exist
  if(rowNum === -1){
    this.addRow();
    this.info[0][this.propAmount] = column_name;
    this.info[column_num][this.propAmount] = column_value
  }
  else{
    this.info[column_num][rowNum] = column_value;
  }
}

SystemObject.prototype.fill = function(){
  var i = 0;
  for(i; i < 5; i++){
    var g = 1;
    for(g; g < propAmount + 1; g++){
        this.info[g][i] = 'N/A';
    }
  }
}

SystemObject.prototype.finalize = function(){
  // Returns the row nums if there are conflicts
  var i = 1;
  var conflicts = [];
  for(i; i < this.info[0].length; i++){
    if(this.info[2][i] === this.info[3][i]){
      this.info[4][i] = this.info[2][i];
    }
    else if( this.info[2][i] === "N/A" ){
      this.info[4][i] = this.info[3][i];
    }
    else if(this.info[3][i] === "N/A"){
      this.info[4][i] = this.info[2][i];
    }
    else{
      conflicts.push(i);
      this.info[4][i] = "CONFLICT OCCURS";
    }
  }
  return conflicts;
}

// Dispalys the correct row for the table to permute
function selectRow(systemObjNum, childNum){
  // Saves the current row
  if($(".selected > label").length){
    var rowNum = $(".selected").prop("id").substring(3);
    var i = 1;
    var index = rowNum.split("-");

    var last = systemObjs[parseInt(index[0])].getChild(parseInt(index[1]));
    for(i; i < last.propAmount; i++){
      last.info[4][i] = $("#info" + i).val();
    }
  }

  // Changes the selected row class
  $('.selected').removeClass('selected');
  $('#row' + systemObjNum + '-' + childNum).addClass('selected');

  //Clears the html
  var systemObj = systemObjs[systemObjNum].getChild(childNum);
  $('#current-title').text('Changes for: ' + systemObj.name);

  $("#info-table").html("");
  $("#info-table").append("<tr><th>Property</th><th>Current</th><th>Nasa</th><th>Exoplanet</th><th>Result</th></tr>");

  var i = 1
  // Looping with + 1
  for(i; i < systemObj.info[0].length; i++){
    if(systemObj.info[0][i].indexOf('name') === -1){
      $("#info-table").append(generateRowHTML(systemObj.info[0][i], systemObj.info[1][i], systemObj.info[2][i], systemObj.info[3][i], systemObj.info[4][i], i));
    }
  }
}

function generateRowHTML(info0, info1, info2, info3, info4, num){
  var result = '';

  result += '<tr>' +
  '<td>' + info0 +  '</td>' +
  '<td>' + info1 +  '</td>' +
  '<td>' + info2 +  '</td>' +
  '<td>' + info3 +  '</td>' +
  '<td>' +
  '<div class="form-group">' +
  '<input type="text" id="info' + num + '"value="' + info4 + '" placeholder="Wanted Value" class="form-control">' +
  '</div>' +
  '</td>' +
  '</tr>';
  return result;
}

function update(){
  // Getting the string data from the server
  $.get("https://pacific-shelf-92985.herokuapp.com/img/sample.txt", function(data) {
    systemObjs = [];
    populate(data);
    setNewRows();
  });
  //request();
}

function request(){
  $.get("https://pacific-shelf-92985.herokuapp.com/request", function(data) {
    console.log(data);
    if(data === "Still updating..."){
        setTimeout(function(){
          request();
      }, 5000);
    }
    else{
      // Restting systemObjs
      systemObjs = [];
      populate(data);
      setNewRows();
    }
  });
}

function setNewRows(){
  clearRows();
  var i = 0;
  for(obj in systemObjs){
    // Pointing the obj correctly
    obj = systemObjs[i];
    // Counter to do children
    var childCounter = 0;
    var lastId = 'changed-list';
    var line = '';
    // Loopign through to see if it has children
    while(obj != null){
      line = '<li class="child-' + childCounter + ' row changed-row" id="row' + i + '-' + childCounter + '" onclick="selectRow(' + i + ',' + childCounter + ')">' +
             '<label class="checkbox col-xs-offset-1 col-xs-4" for="checkbox' + i + '-' + childCounter + '">' +
             '<input type="checkbox" value="" id="checkbox' + i + '-' + childCounter + '" data-toggle="checkbox" class="custom-checkbox"><span class="icons"><span class="icon-unchecked"></span><span class="icon-checked"></span></span>' +
             obj.name +
             '</label>' +
             '</li>';
      // Appending to the last Id
      $('#changed-list').append(line);
      // Indentding to get children
      lastId = 'row' + i + '-' + childCounter;
      // Marking conflicts
      var conflicts = obj.finalize();
      if(conflicts.length > 0){
        $('#' + lastId).addClass("conflict")
      }
      childCounter++;
      obj = obj.child;
    }
    i++;
  }
}

function clearRows(){
  $("#changed-list").empty();
  $("#current-title").text("");
  $("#info-table").empty();
}

function populate(data){
  total = JSON.parse(data);

  // List type if based on which list was passed e.g. newPLanets, newSystems etc
  for(var listTypeIndex in data){

    var data = total[listTypeIndex];
    var listTypeName = "newSystem";

    switch(listTypeIndex){
      case 1:
        listTypeName = "newStars";
        break;
      case 2:
        listTypeName = "newStars";
        break;
    }

    var system;
    // Looping through the system changes
    for(var system_key in data){
      // Setting the system
      system = data[system_key];

      if(system.length != 0){
        var initSource = system[0];
        // Some initial Vars to help us keep track
        var column = 1;
        var sy_name = initSource["sy_name"];
        var st_name = initSource["st_name"];
        var pl_name = initSource["pl_name"];

        // The new system object created
        var created = new SystemObject(sy_name, null, "system");
        if(st_name){
           created.child = new SystemObject(st_name, null, "star")
           if(pl_name){
             created.child.child = new SystemObject(pl_name, null, "planet");
           }
        }

        // Looping through each source in the system
        for(var source_key in system){
          // Which column they belong to
          var source = system[source_key];
          var source_key = source["src"];
          if (source_key === ""){
            column = 1;
          }
          else if(source_key === "nasa"){
            column = 2;
          }
          else if(source_key === "eu"){
            column = 3;
          }

          // Looping through the keys
          for(var att_key in source){
            // After seperating the planet/system/star from the tag
            var column_name = att_key.substring(0);

            // Looping through which key it is
            if(att_key.includes("sy_")){
              created.addAttribute(column, column_name, source[att_key]);
            }
            else if(att_key.includes("st_")){
              created.child.addAttribute(column, column_name, source[att_key]);
            }
            else if(att_key.includes("pl_")){
              created.child.child.addAttribute(column, column_name, source[att_key]);
            }
          }
        }
        systemObjs.push(created);

      }
    }
  }
}

function commitChanges(){
  var text = document.getElementById("commit-message").value;
  window.alert("Your Changes Have Been Made: "+ text);
  var data = exportAsJSON();

  // Sending it as a post
  $.post("https://pacific-shelf-92985.herokuapp.com/update", function(data) {
    window.alert("Your data has been sent");
  });
}

function exportAsJSON(){
  $('.theClass:checkbox:checked');

  var total = [];

  // Exporting it as a JSON
  var i = 0;
  for(i; i < systemObjs.length; i++){
    // looping through and making a temp dict
    var temp = {};
    var curr = systemObjs[i];
    while(curr != null){
      var k = 1;
      for(k; k < curr.info[0].length; k++){
        temp[curr.info[0][k]] = curr.info[4][k];
      }
      curr = curr.child;
    }
    total.push(temp);
  }
  return JSON.stringify(total);
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

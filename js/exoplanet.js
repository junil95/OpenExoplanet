
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

    // In one of four lists
    // New/Conflicts/newAttribute/newCOnflict
    this.listType = "new";

    this.checked = false;
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

SystemObject.prototype.setSystemType = function(number){
  var list =["newSystem", "newStar", "newPlanet", "newConflictingSystem", "newConflictingStar", "newConflictingPlanet", "existingSystem", "existingStar", "existingPlanet", "existingConflictingSystem", "existingConflictingStar", "existingConflictingPlanet"];
  this.listType = list[number];

  if(this.child != null){
    this.child.setSystemType(number);
  }
}


function selectRowType(rowName){
  if(rowName === "All"){
      setNewRows(systemObjs);
  }
  else{
    setNewRows(seperateFunctions(rowName));
  }
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
  /*

  // Getting the string data from the server
  $.get("https://pacific-shelf-92985.herokuapp.com/img/update", function(data) {
  });
  request();

  */

  systemObjs = [];
  populate('[[[{"st_spectral_type":"F7","pl_inclination":"83.75","pl_mass":"0.805","pl_eccentricity":"0.0","st_name":"mu Ara","src":"eu","pl_period":"2.1746742","sy_distance":"530.0","st_metallicity":"0.0","st_magV":"13.18","st_temperature":"6280.0","sy_name":"mu Arae","sy_right_ascension":"246.692000015","pl_impact_parameter":"0.608","st_radius":"1.341","pl_radius":"1.461","sy_declination":"51.0411666736","pl_name":"mu Ara 99","pl_semi_major_axis":"0.0348","st_mass":"1.19"}],[{"st_magJ_min":"0.025","st_magK_max":"0.028","st_temperature":"5309.00","sy_name":"11 Com","st_magJ_max":"0.025","pl_periastron":"330.0000","pl_temperature":"455","sy_declination":"+42d36m15.0s","pl_name":"alright","st_mass":"0.90","st_magK_min":"0.028","pl_inclination":"87.400","pl_mass":"0.37600","pl_eccentricity":"0.014600","st_name":"11 com","src":"nasa","pl_period":"57.01100000","sy_distance":"855.00","pl_temperature_min":"-13","st_metallicity":"[M/H]","st_magH_min":"0.020","st_age":"9.700","sy_right_ascension":"19h17m04.50s","st_magH_max":"0.020","st_radius":"0.79","st_magJ":"13.814","pl_temperature_max":"14","st_magK":"13.347","pl_semi_major_axis":"0.279900","st_magH":"13.436"}]], []]')
  setNewRows(systemObjs);
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
      setNewRows(systemObjs);
    }
  });
}

function setNewRows(wantedSystemObjs){
  clearRows();
  var i = 0;
  for(obj in wantedSystemObjs){
    i = systemObjs.indexOf(wantedSystemObjs[obj]);
    // Pointing the obj correctly
    obj = wantedSystemObjs[obj];
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
  for(var listTypeIndex in total){
    var dataList = total[listTypeIndex];
    var listTypeName = "newSystem";

    var system;
    // Looping through the system changes
    for(var system_key in dataList){
      // Setting the system
      system = dataList[system_key];

      if(system.length != 0){
        // Some initial Vars to help us keep track
        var column = 1;
        var sy_name = system[0]["sy_name"];
        var st_name = system[0]["st_name"];
        var pl_name = system[0]["pl_name"];

        // The new system object created
        var created = new SystemObject(sy_name, null, "system");
        if(st_name){
           created.child = new SystemObject(st_name, null, "star")
           if(pl_name){
             created.child.child = new SystemObject(pl_name, null, "planet");
           }
        }

        created.setSystemType(listTypeIndex);

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

function seperateFunctions(listType){
  var result = [];
  for(systemObjIndex in systemObjs){
    var curr = systemObjs[systemObjIndex];
    if(curr.listType === listType){
      result.push(curr);
    }
  };
  return result;
}

function commitChanges(){
  var text = document.getElementById("commit-message").value;

  var result = []
  result.push(exportAsJSON(seperateFunctions("newSystem")));
  result.push(exportAsJSON(seperateFunctions("newStar")));
  result.push(exportAsJSON(seperateFunctions("newPlanet")));
  result.push(exportAsJSON(seperateFunctions("newConflictingSystem")));
  result.push(exportAsJSON(seperateFunctions("newConflictingStar")));
  result.push(exportAsJSON(seperateFunctions("newConflictingPlanet")));
  result.push(exportAsJSON(seperateFunctions("existingSystem")));
  result.push(exportAsJSON(seperateFunctions("existingStar")));
  result.push(exportAsJSON(seperateFunctions("existingPlanet")));
  result.push(exportAsJSON(seperateFunctions("existingConflictingSystem")));
  result.push(exportAsJSON(seperateFunctions("existingConflictingStar")));
  result.push(exportAsJSON(seperateFunctions("existingConflictingPlanet")));

  console.log(JSON.stringify(result));

  // Sending it as a post
  $.post("https://pacific-shelf-92985.herokuapp.com/setkey", function(text) {


    $.post("https://pacific-shelf-92985.herokuapp.com/upload", {result: 'result'}, function(data) {
      window.alert("Your data has been sent");
    });
  });
}

function exportAsJSON(systemObjList){
  var total = [];

  // Exporting it as a JSON
  var i = 0;
  for(i; i < systemObjList.length; i++){
    // looping through and making a temp dict
    var temp = {};
    var curr = systemObjList[i];
    while(curr != null){
      var k = 1;
      for(k; k < curr.info[0].length; k++){
        temp[curr.info[0][k]] = curr.info[4][k];
      }
      curr = curr.child;
    }
    // Pushing the dict past
    total.push(temp);
  }
  return total;
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

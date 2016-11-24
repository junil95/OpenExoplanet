function SystemObject(name, child, type){
    this.name = name;
    this.propAmount = propAmount;
    this.child = child || null;

    this.info = createArray(1, 5);

    this.info[0][0] = 'Property';
    this.info[0][1] = 'Current';
    this.info[0][2] = 'Nasa';
    this.info[0][3] = 'exoplanet';
    this.info[0][4] = 'Result';

    this.type = type || "system";
}

Person.prototype.addRow = function(row) {
  var i = 0;
  for(i; i < 5; i++){
    this.info[i].push(row[i]);
  }
};

Person.prototype.fill = function(){
  var i = 0;
  for(i; i < 5; i++){
    var g = 1;
    for(g; g < propAmount + 1; g++){
        this.info[g][i] = 'N/A';
    }
  }
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

      	  this.properties.replace(property.substring(3), properties.get(property));

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

function update(){
  systemObjs = {};
  populate('[[{"st_magJ_min":"","pl_longitude":"","pl_mass_error_max":"","st_age_error_max":"","pl_longitude_error_max":"","st_magK_max":"","pl_last_update":"","st_temperature":"6280.0","sy_name":"mu Arae","st_magJ_max":"","st_magV_max":"","pl_alternatenames":"","pl_periastron":"","pl_longitude_error_min":"","st_mass_error_max":"","pl_inclination_error_min":"","pl_impact_parameter_error_max":"","pl_discovery_year":"","pl_name":"mu Ara 99","st_magK_min":"","st_alternatenames":"","st_temperature_error_min":"","pl_periastron_error_max":"","pl_inclination":"83.75","st_radius_error_max":"","pl_mass":"0.805","st_magV_min":"","pl_period":"2.1746742","sy_distance":"530.0","pl_temperature_min":"","st_metallicity":"0.0","pl_eccentricity_error_min":"","pl_eccentricity_error_max":"","st_radius_error_min":"","pl_semi_major_axis_error_max":"","st_magI":"","st_magJ":"","pl_temperature_max":"","st_magK":"","st_magH":"","pl_mass_other_error_min":"","pl_semi_major_axis_error_min":"","pl_period_error_min":"","pl_radius_error_minpl_radius_error_min":"","pl_radius_error_max":"","st_magV":"13.18","pl_mass_other_error_max":"","st_temperature_error_max":"","pl_periastron_error_min":"","pl_radius":"1.461","pl_temperature":"","pl_period_error_max":"","sy_declination":"51.0411666736","pl_discovery_method":"","st_mass":"1.19","st_spectral_type":"F7","st_magI_max":"","sy_distance_error_max":"","pl_eccentricity":"0.0","st_name":"mu Ara","src":"eu","st_magH_min":"","st_age":"","pl_inclination_error_max":"","pl_mass_other":"","sy_distance_error_min":"","sy_right_ascension":"246.692000015","pl_impact_parameter":"0.608","st_magH_max":"","st_radius":"1.341","st_mass_error_min":"","pl_impact_parameter_error_min":"","st_age_error_min":"","pl_mass_error_min":"","st_magI_min":"","pl_semi_major_axis":"0.0348"}]]')

    $.get("https://pacific-shelf-92985.herokuapp.com/update", function(data) {
      console.log(data);

    });
}

function populate(data){
  data = JSON.parse(data);
  var system;
  // Looping through the system changes
  for(var system_key in data){
    // Setting the system
    system = data[system_key];
    var source;
    // Looping through each source in the system
    for(var source_key in system){
      source = system[source_key];

      var column = 0;
      var sy_name = source["sy_name"];
      var st_name = source["st_name"];
      var pl_name = source["pl_name"];
      var source_key = source["source"];

      if (source === ""){
        nasa = 0;

      }

      // Looping through the keys
      for(var att_key in source){
        console.log(source[att_key]);

        new SystemObject(sy_name, new SystemObject(st_name, new SystemObject(pl_name, null, "planet") "star"), "system");

        // Looping through which key it is
        if(str.includes("sy_")){
          console.log();
        }
        else if(str.includes("st_")){

        }
        else if(str.includes("pl_")){

        }
      }
    }
  }
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

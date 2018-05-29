$(document).ready(initPage());

function initPage() {
  showLocation();
  loadCountries();

  $("th").click(function(e) {
    sortCountries(e);
  });

  $("#searchInput").keyup(function(e) {
    filterCountries(e);
  });
}

function showLocation() {
  var myLocation = $("#myLocation");

  $.get("https://ipapi.co/json/", function(data) {
    myLocation.find("#code").text(data.country);
    myLocation.find("#land").text(data.country_name);
    myLocation.find("#regio").text(data.region);
    myLocation.find("#stad").text(data.city);
    myLocation.find("#postcode").text(data.postal);
    myLocation.find("#lat").text(data.latitude);
    myLocation.find("#long").text(data.longitude);
    myLocation.find("#ip").text(data.ip);

    myLocation.find("#stad").click(function() {
      showWeather(data.latitude, data.longitude, data.city, data.country);
    });

    showWeather(data.latitude, data.longitude, data.city, data.country);
  });
}

function showWeather(lat, long, city, country) {
  $("#weatherCity").text("Het weer in " + city);

  if (localStorage.getItem(city) === null) {
    get();
  } else {
    var data = JSON.parse(localStorage.getItem(city));
    Date.now() - data.requesttime > 600000 ? get() : show(data);
  }

  function get() {
    var getURL =
      "https://api.apixu.com/v1/current.json?key=2912772a6f794458b29205324172405&q=" +
      lat +
      "," +
      long;

    $.get(getURL, function(weather) {
      var data = {
        temp: weather.current.temp_c + "°C",
        vocht: weather.current.humidity + "%",
        snelheid: weather.current.wind_kph + " km/u",
        richting: weather.current.wind_dir,
        opgang: "",
        ondergang: "",
        requesttime: Date.now(),
        flag: country.toLowerCase(),
        icon: weather.current.condition.icon
      };

      localStorage.setItem(city, JSON.stringify(data));

      show(data);
    });
  }

  function show(data) {
    var weatherInfo = $("#weatherInfo");

    weatherInfo.find("#temp").text(data.temp);
    weatherInfo.find("#vocht").text(data.vocht);
    weatherInfo.find("#snelheid").text(data.snelheid);
    weatherInfo.find("#richting").text(data.richting);
    weatherInfo.find("#opgang").text(data.opgang);
    weatherInfo.find("#ondergang").text(data.ondergang);

    $("#icon").html("<img src='" + data.icon + "'/>");

    $("#weatherFlag").attr("class", "flag-icon flag-icon-" + data.flag);
  }
}

function loadCountries() {
  $.get("/restservices/countries", function(data) {
    var countryTable = $("#countryTable");
    $.each(data, function(index, object) {
      console.log(object);
      var String =
        "<tr id='row" +
        index +
        "'>" +
        "<td>" +
        object.name +
        "</td>" +
        "<td>" +
        object.capital +
        "</td>" +
        "<td>" +
        object.region +
        "</td>" +
        "<td>" +
        object.surface +
        "</td>" +
        "<td>" +
        object.population +
        "</td>" +
        "</tr>";
      countryTable.append(String);
      $("#row" + index).click(function() {
        showWeather(
          object.latitude,
          object.longitude,
          object.capital,
          object.code
        );
      });
    });
  });
}

function sortCountries(e) {
  var table, rows, switching, i, x, y, shouldSwitch;
  table = document.getElementById("countryTable");
  switching = true;
  /*
		 * Make a loop that will continue until no switching has been done:
		 */
  while (switching) {
    // start by saying: no switching is done:
    switching = false;
    rows = table.getElementsByTagName("TR");
    /*
		 * Loop through all table rows (except the first, which contains
		 * table headers):
		 */
    for (i = 1; i < rows.length - 1; i++) {
      // start by saying there should be no switching:
      shouldSwitch = false;
      /*
			 * Get the two elements you want to compare, one from current
			 * row and one from the next:
			 */
      x = rows[i].getElementsByTagName("TD")[e.target.cellIndex];
      y = rows[i + 1].getElementsByTagName("TD")[e.target.cellIndex];
      // check if the two rows should switch place:
      if (
        e.target.cellIndex > 2 &&
        parseInt(x.innerHTML) > parseInt(y.innerHTML)
      ) {
        // if so, mark as a switch and break the loop:
        shouldSwitch = true;
        break;
      } else if (
        e.target.cellIndex <= 2 &&
        x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()
      ) {
        // if so, mark as a switch and break the loop:
        shouldSwitch = true;
        break;
      }
    }
    if (shouldSwitch) {
      /*
			 * If a switch has been marked, make the switch and mark that a
			 * switch has been done:
			 */
      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
      switching = true;
    }
  }
}

function filterCountries(e) {
  // Declare variables
  var input, filter, table, tr, td, i, j;
  input = e.target;
  filter = input.value.toUpperCase();
  table = document.getElementById("countryTable");
  tr = table.getElementsByTagName("tr");

  // Loop through all table rows, and hide those who don't match the search query
  for (i = 1; i < tr.length; i++) {
    for (j = 0; j < 5; j++) {
      td = tr[i].getElementsByTagName("td")[j];
      if (td) {
        if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
          tr[i].style.display = "";
          break;
        } else {
          tr[i].style.display = "none";
        }
      }
    }
  }
}

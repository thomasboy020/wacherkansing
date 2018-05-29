$(document).ready(initPage());

function initPage() {
    loadCountries();

    $("#searchInput").keyup(function (e) {
        filterCountries(e);
    });

    $("#countryTable").click(changeContent);

    $("#close").click(clearFields);

    $("#add_country").click(function () {
        document.getElementById("myModal").style.display = "block";
    });

    $("#cancel").click(clearFields);

    $("#save").click(saveFields);
}

//TODO : Labels, update, insert, delete

function loadCountries() {
    $.get("../restservices/countries", function (data) {
        var countryTable = $("#countryTable");
        $.each(data, function (index, object) {
            var String =
                "<tr id='row" +
                index +
                "'>" +
                "<td class='code_row'>" +
                object.code +
                "</td>" +
                "<td>" +
                object.name +
                "</td>" +
                "<td>" +
                object.capital +
                "</td>" +
                "<td>" +
                object.region +
                "</td>" +
                "<td class='delete'>&times;</td>" +
                "</tr>";
            countryTable.append(String);
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

function deleteCountry(e) {
    var row = e.target.parentNode;
    var countryCode = row.firstChild.innerText;

    $.ajax({
        url: "/restservices/countries/" + countryCode,
        type: "DELETE",
        beforeSend: function (xhr) {
            var token = window.sessionStorage.getItem("sessionToken");
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        },
        success: function (result) {
            $(row).remove();
        }
    });
}

function showContent(e) {
    var row = e.target.parentNode;
    var countryCode = row.firstChild.innerText;
    var country = getCountry(countryCode);
    document.getElementById("myModal").style.display = "block";
}

function getCountry(code) {
    $.get("/restservices/countries/" + code, function (data) {
        var inputs = $("#inputContainer input");

        $.each(inputs, function (index, input) {
            input.value = data[input.id];
        });

        document.getElementById("code3").disabled = true;
    });
}

//$(function() {
//    $.getJSON("./restservices/countries", function(data) {
//      $.each(data, function(v, n) {
//               tr = $('<tr/>');
//               tr.append("<td>" + n.code + "</td>");
//               tr.append("<td>" + n.name + "</td>");
//               tr.append("<td>" + n.continent + "</td>");
//               $('table').append(tr);
//      });
//    });
// });

function changeContent(e) {
    if (e.target.nodeName === "TH") sortCountries(e);
    else if (e.target.classList.contains("delete")) deleteCountry(e);
    else showContent(e);
}

function addCountry(e) {
    var object = {};

    var inputs = $("#inputContainer input");

    $.each(inputs, function (index, input) {
        object["" + input.id + ""] = input.value;
    });

    var countryTable = $("#countryTable");

    $.ajax({
        url: "/restservices/countries/",
        type: "POST",
        contentType: "application/json",
        beforeSend: function (xhr) {
            var token = window.sessionStorage.getItem("sessionToken");
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        },
        data: JSON.stringify(object),
        success: function (result) {
            var String =
                "<tr id='row" +
                object.code +
                "'>" +
                "<td class='code_row'>" +
                object.code +
                "</td>" +
                "<td>" +
                object.name +
                "</td>" +
                "<td>" +
                object.capital +
                "</td>" +
                "<td>" +
                object.region +
                "</td>" +
                "<td class='delete'>&times;</td>" +
                "</tr>";
            countryTable.append(String);
            clearFields();
            document.getElementById("myModal").style.display = "none";
        }
    });
}

function clearFields() {
    var inputs = $("#inputContainer input");

    $.each(inputs, function (index, input) {
        input.value = "";
    });

    document.getElementById("code3").disabled = false;
    document.getElementById("myModal").style.display = "none";
}

function saveFields(e) {
    document.getElementById("code3").disabled ? updateCountry() : addCountry();
}

function updateCountry() {
    var object = {};

    var inputs = $("#inputContainer input");

    $.each(inputs, function (index, input) {
        object["" + input.id + ""] = input.value;
    });

    var countryTable = $("#countryTable");


    $.ajax({
        url: "/restservices/countries/",
        type: "PUT",
        contentType: "application/json",
        data: JSON.stringify(object),
        success: function (result) {
            var code_rows = document.getElementsByClassName("code_row");
            var searchText = object.code;
            var found;

            for (var i = 0; i < code_rows.length; i++) {
                if (code_rows[i].textContent === searchText) {
                    found = code_rows[i];
                    break;
                }
            }

            found.parentNode.innerHTML =
                "<td class='code_row'>" +
                object.code +
                "</td>" +
                "<td>" +
                object.name +
                "</td>" +
                "<td>" +
                object.capital +
                "</td>" +
                "<td>" +
                object.region +
                "</td>" +
                "<td class='delete'>&times;</td>";

            clearFields();
            document.getElementById("myModal").style.display = "none";
        },
        beforeSend: function (xhr) {
            var token = window.sessionStorage.getItem("sessionToken");
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        }
    });
}









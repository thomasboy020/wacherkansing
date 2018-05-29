$(document).ready(function() {
	$("#localstorage").keyup(function() {
		localStorage.setItem("textfield", $("#localstorage").val());
	});
});

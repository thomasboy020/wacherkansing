var old = document.getElementById("text").value;

setInterval(function() {
	var newText = document.getElementById("text").value;
	if (newText !== old) {
		console.log(newText);
		old = newText;
	}
	},5000);
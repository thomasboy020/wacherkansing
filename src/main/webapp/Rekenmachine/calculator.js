(function(){
	var oldNumber = "0";
	var newNumber = "0";
	var operator = "";
	var count = true;
	var numbers = document.getElementsByClassName("nr");
	var others = document.getElementsByClassName("other");
	for(var i=0; i<numbers.length; i++) {
		numbers[i].addEventListener("click", clickedNumber);
	}
	for(i=0; i<others.length; i++) {
		others[i].addEventListener("click", clickedOther);
	}
	
	
	function clickedNumber(e) {
		newNumber === "0" ? newNumber = e.target.innerText : newNumber += e.target.innerText;
		count = true;
		changeDisplay(newNumber);
	}
	
	function clickedOther(e) {
		switch(e.target.innerText) {
		case "C": {
			newNumber = "0";
			changeDisplay(newNumber);
			count = true;
			break;
		}
		case "=": {
			var temp = newNumber;
			
			if(count) {
				newNumber = eval(oldNumber + operator + newNumber);
				oldNumber = temp;
				count = false;
			} else {
				newNumber = eval(newNumber + operator + oldNumber);
			}
			changeDisplay(newNumber);
			break;
		}
		default: {
			operator = e.target.innerText;
			oldNumber = newNumber;
			newNumber = "0";
			changeDisplay(newNumber);
			count = true;
			break;
		}
		}
	}
	
	function changeDisplay(text) {
		document.getElementById("display").innerText = text;
	}
	})();
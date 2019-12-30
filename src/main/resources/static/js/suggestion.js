function loadMock() {

	jQuery.getJSON("./suggestion/userSuggestion", function(json) {
		var recommendation1 = JSON.parse(json)
	})

    jQuery.getJSON("./suggestion/itemSuggestion", function(json) {
		var recommendation2 = JSON.parse(json)
	})

    jQuery.getJSON("./user/current", function(json) {
		var currentUser = JSON.parse(json)
	})
}

function myFunction() {
  var x = document.getElementById("card1" + "card2");
  if (currentUser.totalRatings <20 && x.style.display  === "none") {
    x.style.display = "block";
  } else {
    x.style.display = "none";
  }
}
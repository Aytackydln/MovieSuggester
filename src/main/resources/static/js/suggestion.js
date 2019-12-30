function loadMock() {

	jQuery.getJSON("./suggestion/userSuggestion", function(json) {
		var recommendation1 = JSON.parse(json)
                $("#card1").innerHTML=+"<img src="+ recommendation1.movie.poster +">";
		$("#card1").innerHTML=+"<label>"+ recommendation1.movie.id + "</label>";
		$("#card1").innerHTML=+"<label>"+ "Runtime :"  +recommendation1.movie.runtime+"</label>";
		$("#card1").innerHTML=+"<label>"+ "Released :"  +recommendation1.movie.releaseDate+"</label>";
		$("#card1").innerHTML=+"<a href="recommendation1.movie.imdbUrl" target="_blank">"+"<i class="icon-external-link">"+"</i>"+"</a>";
		$("#card1").innerHTML=+"<h3>"+recommendation1.movie.title"</h3>";
	})

    jQuery.getJSON("./suggestion/itemSuggestion", function(json) {
		var recommendation2 = JSON.parse(json)
		$("#card2").innerHTML=+"<img src="+recommendation2.movie.poster+">";
		$("#card2").innerHTML=+"<label>"+recommendation2.movie.id + "</label>";
		$("#card2").innerHTML=+"<label>"+ "Runtime :" +recommendation2.movie.runtime + "</label>";
		$("#card2").innerHTML=+"<label>" + "Released :" : +recommendation2.movie.releaseDate + "</label>";
		$("#card2").innerHTML=+"<a href="+recommendation2.movie.imdbUrl" target="_blank">"+"<i class="icon-external-link">"+"</i>"+"</a>";
		$("#card2").innerHTML=+"<h3>"+recommendation2.movie.title +"</h3>";
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

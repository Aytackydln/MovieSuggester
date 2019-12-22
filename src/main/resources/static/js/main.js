
$(document).ready(function(){
	"use strict";

    $('input:radio').change(
      function(){
		  
		var value = this.value.split('-');
		 
		var cardName = '#card'+value[0];
		var card = document.querySelector(cardName);
		  
		var movieId = card.querySelector('.movieId');
		var userRating = card.querySelector('.userRating');
        userRating.value = value[1];
		alert(movieId.value + " : " + userRating.value);
		  

		  
		 
		  //$("#form1").submit();
		  
		jQuery.getJSON("./models/model", function(json) {
			console.log(json); // this will show the info in firebug console 
			
			var movieheader = card.children.item(0);
			movieheader.style.backgroundImage = "url("+json.poster+")";
			
			var  movieContent = card.children.item(1);
			
			var movieTitle = movieContent.children.item(0).children.item(0).children.item(0);
			movieTitle.textContent = json.title;
			
			var movieReleaseDate = movieContent.children.item(1).children.item(0).children.item(1);
			movieReleaseDate.textContent = json.releaseDate;
			
			var movieRuntime = movieContent.children.item(1).children.item(1).children.item(1);
			movieRuntime.textContent = json.runtime;
			
			var movieImdb = movieContent.children.item(1).children.item(2).children.item(1);
			movieImdb.setAttribute("href", json.imdbUrl);
			
			movieId = json.id;
			userRating.value = "0";
			
		});
		  
		  $(this).prop('checked', false);
		  
		  
    }); 
});


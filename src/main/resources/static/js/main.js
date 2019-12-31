var array = [];

$(document).ready(function(){
	"use strict";

    $('input:radio').change(
      function(){

          let value = this.value.split('-');

          let cardName = '#card' + value[0];
          let card = document.querySelector(cardName);

          let movieId = card.querySelector('.movieId');

          let ratedMovieId = parseInt(movieId.value);
          let rating = parseInt(value[1]);

          let voteResult;
          if (rating === 0) {
              array.push(array[value[0] - 1]);
              array[value[0] - 1] = null;
              voteResult = new VoteResult(null, null, array)
          } else {
              voteResult = new VoteResult(ratedMovieId, rating, array)
          }

          console.log("VoteResult = " + voteResult.toJson());

          //alert(ratedMovieId + " : " + rating);

          jQuery.ajax({
              url: "./suggestion/vote",
              type: "POST",
              data: voteResult.toJson(),
              dataType: "json",
			  contentType: "application/json; charset=utf-8",
			  beforeSend: function(){
				  $("#overlay").fadeIn(300);
			  },
			  success: function(data){
				  let movie = new Movie();
				  movie.applyData(data);
				  array[value[0]-1] = movie;
				  setCardValues(card,data);
			  },
			  complete: function() {
					  $("#overlay").fadeOut(300);
			  },
			  fail: function () {
			  	  alert("There is an error while sending your vote!")
			  }});

		  $(this).prop('checked', false);
	});
		  

});

function loadBody() {

	jQuery.getJSON("./suggestion/randomList", function(json) {
		for (let i=1; i<5; i++){
			let cardName = '#card'+i;
			let card = document.querySelector(cardName);

			let movie = new Movie();
			movie.applyData(json[i-1]);

			array.push(movie);

			console.log(movie);


			setCardValues(card, movie);

		}
	})
}

function loadSearchResults(){
	let url = new URL(window.location.href);
	let title = url.searchParams.get("title");

	jQuery.getJSON("./suggestion/search?title=" + title + "&size=1682" , function (json) {
		let totalMovies = json.totalElements;
		let numberOfElements, totalPages, lastElements;

		if(totalMovies<4){
			numberOfElements = json.numberOfElements;
			totalPages = 1;
		}else{
			numberOfElements = 4;
			totalPages = Math.ceil(totalMovies/4);
			lastElements = totalMovies%4;
		}

		for (let i=0; i<4; i++){
			let cardName = "#card" + (i+1);
			let card = document.querySelector(cardName);
			if(i<numberOfElements){
				let movie = new Movie();
				movie.applyData(json.content[i]);
				array.push(movie);
				setCardValues(card, movie);
			}else{
				card.style.display = "none";
			}
		}

		$('#pagination-demo').twbsPagination({
			totalPages: totalPages,
			visiblePages: 5,
			onPageClick: function (event, page) {
				for (let i=0; i<4; i++){
					let cardName = "#card" + (i+1);
					let card = document.querySelector(cardName);
					numberOfElements = (page==totalPages)?lastElements:4;
					if(i<numberOfElements){
						card.style.display = "block";
						let movie = new Movie();
						movie.applyData(json.content[i+((page-1)*4)]);
						array[i] = movie;
						setCardValues(card, movie);
					}else{
						card.style.display = "none";
					}
				}
			}
		});

	});


}

function setCardValues(card, movie) {

	let movieId = card.querySelector('.movieId');
	let userRating = card.querySelector('.userRating');

	let movieHeader = card.children.item(0);
	movieHeader.style.backgroundImage = "url("+ movie.poster +")";

	let  movieContent = card.children.item(1);

	let movieTitle = movieContent.children.item(0).children.item(0).children.item(0);
	movieTitle.textContent = movie.title;

	let movieReleaseDate = movieContent.children.item(1).children.item(0).children.item(1);
	movieReleaseDate.textContent = movie.releaseDate;

	let movieRuntime = movieContent.children.item(1).children.item(1).children.item(1);
	movieRuntime.textContent = movie.runtime;

	let movieImdb = movieContent.children.item(1).children.item(2).children.item(1);
	movieImdb.setAttribute("href", movie.imdbUrl);

	movieId.value = movie.id;
	userRating.value = "0";

}

class Movie {
	constructor(id, title, releaseDate, imdbUrl, poster, runtime, rating, totalRatings) {
		this.id = id;
		this.title = title;
		this.releaseDate = releaseDate;
		this.imdbUrl = imdbUrl;
		this.poster = poster;
		this.runtime = runtime;
		this.rating = rating;
		this.totalRatings = totalRatings
	}

	applyData(json) {
		Object.assign(this, json);
	}
}

class VoteResult{
	constructor(ratedMovieId, rating, filterOut) {
		this.ratedMovieId = ratedMovieId;
		this.rating = rating;
		this.filterOut = filterOut
	}

	toJson(){
		return JSON.stringify(this)
	}
}


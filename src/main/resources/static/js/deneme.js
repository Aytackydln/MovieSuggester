function loadSearchResults(){
    let url = new URL(window.location.href);
    let title = url.searchParams.get("title");

    let totalPages;

    jQuery.getJSON("./suggestion/search?title=" + title +"&size=4", function (json) {
        let totalMovies = json.totalElements;

        let numberOfElementsInPage;
        totalPages = Math.ceil(totalMovies / 4);

        console.log(json);

        /*        if(totalMovies<4){
                    numberOfElementsInPage = json.numberOfElements;
                    totalPages = 1;
                }else{
                    numberOfElementsInPage = 4;
                    totalPages = Math.ceil(totalMovies/4);
                }*/
    });

    for (let i=0; i< totalPages; i++ ){
        jQuery.getJSON("./suggestion/search?title=" + title + "&size=4&page=" + i, function (json){
            let numberOfElementsInPage = json.numberOfElements;
            for (let j=0; j<4; i++){
                let cardName = "#card" + (j+1);
                let card = document.querySelector(cardName);
                if(j<numberOfElementsInPage){
                    let movie = new Movie();
                    movie.applyData(json.content[j]);
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
                        if(i<numberOfElementsInPage){
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
}
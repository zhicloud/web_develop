$(document).ready(function(){
    var options = {
        nextButton: false,
        prevButton: false,
        pagination: false,
        animateStartingFrameIn: true,
        autoPlay: false,
        autoPlayDelay: 3000,
        preloader: true,
        preloadTheseFrames: [1],
        preloadTheseImages: [
        ]
    };
    
    var mySequence = $("#sequence").sequence(options).data("sequence");
});
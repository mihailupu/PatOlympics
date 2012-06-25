$(document).ready(function() {
    $('marquee').marquee('sbTicker');
    updateScoreBoard();
    setInterval('updateScoreBoard()', 15000);
    return false;
});

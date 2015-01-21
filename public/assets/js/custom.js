// Calling the function
$(function() {
    $('.toggle-nav').click(function() {
        toggleNavigation();
    });

    // SLiding codes
    $("#toggle > li > div").click(function () {
        if (false == $(this).next().is(':visible')) {
            $('#toggle ul').slideUp();
        }
     
        var $currIcon=$(this).find("span.the-btn");
     
        $("span.the-btn").not($currIcon).addClass('fa-plus').removeClass('fa-minus');
     
        $currIcon.toggleClass('fa-minus fa-plus');
     
        $(this).next().stop().slideToggle();
     
        $("#toggle > li > div").removeClass("active");
        $(this).addClass('active');
    });
});
 
// The toggleNav function itself
function toggleNavigation() {
    if ($('#container').hasClass('display-nav')) {
        // Close Nav
        $('#container').removeClass('display-nav');
        
        // Change icon
        $('#bars > i').removeClass('fa-times');
        $('#bars > i').addClass('faa-bounce');
        $('#bars > i').addClass('fa-bars');
    } else {
        // Open Nav
        $('#container').addClass('display-nav');
        
        // Change icon
        $('#bars > i').removeClass('fa-bars');
        $('#bars > i').removeClass('faa-bounce');
        $('#bars > i').addClass('fa-times');
    }
}
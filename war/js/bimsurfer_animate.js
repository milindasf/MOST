var feedbackTab = {
 
    speed:300,
    containerWidth:$('.feedback-panel').outerWidth(),
    containerHeight:$('.feedback-panel').outerHeight(),
    tabWidth:$('.feedback-tab').outerWidth(),
 
 
    init:function(){
        $('.feedback-panel').css('height',feedbackTab.containerHeight + 'px');
 
        $('a.feedback-tab').click(function(event){
            if ($('.feedback-panel').hasClass('open')) {
                $('.feedback-panel')
                .animate({right:'-' + feedbackTab.containerWidth}, feedbackTab.speed)
                .removeClass('open');
            } else {
                $('.feedback-panel')
                .animate({right:'0'},  feedbackTab.speed)
                .addClass('open');
            }
            event.preventDefault();
        });
    }
};
 
feedbackTab.init();

var count = 1;
document.getElementById("snapshots-fold").style.display = "none";
function snapshot() {
	
	if (document.getElementById("snapshots-fold").style.display = "none") 
	{
		
		document.getElementById("snapshots-fold").style.display = "table-cell";
		document.getElementById("button-link").style.marginLeft ="-195px";
		document.getElementById("border").style.marginLeft ="-129px";
		
	}
	else if (document.getElementById("snapshots-fold").style.display = "table-cell")
	{
		
		document.getElementById("snapshots-fold").style.display = "none";
		document.getElementById("button-link").style.marginLeft ="-38px";
		document.getElementById("border").style.marginLeft ="28px";	
	}
	else {
		
	}
	
}
 
  var ongoingTouches = new Array(); 
  function ongoingTouchIndexById(idtofind) {
	  for ( var i = 0; i < ongoingTouches.length; i++) {
		 // console.log(ongoingTouches.length + " touch: " + i + " wert: " + ongoingTouches[i].id)
		  var id = parseInt(ongoingTouches[i].id);
		  if( id == parseInt(idtofind)) {
			  return i;
		  }
		  else {
			  return -1;
		  }
	  }
  }
  document.addEventListener('MozTouchDown', function(event) {
	  event.preventDefault();
	 if(parseInt(event.streamId) > 0) { 
		var currentTouch = {
		 		id: event.streamId,
				 X: event.clientX,
				 Y: event.clientY
		 	}
	 	ongoingTouches.push(currentTouch);
	 }	  
	/*   var touches = event.changedTouches;
	  
	  for(var i=0; i<touches.length; i++) {
		  ongoingTouches.push(touches[i]);
	  } */
	//  console.log(ongoingTouches);
  }, false);
  var currentTouches = new Array();
  var initA, initB;

  var distance = 0;
  function calcDist() {
	return  Math.sqrt((parseInt(ongoingTouches[1].X) - parseInt(ongoingTouches[0].X)) * (parseInt(ongoingTouches[1].X) - parseInt(ongoingTouches[0].X))) + ((parseInt(ongoingTouches[1].Y) - parseInt(ongoingTouches[0].Y)) * (parseInt(ongoingTouches[1].Y) - parseInt(ongoingTouches[0].Y))) ;
  }
  document.addEventListener('MozTouchMove', function(event){
	 
	  event.preventDefault();
	 
	  if(ongoingTouches.length == 2) {
		   var initDist;
		   if ( distance == 0) {
		   initDist = calcDist();
		   distance = initDist;
		   }
		   var currDistance;
		//console.log("initdist: " + initDist);

	/* 	  console.log(event.streamId); */
		    //for( var i = 0; i < ongoingTouches.length; i++ ) {  
			   var idx = ongoingTouchIndexById(parseInt(event.streamId));
			   //console.log(idx + " : " + event.streamId + "ongoingTouches:0 " + ongoingTouches[0].id + " 1: " + ongoingTouches[1].id);
			   if ( idx != -1 ) {
			   	   ongoingTouches[idx].X = event.clientX;
			   	   ongoingTouches[idx].Y = event.clientY;
			   }
			   currDistance = calcDist();
			   console.log("currdist: " +currDistance);
			  /*  if ( event.clientX * event.clientY > ongoingTouches[idx].X * ongoingTouches[idx].Y) {
				   window.setZoomLevel(-1);
				   console.log("zoom in");
			   } else {
				   window.setZoomLevel(1);
				   console.log("zoom out");
			   } */
			 /*  if(event.streamId == ongoingTouches[i].id ) {
				  var a = {  id: event.streamId,
						  X: event.clientX,
				  		  Y: event.clientY
				  }
				  if(currentTouches.length < 2) {
				  currentTouches.push(a);
				  } else {currentTouches = [];
				  currentTouches.push(a);}
			  } */
			 /*  if (event.streamId == ongoingTouches[i].id){
				  b = {  id: event.streamId,
						  X: event.clientX,
				  		  Y: event.clientY
				  }
			  } */
		   // } 
		   if(currDistance > distance) {
			   window.setZoomLevel(-1);
				  console.log("---");
			  } else {
				  window.setZoomLevel(1);
				  console.log("+++");
			  }
			  
			  distance = currDistance;
		  
	  }

	 

	  
	 /*  console.log("init: " + ongoingTouches);
	  console.log(event);
	  console.log("touchmove");
	  var touches = evt.changedTouches;
	  
	  for(var i=0; i<touches.length; i++) {
		  var idx = ongoingTouchIndexById(touches[i].identifier);
		  console.log(ongoingTouches.splice(idx, 1, touches[i]));
	  } */
  }, false);
  document.addEventListener('MozTouchUp', function(event){
	  event.preventDefault();
	  console.log("clean");
	  ongoingTouches = [];
	  currentTouches = [];
  }, false);
  
    var pageElement = document.getElementById('main-layout');
    pageElement.style.MozUserSelect='none';
    pageElement.onmousedown=function(){return false};

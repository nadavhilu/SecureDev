$(document).ready(function(){
	$('table tr').not(".table-header").click(function(){
		window.location = $(this).attr('href');
		return false;
	});
	$(window).keydown(function(event){
		if(event.keyCode == 13) {
			event.preventDefault();
			return false;
		}
	});
});
function myFunction() {
	// Declare variables 
	var input, filter, table, tr, td, i,j,remove=true;
	input = document.getElementById("myInput");
	filter = input.value.toUpperCase();
	tr = $('table tr').not(".table-header");
	
	// Loop through all table rows, and hide those who don't match the search query
	for (i = 0; i < tr.length; i++) {
		for(j=0;j<3;j++)
		{
			td = tr[i].getElementsByTagName("td")[j];
			if(remove==true)
			{
				if (td) {
					if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
						remove=false;
						tr[i].style.display = "";
					} else {
						tr[i].style.display = "none";
					}
				}
			}
		}
		remove=true;
	}
}

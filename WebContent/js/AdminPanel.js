function myFunction() {
	// Declare variables 
	var input, filter, table, tr, td, i,j,remove=true;
	input = document.getElementById("myInput");
	filter = input.value.toUpperCase();
	tr = $('table tr').not(".table-header");

	// Loop through all table rows, and hide those who don't match the search query
	for (i = 0; i < tr.length; i++) {

		td = tr[i].getElementsByTagName("td")[0];
		if(remove==true)
		{
			if (td) {
				if (td.textContent.toUpperCase().indexOf(filter) > -1) {
					remove=false;
					tr[i].style.display = "";
				} else {
					tr[i].style.display = "none";
				}
			}
		}
		remove=true;
	}
}

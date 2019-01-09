$(document).ready(function(){
	$(".edit").on("click",function(){
		$("#delete").attr('href',"/SecureDev/DeleationController?post="+$(this).attr("id"));
		$("#editReply").val("Edit");
		$("#PostBody").val($("#message"+$(this).attr("id")).attr("value"));
	});

	$("#myInput").keydown(function(event){
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
	table = document.getElementById("myTable");
	tr = table.getElementsByTagName("tr");

	// Loop through all table rows, and hide those who don't match the search query
	for (i = 2; i < tr.length; i=i+2) {
		for(j=0;j<2;j++)
		{
			td = tr[i].getElementsByTagName("td")[j];
			if(remove==true)
			{
				if (td) {
					if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
						remove=false;
						tr[i].style.display = "";
						tr[i-1].style.display = "";
					} else {
						tr[i].style.display = "none";
						tr[i-1].style.display = "none";
					}
				}
			}
		}
		remove=true;
	}
}
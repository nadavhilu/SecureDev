$(function() {
	var url = window.location.pathname,
	urlRegExp = new RegExp(url.replace(/\/$/, '') + "$");
	if(urlRegExp.test("/SecureDev/ThreadController"))
	{
		$("#Forum").addClass('active');
	}
	else
		if(urlRegExp.test("/SecureDev/"))
		{
			$("#Home").addClass('active');
		}	
		else
		{
			$('.navbar-collapse a').each(function() {
				if (urlRegExp.test(this.href.replace(/\/$/, ''))) {
					$(this).parent().addClass('active');
				}
			});
		}
});
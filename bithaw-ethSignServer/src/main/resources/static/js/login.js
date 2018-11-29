$(function(){
	
});

$("#login").click(function(){
	var params = {};
	params.username = $("#username").val();
	params.password = $("#password").val();
	params.sign = $("#sign").val();
	$.post("login-in",params,function(data){
		if(data.code == "0"){
			alert(data.message);
		}
		if(data.code == "1"){
			$.cookie("c5SignServerToken", data.token);
			//跳转到主页
			window.location.href="index";
		}
	},"JSON");
});
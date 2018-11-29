$("#createGoogleSign").click(function(){
	var params = {};
	params.password = $("#password").val();
	params.password_re = $("#passwordRe").val();
	
	$.ajax({
		async:false, 
		url:"first-init",
		success:function(data){
			if(data.code == "1"){
				var googleSign = data.google_sign;
				$("#googleSign").html("生成的google秘钥文件: " + googleSign + " ");
			}else if(data.code == "0"){
				alert(data.message)
			}
		},
		type:"post",
		data:params,
		dataType:"json"
	});
});
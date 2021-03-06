//显示登录页
//	function loginPage(){
//		$("#loginDiv").show();
//		$("#mainDiv").hide();
//		$("#loginout").hide();
//	}
	
	//显示主页
	function mainPage(){
		$("#loginDiv").hide();
		$("#mainDiv").show();
		$("#loginout").show();
		getPublicKey();
		$("#addtypeSource").hide();
		getSignServerStatus();
	}
	var ADDTYPE = 1;//文件格式
	$("#changeToSource").click(function(){
		$("#addtypeFile").hide();
		$("#addtypeSource").show();
		ADDTYPE = 2;
	});
	
	$("#changeToFile").click(function(){
		$("#addtypeFile").show();
		$("#addtypeSource").hide();
		ADDTYPE = 1;
	});
	
	function getPublicKey(){
		$.post("get-public-key",null,function(data){
			var addresses = data.split("|");
			var tBodyText = "<tr><td>私钥</td><td>操作</td></tr>";
			for(var i = 0; i < addresses.length; i++){
				tBodyText += "<tr><td>" + addresses[i] + "</td><td><button onClick = \"removeKey('"+addresses[i]+"')\">删除</button></td></tr>";
			}
			tBodyText += "<tr><td colspan = '3'>谷歌验证码:<input id = \"removeSign\" type = \"text\"/></td></tr>";
			$("#addressesTbody").html(tBodyText);
		},"TEXT");
	}
	
	$(function(){
		var signServerToken = $.cookie("c5SignServerToken");
		if(signServerToken == null){
			window.location.href="login";//没有登录跳转到登录页
		}else{
			mainPage();//登录显示操作页
		}
	});
	
	//获取当前签名服务器状态
	function getSignServerStatus(){
		$.ajax({
			async:false, 
			url:"get-sign-server-status",
			success:function(data){
				if(data == "1"){
					$("#runStatus").html("当前签名服务器状态:运行中");
					$("#startSignServer").hide();
					$("#stopSignServer").show();
				}else{
					$("#runStatus").html("当前签名服务器状态:暂停");
					$("#stopSignServer").hide();
					$("#startSignServer").show();
				}
			},
			type:"post",
			data:null,
			dataType:"text"
		});
	}
	
	//退出登录
	$("#loginout").click(function(){
		$.cookie("c5SignServerToken", "", { expires: -1 });
		window.location.reload();//没有登录跳转到登录页
	});
	
	$("#stopSignServer").click(function(){
		var params = {};
		params.flag = "0";
		params.sign = $("#changeSignServerStatusSign").val();
		$.ajax({
			async:false, 
			url:"change-sign-server-status",
			success:function(data){
				if(data.code == "0"){
					alert(data.message)
				}else if(data.code == "1"){
					window.location.reload();
				}else{
					alert("未知错误");
				}
			},
			data:params,
			type:"post",
			dataType:"json"
		});
	});
	
	$("#startSignServer").click(function(){
		var params = {};
		params.flag = "1";
		params.sign = $("#changeSignServerStatusSign").val();
		$.ajax({
			async:false, 
			url:"change-sign-server-status",
			success:function(data){
				if(data.code == "0"){
					alert(data.message)
				}else if(data.code == "1"){
					window.location.reload();
				}else{
					alert("未知错误");
				}
			},
			data:params,
			type:"post",
			dataType:"json"
		});
	});
	
	//点击removekey执行的操作
	function removeKey(address){
		var removeSign = $("#removeSign").val();
		var params = {};
		params.address = address;
		params.sign = removeSign;
		$.ajax({
			async:false, 
			url:"remove-private-key",
			success:function(data){
				if(data.code == "0"){
					alert(data.message);
				}else{
					//刷新页面
					window.location.reload();
				}
			},
			type:"post",
			data:params,
			dataType:"JSON"
		});
	}
	
	$("#addPrivateKey").click(function(){
		if(ADDTYPE == 1){
			//文件格式
			var keystoreFile = $("#keystoreFile")[0].files[0];
			var keystorePassword = $("#keystorePwd").val();
			var addSign = $("#addSign").val();
			
			var form = new FormData();
			form.append("file",keystoreFile);
			form.append("password",keystorePassword);
			form.append("sign",addSign);
			$.ajax({
				async:false, 
				url:"add-private-key",
				success:function(data){
					if(data.code == "0"){
						alert(data.message)
					}else{
						window.location.reload();
					}
				},
				type:"post",
				data:form,
				processData: false,
		        contentType: false,
				dataType:"JSON"
			});
		}else if(ADDTYPE == 2){
			//文本格式
			//文件格式
			var keystoreSource = $("#keystoreSource").val();
			var keystorePassword = $("#keystorePwd").val();
			var addSign = $("#addSign").val();
			
			var form = new FormData();
			form.append("keystoresource",keystoreSource);
			form.append("password",keystorePassword);
			form.append("sign",addSign);
			$.ajax({
				async:false, 
				url:"add-private-key-keystoresource",
				success:function(data){
					if(data.code == "0"){
						alert(data.message)
					}else{
						window.location.reload();
					}
				},
				type:"post",
				data:form,
				processData: false,
		        contentType: false,
				dataType:"JSON"
			});
		}
	});
	
	//点击按钮修改密码
	$("#changePassword").click(function(){
		var params = {};
		params.password = $("#changePasswordInput").val();
		params.password_re = $("#changePasswordReInput").val();
		params.sign = $("#changePasswordGoogleSign").val();
		$.ajax({
			async:false, 
			url:"change-password",
			success:function(data){
				if(data.code == "0"){
					alert(data.message)
				}else if(data.code == "1"){
					alert(data.message);
					window.location.reload();
				}
			},
			type:"post",
			data:params,
			dataType:"JSON"
		});
	});
var _isEdit = false;
var _editID = 0;
var _listJSON;
$(document).ready(function(){
	get();
});		

function get(){
	var _itemList = document.getElementById('listItems');
	$.getJSON("../expert/get.html",
    function(data){
		_listJSON = data.value; 
		_itemList.length = 0;
		$.each(data.value, function(i,item){
			  _itemList.length++;
			  _itemList.options[_itemList.length-1].value = item[0];
			  _itemList.options[_itemList.length-1].text = item[0] + "/" + item[1];	
		});
	 
    });
}

function add(){
	var error = "";
	
	if (trim($("#id").val()) ==""){
		error = "ID is null!";
		$("#id").focus();
	}else if ((!_isEdit)&&(trim($("#password").val()) =="")){
		error = "Password is null!";
		$("#password").focus();
	}else if (trim($("#password").val()) != trim($("#prepassword").val())){
		error = "Pre-password is not correct!";
		$("#password").focus();
	}else if (trim($("#name").val()) ==""){
		error = "Expert's name is null!";
		$("#name").focus();
	}
	
	if (error != ""){
		alert(error);
	}else if (_isEdit) {
			$.getJSON("../expert/edit.html", {
				id: $("#id").val(),
				name: $("#name").val(),
				pass: $("#password").val()
			}, function(data){
				if (data.error!=null){
					alert(data.error);
				}else if (data.value) {
					alert("Edit success!");
					$("#AddNew").val("Add new");
					$("#id").val("");
					$("#name").val("");
					$("#password").val("");
					$("#prepassword").val("");
					$("#id").attr("disabled",false);
					_isEdit = false;
					_editID = 0;
					get();
				}
				else {
					alert("Edit fall!");
				}
			});
		}
		else {
			$.getJSON("../expert/userExists.html", {
				id: $("#id").val()
			}, function(data){
				if (data.value){
					alert("ID is exists!");
				}else {
					$.getJSON("../expert/add.html", {
						id: $("#id").val(),
						name: $("#name").val(),
						pass: $("#password").val()
					}, function(data){
						if (data.error!=null){
							alert(data.error);
						}else if (data.value) {
							alert("Add new success!");
							$("#id").val("");
							$("#name").val("");
							$("#password").val("");
							$("#prepassword").val("");
							get();
						}
						else {
							alert("Add new fall!");
						}
					});
				}
			});
			
		}
	
}


function edit(active){
	var _itemList = document.getElementById('listItems');
	
	var INDEX = _itemList.selectedIndex;
	if (INDEX>=0){
		
		_isEdit = true;
		_editID = _listJSON[INDEX][0];
		$("#id").val(_listJSON[INDEX][0]);
		$("#name").val(_listJSON[INDEX][1]);
		$("#password").val(_listJSON[INDEX][2]);
		$("#prepassword").val(_listJSON[INDEX][2]);
		
		$("#id").attr("disabled","disabled");
		$("#AddNew").val("Update");
	}
	
	
}

function del(active){
	var _itemList = document.getElementById('listItems');
	
	var INDEX = _itemList.selectedIndex;
	if (INDEX>=0){
		if (confirm("Are you sure you want to delete?")) {
			$.getJSON("../expert/delete.html", {
				id: _itemList.options[INDEX].value
			}, function(data){
				if (data.error != null) {
					alert(data.error);
				}
				else 
					if (data.value) {
						alert("Delete success!");
						get();
					}
					else {
						alert("Delete fall!");
					}
			});
		}
	}
	

}
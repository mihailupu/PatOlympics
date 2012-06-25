var _isEdit = false;
var _editID = 0;
var _listJSON;
var timeStep =120000;
var timeDuring = 1200000;
$(document).ready(function(){
	get();
	
	var dateObj = new Date();
	
	$("#starttime").val(numberToDateStr(dateObj.getTime()));
	$("#endtime").val(numberToDateStr(dateObj.getTime()+timeDuring));
});		

function updateEndtime(){
	$("#endtime").val(numberToDateStr(dateStrToBumber($("#starttime").val())+timeDuring));
}
function get(){
	var _itemList = document.getElementById('listItems');
	$.getJSON("../round/get.html",
    function(data){
		_listJSON = data.value; 
		_itemList.length = 0;
		$.each(data.value, function(i,item){
			  _itemList.length++;
			  _itemList.options[_itemList.length-1].value = item[0];
			  _itemList.options[_itemList.length-1].text = item[1] + "/" + numberToDateStr(item[2]) + " - " + numberToDateStr(item[3]);	
		});
	 
    });
}

function add(){
	var error = "";
	var test = dateStrToBumber(trim($("#starttime").val()));
	//alert(numberToDateStr(test) );
	if (trim($("#name").val()) ==""){
		error = "Name is null!";
		$("#name").focus();
	}else if ((trim($("#starttime").val()) =="")||(dateStrToBumber($("#starttime").val())==0)){
		error = "Time start is null!";
		$("#starttime").focus();
	}else if (dateStrToBumber($("#starttime").val()) >= dateStrToBumber($("#endtime").val())){
		error = "Time end is not correct!";
		$("#endtime").focus();
	}
	
	if (error != ""){
		alert(error);
	}else if (_isEdit) {
			$.getJSON("../round/edit.html", {
				id: _editID,
				name: $("#name").val(),
				starttime: dateStrToBumber($("#starttime").val()),
				endtime: dateStrToBumber($("#endtime").val())
			}, function(data){
				if (data.error!=null){
					alert(data.error);
				}else if (data.value) {
					alert("Edit success!");
					$("#AddNew").val("Add new");
					$("#name").val("");
					$("#starttime").val(numberToDateStr(dateStrToBumber($("#endtime").val())+timeStep));
					$("#endtime").val(numberToDateStr(dateStrToBumber($("#starttime").val())+timeDuring));

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
			$.getJSON("../round/add.html", {
				name: $("#name").val(),
				starttime: dateStrToBumber($("#starttime").val()),
				endtime: dateStrToBumber($("#endtime").val())
			}, function(data){
				if (data.error!=null){
					alert(data.error);
				}else if (data.value) {
					alert("Add new success!");
					$("#id").val("");
					$("#name").val("");
					$("#starttime").val(numberToDateStr(dateStrToBumber($("#endtime").val())+timeStep));
					$("#endtime").val(numberToDateStr(dateStrToBumber($("#starttime").val())+timeDuring));
					get();
				}
				else {
					alert("Add new fall!");
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
		$("#name").val(_listJSON[INDEX][1]);
		$("#starttime").val(numberToDateStr(_listJSON[INDEX][2]));
		$("#endtime").val(numberToDateStr(_listJSON[INDEX][3]));
		
		$("#AddNew").val("Update");
	}
	
	
}

function del(active){
	var _itemList = document.getElementById('listItems');
	
	var INDEX = _itemList.selectedIndex;
	if (INDEX>=0){
		if (confirm("Are you sure you want to delete?")) {
			$.getJSON("../round/delete.html", {
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
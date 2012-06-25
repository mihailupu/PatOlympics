var _isEdit = false;
var _editID = 0;
var _listJSON;
$(document).ready(function(){
	get();
	getExpert();
        getSport();
});		

function get(){
	var _itemList = document.getElementById('listItems');
	$.getJSON("../topic/get.html",
    function(data){
		_listJSON = data.value; 
		_itemList.length = 0;
		$.each(data.value, function(i,item){
			  _itemList.length++;
			  _itemList.options[_itemList.length-1].value = item[0];
			  _itemList.options[_itemList.length-1].text = item[1] + "/" + item[3];
		});
	 
    });
}

function getExpert(){
	var _itemList = document.getElementById('listExperts');
	_itemList.length = 0;
	_itemList.length++;
	_itemList.options[_itemList.length-1].value = "";
	_itemList.options[_itemList.length-1].text = "--Select one Expert--";
		$.getJSON("../expert/get.html",
	    function(data){
			_listJSON = data.value; 
			
			$.each(data.value, function(i,item){
				  _itemList.length++;
				  _itemList.options[_itemList.length-1].value = item[0];
				  _itemList.options[_itemList.length-1].text = item[1];	
			});
		 
	    });
}

function getSport(){
	var _itemList = document.getElementById('listSports');
	_itemList.length = 0;
	_itemList.length++;
	_itemList.options[_itemList.length-1].value = "";
	_itemList.options[_itemList.length-1].text = "--Select one Sport--";
		$.getJSON("../sport/get.html",
	    function(data){
			_listJSON = data.value;

			$.each(data.value, function(i,item){
				  _itemList.length++;
				  _itemList.options[_itemList.length-1].value = item[0];
				  _itemList.options[_itemList.length-1].text = item[1];
			});

	    });
}

function add(){
	var error = "";
	if (trim($("#listExperts").val()) ==""){
		error = "Please select one Expert!";
		$("#id").focus();
	}else if (trim($("#listSports").val()) ==""){
		error = "Please select one Sport!";
		$("#id").focus();
	}else if (trim($("#id").val()) ==""){
		error = "ID is null!";
		$("#id").focus();
	}else if (trim($("#name").val()) ==""){
		error = "topic's name is null!";
		$("#name").focus();
	}
	
	if (error != ""){
		alert(error);
	}else if (_isEdit) {
		
			$.getJSON("../topic/edit.html", {
				id: $("#id").val(),
				expert: $("#listExperts").val(),
                                sport: $("#listSports").val(),
				name: $("#name").val(),
				pass: $("#desc").val()
			}, function(data){
				if (data.error!=null){
					alert(data.error);
				}else if (data.value) {
					alert("Edit success!");
					$("#AddNew").val("Add new");
					$("#id").val("");
					$("#name").val("");
					$("#desc").val("");
					$("#id").attr("disabled",false);
					_isEdit = false;
					_editID = 0;
					get();
				}
				else {
					alert("Edit fail!");
				}
			});
		}
		else {
			$.getJSON("../topic/topicExists.html", {
				id: $("#id").val()
			}, function(data){
				if (data.value){
					alert("Topic ID is exists!");
				}else {
					$.getJSON("../topic/add.html", {
						id: $("#id").val(),
						expert: $("#listExperts").val(),
                                                sport: $("#listSports").val(),
						name: $("#name").val(),
						desc: $("#desc").val()
					}, function(data){
						if (data.error!=null){
							alert(data.error);
						}else if (data.value) {
							alert("Add new success!");
							$("#id").val("");
							$("#name").val("");
							$("#desc").val("");
							get();
						}
						else {
							alert("Add new fail!");
						}
					});
				}
			});
			
		}
	
}


function edit(active){
	var _itemList = document.getElementById('listItems');
	var _expertList = document.getElementById('listExperts');
        var _sportList = document.getElementById('listSports');
	var INDEX = _itemList.selectedIndex;
	if (INDEX>=0){
		
		_isEdit = true;
		_editID = _listJSON[INDEX][0];
		$("#id").val(_listJSON[INDEX][0]);
		
		for(i=0;i<_expertList.length;i++){
			if (_expertList.options[i].value==_listJSON[INDEX][1]){
				_expertList.options[i].selected=true;
			}
		}

                for(i=0;i<_sportList.length;i++){
			if (_sportList.options[i].value==_listJSON[INDEX][2]){
				_sportList.options[i].selected=true;
			}
		}

		
		$("#name").val(_listJSON[INDEX][3]);
		$("#desc").val(_listJSON[INDEX][4]);
		
		$("#id").attr("disabled","disabled");
		$("#AddNew").val("Update");
	}
	
	
}

function del(active){
	var _itemList = document.getElementById('listItems');
	
	var INDEX = _itemList.selectedIndex;
	if (INDEX>=0){
		if (confirm("Are you sure you want to delete?")) {
			$.getJSON("../topic/delete.html", {
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
						alert("Delete fail!");
					}
			});
		}
	}
	

}
var _isEdit = false;
var _editID = 0;

$(document).ready(function(){
	get();
});		

function get(){
	var _sportList = document.getElementById('activeSports');
	var _inActiveSportList = document.getElementById('inactiveSports');
	$.getJSON("../sport/get.html",
    function(data){
	 _sportList.length = 0;
	 _inActiveSportList.length = 0;
      $.each(data.value, function(i,item){
	  	if (item[2] =="1"){
			_sportList.length++;
			_sportList.options[_sportList.length-1].value = item[0];
			_sportList.options[_sportList.length-1].text = item[1];	
		}else{
			_inActiveSportList.length++;
			_inActiveSportList.options[_inActiveSportList.length-1].value = item[0];
			_inActiveSportList.options[_inActiveSportList.length-1].text = item[1];
		}
	  	
      });
	 
    });
}

function add(){
	var active = 0;
	var error = "";
	if ($("#active").attr("checked")){
		active = 1;
	} 
	
	
	if (trim($("#sportName").val()) ==""){
		error = "Sport's name is null!";
	}
	
	if (error != ""){
		alert(error);
	}else if (_isEdit) {
		$.getJSON("../sport/edit.html", {
			id: _editID,
			name: $("#sportName").val(),
			act: active
		}, function(data){
			if (data.error!=null){
				alert(data.error);
			}else if (data.value) {
				alert("Edit success!");
				$("#AddNew").val("Add new");
				$("#sportName").val("");
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
		$.getJSON("../sport/add.html", {
			name: $("#sportName").val(),
			act: active
		}, function(data){
			if (data.error!=null){
				alert(data.error);
			}else if (data.value) {
				alert("Add new success!");
				$("#sportName").val("");
				get();
			}
			else {
				alert("Add new fall!");
			}
		});
	}
}


function edit(active){
	var _sportList = null;
	if (active==1){
		_sportList = document.getElementById('activeSports');
	}else{
		_sportList = document.getElementById('inactiveSports');
	}
	var INDEX = _sportList.selectedIndex;
	if (INDEX>=0){
		
		_isEdit = true;
		_editID = _sportList.options[INDEX].value;	
		$("#sportName").val(_sportList.options[INDEX].text);
		
		$("#active").attr("checked",active);
		$("#AddNew").val("Update");
	}
	
	
}

function del(active){
	var _sportList = null;
	if (active==1){
		_sportList = document.getElementById('activeSports');
	}else{
		_sportList = document.getElementById('inactiveSports');
	}
	var INDEX = _sportList.selectedIndex;
	if (INDEX>=0){
		//alert("DELETE FROM sports WHERE sport_id = "+_sportList.options[INDEX].value);
		if (confirm("Are you sure you want to delete?")) {
			$.getJSON("../sport/delete.html", {
				id: _sportList.options[INDEX].value
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

function moveItems(s,t,active){
	var sSourceVal = new Array(); 
	var sSourceText = new Array();
	for(i=0;i<s.length;i++){
		if (s.options[i].selected){
			t.length++;
			t.options[t.length-1].value = s.options[i].value;
			t.options[t.length-1].text = s.options[i].text;
			
			$.getJSON("../sport/edit.html", {
				id: s.options[i].value,
				name: s.options[i].text,
				act: active
			}, function(data){}
			);
		}else{
			sSourceVal.push(s.options[i].value);
			sSourceText.push(s.options[i].text);
		}
	}
	
	s.length=0;
	for(i=0;i<sSourceVal.length;i++){
		s.length++;
		s.options[s.length-1].value = sSourceVal[i];
		s.options[s.length-1].text = sSourceText[i];
	}
}
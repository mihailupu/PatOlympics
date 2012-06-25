var _isEdit = false;
var _editID = 0;
var _listJSON;
$(document).ready(function(){
	getSport();
	getRound();
	getTopic();
	getExpert();
	getTeam();
});		

function getSport(){
	var _itemList = document.getElementById('listSports');
	$.getJSON("../playing/getSport.html",
    function(data){
		_listJSON = data.value; 
		_itemList.length = 0;
		_itemList.length++;
		  _itemList.options[_itemList.length-1].value = "";
		  _itemList.options[_itemList.length-1].text = "--Select one Sport--";
		$.each(data.value, function(i,item){
			  _itemList.length++;
			  _itemList.options[_itemList.length-1].value = item[0];
			  _itemList.options[_itemList.length-1].text = item[1];	
		});
	 
    });
}

function getRound(){
	var _itemList = document.getElementById('listRounds');
	$.getJSON("../playing/getRound.html",
    function(data){
		_listJSON = data.value; 
		_itemList.length = 0;
		_itemList.length++;
		  _itemList.options[_itemList.length-1].value = "";
		  _itemList.options[_itemList.length-1].text = "--Select one Round--";
		$.each(data.value, function(i,item){
			  _itemList.length++;
			  _itemList.options[_itemList.length-1].value = item[0];
			  _itemList.options[_itemList.length-1].text = item[1];	
		});
	 
    });
}

function getTopic(){
	var _itemList = document.getElementById('listTopics');
	_itemList.length = 0;
	_itemList.length++;
	  _itemList.options[_itemList.length-1].value = "";
	  _itemList.options[_itemList.length-1].text = "--Select one Topic--";
	if (($("#listExperts").val() !=null)&&($("#listExperts").val() !="")){
		$.getJSON("../playing/getTopic.html",
			{
				expert:$("#listExperts").val()
			},
	    function(data){
			_listJSON = data.value; 
			
			$.each(data.value, function(i,item){
				  _itemList.length++;
				  _itemList.options[_itemList.length-1].value = item[0];
				  _itemList.options[_itemList.length-1].text = item[1];	
			});
		 
	    });
	}
}

function getTeam(){
	var _itemList = document.getElementById('listTeams');
	_itemList.length = 0;
	_itemList.length++;
	  _itemList.options[_itemList.length-1].value = "";
	  _itemList.options[_itemList.length-1].text = "--Select one Team--";
	if (($("#listRounds").val() !=null)&&($("#listRounds").val() !="")&&($("#listTopics").val() !="")){
		$.getJSON("../playing/getTeam.html",
			{
				round:$("#listRounds").val(),
				topic:$("#listTopics").val()
			},
	    function(data){
			_listJSON = data.value; 
			$.each(data.value, function(i,item){
				  _itemList.length++;
				  _itemList.options[_itemList.length-1].value = item[0];
				  _itemList.options[_itemList.length-1].text = item[1];	
			});
		 
	    });
	}
}

function getExpert(){
	var _itemList = document.getElementById('listExperts');
	_itemList.length = 0;
	_itemList.length++;
	_itemList.options[_itemList.length-1].value = "";
	_itemList.options[_itemList.length-1].text = "--Select one Expert--";
	if (($("#listRounds").val() !=null)&&($("#listRounds").val() !="")){
		$.getJSON("../playing/getExpert.html",
			{
				round:$("#listRounds").val(),
				topic:$("#listTopics").val()
			},
	    function(data){
			_listJSON = data.value; 
			
			$.each(data.value, function(i,item){
				  _itemList.length++;
				  _itemList.options[_itemList.length-1].value = item[0];
				  _itemList.options[_itemList.length-1].text = item[1];	
			});
		 
	    });
	}
}

function get(){
	var _itemList = document.getElementById('listItems');
	$("#sport_title").text(document.getElementById('listSports').options[document.getElementById('listSports').selectedIndex].text);
	
	$.getJSON("../playing/get.html",
		{
			sport:$("#listSports").val()
		},
    function(data){
		_listJSON = data.value; 
		_itemList.length = 0;
		$.each(data.value, function(i,item){
			  _itemList.length++;
			  _itemList.options[_itemList.length-1].value = item[0]+item[2]+item[3]+item[4];
			  _itemList.options[_itemList.length-1].text = item[6] + "/" + item[7] + "/" + item[8] + "/" + item[9];	
		});
	 
    });
}

function add(){
	var error = "";
	
	if ($("#listSports").val() ==""){
		error = "Please select one Sport!";
		$("#listSports").focus();
	}else if ($("#listRounds").val() ==""){
		error = "Please select one Round!";
		$("#listRounds").focus();
	}else if ($("#listTopics").val() ==""){
		error = "Please select one Topic!";
		$("#listTopics").focus();
	}else if ($("#listExperts").val() ==""){
		error = "Please select one Expert!";
		$("#listExperts").focus();
	}else if ($("#listTeams").val() ==""){
		error = "Please select one Team!";
		$("#listTeams").focus();
	}
	//alert($("#listSports").val() + " - " +$("#listRounds").val() + " - " +$("#listTopics").val() + " - " +$("#listExperts").val() + " - " +$("#listTeams").val() + " - " );
	if (error != ""){
		alert(error);
	}else {
			$.getJSON("../playing/add.html", {
				sport: $("#listSports").val(),
				round: $("#listRounds").val(),
				topic: $("#listTopics").val(),
				expert: $("#listExperts").val(),
				team: $("#listTeams").val(),
				sport_txt: document.getElementById('listSports').options[document.getElementById('listSports').selectedIndex].text,
				round_txt: document.getElementById('listRounds').options[document.getElementById('listRounds').selectedIndex].text,
				topic_txt: document.getElementById('listTopics').options[document.getElementById('listTopics').selectedIndex].text,
				expert_txt: document.getElementById('listExperts').options[document.getElementById('listExperts').selectedIndex].text,
				team_txt: document.getElementById('listTeams').options[document.getElementById('listTeams').selectedIndex].text
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
					getExpert();
					getTopic();
					getTeam();
				}
				else {
					alert("Add new fall!");
				}
			});
				
		}
	
}

function del(active){
	var _itemList = document.getElementById('listItems');
	
	var INDEX = _itemList.selectedIndex;
	if (INDEX>=0){
		if (confirm("Are you sure you want to delete?")) {
			$.getJSON("../playing/delete.html", {
				sport: _listJSON[INDEX][0],
				round: _listJSON[INDEX][1],
				topic: _listJSON[INDEX][2],
				expert: _listJSON[INDEX][3],
				team: _listJSON[INDEX][4]
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
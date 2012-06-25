var _listJSON;
$(document).ready(function(){
	getSport();
});		


function getSport(){
	var _itemList = document.getElementById('listSports');
	$.getJSON("../game/getSport.html",
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


function getCurrentTable(){
	$("#sport_title").text(document.getElementById('listSports').options[document.getElementById('listSports').selectedIndex].text);
	if (($("#listSports").val() != null) && ($("#listSports").val() != "")) {
		$.getJSON("../game/getCurrentRound.html", {
			sport: $("#listSports").val()
		}, function(data){
			if (data.error != null) {
				alert(data.error);
			}
			else 
				if (data.value) {
					//alert(data.value[3])
					printTable($("#listSports").val(),data.value[0], "currentTable");
					
					$("#current_round_title").text(data.value[1]);
					$("#current_starttime_title").text(numberToDateStr(data.value[2]));
					$("#current_endtime_title").text(numberToDateStr(data.value[3]));
					
					getNextTable();
				}
		});
	}
}

function getNextTable(){
	
	$.getJSON("../game/getNextRound.html", {
		sport: $("#listSports").val()
	}, function(data){
		if (data.error!=null){
			alert(data.error);
		}else if (data.value) {
			//result = getTable($("#listSports").val(),data.value[0]);
			printTable($("#listSports").val(),data.value[0], "nextTable");
			
			$("#next_round_title").text(data.value[1]);
			$("#next_starttime_title").text(numberToDateStr(data.value[2]));
			$("#next_endtime_title").text(numberToDateStr(data.value[3]));
		}
	});
}

function printTable(sportID,roundID, tableID){
	$.getJSON("../game/get.html", {
			sport: sportID,
			round: roundID
		}, function(data){
			if (data.error!=null){
				alert(data.error);
			}else{
				DataSet = data.value;
				var tempRow = "<tr><th>INDEX</th><th>TOPIC</th><th>REFEREE</th><th>TEAM</th></tr>";
		
				for (i=0;i<DataSet.length;i++){
					tempRow += "<tr class='printRow'><td>"+(i+1)+"</td>";
					tempRow += "<td>"+DataSet[i][7]+"</td>";
					tempRow += "<td>"+DataSet[i][8]+"</td>";
					tempRow += "<td>"+DataSet[i][9]+"</td></tr>";
					
				}
				
				$("#"+tableID).html(tempRow);
			}
				
		}
	);
	
	
}

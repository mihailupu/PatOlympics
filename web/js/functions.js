function trim(str, chars) {
	return ltrim(rtrim(str, chars), chars);
}
 
function ltrim(str, chars) {
	chars = chars || "\\s";
	return str.replace(new RegExp("^[" + chars + "]+", "g"), "");
}
 
function rtrim(str, chars) {
	chars = chars || "\\s";
	return str.replace(new RegExp("[" + chars + "]+$", "g"), "");
}

function _isdefined(_obj) {
	if (null == _obj) {
		return false;
	}
	if ("undefined" == typeof(_obj) ) {
		return false;
	}
	return true;
}

function pause(millis)
{
	var date = new Date();
	var curDate = null;
	
	do { curDate = new Date(); }
	while(curDate-date < millis);
} 

function numberToDateStr(dateNum){
	var dateObj = new Date(parseInt(dateNum));
	//var dateObj = new Date(1274757652860);
	
	var hours = dateObj.getHours();
	if (hours<10){
		hours = "0" + hours;
	}
	var minutes = dateObj.getMinutes();
	if (minutes<10){
		minutes = "0" + minutes;
	}
	var seconds = dateObj.getSeconds();
	if (seconds<10){
		seconds = "0" + seconds;
	}
	
	var year = dateObj.getFullYear();
	var month = dateObj.getMonth();
	if (month<10){
		month = "0" + month;
	}
	var day = dateObj.getDate();
	if (day<10){
		day = "0" + day;
	}
	
	return hours + ":" + minutes + ":" + seconds + " " + year + "-" + month + "-" + day;
}

function dateStrToBumber(dateStr){
	//dateStr = "";
	//13:30:00 2010-06-20
	var result = 0;
	if (dateStr.length==19){
		var hours = dateStr.substring(0, 2);
		var minutes = dateStr.substring(3, 5);
		var seconds = dateStr.substring(6, 8);
		
		var year = dateStr.substring(9, 13);
		var month = dateStr.substring(14, 16);
		var day = dateStr.substring(17, 19);
		
		//alert(hours + ":" + minutes + ":" + seconds + " " + year + "-" + month + "-" + day);
		var dateObj = new Date(year, month, day, hours, minutes, seconds, 0);
		result = dateObj.getTime();
	}
	return result;
}
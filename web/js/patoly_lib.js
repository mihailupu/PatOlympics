///////////////////
// Configuration //
///////////////////

// Local part of the root url of the frontend 
ROOT_URL = '/PatOlympics/';
// Local part of the URL used to access the backend
URL_PREFIX = ROOT_URL + '';
// Local part of the login form page
LOGIN_URL = ROOT_URL + 'login.html';
// Default document listing sorting
SORT_ON = 'DOCID';
// Session cookie key set by backend
SESSION_KEY = 'JSESSIONID';
// Path set for cookie set by backend
COOKIE_PATH = '/PatOlympics';

//// Configuration END ////


SERVERTIME = Date(0);
SCORE_CACHE = new Array();

function getSync(strUrl, sendData)
{
 var jsonReturn = "";
 $.ajax({url:strUrl, success:function(jsondata){jsonReturn = jsondata;}, async:false, dataType:"json", data:sendData});
 return jsonReturn;
}

function set(arr) {
  var result = {};
  for (var i = 0; i < arr.length; i++) result[arr[i]] = true;
  return result;
}

function updateCache(key, val) {
  var cache = $(document).data('cache');
  cache[key] = val;
  $(document).data('cache', cache);
}

function getCache(k, def) {
  var cache = $(document).data('cache');
  if (!cache) {
    $(document).data('cache', new Array());
    cache = $(document).data('cache');
  }
  if (cache[k]) {
    return cache[k];
  } else {
    return def;
  }
}

function cookVal() {
  return $.cookie('JSESSIONID');
}

onLoginStatus = function(cback) {
    var cook_val = cookVal();
    var session_url = URL_PREFIX + 'getUseridBySessionid';
    $.post(session_url, {'session_id': cook_val}, cback, 'json');
    //var session_url = URL_PREFIX + 'getUserName';
    //var userIDdata = $.post(session_url, {'session_id': cook_val}, cback, 'json');
    //session_url=URL_PREFIX+'getUserName';
    //$.post(session_url, {'userID': userIDdata.value}, cback, 'json');
};

function handleLogout() {
  cook_val = cookVal();
  logout_url = URL_PREFIX + 'logoutSession';
  $.post(logout_url,
         {'session_id': cook_val}, 
         function(data) { 
           $.cookie('JSESSIONID', 'fubar',{'path':COOKIE_PATH});
           redirectToLogin();
         });
  return false;
}

function getRole() {
    var o_role = getCache('role', false);
    // if (o_role=='') { handleLogout(); return false;}
    if ((o_role) && (o_role!='')) { return o_role; }
    var cook_val = cookVal();
    var role_ret = getSync(URL_PREFIX + 'getRole', {'session_id':cook_val});
    if (role_ret.value=='') { handleLogout(); return false; }
    var role = role_ret.value;
    // cache role
    updateCache('role', role);
    return role;
};

function getSports() {
  var o_sports = getCache('sports', false);
  if (o_sports) { return o_sports; }
  var sports_ret = getSync(URL_PREFIX + 'getSports', {});
  var sports = sports_ret.value;
  updateCache('sports', sports);
  return sports;
}

function getTopic() {
  var o_topic = getCache('cur_topic', false);
  if (o_topic) { return o_topic; }
  return false
}

function setTopic(topicdata) {
  updateCache('cur_topic', topicdata);
}

function redirectToLogin() {
  location.href=LOGIN_URL;
  return false;
}

function redirectToRolePage() {
  $.get(URL_PREFIX + 'getRole',  {'session_id':cookVal()},
        function(data) {
	  if (data.error) {
            // assume session broken
            handleLogout();
	    return false;
	  } else {
             if (data.value=='') { handleLogout(); return false; }
	     myrole = data.value;
             if (myrole=='TEAM' || myrole=='REFEREE') { 
               var redir_url=ROOT_URL+myrole+'.html'; 
             } else {
               var redir_url = LOGIN_URL;
             }
	     location.href=redir_url;
	     return false;
          }
	}, 'json');
  return false;
}

function displayTime() {
  $.get(URL_PREFIX+'getServerTime', {}, renderTime, 'json');
}

function renderTime(data) {
  var cur_dt = new Date(parseInt(data.value));
  var cur_hour = ((cur_dt.getHours() < 10) ? "0" : "") + cur_dt.getHours();
  var cur_min = ((cur_dt.getMinutes() < 10) ? "0" : "") + cur_dt.getMinutes();
  var cur_dt_string = (cur_dt.getDate())+'/'+(cur_dt.getMonth()+1)+'/'+(cur_dt.getFullYear())+' '+cur_hour+':'+cur_min;
  $("#timer").html(cur_dt_string);
  return false;
}

function loadIndex(data) {
    if (data.error) {
      redirectToLogin();
    } else {
      redirectToRolePage();
    }
    return false;
}

function displayTimeToNextRound() {
  $.get(URL_PREFIX + 'getNextRound', {}, 
        function(data) {
          if (data.error) {
            // END OF GAME
	    $('#timeRemaining').html('Games over');
            return false;
          }
          var ar = new Array();
          var round_data = getCache('roundInfo', ar);
	  var round_id = data.value;
	  if (!round_data[round_id]) {
	    $.get(URL_PREFIX+'getRoundInfo', {'round_id': round_id}, renderTimeToNextRound);
	  } else {
	    renderTimeToNextRound(round_data[round_id]);
	  }
        }, 
        'json');
}

function renderTimeToNextRound(data) {
  if (data) {
    var tmp = eval('('+data+')');
    var now = new Date();
    var timeEnd = new Date(parseInt(tmp.value[1]));
    var delta = timeEnd - now;
    $('#timeRemaining').html(parseInt(delta/1000/60)+' minutes until the next round');
  }
  return false;
}

function renderRoundInfo(data) {
  if (data) {
    var tmp = eval('('+data+')');
    var now = new Date();
    var timeEnd = new Date(parseInt(tmp.value[1]));
    var delta = timeEnd - now;
    var round_id = getCache('round_id', false);
    $('#timeRemaining').html(parseInt(delta/1000/60)+' minutes remaining');
    var round_data = getCache('roundInfo', new Array());
    round_data[round_id] = data;
    updateCache('roundInfo', round_data);
  }
  return false;
}

function displayRoundInfo(round_id) {
  var ar = new Array();
  var round_data = getCache('roundInfo', ar);
  updateCache('round_id', round_id);
  if (!round_data[round_id]) {
    $.get(URL_PREFIX+'getRoundInfo', {'round_id': round_id},
        renderRoundInfo);
  } else {
    renderRoundInfo(round_data[round_id]);
  }
  return false;
}

function renderTopicInfo(data) {
  if (data.error) { setRoundInactive(); return false; };
  var topic_id = getCache('topic_id', false);
  $('#roundTopic').html(data.value[0]);
  $('#roundTopicDescription').html(data.value[1]);
  var topic_data = getCache('topicInfo', new Array());
  topic_data[topic_id] = data;
  updateCache('topicInfo', topic_data);
  return false;
}

function displayRefTopicInfo() {
  var st_data = getTopic();
  if (!st_data) { return false; }
  var topic_id = st_data[1];
  var topic_data = getCache('topicInfo', new Array());
  updateCache('topic_id', topic_id);
  if (!topic_data[topic_id]) {
    $.get(URL_PREFIX+'getTopicData', {'topic_id': topic_id},
          renderTopicInfo,
          'json');
  } else {
    renderTopicInfo(topic_data[topic_id]);
  }
  return false;
}

function displayTopicInfo(userid) {
  $.get(URL_PREFIX+'getCurrentTeamTopic', {'user_id': userid},
        function(data) {
	  if (data.error) {
	    if (data.error=='ERROR_NOTOPIC') {
	      setRoundInactive();
	      return false;
	    }
          }
          var topic_data = getCache('topicInfo', new Array());
          updateCache('topic_id', data.value[0]);
          if (!topic_data[data.value[0]]) {
 	    $.get(URL_PREFIX+'getTopicData', {'topic_id': data.value[0]},
		  renderTopicInfo,
	 	  'json');
          } else {
            renderTopicInfo(topic_data[data.value[0]]);
          }
	  return false;
	},
	'json');
  return false;
}

function removeDocFromList() {
  var rmid = this.id.replace(/^docrm_/, '');
  $('.doclistItem span.button').unbind().fadeTo(0,0.2);
  //$('#docid_'+rmid).hide(300);
  $('#docid_'+rmid).fadeTo(200,0);
  var role = getRole();
  if (role=="TEAM") {
    var rmdoc_url = URL_PREFIX + 'clearTeamEntryForCurrentRound';
    var rmdoc_data = {'session_id': cookVal(), 'doc_id': rmid};
  } else {
    if (role=="REFEREE") {
      var rmdoc_url = URL_PREFIX + 'clearRefereeEntryForTopic';
      var rmdoc_data = {'session_id': cookVal(), 'topic_id': getTopic()[1], 'doc_id': rmid};
    } else {
      return false;
    }
  }
  $.post(rmdoc_url, 
         rmdoc_data,
         function(data) {
           if (data.error) {
             //$('#docid_'+rmid).show();
             $('#docid_'+rmid).fadeTo(0,1);
	     if ((data.error=='ERROR_NOTOPIC') || (data.error=='ERROR_NOSESSION') || (data.error=='ERROR_NOTASSIGNED')) {
	       // logout!
	       handleLogout();
	       return false;
	     } else {
	       if (data.error=='ERROR_NOROUND') {
	         // too late...
		 setRoundInactive();
		 return false;
	       }
	       if (data.error=='ERROR_NODOCUMENT') {
	         return false;
	       }
	     }
             $('#doclistMessageRight').html('Error removing document: '+data.error).fateTo(0,0).delay(300).fadeTo(400,1);
             return false;
           }
           $('#doclistMessageRight').html('Document has been removed').fadeTo(0,0).delay(300).fadeTo(400,1);;
           displayDocumentList();
         },
         'json');
  return false;
}

function handleClearall() {
  var role = getRole();
  if (role=="TEAM") {
    var rmall_url = URL_PREFIX + 'clearTeamDocListForCurrentRound';
    var rmall_data = {'session_id': cookVal()};
    var r_msg = "Are you really sure you want to remove all documents from the list of your team?";
  } else {
    if (role=="REFEREE") {
      var rmall_url = URL_PREFIX + 'clearRefereeDocListForTopic';
      var rmall_data = {'session_id': cookVal(), 'topic_id': getTopic()[1]};  
      var r_msg = "Are you really sure you want to remove all documents from the golden standard list of the topic?";
    } else {
      return false;
    }
  }
  var r = confirm(r_msg);
  if (r==false) { return false; }
  $('.doclistItem span.button').unbind().fadeTo(0,0.2);
  $('#doclistListing div').fadeTo(200,0);

  $.post(rmall_url,
         rmall_data,
         function(data) {
           displayDocumentList();
           return false;
         },
         'json');
  return false;
}

function handleClear() {
  var r = confirm('Do you want to clear the input area?');
  if (r==true) { $('#doclistInput').val(''); }
  return false;
}

function displayDocumentList(aftersubmit) {
  var role = getRole();
  if (role=="TEAM") {
    var doclist_url = URL_PREFIX + 'getTeamDocListForCurrentRound';
    var doclist_data = {'session_id':cookVal(), 'sort_on':SORT_ON};
  } else {
    if (role=="REFEREE") {
      var doclist_url = URL_PREFIX + 'getRefereeDocListForTopic';
      var topic_data = getTopic();
      var doclist_data = {'session_id':cookVal(), 'topic_id': topic_data[1], 'sort_on':SORT_ON};
    } else {
      return false;
    }
  }
  $.get(doclist_url, doclist_data,
          function(data) { 
          if ((data.error) && (data.error!='ERROR_NODOCUMENT')) {
	    if ((data.error=='ERROR_NOTOPIC') || (data.error=='ERROR_NOROUND')) {
	      setRoundInactive();
	      return false;
	    }
	    if ((data.error=='ERROR_NOSESSION') || (data.error=='ERROR_NOTASSIGNED') || (data.error=='ERROR_NOREFEREE') || (data.error=='ERROR_NOTEAM')) {
	      handleLogout();
	      return false;
	    }
            $('#doclistMessage').html('Error fetching documents: '+data.error);
            $('#doclistListing').html('');
            return false;
          }
          $('#doclistListing').html('');
          for (var i in data.value) {
            var cur_id = $.trim(data.value[i][0].toLowerCase());
            var cur_title = data.value[i][1];
            var cur_dt = new Date(parseInt(data.value[i][2]));
            var cur_hour = ((cur_dt.getHours() < 10) ? "0" : "") + cur_dt.getHours();
            var cur_min = ((cur_dt.getMinutes() < 10) ? "0" : "") + cur_dt.getMinutes();
            var cur_dt_string = (cur_dt.getDate())+'/'+(cur_dt.getMonth()+1)+' '+cur_hour+':'+cur_min;
            dl_elem = $('<div class="doclistItem" id="docid_'+cur_id+'"><span class="doclistItemId">'+cur_id+'</span> <span class="doclistItemTitle">'+cur_title+'</span> <span class="doclistItemDate">'+cur_dt_string+'</span> <span class="button" id="docrm_'+cur_id+'">x</span></div>');
            dl_elem.appendTo('#doclistListing');
            $('#docrm_'+cur_id).click(removeDocFromList);
            var newelem = 0;
            if (aftersubmit==1) {
              var doclist_tarea_orig = $('#doclistInput').val().split('\n');
              var doclist_tarea = $('#doclistInput').val().toLowerCase().split('\n').map($.trim);
              var f_idx = 666;
              while (f_idx > -1) {
                f_idx = $.inArray(cur_id, doclist_tarea);
                if (f_idx > -1) {
                  newelem = 1;
                  doclist_tarea_orig.splice(f_idx, 1);
                  doclist_tarea.splice(f_idx, 1);
                }
              }
              $('#doclistInput').val(doclist_tarea_orig.join('\n'));
            }
            if (newelem==1) {
              addclass = ' emph';
              $('#docid_'+cur_id).addClass('newItem');
            }
          }
          if (aftersubmit==1) { $('#doclistInput').removeAttr('disabled'); }
          return false;
	  },'json');
}

function pollStatus(role,userid) {
  $.get(URL_PREFIX+'getCurrentRound', {'user_id': userid},
        function(data) {
          displayTime();
          if (data.error) {
            setRoundInactive();
            return false;
          } else {
            //$('.doclistItem span.button').unbind().fadeTo(200,0.3);
	    setRoundActive();
	    displayRoundInfo(data.value);
	    displayTopicInfo(userid);
            displayDocumentList();
          }
        },
        'json');
}

function pollRefStatus() {
  //$('.doclistItem span.button').unbind().fadeTo(200,0.3);
  displayDocumentList();
  displayTime();
}

function setRoundActive() {
  $('#doclistInput').removeAttr('disabled');
  $('#doclistSubmit').unbind('click').click(handleTextareaSubmit).fadeTo(300, 1);
  $('#doclistClearall').unbind('click').click(handleClearall).fadeTo(300, 1);
  $('#doclistClear').unbind('click').click(handleClear).fadeTo(300, 1);
  return false;
}

function setRoundInactive() {
  $('#doclistInput').attr('disabled', 'disabled');
  $('#doclistSubmit').unbind().fadeTo(300, 0.1);
  $('#doclistClearall').unbind().fadeTo(300, 0.1);
  $('#doclistClear').unbind().fadeTo(300, 0.1);
  $('#doclistListing').fadeTo(300,0);
  $('#roundTopic').html('');
  $('#roundTopicDescription').html('');
  displayTimeToNextRound();
  return false;
}

function handleTextareaInput() {
  var el = $('#doclistInput')
  var validated = el.data('validated');
  if (!validated) { 
    validated = new Array(); 
    el.data('validated', validated);
  }
  var splat = el.val().toLowerCase().split('\n');
  var to_validate = new Array();
  var cleared = false;
  for (var idx in validated) {
    if (!(validated[idx] in set(splat))) {
      validated = new Array();
      el.data('validated', validated);
      cleared = true;
      break;
    }
  }
  for (var idx in splat) {
    if (!(splat[idx] in set(validated))) {
      if ($.trim(splat[idx]).length>6) { to_validate.push(splat[idx]);}
      validated = el.data('validated');
      validated.push(splat[idx]);
      el.data('validated', validated);
    }
  }
  if (to_validate.length > 0) {
    $.post(URL_PREFIX + 'validateDocList',
          {'doclist':to_validate.join('||')},
          function(data) {
            if (data.error) {
              return false;
            } 
            for (var idx in data.value) {
              if (data.value[idx][1]==true) {
                if (cleared) { 
                  var addfunc = function(data) { $('#inputValid').html(data);};
                } else { 
                  var addfunc = function(data) { $('#inputValid').append(data);};
                }
                addfunc('<div>'+data.value[idx][0].toLowerCase()+':'+data.value[idx][2]+'<div>');
              }
            }
            return false;
          },
          'json');
  }
}

function postSendDoclist(data) {
  if (data.error) { 
    switch (data.error) {
      case "ERROR_NOSESSION":
        handleLogout();
        return false;
      case "ERROR_NOREFEREE":
        handleLogout();
        return false;
      case "ERROR_NOTASSIGNED":
        handleLogout();
        return false;
      case "ERROR_NOTEAM":
        handleLogout();
        return false;
      case "ERROR_NOROUND":
        setRoundInactive();
        return false;
      case "ERROR_NOTOPIC":
        setRoundInactive();
        return false;
      default:
        $('#doclistMessage').html('Error adding documents: '+data.error);
    }
  } else {
    displayDocumentList(1);
    $('#doclistMessage').html(data.value+' document(s) added.').fadeTo(0,0).delay(300).fadeTo(400,1);
    $('#doclistMessageRight').fadeTo(200,0).delay(200).html('');
    $('#doclistMessageVerbose').html('Invalid document ids remain in the input field.');
  }
  return false;
}

function handleTextareaSubmit() {
  var doclist_string = $('#doclistInput').val();
  var splat = doclist_string.replace(/\n/g, '||').split('||');
  var trimmed = splat.map($.trim);
  //var doclist_fmt = doclist_string.replace(/\n/g, '||');
  var doclist_fmt = trimmed.join('||');
  var myrole = getRole();
  if (myrole=='TEAM') {
    var submit_url = URL_PREFIX + 'submitTeamDocListForCurrentRound';
    var submit_data = {'session_id': cookVal(), 'doclist':doclist_fmt};
  } else {
    var submit_url = URL_PREFIX + 'submitRefereeDocListForTopic';
    var submit_data = {'session_id': cookVal(), 'topic_id': getTopic()[1],'doclist':doclist_fmt};
  }
  $('#doclistInput').attr('disabled', 'disabled');
  $.post(submit_url, 
         submit_data,
         postSendDoclist,
         'json');
  return false;
}

function sortHandler(ev) {
  SORT_ON = this.id;
  displayDocumentList();
  return false;
}

function loadPage(data) {
    if (data.error) {
      redirectToLogin();
    } else {
        var myrole = getRole();
        $('#loginStatus').html(myrole+': '+data.value);
        $('#logoutButton').html('logout');
        $('#doclistSort span').unbind('click').click(sortHandler); 
        pollStatus(myrole, data.value);
        setInterval('pollStatus("'+myrole+'", "'+data.value+'")', 15000);
        $('#doclistSubmit').unbind('click').click(handleTextareaSubmit);
        $('#doclistClearall').unbind('click').click(handleClearall);
        $('#doclistClear').unbind('click').click(handleClear);
    }
    return false;
}

function getSportTitle(sports, id) {
  for (var i in sports) {
    if (parseInt(id)==parseInt(sports[i][0])) { return sports[i][1]; }
  }
  return false;
}

function loadRefData() {
  displayRefTopicInfo();
  return false;
}

function loadPageRef(data) {
  if (data.error) {
    redirectToLogin();
  } else {
    var myrole = getRole();
    $('#loginStatus').html(myrole+': '+data.value);
    $('#logoutButton').html('logout');
    $('#doclistSort span').unbind('click').click(sortHandler);
    $.post(URL_PREFIX + 'getRefereeTopics', {'user_id': data.value},
           function(data) {
             var sports = getSports();
             var c_top = getTopic();
             if (!c_top) {
               for (var q in data.value) {
                 c_top = new Array(q, data.value[q][0]);
                 setTopic(c_top);
                 break;
               }
             }
             $('#topicSelector').html('');
             for (var i in data.value) {
               for (var j in data.value[i]) {
                 var extra_class = '';
                 if ((i==c_top[0]) && (data.value[i][j]==c_top[1])) { extra_class = ' active'; }
                 var n_elem = $('<span class="topicButton'+extra_class+'" id="t_'+i+'_'+data.value[i][j]+'">'+getSportTitle(sports, i)+': '+data.value[i][j]+'</span>');
                 n_elem.unbind('click').click(handleSwitchTopic);
                 n_elem.appendTo($('#topicSelector'));
               }
             }
             loadRefData();
             pollRefStatus();
             setInterval('pollRefStatus()', 15000);
             $('#doclistSubmit').unbind('click').click(handleTextareaSubmit);
             $('#doclistClearall').unbind('click').click(handleClearall);
             $('#doclistClear').unbind('click').click(handleClear);
             return false;
           },
           'json'
           );

  }
  return false;
}

function handleSwitchTopic() {
  var my_id = this.id;
  var my_parts = my_id.split('_');
  var topicdata = new Array(my_parts[1], my_parts[2]);
  $('#doclistListing').html('');
  setTopic(topicdata);
  $('#topicSelector span').removeClass('active');
  $('#t_'+topicdata[0]+'_'+topicdata[1]).addClass('active');
  loadRefData();
  displayDocumentList();
  return false;
}

function sbResetRound() {
  $('.round').fadeTo(500,0);
  $('.time').fadeTo(500,0);
  return false;
}

function sbDisplayRound(data) {
  if (data.error) {
    sbResetRound();
    return false;
  }
  var round_id = data.value;
  $('#scoreRound').html(round_id);
  $('.round').fadeTo(0,1);
  $('.time').fadeTo(0,1);
  $.get(URL_PREFIX+'getRoundInfo', {'round_id': round_id},
        function(data) {
          if (data) {
            var tmp = eval('('+data+')');
            var now = new Date();
            var timeEnd = new Date(parseInt(tmp.value[1]));
            var delta = timeEnd - now;
  	    $('#timeRemaining').html(parseInt(delta/1000/60));
          }
	  return false;
	}
	);
  return false;
}

function sbRenderTable(data, sports_name, tid) {
  SCORE_CACHE[tid] = data.value;
  for (var row in data.value) {
    var i = parseInt(row)+2;
    tr_elem = $('table.'+tid+' .srow:nth-child('+i+')');
    if (tr_elem.length<=0) {
      tr_elem = $('<tr class="srow"></tr>');
      tr_elem.appendTo('table.'+tid);
    }
    tr_elem.delay(i*200).fadeTo(200,0, function() {
      var relem = $(this);
      var validx = relem.index()-1;
      var parelem = this.parentNode.parentNode;
      var tcls = $(parelem).attr('class');
      var dataset = SCORE_CACHE[tcls][validx];
      var rowstr = '<td class="team">'+dataset[0]+'</td><td class="referee">'+dataset[1]+'</td>';
      rowstr = rowstr+'<td class="referee_next">'+dataset[2]+'</td>';
      rowstr = rowstr+'<td class="reldocs">'+dataset[4]+'</td>';
      rowstr = rowstr+'<td class="user">'+dataset[5]+'</td>';
      rowstr = rowstr+'<td class="score">'+dataset[3]+'</td>';
      relem.html(rowstr);
      return false;
    });
    tr_elem.fadeTo(400,1).delay(i*200);
  }
  return false;
}

function sbDisplayScores(data) {
  if (data.error) {
    sbResetRound();
    return false;
  }
  if (data.value[0]) {
    sp0_name = data.value[0][1];
    $.get(URL_PREFIX+'getScoreboardForSport', {'sport_id': data.value[0][0]},
          function(data) {
            sbRenderTable(data, sp0_name, 'ca');
          },
          'json');
  }
  if (data.value[1]) {
    sp1_name = data.value[1][1];
    $.get(URL_PREFIX+'getScoreboardForSport', {'sport_id': data.value[1][0]},
          function(data) {
            sbRenderTable(data, sp1_name, 'clr');
          },
          'json');
  }
  return false;
}

function sbDisplayTicker(data) {
  if (data.error) {
    return false;
  }
  /*var randomnumber=Math.floor(Math.random()*11);
  if (randomnumber<0) {
    data.value = '';
  } else {
    data.value = data.value + ' ' + randomnumber.toString();
  } */
  var tck = $('.sbTicker div');
  var oldval = getCache('ticker', '');
  updateCache('ticker', data.value);
  if ($.trim(data.value)=='') {
    $('.sbTicker').fadeTo(3000, 0);
    return false;
  } else {
    if ($.trim(oldval)=='') {
      $('.sbTicker').fadeTo(1000, 1);
      $('.sbTicker').scrollLeft(0);
      tck.html(data.value);
      return false;
    }
  }
  // just in case...
  if (data.value != oldval) {
    $('.sbTicker').scrollLeft(0);
    tck.html(data.value);
  }
  return false;
} 

function updateScoreBoard() {
  $.get(URL_PREFIX+'getCurrentRound', {},
        sbDisplayRound, 'json');
  $.get(URL_PREFIX+'getSports', {},
        sbDisplayScores, 'json');
  $.get(URL_PREFIX+'getTickerMessage', {},
        sbDisplayTicker, 'json');
  return false;
}

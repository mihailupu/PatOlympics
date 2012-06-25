function loginHandler() {
      $('#loginError').fadeTo(0,0);
      username = $('#loginUsername').val();
      password = $('#loginPassword').val();
      login_url = URL_PREFIX + 'authenticateUser';
      $.post(login_url, {'username':username, 'password':password}, 
             function(data) {
               if (data.error) {
                 $('#loginError').html('<strong>Invalid credentials</strong>').fadeTo(300,1).fadeTo(600,0.6);
               } else {
                 $.get(URL_PREFIX + 'getRole',  {'session_id':$.cookie('JSESSIONID')},
                       function(data) {
                         if (data.error) {
                           $('#loginError').html('<strong>Invalid credentials</strong>').fadeTo(300,1).fadeTo(600,0.6);
                           return false;
                         } else {
                           myrole = data.value;
			   var redir_url=ROOT_URL+myrole+'.html';
			   location.href=redir_url;
			   return false;
                         }
                       },
                       'json'); 
               }
               return false;
             },
             'json');
      return false;
}

$(document).ready(function() {
    $.cookie('JSESSIONID', 'fubar',{'path':'/PatOlympics'});
    $('#loginButton').click(loginHandler);
    $('#loginForm').submit(loginHandler);
});

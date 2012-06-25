$(document).ready(function() {
    $('#logoutButton').click(handleLogout);
    onLoginStatus(loadPage);
    return false;
});

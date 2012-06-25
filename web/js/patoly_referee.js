$(document).ready(function() {
    $('#logoutButton').click(handleLogout);
    onLoginStatus(loadPageRef);
    return false;
});



var wasSuccessful = true;

function fail() {
    if (wasSuccessful) {
        $('oops').show();
        $('content').hide();
    }
    wasSuccessful = false;
}
function win() {
    if (!wasSuccessful) {
        $('oops').hide();
        $('content').show();
    }
    wasSuccessful = true;
    $('oopsContent').innerHTML = $('content').innerHTML;
}

function refreshData() {
    var updateUrl = document.location.toString() + (document.location.toString().indexOf('?') != -1 ? '&fragment=1' : '?fragment=1') + '&time=' + new Date().getTime();
    updater = new Ajax.PeriodicalUpdater(
        { success: "content" },
        updateUrl,
        {
            frequency: 5,
            onSuccess: function(response) {
                // WTF: even gets called when build server goes down!
                if (response.responseText.indexOf('GOOD_RESPONSE') != -1) {
                    win();
                } else {
                    fail();
                }
            },
            onFailure: fail,
            onException: fail
        });
    updater.start();
}

function showHideSettings() {
    var bottomForm = $('bottomForm');
    if (bottomForm.visible()) {
        bottomForm.hide();
    } else {
        bottomForm.show();
    }
}
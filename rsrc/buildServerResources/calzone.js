

var wasSuccessful = true;

function setOpacity(element, opacity) {
    if (element.filters){
        element.style.filter = 'alpha(opacity='+(opacity*100)+')';
    } else {
        element.style.opacity = opacity;
    }
}

function dissolveTo(newContent, rate) {
    if (rate >= 1) {
        // cut, don't dissolve
        $('content').innerHTML = newContent;
        return;
    }

    setOpacity($('oldContent'), 1);
    $('oldContent').innerHTML = $('content').innerHTML;
    setOpacity($('content'), 0);
    $('content').innerHTML = newContent;

    var dissolveState = 0.0;
    var dissolveIntervalId = setInterval(function(){
        dissolveState += rate;
        if (dissolveState >= 1) {
            clearInterval(dissolveIntervalId);
            setOpacity($('content'), 1);
            setOpacity($('oldContent'), 0);
            $('oldContent').innerHTML='';
        } else {
            setOpacity($('oldContent'), 1-dissolveState);
            setOpacity($('content'), dissolveState);
        }
    }, 100);
}

function fail() {
    if (wasSuccessful) {
        $('oops').show();
        $('content').hide();
    }
    wasSuccessful = false;
}
function win(dissolveRate) {
    if (!wasSuccessful) {
        $('oops').hide();
        $('content').show();
    }
    wasSuccessful = true;
    $('oopsContent').innerHTML = $('newContent').innerHTML;
    dissolveTo($('newContent').innerHTML, dissolveRate);
}

function refreshData(frequency, dissolveRate) {
    var updateUrl = document.location.toString() + (document.location.toString().indexOf('?') != -1 ? '&fragment=1' : '?fragment=1') + '&time=' + new Date().getTime();
    updater = new Ajax.PeriodicalUpdater(
        { success: "newContent" },
        updateUrl,
        {
            frequency: frequency,
            onSuccess: function(response) {
                // WTF: even gets called when build server goes down!
                if (response.responseText.indexOf('GOOD_RESPONSE') != -1) {
                    win(dissolveRate);
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
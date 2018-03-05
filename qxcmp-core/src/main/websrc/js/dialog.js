(function ($) {
    $.dialog = function (options) {

        options = options || {};

        let
            title = options.title || '',
            description = options.description || '',
            acceptButtonText = options.acceptButtonText || '是',
            denyButtonText = options.acceptButtonText || '否',
            onAccept = options.onAccept || function () {
                return true;
            },
            onDeny = options.onDeny || function () {
                return true;
            },
            closable = options.closable || true,
            size = options.size || 'mini'
        ;

        let dialog = $('<div class="ui ' + size + ' modal"><div class="header">' + title + '</div><div class="content">' + description + '</div><div class="actions"><div class="ui negative button">' + denyButtonText + '</div><div class="ui positive button">' + acceptButtonText + '</div></div></div>');

        $(dialog).appendTo($("body"));
        $(dialog).modal({
            closable: closable,
            onApprove: onAccept,
            onDeny: onDeny
        }).modal('show');
    }

}(jQuery));
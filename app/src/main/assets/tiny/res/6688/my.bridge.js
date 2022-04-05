if (my) {
    my.calls = {};
    my.call = function (api, params, callback) {
        var id = new Date().getTime()+"";
        my.calls [id] = {
            id : id,
            callback : callback
        };
        my.nativeCall(api, JSON.stringify(params), id);
    };
    my.callSync = function (api, params) {
        try {
            return JSON.parse(my.nativeCallSync(api, JSON.stringify(params)));
        } catch (e) {
            console.error(e);
        }
    }
}
function nativeCallResult(id, result) {
    if (my) {
        try {
            my.calls[id].callback(JSON.parse(result));
            delete my.calls[id];
        } catch (e) {
            console.error(e);
        }
    }
}
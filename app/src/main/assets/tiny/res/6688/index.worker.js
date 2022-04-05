function Page(page) {
    globalThis._page = page;
    if (page['data'] == undefined) {
        page.data = {}
    }

    page.setData = function (data) {
        var keys = Object.keys(data);
        for (var i = 0; i < keys.length; i++) {
            var key = keys[i];
            page.data[key] = data[key];
        }
        render.setData(JSON.stringify(data));
    }
    var methods = {};
    Object.keys(page).forEach(function (key) {
        var obj = page[key];
        if (typeof obj === "function") {
            methods[key] = {}
        }
    });
    render.init(JSON.stringify(page.data), JSON.stringify(methods));
}
function callWorker(name, params) {
    globalThis._page[name](params);
}
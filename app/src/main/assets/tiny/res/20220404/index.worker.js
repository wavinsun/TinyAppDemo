Page({
    data: {
        systemInfo: JSON.stringify(my.callSync("getSystemInfo", {}), null, 2)
    },
    onClickUpdate: function () {
        var that = this;
        my.call("getSystemInfo", {}, function (result) {
            that.setData({
                systemInfo: JSON.stringify(result, null, 2)
            });
        });
    }
});
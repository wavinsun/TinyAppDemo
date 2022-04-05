# TinyAppDemo
小程序架构示例
## 6688
小程序架构包
## 20220404
小程序示例包
### 逻辑代码
```javascript
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
```
### 页面布局
```html
<div>
    <button style="height: 40px;" v-on:click="onClickUpdate">刷新</button>
    <div><pre>{{systemInfo}}</pre></div>
</div>
```

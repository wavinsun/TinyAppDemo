function initRender(data, methods) {
  var page = {};
  page.el = '#app';
  page.data = data;
  page.methods = {};
  Object.keys(methods).forEach(function (name) {
    page.methods[name] = function (params) {
      worker.call(name, JSON.stringify(params));
    }
  });
  self.app = new Vue(page);
  document.getElementById("app").style.display = "block";
};
function setData(obj) {
  var keys = Object.keys(obj);
  keys.forEach(element => {
    self.app[element] = obj[element];
  });
};
worker.onRenderReady();
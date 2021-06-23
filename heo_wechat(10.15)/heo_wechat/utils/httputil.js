const WHITE_URL = ["user/login", "order/query", "type/all","user/info"]// 把user/info加入白名单是下策 因为其他请求要执行时候(如发布订单 要判断是否实名)self_info必须不为空 但是首次请求/user/info的时候self_info肯定是空 然后就绕死了 建议点击`我的`选项卡的时候重新获取self_info 
  const URL_HOST = 'https://clbb.looyeagee.cn/'
// const URL_HOST = 'http://localhost:7877/'

//GET请求  
function GET(uri, reqHandler) {
  request(uri, 'GET', reqHandler)
}

//POST请求  
function POST(uri, reqHandler) {
  request(uri, 'POST', reqHandler)
}

//PUT请求
function PUT(uri, reqHandler) {
  request(uri, 'PUT', reqHandler)
}

//DELETE请求
function DELETE(uri, reqHandler) {
  request(uri, 'DELETE', reqHandler)
}

function showNotInitOK(){
  wx.showModal({
    title: '错误提示',
    content: "系统还未完全加载完毕,请等几秒再试~若反复出现此提示,请尝试重启小程序~",
    showCancel: false
  })
}

function request(uri, method, reqHandler) {
  var app = getApp();
  if (app.globalData.self_info == null && WHITE_URL.indexOf(uri) == -1) {//还没登录且不属于白名单
    showNotInitOK();
    return;
  }
  var header = {
    'jwt': wx.getStorageSync('jwt')
  }
  if (method == "POST") {
    header["Content-Type"] = 'application/x-www-form-urlencoded'
  }
  //console.log(header)
  var params = reqHandler.params;
  wx.request({
    url: URL_HOST + uri,
    data: params,
    method: method,
    header: header,
    success: function (res) {
      if (res.data.errorCode == 0) {
        reqHandler.success(res);
      } else {
        if (reqHandler.hasOwnProperty("onError")) {
          reqHandler.onError(res);
        } else {//没有自定义出错的 就显示统一错误
          wx.showModal({
            title: '错误提示',
            content: res.data.msg,
            showCancel: false
          })
        }
      }
    },
    fail: function () {
      if (reqHandler.hasOwnProperty("fail")) {
        reqHandler.fail()
      }

    },
    complete: function () {
      if (reqHandler.hasOwnProperty("complete")) {
        reqHandler.complete()
      }
      wx.hideNavigationBarLoading()
    }
  })
}


module.exports = {
  GET: GET,
  POST: POST,
  PUT: PUT,
  DELETE: DELETE,
  URL_HOST: URL_HOST,
  showNotInitOK:showNotInitOK
}

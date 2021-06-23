//app.js
App({
  httputil: require("utils/httputil.js"),
  util: require("utils/util.js"),
  onLaunch: function () {
    // 展示本地存储能力
    var that = this;
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)
    setTimeout(function () { 
      that.get_jwt_init()
    }, 100) //延迟100ms以防globalData未加载
   
  },
  //用作平常登录 和 首次登录
  get_jwt_init(){
    var that = this
    var jwt_ = wx.getStorageSync('jwt');
    var exp_ = wx.getStorageSync('exp');
    if (jwt_ != null && exp_ > new Date().getTime()) {
      console.log("jwt没过期 直接获取用户信息 省去获取res.code能节约时间");
      that.globalData.jwt = jwt_;
      that.getSelfInfo()
    } else {
      console.log("jwt 过期或者不存在")
      wx.login({
        success: resCode => {
          this.httputil.GET("user/login", {
            params: {
              code: resCode.code
            },
            success: function (res) {
              console.log("获取了jwt:" + res.data.data.jwt)
              that.globalData.jwt = res.data.data.jwt
              that.globalData.self_info = res.data.data.user
              var exp = new Date().getTime() + 2419200000 //28天后
              wx.setStorageSync("jwt", res.data.data.jwt)
              wx.setStorageSync("exp", exp)
            }
          })
        }
      })
    }
  },
  //只用作更新头像
  get_jwt(userInfo) {
    var that = this
    this.globalData.userInfo = userInfo
      wx.login({
        success: resCode => {
          this.httputil.GET("user/login", {
            params: {
              code: resCode.code,
              img: userInfo.avatarUrl,
              sex: userInfo.gender,
              userName: userInfo.nickName
            },
            success: function (res) {
              that.globalData.jwt = res.data.data.jwt
              that.globalData.self_info = res.data.data.user
              var exp = new Date().getTime() + 2419200000 //28天后
              wx.setStorageSync("jwt", res.data.data.jwt)
              wx.setStorageSync("exp", exp)
            }
          })
        }
      })
   
  },
  amiRealName(h) { // 我是否实名了? 传入h回调success
    if (this.globalData.self_info === null) { //没获取到信息先挡一下
      this.httputil.showNotInitOK();
      return;
    }
    //加入没头像档一下
    if (this.globalData.self_info.sex === -1) { //没授权
      this.alert("您还没给头像和昵称授权呢,点击确定去授权~",function(){wx.navigateTo({
            url: "/pages/need_info/need_info"
         })})
      return;
    }
    if (this.globalData.self_info.hasOwnProperty("trueName")) {
      h.success()
    } else {
      wx.showModal({
        content: '您还没有实名认证呢,点击确定前往~',
        showCancel: false,
        success(res) {
          wx.navigateTo({
            url: '/pages/real_name/real_name',
          })
        }
      })
    }
  },
  getSelfInfo(h) {
    var that = this;
    this.httputil.GET("user/info", {
      params: {},
      success: function (res) {
        that.globalData.self_info = res.data.data.user;
        if (h !== undefined) //回调赋予获取到的值
          h(res.data.data.user);
        console.log("获取个人信息完成")
        console.log(res.data.data.user)
      }
    })
  },
  globalData: {
    self_info: null,
    now_order: null,
    now_order_status:0,
    type: null,
    jwt: null,
    userInfo: null,
    needFresh: false
  },
  alert(msg,h) {
    wx.showModal({
      content: msg,
      showCancel: false,
      success (res) {
        if(h!==undefined)
          h()
      }
    })
  },
  getOrderStatus(type,h){
    var that = this
    this.httputil.GET("user/orderStatus", {
      params:{type:type} ,
      success: function (res) {
      
        res.data.data.forEach(element => {
          console.log(element)
          element.pic_url = JSON.parse(element.pic_url)
        });
        h(res.data.data)
      }
  })
  }
})
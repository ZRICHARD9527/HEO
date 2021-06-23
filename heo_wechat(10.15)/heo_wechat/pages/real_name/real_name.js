const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    name:"",
    idcard:"",
    stuid:"",
    phone:"",
    second: 66,
    btnValue:'获取验证码',
    btnDisabled:false,
    code:"",
  },
  onChangeCode(e){
    this.data.code = e.detail.value;
  },
  onChangeName(e) {
    this.data.name = e.detail.value
  },
  onChangeIdcard(e) {
    this.data.idcard = e.detail.value
  },
  onChangeStuid(e) {
    this.data.stuid = e.detail.value
  },
  onChangePhone(e) {
    this.data.phone = e.detail.value
  },
  getCode(){
      var reg = /^1[3-9]\d{9}$/
      if(!reg.test(this.data.phone)){
        wx.showModal({
          title:"提示",
          showCancel:false,
          content:"请将11位中国大陆手机号输入正确"
        })
      }else{
        var that = this;
        wx.showNavigationBarLoading()
        app.httputil.GET("user/sendSms", {
          params: {
            phone:that.data.phone,
          },
          success: function (res) {
            that.timer()
          }
        })
      }
  },
  timer: function () {
    let promise = new Promise((resolve, reject) => {
      let setTimer = setInterval(
        () => {
          var second = this.data.second - 1;
          this.setData({
            second: second,
            btnValue: second+'秒',
            btnDisabled: true
          })
          if (this.data.second <= 0) {
            this.setData({
              second: 66,
              btnValue: '获取验证码',
              btnDisabled: false
            })
            resolve(setTimer)
          }
        }
        , 1000)
    })
    promise.then((setTimer) => {
      clearInterval(setTimer)
    })
  },
  submit() {
    var that = this;
    app.httputil.GET("user/realName", {
      params: {
        name: that.data.name,
        sfz:that.data.idcard,
        stuid:that.data.stuid,
        phone:that.data.phone,
        code:that.data.code,
      },
      success: function (res) {
        //记得刷新用户状态
        app.getSelfInfo()
        wx.showModal({
          title: '认证成功',
          content: res.data.msg,
          showCancel: false,
          success(res) {
            wx.navigateBack()
          }
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    app.alert("若您是评委 姓名请输入'测试'二字(不要单引号) 其他不用输入按提交即可认证")
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})
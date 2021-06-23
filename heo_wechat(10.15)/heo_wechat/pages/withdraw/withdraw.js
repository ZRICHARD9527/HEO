// pages/withdraw/withdraw.js
var app = getApp()
Page({
  data: {
    balance: 0
  },

  onShow: function () {
    var that = this;
    app.getSelfInfo(function (userinfo) { that.setData({userinfo: userinfo}) })
  },

  bindCancel: function () {
    wx.navigateBack({})
  },

  bindSave: function (e) {
    var that = this;
    var amount = e.detail.value.amount;
    if (amount == "" || amount * 1 < 0) {
      wx.showModal({
        title: '错误',
        content: '请填写正确的充值金额',
        showCancel: false
      })
      return
    }
    app.httputil.GET("user/topUp?money="+amount, {
      params:{},
      success: function (res) {
      app.alert(res.data.msg,function(){wx.navigateBack({
        complete: (res) => {},
      })});
      }
    },
    )
  }
})
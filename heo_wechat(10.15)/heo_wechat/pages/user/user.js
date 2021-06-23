// pages/user/user.js
const app = getApp()

Page({
  data: {
    userinfo: null
  },
  onShow: function () {
    var that = this;
    app.getSelfInfo(function (userinfo) { that.setData({userinfo: userinfo}) })
  },
  gotowithdraw:function(){
    wx.navigateTo({
      url: '/pages/withdraw/withdraw',
      })
  }
})
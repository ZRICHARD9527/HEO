const app = getApp()

Page({
  data: {
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    isOK:false
  },

  onUnload:function(){
    if(!this.data.isOK){
      wx.showModal({
        content:'别走呀!您还没授权头像和昵称呢!',
        showCancel:false,
        success (res) {
          wx.navigateTo({
            url: '/pages/need_info/need_info',
          })
        }
      })
    }
  },
  getUserInfo: function(e) {
    console.log(e)
    if(e.detail.hasOwnProperty("userInfo")){
      console.log("授权成功")
      this.setData({isOK:true})
      app.get_jwt(e.detail.userInfo);
      wx.navigateBack({
        delta: 1
      })
      
    }else{
      wx.showModal({
        content:'您拒绝了授权,我们只是获取一下头像和昵称而已噢~请再次点击获取头像昵称按钮~',
        showCancel:false
      })
    }
  }
})
// pages/ability/ability.js
const app=getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
      abilityList:"",
      currentPage:1,
      size:4,
      total:1
    },
    onChangePage(e) {
      this.setData({
        currentPage: e.detail.current,
      })
      this.getAbilityList()
    },
    getAbilityList(){
      var that=this;
      app.httputil.GET("/study/tag/all/4",{
      params: {"currentPage":this.data.currentPage,"size":this.data.size},
        success(res){
          console.log(res);
          var list=res.data.data.question;
          if (list == null) {
            var toastText = '获取数据失败';
            wx.showToast({
              title: toastText,
              icon: '',
              duration: 2000
            });
          }
          that.setData({
            abilityList:list,
            total:Math.ceil(res.data.data.total/4)
          })
        }
      })
    },
    todetail(e){
      var nowQuestion=JSON.stringify(this.data.abilityList[e.target.id]);
      wx.navigateTo({
        url: '/pages/question_details/question_details?nowQuestion='+nowQuestion,
      })
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
      this.getAbilityList()
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
const app = getApp()
import { $wuxDialog } from '../../wux/index'
Page({


  /**
   * 页面的初始数据
   */
  data: {
    reply_user: null,
    my_user_id: null,
    order_info: null,
    order_status:0,//0 接单时 1发布者已发布 2 发布者查看被接单 3 发布者查看已完成 4 接收者已接 5 接收者已完成
    comment: [],
    actions_reply: [{
      type: 'default',
      text: '回复TA',
    }],
    actions_del: [{
      type: 'default',
      text: '回复TA',
    }, {
      text: '删除评论',
      type: 'primary',
    }],
  },
  onAction(e) {
    var that = this
    if (e.detail.index === 1) {//删除评论
      app.httputil.DELETE("action/comment/" + e.currentTarget.dataset.comment_id, {
        params: {},
        success: function (res) {
          that.refreshComment()
          wx.showModal({
            title: '提示',
            content: res.data.msg,
            showCancel: false
          })
        },
      })
    } else {//回复
      this.data.reply_user = e.currentTarget.dataset.user_id;
      this.pub_comment()
    }
  },
  pub_comment_no_reply() {
    this.data.reply_user = null;
    this.pub_comment()
  },

  pub_comment() {
    var that = this
    $wuxDialog().prompt({
      resetOnClose: true,
      title: '提示',
      content: '输入评论内容吧',
      defaultText: '',
      onConfirm(e, response) {
        if (response.length != 0) {
          var params = { content: response };
          if (that.data.reply_user) {
            params["reply"] = that.data.reply_user
          }
          app.httputil.POST("action/comment/" + that.data.order_info.id, {
              params:params,
            success: function (res) {
              that.refreshComment()
              app.alert("发布成功")
            },
          })
        } else {
          app.alert("好像没输入东西...")
        }
      },
    })
  },
  submit() {
    app.httputil.GET("action/acceptOrder/" + this.data.order_info.id, {
      params: {},
      success: function (res) {
        wx.showModal({
          title: '提示',
          content: res.data.msg,
          showCancel: false,
          success(res) {
            app.globalData.needFresh = true
            wx.navigateBack()
          }
        })
      },
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      order_status:app.globalData.now_order_status,
      my_user_id: app.globalData.self_info.id,
      order_info: app.globalData.now_order
    })
    this.refreshComment()

  },
  refreshComment() {
    var that = this
    app.httputil.GET("action/comment/" + this.data.order_info.id, {
      params: {},
      success: function (res) {
        that.setData({
          comment: res.data.data
        })
      },
    })
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
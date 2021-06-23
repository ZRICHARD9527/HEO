// pages/my_order/my_order.js
var app = getApp();
Page({
  data: {
    current: 'tab1',
    tabs: [{
        key: 'tab1',
        title: 'Tab 1',
      },
      {
        key: 'tab2',
        title: 'Tab 2',
      },
      {
        key: 'tab3',
        title: 'Tab 3',
      },
    ],
    actions: [{
      type: 'primary',
      text: '订单详情',
    }, {
      text: '取消发布',
      type: 'primary',
    }],
    yiwancheng1: [{
      type: 'primary',
      text: '订单详情',
    }, {
      text: '确认收货',
      type: 'primary',
    }],
    beijiedan: [{
      type: 'primary',
      text: '订单详情',
    }],
    yiwancheng2: [{
      type: 'primary',
      text: '订单详情',
    }],
    order1_list: [],
    order2_list: [],
    order3_list: [],
    order_id: null,
    reset: null,
  },
  onChange(e) {
    console.log('onChange', e)
    this.setData({
      current: e.detail.key,
    })
  },

  onShow: function () {
    this.refreshOrder()
  },
  refreshOrder: function () {
    var that = this;
    app.getOrderStatus(1, function (orders) {
      orders.forEach(element => {
        element["rest"] = app.util.datestr_to_rest(element["end_date"]); //计算剩余时间
      });
      that.setData({
        order1_list: orders
      })
    })
  },

  order2() {
    var that = this;
    app.getOrderStatus(2, function (orders) {
      orders.forEach(element => {
        element["rest"] = app.util.datestr_to_rest(element["limit_date"]); //计算剩余时间
      });
      that.setData({
        order2_list: orders
      })
    })
  },

  order3() {
    var that = this;
    app.getOrderStatus(3, function (orders) {
      that.setData({
        order3_list: orders
      })
    })
  },

  cancelorder() {
    var that = this;
    app.httputil.GET("action/cancelOrder/" + this.data.order_id, {
      params: {},
      success: function (res) {
        //记得刷新订单
        that.refreshOrder()
        app.alert(res.data.msg);
      }
    })
  },

  confirmOrder() {
    var that = this;
    app.httputil.GET("action/confirmOrder/" + this.data.order_id, {
      params: {},
      success: function (res) {
        that.order3();
        app.alert(res.data.msg);
      }
    })
  },

  onAction(e) {
    var that = this
    if (e.detail.index === 1) {
      this.data.order_id = e.currentTarget.dataset.order_id
      this.cancelorder()
    } else {
      app.globalData.now_order_status = e.currentTarget.dataset.type
      console.log(e.currentTarget.dataset)
      var o = e.currentTarget.dataset.order
      console.log(o)
      app.globalData.now_order = o
      wx.navigateTo({
        url: '/pages/accept_order/accept_order',
      })
    }
  },
  onAction2(e) {
    var that = this
    if (e.detail.index === 1) {
      console.log(e)
      this.data.order_id = e.currentTarget.dataset.order.id
      this.confirmOrder()
    } else {
      app.globalData.now_order_status = e.currentTarget.dataset.type
      console.log(e.currentTarget.dataset)
      var o = e.currentTarget.dataset.order
      console.log(o)
      app.globalData.now_order = o
      wx.navigateTo({
        url: '/pages/accept_order/accept_order',
      })
    }
  }
})
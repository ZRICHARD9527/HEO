// pages/my_accept/my_accept.js
var app = getApp(); 
Page({
  data: {
      current: 'tab1',
      tabs: [
          {
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
      daiwancheng: [{
        type: 'primary',
        text: '订单详情',
      }, {
        text: '确认完成',
        type: 'primary',
      }],
      yiwancheng: [{
        type: 'primary',
        text: '订单详情',
      },],
      order1_list: [],
      order5_list: [],
  },
  onChange(e) {
      console.log('onChange', e)
      this.setData({
          current: e.detail.key,
      })
  },

  onShow:function(){
    this.refreshOrder();
  },
  refreshOrder: function () {
    var that = this;
    app.getOrderStatus(4, function (orders) {
      orders.forEach(element => {
        element["rest"] = app.util.datestr_to_rest(element["limit_date"]); //计算剩余时间
      });
      that.setData({
        order1_list: orders
      })
    })
  },

  order5() {
    var that = this;
    app.getOrderStatus(5, function (orders) {
      that.setData({
        order5_list: orders
      })
    })
  },

finishOrder(){
  var that = this;
  app.httputil.GET("action/finishOrder/"+this.data.order_id, {
  params:{},
  success: function (res) {
    //记得刷新订单
    that.refreshOrder()
    app.alert(res.data.msg);
  }
})
},

onAction(e) {
  var that = this
  if (e.detail.index === 1) {
    this.data.order_id  = e.currentTarget.dataset.order_id
    this.finishOrder()
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
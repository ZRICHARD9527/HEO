//index.js
//获取应用实例
const app = getApp()
Page({
  fab_onClick(e) {
    console.log('onClick', e.detail)
    if (e.detail.index === 0) { //发起互帮任务
      console.log("发起任务")

      var h = {
        success: function () { //认证了 才跳到发布订单
          wx.hideNavigationBarLoading();
          wx.navigateTo({
            url: '/pages/publish_order/publish_order',
          })
        }
      }
      app.amiRealName(h);
    }
  },
  //通过data里的条件查询订单
  query() {
    wx.showNavigationBarLoading()
    var that = this;
    console.log("开始查询")
    var params = {
      pageNumber: that.data.current,
      pageSize: 5 //默认页大小
    }
    if (this.data.value_type_id != 0) {
      params["type"] = this.data.value_type_id
    }
    if (this.data.value_sort_money != -1) {
      params["highToLow"] = this.data.value_sort_money
      params["orderBy"] = "order_money"
    }
    app.httputil.POST("order/query", {
      params: params,
      success: function (res) {
        console.log(res.data.data);
        res.data.data.data.forEach(element => {
          element["rest"] = app.util.datestr_to_rest(element["end_date"]); //计算剩余时间
          element["pic_url"] = JSON.parse(element["pic_url"]) // 转换json字符串成对象
        });
        that.data.page_info = res.data.data;
        that.setData({
          total: res.data.data.totalPages,
          order_list: res.data.data.data
        })
      },
      onError: function () {
        that.setData({
          total: 0,
          current: 0,
          order_list: []
        })
        wx.showToast({
          icon: "none",
          title: '此条件下没有订单!请清除筛选',
        })
      }
    })
  },
  onChangePage(e) {
    this.setData({
      current: e.detail.current,
    })
    this.query()
  },
  data: {
    current: 1,
    total: 99,
    buttons: [{
      label: '发布订单',
      icon: "/images/fabu.png"
    }],
    page_info: {},
    order_list: [],
    now_page: 1,
    value_sort_money: -1,
    value_type_id: 0,
    sort_money_desc: "-金额:默认排序",
    type_desc: "选择订单类型",
    type_array: null,
    type_index: 0
  },
  //接单
  acceptOrder(e) {
    wx.showNavigationBarLoading()
    var id = e.currentTarget.dataset.index;
    var that = this
    var h = {
      success: function () {
        wx.hideNavigationBarLoading();
        app.globalData.now_order_status = 0
        app.globalData.now_order = that.data.order_list[id]
        wx.navigateTo({
          url: '/pages/accept_order/accept_order',
        })
      }
    }
    app.amiRealName(h);
  },
  //选择订单类型
  bindPickerChange: function (e) {
    this.setData({
      current: 1,
      value_type_id: this.data.type_array[e.detail.value].id,
      type_desc: this.data.type_array[e.detail.value].typeName
    })
    this.query();
  },
  //清除筛选
  del_all() {
    this.setData({
      current: 1,
      value_sort_money: -1,
      sort_money_desc: "-金额:默认排序",
      value_type_id: 0,
      type_desc: "选择订单类型"
    })
    this.query();
  },
  //金额排序
  onOpenSortMoney() {
    console.log("更改金额排序..")
    var sort_money = null
    var sort_desc = null
    if (this.data.value_sort_money === -1) {
      sort_money = 1
      sort_desc = "↓金额:从高到低"
    } else if (this.data.value_sort_money === 1) {
      sort_money = 0
      sort_desc = "↑金额:从低到高"
    } else {
      sort_money = -1
      sort_desc = "-金额:默认排序"
    }
    this.setData({
      current: 1,
      value_sort_money: sort_money,
      sort_money_desc: sort_desc
    })
    this.query();
  },
  //事件处理函数
  bindViewTap: function () {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },

  onLoad: function () {
    var that = this
    console.log("获取类型")
    //初始化类型信息
    app.httputil.GET("type/all", {
      success: function (res) {
        app.globalData.type = res.data.data;
        that.setData({
          type_array: res.data.data
        })
      }
    })
    this.query();
  },
  //每次onShow都刷新一下订单
  onShow() {
    wx.hideNavigationBarLoading()
    if (app.globalData.needFresh) {
      this.del_all()
    }
  },
  toIndex0(){
    wx.reLaunch({
      url: '/pages/index0/index0',
    })
  }
})
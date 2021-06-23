// pages/publish_order/publish_order.js
//获取应用实例
const app = getApp()
import { $wuxToptips } from '../../wux/index'
Page({

  onChangeFile(e) {
    console.log('onChange', e)
    const { file, fileList } = e.detail
    if (file.status === 'uploading') {
      this.setData({
        progress: 0,
      })
      wx.showLoading()
    } else if (file.status === 'done') {
      this.setData({
        imageUrl: file.url,
      })
    }

    // Controlled state should set fileList
    this.setData({ fileList })
  },
  onSuccessFile(e) {
    console.log('onSuccess', e)
  },
  onFailFile(e) {
    console.log('onFail', e)
  },
  onCompleteFile(e) {
    console.log('onComplete', e)
    wx.hideLoading()
  },
  onProgressFile(e) {
    console.log('onProgress', e)
    this.setData({
      progress: e.detail.file.progress,
    })
  },
  onPreviewFile(e) {
    console.log('onPreview', e)
    const { file, fileList } = e.detail
    wx.previewImage({
      current: file.url,
      urls: fileList.map((n) => n.url),
    })
  },


  onConfirmDate(e) {
    this.setData({
      dateArray: e.detail.value,
      endDate: e.detail.label
    })
  },
  onChangeTitle(e) {
    this.data.title = e.detail.value
  },
  onChangeDetail(e) {
    this.data.detail = e.detail.value
  },
  onChangeSecret(e) {
    this.data.secret = e.detail.value
  },
  onChangeLimitHour(e) {
    this.data.limitHour = e.detail.value
  },
  onChangeMoney(e) {
    this.data.money = e.detail.value
  },
  blurLimitHour(e) {
    this.setData({ limitHour: Number(Number(e.detail.value).toFixed()) })
  },
  blurMoney(e) {
    this.setData({ money: Number(Number(e.detail.value).toFixed(1)) })
  },
  /**
   * 页面的初始数据
   */
  data: {
    fileList: [],
    minDate: null,
    maxDate: null,
    dateArray: [],
    lang: 'zh_CN',
    endDate: '请选择',
    limitHour: 12,
    title: "",
    detail: "",
    secret: "",
    money: 2,
    type_id: null,//这个是最终上传到后台的
    type_value: null,//这个是中文 展示在前台
    types_array: [],
    type_des: "请选择",
    now_select_idx: -1,//这个是记录最后选择的 不一定是最终选择 confirm后找对应的真实id给type_id 因为有confirm函数对当前的index的数据有bug 为0的时候是空字符串 可能以后会修复这个问题 为了兼容性 所以只能这么做
    header:null,
  },
  submit() {
    var errorMsg = ""
    var flag = false
    if (this.data.title.length < 3) {
      errorMsg = "订单标题长度应为3-6"
    } else if (this.data.type_id == null) {
      errorMsg = "请选择订单类型"
    }
    else if (this.data.detail.length == 0) {
      errorMsg = "请输入订单详细描述"
    } else if (this.data.endDate === "请选择") {
      errorMsg = "请选择截止接单的时间"
    } else {
      flag = true
    }
    
    var picUrlArray = []
    this.data.fileList.forEach(function(i){
      var temp = JSON.parse(i.res.data);
      if(temp.errorCode===0){
        picUrlArray.push(temp.data)
      }
    })
    console.log("已经上传的图片")
    console.log(picUrlArray)
    var that = this
    if (flag) {
      app.httputil.POST("action/submitOrder", {
        params: {
          title: that.data.title,
          typeId: that.data.type_id,
          detail: that.data.detail,
          secret: that.data.secret,
          endDate: that.data.endDate.replace(/-/g, "/"),
          limitHour: that.data.limitHour,
          orderMoney: that.data.money,
          picUrl:JSON.stringify(picUrlArray)
        },
        success: function (res) {
          wx.showModal({
            title: '发布成功',
            content: res.data.msg,
            showCancel: false,
            success(res) {
              app.globalData.needFresh = true
              wx.navigateBack()
            }
          })
        }
      })
    } else {
      this.showError(errorMsg);
    }

  },
  showError(text) {
    $wuxToptips().error({
      hidden: false,
      text: text,
      duration: 2000,
      success() { },
    })
  },
  onConfirmType(e) {
    console.log(e);
    this.setData({
      type_value: e.detail.value,
      type_id: app.globalData.type[this.data.now_select_idx].id,
      type_des: e.detail.label,
    })
  },
  onValueChangeType(e) {
    this.data.now_select_idx = e.detail.index
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var now = new Date();
    var haomiao = now.valueOf();
    var type_arr = []
    app.globalData.type.forEach(function (i) {
      type_arr.push(i.typeName)
    })
    this.setData({
      types_array: type_arr,
      minDate: app.util.dateFormat("YYYY-mm-dd HH:MM", now),
      maxDate: app.util.dateFormat("YYYY-mm-dd HH:MM", new Date(haomiao + 2592000000)),
      header : {jwt:app.globalData.jwt}
    })
    console.log(app.globalData.type)
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  }
})
const app = getApp()
Page({
    data: {
        search:"",
        list:[],
        currentPage:1,
        size:4,
        total:1
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.setData({
            search: options.search
        })
        
    },
    onChangePage(e) {
      this.setData({
        currentPage: e.detail.current,
      })
      this.getSearch()
    },
    getSearch(){
        var that=this;
        app.httputil.GET("/study/all/like",{
        params: {"content":this.data.search,"currentPage":this.data.currentPage,"size":this.data.size},
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
            list:list,
            total:Math.ceil(res.data.data.total/3)
            })
          }
        })
      },
    todetail(e){
      var nowQuestion=JSON.stringify(this.data.list[e.target.id]);
        wx.navigateTo({
          url: '/pages/question_details/question_details?nowQuestion='+nowQuestion,
        })
    },
    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {
      this.getSearch()
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
        this.setData({
            search:"",
        })
    },
})
// pages/questions/questions.js
const app = getApp()
Page({
    // options: {
    //     addGlobalClass: true,
    //   },
      addquestion(e) {
        wx.navigateTo({
          url: '/pages/addquestion/addquestion',
        })
      },
    data: {
        value: '',
        elements: [{
            id: '1',
            title: '学习',
            name: 'study',
            color: 'cyan',
            icon: 'newsfill'
          },
          {
            id: '3',
            title: '生活',
            name: 'life',
            color: 'blue',
            icon: 'colorlens'
          },
          {
            id: '2',
            title: '活动',
            name: 'activity',
            color: 'purple',
            icon: 'font'
          },
          {
            id: '4',
            title: '技能',
            name: 'ability',
            color: 'mauve',
            icon: 'icon'
          },
        ],
        list: [],
    },
    onLoad: function (options) {
      this.getHotQuestion()
    },
    getHotQuestion(){
      let that=this;
      app.httputil.GET("/study/all/fond",{
      params: {},
        success(res){
          console.log(res);
          var list=[];
          if (res.data.data == null) {
            var toastText = '获取数据失败';
            wx.showToast({
              title: toastText,
              icon: '',
              duration: 2000
            });
          }else{
           var length=(res.data.data.question.length>5? 5: res.data.data.question.length);
          for(var i=0;i<length;i++){
            list.push(res.data.data.question[i])
        }
          that.setData({
          list:list
          })
         }
        }
      })
    },
    onChange(e) {
        this.setData({
            value: e.detail.value,
        })
        
    },
    onClear(e) {
      this.setData({
            value: '',
        })
    },
    onCancel(e) {
      if(e.detail.value!=""){
      wx.navigateTo({
        url: '/pages/search_result/search result?search='+e.detail.value,
      })
    }
  },
  onShow(){
    this.getHotQuestion()
  },
    to_Detail(e){
        var nowQuestion=JSON.stringify(this.data.list[e.target.id])
        console.log(nowQuestion);
        wx.navigateTo({
          url: '/pages/question_details/question_details?nowQuestion='+nowQuestion,
        })
    },
})

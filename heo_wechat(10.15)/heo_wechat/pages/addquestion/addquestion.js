// pages/addquestion/addquestion.js
const app=getApp()
Page({
    data: {
        picker: ['学习', '活动', '生活','技能'],
        tag_id:1,
        index:0,
        textareaAValue: '',
    },
    PickerChange(e) {
        console.log(e);
        this.setData({
          index: e.detail.value,
        })
        var tag_id = parseInt(this.data.index)+1;
        this.setData({
          tag_id: tag_id,
        })
      },
    textareaAInput(e) {
      this.setData({
        textareaAValue: e.detail.value
      })
    },
    addquestion(e){
      app.httputil.POST("/study/question/"+this.data.tag_id,{
        params: {"content":this.data.textareaAValue},
          success(res){
              wx.showToast({
                title: res.data.msg,
                icon: '',
                duration: 2000
              });
              console.log(res);
              
                wx.navigateTo({
                  url: '/pages/question/question',
                })
              
            }
        })
      
      },
})
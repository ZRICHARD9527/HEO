// pages/question_details/question_details.js
const app = getApp()
Page({
    data: {
        answer:"",
        comment:"",
        type:0,
        currentPage:1,
        reply_id:0,  //发送评论数据
        nowAnswerId:1,
        size:3,
        total:1,
        question:{},//问题详情
    },
    onLoad: function (e) {
      var nowQuestion=JSON.parse(e.nowQuestion)
      this.setData({
        question: nowQuestion
      })
      this.getAnswer()
      
  },
    getAnswer(){
      var that=this;
      app.httputil.GET("/study/answer/"+this.data.question.id,{
      params: {"currentPage":this.data.currentPage,"size":this.data.size},
        success(res){
          console.log(res);
          var list=res.data.data.answer;
          if (list == null) {
            var toastText = '获取数据失败';
            wx.showToast({
              title: toastText,
              icon: '',
              duration: 2000
            });
          }
          else{
          if(res.data.data.total>0){
            that.setData({
            yuanAnswer:list,
            total:Math.ceil(res.data.data.total/3)
            })
            if(that.data.currentPage>that.data.total){
              that.setData({
                currentPage:total
              })
            }
          }
          else{
            that.setData({
              currentPage:1,
              total:res.data.data.total
            })
          }
        }
        }
      })
    },
    //删除疑问
    longPressQuestionTure(e){
      app.httputil.DELETE("/study/question/"+this.data.question.id,{
        params: {},
          success(res){
              wx.showToast({
                title: res.data.msg,
                icon: '',
                duration: 2000
              });
              console.log("删回答");
              console.log(res);
              console.log(res.data.errorcode);
              if(res.data.errorcode==0){
                wx.navigateTo({
                  url: '/pages/question/question',
                })
              };
            }
        })
    },
    longPressQuestion(){
          wx.showModal({ //使用模态框提示用户进行操作
              title:'警告',  
              content:'你将删除这条回答！',
              success(res){
                   if(res.confirm){ //判断用户是否点击了确定
                       this.longPressQuestionTure()
                   }
             }
          })
      },
    longPress(){
          wx.showModal({ //使用模态框提示用户进行操作
              title:'警告',  
              content:'你将删除这条回答！',
              success(res){
                   if(res.confirm){ //判断用户是否点击了确定
                       wx.clearStorageSync();
                   }
             }
          })
      },
    //删除回答或评论
    longPressAnswer(e){
      var that=this;
      that.setData({
        nowAnswerId: e.currentTarget.dataset.id,
      })
        wx.showModal({ //使用模态框提示用户进行操作
            title:'警告',  
            content:'你将删除这条回答！',
            success(res){
                 if(res.confirm){ //判断用户是否点击了确定
                      that.longPressAnswerTrue()
                 }
           }
        })
      },
    longPressAnswerTrue(){
      var that=this;
      app.httputil.DELETE("/study/answer/"+that.data.nowAnswerId,{
        params: {},
          success(res){
              wx.showToast({
                title: res.data.msg,
                icon: '',
                duration: 2000
              });
              that.getAnswer();
            }
        })
    },
    showModal(e) {
      var that=this;
        that.setData({
          modalName: e.currentTarget.dataset.target,
          type:e.currentTarget.dataset.type
        })
        if(e.currentTarget.dataset.type==1){
          console.log("reply");
          console.log(e.currentTarget.dataset.reply_id);
          that.setData({
            reply_id:e.currentTarget.dataset.reply_id,
          })
        }
      },
      hideModal(e) {
        if(e.currentTarget.dataset.type=='0'){
          this.setData({
            modalName: null,
            answer:'',
            reply_id:0
          })
        }else{
          this.setData({
            modalName: null,
            comment:'',
            reply_id:0
          })
        }
      },
      // 发送评论
      send(e){
        var that=this;
        console.log(that.data.type);
        if(that.data.type==0){//回答
          app.httputil.POST("/study/answer/"+that.data.question.id,{
            params: {"content":that.data.answer},
              success(res){
                  wx.showToast({
                    title: res.data.msg,
                    icon: '',
                    duration: 2000
                  });
                  if(res.data.errorCode==0){
                    that.getAnswer()
                    that.hideModal(e)
                  }
              }
            })
          }else{//评论
          app.httputil.POST("/study/answer/"+that.data.question.id,{
            params: {"reply":that.data.reply_id,"content":that.data.comment},
              success(res){
                  wx.showToast({
                    title: res.data.msg,
                    icon: '',
                    duration: 2000
                  });
                  if(res.data.errorCode==0){
                    that.getAnswer()
                    that.hideModal(e)
                  }
              }
            })
          }
      },
      my_Answer(e){
        this.setData({
            answer:e.detail.value,
        })
      },
      my_Comment(e){
        this.setData({
            comment:e.detail.value,
        })
      },
      onChangePage(e) {
        this.setData({
          currentPage: e.detail.current,
        })
        this.getAnswer()
      },
})
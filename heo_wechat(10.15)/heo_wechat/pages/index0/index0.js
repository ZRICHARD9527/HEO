// pages/index0/index0.js
const app = getApp()
Page({
  data: {
    items: [
      {
        "text": "首页",
        "iconPath": "../../images/home_.png",
        "selectedIconPath": "../../images/home.png"
      },
      {
        "text": "我的",
        "iconPath": "../../images/pub_.png",
        "selectedIconPath": "../../images/pub.png"
      }],
    value: '',
        elements: [{
            title: '学习',
            name: 'study',
            color: 'cyan',
            icon: 'newsfill'
          },
          {
            title: '生活',
            name: 'life',
            color: 'blue',
            icon: 'colorlens'
          }],
  cardCur: 0,
  swiperList: [{
    id: 0,
    type: 'image',
    url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big84000.jpg'
  }, {
    id: 1,
      type: 'image',
      url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big84001.jpg',
  }, {
    id: 2,
    type: 'image',
    url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big39000.jpg'
  }, {
    id: 3,
    type: 'image',
    url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big10001.jpg'
  }, {
    id: 4,
    type: 'image',
    url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big25011.jpg'
  }, {
    id: 5,
    type: 'image',
    url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big21016.jpg'
  }, {
    id: 6,
    type: 'image',
    url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big99008.jpg'
  }],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {
      this.towerSwiper('swiperList');
      // 初始化towerSwiper 传已有的数组名即可
    },
   

    cardSwiper(e) {
      this.setData({
        cardCur: e.detail.current
      })
    },
    // towerSwiper
    // 初始化towerSwiper
    towerSwiper(name) {
      let list = this.data[name];
      for (let i = 0; i < list.length; i++) {
        list[i].zIndex = parseInt(list.length / 2) + 1 - Math.abs(i - parseInt(list.length / 2))
        list[i].mLeft = i - parseInt(list.length / 2)
      }
      this.setData({
        swiperList: list
      })
    },
    // towerSwiper触摸开始
    towerStart(e) {
      this.setData({
        towerStart: e.touches[0].pageX
      })
    },
    // towerSwiper计算方向
    towerMove(e) {
      this.setData({
        direction: e.touches[0].pageX - this.data.towerStart > 0 ? 'right' : 'left'
      })
    },
    // towerSwiper计算滚动
    towerEnd(e) {
      let direction = this.data.direction;
      let list = this.data.swiperList;
      if (direction == 'right') {
        let mLeft = list[0].mLeft;
        let zIndex = list[0].zIndex;
        for (let i = 1; i < list.length; i++) {
          list[i - 1].mLeft = list[i].mLeft
          list[i - 1].zIndex = list[i].zIndex
        }
        list[list.length - 1].mLeft = mLeft;
        list[list.length - 1].zIndex = zIndex;
        this.setData({
          swiperList: list
        })
      } else {
        let mLeft = list[list.length - 1].mLeft;
        let zIndex = list[list.length - 1].zIndex;
        for (let i = list.length - 1; i > 0; i--) {
          list[i].mLeft = list[i - 1].mLeft
          list[i].zIndex = list[i - 1].zIndex
        }
        list[0].mLeft = mLeft;
        list[0].zIndex = zIndex;
        this.setData({
          swiperList: list
        })
      }
    },
    toUser(){
      wx.navigateTo({
        url: '/pages/user/user'
      })
   },
})

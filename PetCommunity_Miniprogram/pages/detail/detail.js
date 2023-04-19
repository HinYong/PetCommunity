const app = getApp();
var WxParse = require('../../utils/wxParse/wxParse.js');  
const util = require('../../utils/util.js')

Page({
  data: {
    product: {},
    count: 1,
  },
  /**
    * 生命周期函数--监听页面加载
    */
  onLoad: function (options) {
    let id = options.id
    this.getProduct(id)
    this.getRecomList(id)
    this.getCommentList(id)
  },

  getRecomList(id) {
    let self = this
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getRecomList',
      data: ({
        id: id
      }),
      success: (res) => {
        // console.log(res.data)
        self.setData({
          recomList: res.data
        })
      }
    })
  },

  getCommentList(id) {
    let self = this
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getCommentCountByGoodsId',
      data: ({
        id: id
      }),
      success: (res) => {
        self.setData({
          reviewCount: res.data
        })
      }
    })
  },

  getProduct(id) {
    let self = this
    wx.showLoading({
      title: '正在加载中...',
    })
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getGoodsById',
      data:({
        id:id
      }),
      success: (res) => {
        wx.hideLoading()
        self.setData({
          product: res.data
        })
      }
    })
  },

  addToTrolley() {
    let goods_id = this.data.product._id
    wx.showLoading({
      title: '正在添加到购物车...',
    })
    if (app.globalData.openid == null) {
      wx.hideLoading()
      wx.showModal({
        title: '提示',
        content: '请先登录',
        success(res) {
          if (res.confirm) {
            wx.switchTab({
              url: '../user/user',
            })
          }
        }
      })
    } else {
      wx.request({
        url: 'http://127.0.0.1:8081/wx/addToTrolley',
        data: {
          'user_openid': app.globalData.openid,
          'goods_id': goods_id
        }, success: (res) => {
          wx.hideLoading();
          wx.showToast({
            title: '已添加到购物车'
          })
        }
      })
    }
  },

  // 跳转到评论页面
  onTapCommentEntry() {
    let string = JSON.stringify(this.data.product)
    string = encodeURIComponent(string)
    wx.navigateTo({
      url: '/pages/comment/comment?data=' + string
    })
  },

  //点击我显示底部弹出框
  buyDirectly(){
    if (app.globalData.openid == null) {
      wx.hideLoading()
      wx.showModal({
        title: '提示',
        content: '请先登录',
        success(res) {
          if (res.confirm) {
            wx.switchTab({
              url: '../user/user',
            })
          }
        }
      })
    }
    else{
      this.showModal();
    }
  },

  adjustCount(e) {
    let count = this.data.count
    let adjustType = e.currentTarget.dataset.type
    if (adjustType === "add") {
      count++
    } else {
      if (count <= 1) {
        wx.showToast({
          title: '已是最小数量！',
          icon: 'none'
        })
      } else {
        // 商品数量大于1，可以减
        count--
      }
    }
    this.setData({
      count: count
    })
  },

  gotoPayment(){
    let needToPayProductList=[{
      goods: this.data.product,
      count: this.data.count
    }]
    // 转为字符串传到payment页面
    let string = JSON.stringify(needToPayProductList)
    string = encodeURIComponent(string)
    this.setData({
      showModalStatus: false
    })
    wx.navigateTo({
      url: '/pages/payment/payment?is_buyFromTrolley=0&needToPayProductList=' + string
    })
  },

  //显示对话框
  showModal: function () {
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
      showModalStatus: true,
      count: 1
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export()
      })
    }.bind(this), 200)
  },

  //隐藏对话框
  hideModal: function () {
    // 隐藏遮罩层
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
      count: 1,
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export(),
        showModalStatus: false
      })
    }.bind(this), 200)
  },
  
  // 预览图片
  showImage:function(e){
    let index = e.currentTarget.dataset.imageid
    let list = e.currentTarget.dataset.imageslist
    wx.previewImage({
      urls: list,
      current: list[index]
    })
  },

})
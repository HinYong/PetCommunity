// pages/add-comment/add-comment.js
const app = getApp()
const util = require('../../utils/util')

Page({
  onLoad: function (options) {
    let string = decodeURIComponent(options.data)
    let product = JSON.parse(string)
    let orderitem_id = options.orderitem_id
    console.log(orderitem_id)
    this.setData({
      product: product,
      orderitem_id: orderitem_id
    })
  },

  onInput(event) {
    this.setData({
      commentValue: event.detail.value.trim()
    })
  },

  addComment(event) {
    let content = this.data.commentValue;
    if (!content) return
    wx.showLoading({
      title: '正在发表评论'
    })
    wx.request({
      url: 'http://127.0.0.1:8081/wx/addComment',
      data: {
        openid: app.globalData.openid,
        goods_id: this.data.product._id,
        orderitem_id: this.data.orderitem_id,
        content: content
      },
      success: (res) => {
        wx.hideLoading()
        wx.showToast({
          title: '发表成功！'
        })
        setTimeout(() => {
          wx.navigateBack()
        }, 1500)
      }
    })
  },

})
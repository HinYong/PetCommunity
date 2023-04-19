// pages/add-comment/add-comment.js
const app = getApp()
const util = require('../../utils/util')

Page({
  onLoad: function (options) {
    let string = decodeURIComponent(options.data)
    let product = JSON.parse(string)
    let orderitem_id = options.orderitem_id
    this.setData({
      product: product,
      orderitem_id: orderitem_id
    })
    wx.request({
      url: 'http://127.0.0.1:8081/wx/checkComment',
      data: {
        orderitem_id: this.data.orderitem_id,
      },
      success: (res) => {
        let content = res.data
        this.setData({
          content
        })
      }
    })
  },

})
// pages/comment/comment.js

const util = require('../../utils/util')

Page({
  onLoad: function (options) {
    let product = this.data.product
    let string = decodeURIComponent(options.data)
    product = JSON.parse(string)
    this.setData({
      product
    })
    this.getCommentList(product._id)
  },
  getCommentList(id) {
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllCommentsByGoodsId',
      data: {
        'id': id
      }, success: (res) => {
        if (res.data==''){
          this.setData({
            commentList: []
          })
        }
        else{
          this.setData({
            commentList: res.data.map(review => {
              review.time = util.formatDate(review.time, 'yyyy/MM/dd')
              return review
            })
          })
        }
      }
   })
  }
})
// pages/pet-adopt/adopt-detail/adopt-detail.js
var app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    adopt: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // decode
    let string = decodeURIComponent(options.adopt)
    let adopt = JSON.parse(string)
    this.setData({
      adopt: adopt,
    })
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

  // 我要领养
  gotoReuqest(e){
    // 多层嵌套必须必须encode
    let string = JSON.stringify(this.data.adopt)
    string = encodeURIComponent(string)
    wx.navigateTo({
      url: '/pages/pet-adopt/request-adopt/request-adopt?adopt=' + string,
    })
  },
})
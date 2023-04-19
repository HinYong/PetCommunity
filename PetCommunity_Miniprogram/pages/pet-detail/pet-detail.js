const app = getApp();
var WxParse = require('../../utils/wxParse/wxParse.js');  
const util = require('../../utils/util.js')

Page({
  data: {
    pet: {},
  },
  /**
    * 生命周期函数--监听页面加载
    */
  onLoad: function (options) {
    let id = options.id
    let self = this
    wx.showLoading({
      title: '正在加载中...',
    })
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPetsById',
      data:({
        pet_id: id
      }),
      success: (res) => {
        wx.hideLoading()
        self.setData({
          pet: res.data
        })
      }
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

})
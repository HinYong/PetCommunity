// pages/pet-help/help-detail/help-detail.js

var app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    help: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // decode
    let string = decodeURIComponent(options.help)
    let help = JSON.parse(string)
    this.setData({
      help: help,
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
    this.setData({
      is_preview_image:true
    })
  },

  // 取消求助请求
  cancelHelp(e){
    let help_id = this.data.help.help_id
    wx.showModal({
      title: '提示',
      content: '确认要取消这个求助请求吗？',
      success:(res)=> {
        if (res.confirm) {
          wx.request({
            url: 'http://127.0.0.1:8081/wx/cancelHelp',
            data: {
              user_openid: app.globalData.openid,
              help_id: help_id,
            },
            success: (res) => {
              // 取消请求成功，返回上一页面需要将该请求“未处理”的请求列表删除，并且重新加载已取消的请求列表
              var pages = getCurrentPages(); 
              var prevPage = pages[pages.length - 2]; // 上一页
              prevPage.setData({
                cancel_help_id: help_id
              })
              wx.hideLoading()
              wx.showToast({
                title: '取消请求成功！',
                duration: 1500,
                complete: () => {
                  setTimeout(
                    ()=> {
                      wx.navigateBack({
                        delta: 1,
                      })
                    },
                    2000
                  )
                }
              })
            }
          })
        }
      }
    })
  },
})
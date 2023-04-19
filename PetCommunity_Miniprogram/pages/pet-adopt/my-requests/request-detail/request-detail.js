// pages/pet-adopt/my-requests/request-detail/request-detail.js
var app = getApp()
const util = require('../../../../utils/util')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    adopt_request: [],
    adopt: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // decode
    let string = decodeURIComponent(options.adopt_request)
    let adopt_request = JSON.parse(string)
    let adopt = adopt_request.adopt
    adopt.publish_time = util.formatTime(adopt.publish_time , 'yyyy/MM/dd/HH/mm/ss')
    if(adopt.finish_time!=null){
      adopt.finish_time = util.formatTime(adopt.finish_time , 'yyyy/MM/dd/HH/mm/ss')
    }
    adopt_request.request_time = util.formatTime(adopt_request.request_time , 'yyyy/MM/dd/HH/mm/ss')
    if(adopt_request.process_time!=null){
      adopt_request.process_time = util.formatTime(adopt_request.process_time , 'yyyy/MM/dd/HH/mm/ss')
    }
    if(adopt_request.finish_time!=null){
      adopt_request.finish_time = util.formatTime(adopt_request.finish_time , 'yyyy/MM/dd/HH/mm/ss')
    }
    if(adopt_request.refuse_time!=null){
      adopt_request.refuse_time = util.formatTime(adopt_request.refuse_time , 'yyyy/MM/dd/HH/mm/ss')
    }
    if(adopt_request.cancel_time!=null){
      adopt_request.cancel_time = util.formatTime(adopt_request.cancel_time , 'yyyy/MM/dd/HH/mm/ss')
    }
    this.setData({
      adopt_request: adopt_request,
      adopt: adopt
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

  // 取消领养申请
  cancelAdoptRequest(e){
    wx.showModal({
      title: '提示',
      content: '确认要取消这个领养申请吗？',
      success:(res)=> {
        if (res.confirm) {
          wx.request({
            url: 'http://127.0.0.1:8081/wx/cancelAdoptRequest',
            data: {
              user_openid: app.globalData.openid,
              request_id: this.data.adopt_request.request_id,
              adopt_id: this.data.adopt_request.adopt_id
            },
            success: (res) => {
              // 取消请求成功，返回上一页面需要将该请求“未处理”的请求列表删除，并且重新加载已取消的请求列表
              var pages = getCurrentPages(); 
              var prevPage = pages[pages.length - 2]; // 上一页
              prevPage.setData({
                is_cancel_adopt_request: true
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
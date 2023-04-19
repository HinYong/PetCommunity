// pages/order/order-detail/order-detail.js

const app = getApp()
var WxParse = require('../../../utils/wxParse/wxParse.js');  
const util = require('../../../utils/util.js')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    Address: null,
    openid: app.globalData.openid,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      orderid: options.orderid
    })
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getOrderById',
      data:{
        user_openid:app.globalData.openid,
        orderid:this.data.orderid
      },
      success: (res) => {
        res.data.totalPrice = util.formatPrice(res.data.totalPrice)
        if(res.data.create_time!=null){
          res.data.create_time = util.formatTime(res.data.create_time, 'yyyy/MM/dd/HH/mm/ss')
        }
        if(res.data.delivery_time!=null){
          res.data.delivery_time = util.formatTime(res.data.delivery_time, 'yyyy/MM/dd/HH/mm/ss')
        }
        if(res.data.finish_time != null){
          res.data.finish_time = util.formatTime(res.data.finish_time, 'yyyy/MM/dd/HH/mm/ss')
        }
        this.setData({
          order: res.data
        })
        console.log(res.data)
        wx.request({
          url: 'http://127.0.0.1:8081/wx/getUserAddressById',
          data:{
            openid: app.globalData.openid,
            address_id: this.data.order.address_id
          },
          success: (res) => {
            console.log(res.data)
            this.setData({
              Address: res.data
            })
          }
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getOrderById',
      data:{
        user_openid:app.globalData.openid,
        orderid:this.data.orderid
      },
      success: (res) => {
        res.data.totalPrice = util.formatPrice(res.data.totalPrice)
        if(res.data.create_time!=null){
          res.data.create_time = util.formatTime(res.data.create_time, 'yyyy/MM/dd/HH/mm/ss')
        }
        if(res.data.delivery_time!=null){
          res.data.delivery_time = util.formatTime(res.data.delivery_time, 'yyyy/MM/dd/HH/mm/ss')
        }
        if(res.data.finish_time != null){
          res.data.finish_time = util.formatTime(res.data.finish_time, 'yyyy/MM/dd/HH/mm/ss')
        }
        if(res.data.cancel_time != null){
          res.data.cancel_time = util.formatTime(res.data.cancel_time, 'yyyy/MM/dd/HH/mm/ss')
        }
        this.setData({
          order: res.data
        })
        wx.request({
          url: 'http://127.0.0.1:8081/wx/getUserAddressById',
          data:{
            openid: app.globalData.openid,
            address_id: this.data.order.address_id
          },
          success: (res) => {
            this.setData({
              Address: res.data
            })
          }
        })
      }
    })
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  // 确认收货
  gotoConfirm(e){
    let orderid = this.data.order.id
    wx.showModal({
      title: '提示',
      content: '确认要收货吗？',
      success:(res)=> {
        if (res.confirm) {
          wx.request({
            url: 'http://127.0.0.1:8081/wx/userConfirmOrder',
            data: {
              user_openid: app.globalData.openid,
              orderid: orderid,
            },
            success: (res) => {
              wx.hideLoading()
              wx.showToast({
                title: '确认收货成功！',
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
        else{
          this.setData({
            isConfirmBtn: false
          })
        }
      }
    })
  },

  // 取消订单
  gotoCancel(e){
    let order_id = this.data.order.id
    this.setData({
      isConfirmBtn: true
    })
    wx.showModal({
      title: '提示',
      content: '确认要取消这个订单吗？',
      success:(res)=> {
        if (res.confirm) {
          wx.request({
            url: 'http://127.0.0.1:8081/wx/userCancelOrder',
            data: {
              user_openid: app.globalData.openid,
              order_id: order_id,
            },
            success: (res) => {
              wx.hideLoading()
              wx.showToast({
                title: '取消订单成功！',
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
              this.setData({
                isConfirmBtn: false
              })
            }
          })
        }
        else{
          this.setData({
            isConfirmBtn: false
          })
        }
      }
    })
  },

  // 评论
  goComment(event) {
    let orderitem_id = event.currentTarget.dataset.id
    let string = JSON.stringify(event.currentTarget.dataset.item)
    string = encodeURIComponent(string)
    wx.navigateTo({
      url: '/pages/add-comment/add-comment?orderitem_id=' + orderitem_id + '&data=' + string,
    })
  },

  // 查看评价
  checkComment(event) {
    let orderitem_id = event.currentTarget.dataset.id
    let string = JSON.stringify(event.currentTarget.dataset.item)
    string = encodeURIComponent(string)
    wx.navigateTo({
      url: '/pages/check-comment/check-comment?orderitem_id=' + orderitem_id + '&data=' + string,
    })
  },

  // 退货申请
  gotoApplyReturn(e){
    let string = JSON.stringify(e.currentTarget.dataset.item)
    string = encodeURIComponent(string)
    wx.navigateTo({
      url: '/pages/order/order-return-apply/order-return-apply?orderitem=' + string
    })
  },

  // 退货详情
  gotoReturnDetail(e){
    let string = JSON.stringify(e.currentTarget.dataset.item)
    string = encodeURIComponent(string)
    wx.navigateTo({
      url: '/pages/order/order-return-detail/order-return-detail?orderitem=' + string
    })
  }

})
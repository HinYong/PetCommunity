// pages/order/order.js

const app = getApp()
const util = require('../../utils/util')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: null,
    orderList: [], // 订单列表
    notDeliveryList:[], // 未发货
    deliveryList:[], // 已发货
    finishList:[], // 已完成
    cancelList:[], // 已取消
    isConfirmBtn: false, // 确认收货按钮是否被激活
    navbar: ['全部订单', '未发货', '已发货', '已完成', '已取消'],
    currentTab: 0,
  },

  onShow() {
    wx.showLoading({
      title: '刷新订单数据...',
    })
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllOrders',
      data: {
        openid: app.globalData.openid
      },
      success: (res) => {
        wx.hideLoading()
        let orderList = res.data
        this.setData({
          orderList: res.data,
          notDeliveryList: [],
          deliveryList: [],
          finishList: [],
          cancelList:[]
        })
        let notDeliveryList = this.data.notDeliveryList
        let deliveryList = this.data.deliveryList
        let finishList = this.data.finishList
        let cancelList = this.data.cancelList
        for(var i=0;i<orderList.length;i++){
          orderList[i].totalPrice = util.formatPrice(orderList[i].totalPrice)
          if(orderList[i].status==1){
            notDeliveryList.splice(notDeliveryList.length, 1, orderList[i])
          }
          else if(orderList[i].status==2){
            deliveryList.splice(deliveryList.length, 1, orderList[i])
          }
          else if(orderList[i].status==3){
            finishList.splice(finishList.length, 1, orderList[i])
          }
          else{
            cancelList.splice(cancelList.length, 1, orderList[i])
          }
        }
        this.setData({
          orderList: res.data,
          notDeliveryList: notDeliveryList,
          deliveryList: deliveryList,
          finishList: finishList,
          cancelList: cancelList
        })
        console.log(res.data)
      }
    })
  },

  onPullDownRefresh(){
    wx.showLoading({
      title: '刷新订单数据...',
    })
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllOrders',
      data: {
        openid: app.globalData.openid
      },
      success: (res) => {
        wx.hideLoading()
        let orderList = res.data
        this.setData({
          orderList: res.data,
          notDeliveryList: [],
          deliveryList: [],
          finishList: [],
          cancelList:[]
        })
        let notDeliveryList = this.data.notDeliveryList
        let deliveryList = this.data.deliveryList
        let finishList = this.data.finishList
        let cancelList = this.data.cancelList
        for(var i=0;i<orderList.length;i++){
          orderList[i].totalPrice = util.formatPrice(orderList[i].totalPrice)
          if(orderList[i].status==1){
            notDeliveryList.splice(notDeliveryList.length, 1, orderList[i])
          }
          else if(orderList[i].status==2){
            deliveryList.splice(deliveryList.length, 1, orderList[i])
          }
          else if(orderList[i].status==3){
            finishList.splice(finishList.length, 1, orderList[i])
          }
          else{
            cancelList.splice(cancelList.length, 1, orderList[i])
          }
        }
        this.setData({
          orderList: res.data,
          notDeliveryList: notDeliveryList,
          deliveryList: deliveryList,
          finishList: finishList,
          cancelList: cancelList
        })
        console.log(res.data)
      }
    })
  },

  // 切换顶部bar
  navbarTap: function(e){
    this.setData({
      currentTab: e.currentTarget.dataset.idx
    })
  },

  // 确认收货
  gotoConfirm(e){
    let orderid = e.currentTarget.dataset.id
    this.setData({
      isConfirmBtn: true
    })
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
                      this.onShow()
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

  // 取消订单
  gotoCancel(e){
    let order_id = e.currentTarget.dataset.id
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
                      this.onShow()
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

  // 订单详情页
  gotoDetail(e){
    if(!this.data.isConfirmBtn){
      console.log(e.currentTarget.dataset.id)
      wx.navigateTo({
        url: '/pages/order/order-detail/order-detail?orderid=' + e.currentTarget.dataset.id
      })
    }
  },

  // 退货详情页
  gotoReturnDetail(e){
    let string = JSON.stringify(e.currentTarget.dataset.item)
    string = encodeURIComponent(string)
    wx.navigateTo({
      url: '/pages/order/order-return-detail/order-return-detail?orderitem=' + string
    })
  },

  // 评论
  goComment(event) {
    let orderitem_id = event.currentTarget.dataset.id
    let string = JSON.stringify(event.currentTarget.dataset.item)
    string = encodeURIComponent(string)
    wx.navigateTo({
      url: '/pages/add-comment/add-comment?orderitem_id=' + orderitem_id + '&data=' + string
    })
  },

  // 查看评价
  checkComment(event) {
    let orderitem_id = event.currentTarget.dataset.id
    let string = JSON.stringify(event.currentTarget.dataset.item)
    string = encodeURIComponent(string)
    wx.navigateTo({
      url: '/pages/check-comment/check-comment?orderitem_id=' + orderitem_id + '&data=' + string
    })
  },

})
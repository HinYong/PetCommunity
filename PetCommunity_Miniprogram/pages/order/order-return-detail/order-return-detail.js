// pages/order/order-return-detail/order-return-detail.js

const app = getApp()
var WxParse = require('../../../utils/wxParse/wxParse.js');  
const util = require('../../../utils/util.js')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    Address:{
      receiver_name: "猫狗小站官方售后",
      receiver_phone: "88888888",
      province: "广东省",
      city: "深圳市",
      area: "福田区",
      detail_address: "皇庭广场A座"
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let string = decodeURIComponent(options.orderitem)
    let orderitem = JSON.parse(string)
    this.setData({
      orderitem: orderitem
    })
    console.log(this.data.orderitem)
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getReturnItemByReturnId',
      data:{
        user_openid: app.globalData.openid,
        return_id: this.data.orderitem.return_id
      },
      success: (res) => {
        res.data.return_apply_time = util.formatTime(res.data.return_apply_time, 'yyyy/MM/dd/HH/mm/ss')
        if(res.data.return_finish_time!=null){
          res.data.return_finish_time = util.formatTime(res.data.return_finish_time, 'yyyy/MM/dd/HH/mm/ss')
        }
        this.setData({
          returnitem: res.data
        }) 
      }
    })
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },
})
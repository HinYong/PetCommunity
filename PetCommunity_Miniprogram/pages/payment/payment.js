// pages/payment/payment.js

const app = getApp()
var WxParse = require('../../utils/wxParse/wxParse.js');  
const util = require('../../utils/util.js')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    defaultAddress: null,
    openid: app.globalData.openid
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // 将传过来的字符串转码为json格式数据
    let string = decodeURIComponent(options.needToPayProductList)
    let list = JSON.parse(string)
    let TotalPrice = 0
    let is_buyFromTrolley = options.is_buyFromTrolley  // 是否是在购物车中购买，0代表不是，1代表是
    this.setData({
      is_buyFromTrolley: is_buyFromTrolley
    })
    for(var i = 0; i < list.length; i++){
      TotalPrice+= list[i].goods.price * list[i].count;
    }
    TotalPrice = util.formatPrice(TotalPrice)
    // console.log(list)
    // console.log(TotalPrice)
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getDefaultUserAddress',
      data:{
        openid:app.globalData.openid
      },
      success: (res) => {
        // console.log(res.data)
        this.setData({
          defaultAddress: res.data
        })
        if(this.data.defaultAddress==''){
          this.setData({
            haveAddress: false
          })
        }
        else{
          this.setData({
            haveAddress: true
          })
        }
      }
    })
    this.setData({
      needToPayProductList: list,
      openid: app.globalData.openid,
      TotalPrice:TotalPrice
    })    
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    // 获取所选择的地址
    let pages = getCurrentPages();
    let currPage = pages[pages.length-1];
    // console.log(currPage.data.selAddress);
    // 不是从地址选择列表navigateback的情况
    if(currPage.data.selAddress==null){
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getDefaultUserAddress',
        data:{
          openid:app.globalData.openid
        },
        success: (res) => {
          // console.log(res.data)
          this.setData({
            defaultAddress: res.data
          })
          if(this.data.defaultAddress==''){
            this.setData({
              haveAddress: false
            })
          }
          else{
            this.setData({
              haveAddress: true
            })
          }
        }
      })
    }
    else{
      this.setData({
        defaultAddress: currPage.data.selAddress
      })
    }
    
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  changeAddress(){
    if(this.data.haveAddress){
      wx.navigateTo({
        url: '/pages/payment/select-address/select-address',
      })
    }
    else{
      wx.navigateTo({
        url: '/pages/address/create-new-address/create-new-address?flag=0',
      })
    }
  },

  onTapPay() {
    wx.showModal({
      title: '提示',
      content: '确认支付？',
      success:(res)=> {
        if (res.confirm) {
          wx.showLoading({
            title: '正在支付...',
          })
          let string = JSON.stringify(this.data.needToPayProductList)
          let url
          // 从购物车购买则请求购物车的端口
          if(this.data.is_buyFromTrolley==1){
            console.log("buyFromTrolley")
            url = 'http://127.0.0.1:8081/wx/buyFromTrolley'
          }
          // 从详情页直接购买则请求直接购买的端口
          else{
            console.log("notbuyFromTrolley")
            url = 'http://127.0.0.1:8081/wx/buyDirectly'
          }
          wx.request({
            url: url,
            method: 'post',
            // 注意这里方法为post时header的Content-Type要设置为application/x-www-form-urlencoded，否则默认是json，会报400错误
            header:{
              "Content-Type": "application/x-www-form-urlencoded"
            },
            // 注意这里data中的list有嵌套，所有有嵌套的都必须先用JSON.stringify转为字符串
            data: {
              list: string,
              openid: app.globalData.openid,
              addressid: this.data.defaultAddress.id
            },
            success: (res) => {
              wx.hideLoading()
              wx.showToast({
                title: "支付成功！",
                duration: 2000,
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
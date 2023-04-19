// pages/trolley/trolley.js

const app = getApp()
const util = require('../../utils/util')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    length: 0,
    userInfo: null,
    trolleyList: [], // 购物车商品列表
    trolleyCheckMap: {}, // 购物车中选中的id哈希表
    trolleyAccount: 0, // 购物车结算总价
    isTrolleyEdit: false, // 购物车是否处于编辑状态
    isTrolleyTotalCheck: false, // 购物车中商品是否全选
  },

  onShow() {
    if (app.globalData.openid != null) {
      this.setData({
        userInfo: app.globalData.userInfo,
        isTrolleyTotalCheck: false,  // 全选按钮设为false
        trolleyCheckMap: {}, // 重新加载的时候，购物车中选中的id哈希表置为空
        isTrolleyEdit: false, // 购物车置为非编辑状态
        trolleyAccount: 0, // 购物车结算总价清零
      })
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getTrolley',
        data: {
          openid: app.globalData.openid
        },
        success: (res) => {
          this.setData({
            trolleyAccount: util.formatPrice(0),
            trolleyList: res.data,
            length: res.data.length
          })
        }
      })
    }
  },

  onTapLogin(event) {
    if (app.globalData.openid == null) {
      wx.getUserInfo({
        success: res => {
          app.globalData.userInfo = res.userInfo
          wx.login({
            success: login_res => {
              // 发送 res.code 到后台换取 openId, sessionKey, unionId
              wx.request({
                url: 'http://127.0.0.1:8081/wx/login',
                data: {
                  code: login_res.code,
                  rawData: res.rawData, //用户非敏感信息
                  signature: res.signature, //签名
                  encrypteData: res.encryptedData, //用户敏感信息
                  iv: res.iv //解密算法的向量
                },
                success: (res) => {
                  app.globalData.openid = res.data.msg;
                  this.onShow()
                }
              })
            }
          })
        }
      })
    }
  },

  onTapCheckSingle(event) {
    let checkId = event.currentTarget.dataset.id
    let trolleyCheckMap = this.data.trolleyCheckMap
    let trolleyList = this.data.trolleyList
    let isTrolleyTotalCheck = this.data.isTrolleyTotalCheck
    let trolleyAccount = this.data.trolleyAccount
    // 单项商品被选中/取消
    trolleyCheckMap[checkId] = !trolleyCheckMap[checkId];  // 哈希表中取反
    isTrolleyTotalCheck = true  // 将全选按钮置为true
    // 遍历，若有商品编号不在哈希表中，则将全选按钮置为false
    trolleyList.forEach(product => {
      if (!trolleyCheckMap[product.goods_id]) {
        isTrolleyTotalCheck = false
      }
    });
    trolleyAccount = this.calcAccount(trolleyList, trolleyCheckMap);
    this.setData({
      trolleyCheckMap,
      isTrolleyTotalCheck,
      trolleyAccount
    })
  },

  onTapCheckTotal() {
    let trolleyCheckMap = this.data.trolleyCheckMap
    let trolleyList = this.data.trolleyList
    let isTrolleyTotalCheck = this.data.isTrolleyTotalCheck
    let trolleyAccount = this.data.trolleyAccount

    // 全选按钮被选中/取消
    isTrolleyTotalCheck = !isTrolleyTotalCheck

    // 遍历并修改所有商品的状态
    trolleyList.forEach(product => {
      trolleyCheckMap[product.goods_id] = isTrolleyTotalCheck
    })
    trolleyAccount = this.calcAccount(trolleyList, trolleyCheckMap)
    this.setData({
      isTrolleyTotalCheck,
      trolleyCheckMap,
      trolleyAccount
    })

  },

  calcAccount(trolleyList, trolleyCheckMap) {
    let account = 0
    trolleyList.forEach(product => {
      account = trolleyCheckMap[product.goods_id] ? account + product.goods.price * product.count : account
    })
    return util.formatPrice(account)
  },

  onTapEdit() {
    let isTrolleyEdit = this.data.isTrolleyEdit
    if (isTrolleyEdit) {
      this.updateTrolley()
    } else {
      this.setData({
        isTrolleyEdit: !isTrolleyEdit
      })
    }
  },

  onTapDelete() {
    if (this.data.trolleyAccount==0){
      wx.showToast({
        title: '您未选择商品',
        icon: 'none'
      })
    }
    else{
      let trolleyCheckMap = this.data.trolleyCheckMap
      let trolleyList = this.data.trolleyList
      let index
      // 遍历已选中的物品
      for(var sel_id in trolleyCheckMap){
        // 从trolleyList取出选中的商品
        for (index = 0; index < trolleyList.length; index++) {
          if (sel_id == trolleyList[index].goods_id) {
            break
          }
        }
        delete trolleyCheckMap[sel_id]  // 从选中的map中删除
        trolleyList.splice(index, 1)  // 从trolleyList中删除
      }
      this.setData({
        trolleyList,
        trolleyCheckMap
      })
      this.updateTrolley()
    }
  },

  adjustTrolleyCount(event) {
    let trolleyCheckMap = this.data.trolleyCheckMap
    let trolleyList = this.data.trolleyList
    let dataset = event.currentTarget.dataset
    let adjustType = dataset.type
    let productId = dataset.id
    let product
    let index
    // 从trolleyList取出对应的商品
    for (index = 0; index < trolleyList.length; index++) {
      if (productId === trolleyList[index].goods_id) {
        product = trolleyList[index]
        break
      }
    }
    if (product) {
      if (adjustType === "add") {
        product.count++
      } else {
        if (product.count <= 1) {
          // 商品数量不超过1，点击减号相当于删除
          delete trolleyCheckMap[productId]  // 从存储选中项目的map中删除
          trolleyList.splice(index, 1)  // 从购物车中删除
        } else {
          // 商品数量大于1
          product.count--
        }
      }
    }
    // 调整结算总价
    let trolleyAccount = this.calcAccount(trolleyList, trolleyCheckMap)
    this.setData({
      trolleyAccount,
      trolleyList,
      trolleyCheckMap
    })
  },

  updateTrolley() {
    wx.showLoading({
      title: '更新购物车数据...',
    })
    console.log(this.data.trolleyList)
    let string = JSON.stringify(this.data.trolleyList)
    wx.request({
      url: 'http://127.0.0.1:8081/wx/updateTrolley',
      method: 'post',
      // 注意这里方法为post时header的Content-Type要设置为application/x-www-form-urlencoded，否则默认是json，会报400错误
      header:{
        "Content-Type": "application/x-www-form-urlencoded"
      },
      // 注意这里data中的list有嵌套，所有有嵌套的都必须先用JSON.stringify转为字符串
      data: {
        list: string,
        openid:app.globalData.openid,
      },
      success: (res) => {
        wx.hideLoading()
        wx.showToast({
          title: '更新购物车成功'
        })
        this.onShow()
      }
    })
    this.setData({
      isTrolleyEdit: false
    })
  },

  onTapPay() {
    let self = this
    if (this.data.trolleyAccount==0){
      wx.showToast({
        title: '您未选择商品',
        icon: 'none'
      })
    }
    else{
      let trolleyCheckMap = this.data.trolleyCheckMap
      let trolleyList = this.data.trolleyList
      // 提取出选中的商品
      let needToPayProductList = trolleyList.filter(product => {
        return !!trolleyCheckMap[product.goods_id]
      })
      console.log(needToPayProductList)
      // 转为字符串传到payment页面
      let string = JSON.stringify(needToPayProductList)
      string = encodeURIComponent(string)
      wx.navigateTo({
        url: '/pages/payment/payment?is_buyFromTrolley=1&needToPayProductList=' + string
      })
    }
  },

  gotoDetail(e){
    if(!this.data.isTrolleyEdit){
      wx.navigateTo({
        url: '/pages/detail/detail?id=' + e.currentTarget.dataset.id
     })
    }
  }
})
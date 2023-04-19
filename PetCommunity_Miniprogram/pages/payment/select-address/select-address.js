// pages/payment/select-address/select-address.js

const app = getApp()
const util = require('../../../utils/util')

Page({

  /**
   * 页面的初始数据
   */

  data: {
    length: 0,
    userInfo: null,
    // 地址列表
    AddressList: [],
    selAddress: null,
    // 设置开始的位置
    startX: 0,
    startY: 0,
  },

  onShow() {
    if (app.globalData.openid != null) {
      this.setData({
        userInfo: app.globalData.userInfo,
      })
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getUserAddress',
        data: {
          openid: app.globalData.openid
        },
        success: (res) => {
          this.setData({
            AddressList: res.data,
            length: res.data.length
          })
        }
      })
    }
  },

  // 开始滑动
  touchStart(e) {
    // console.log('touchStart=====>', e);
    let AddressList = [...this.data.AddressList]
    AddressList.forEach(item => {
      if (item.isTouchMove) {
        item.isTouchMove = !item.isTouchMove;
      }
    });
    this.setData({
      AddressList: AddressList,
      startX: e.touches[0].clientX,
      startY: e.touches[0].clientY
    })
  },

  touchMove(e) {
    // console.log('touchMove=====>', e);
    let moveX = e.changedTouches[0].clientX;
    let moveY = e.changedTouches[0].clientY;
    let indexs = e.currentTarget.dataset.index;
    let AddressList = [...this.data.AddressList]

    let angle = this.angle({
      X: this.data.startX,
      Y: this.data.startY
    }, {
      X: moveX,
      Y: moveY
    });

    AddressList.forEach((item, index) => {
      item.isTouchMove = false;
      // 如果滑动的角度大于30° 则直接return；
      if (angle > 30) {
        return
      }

      if (indexs === index) {
        if (moveX > this.data.startX) { // 右滑
          item.isTouchMove = false;
        } else { // 左滑
          item.isTouchMove = true;
        }
      }
    })
    this.setData({
      AddressList
    })
  },

  /**
   * 计算滑动角度
   * @param {Object} start 起点坐标
   * @param {Object} end 终点坐标
   */
  angle: function (start, end) {
    var _X = end.X - start.X,
      _Y = end.Y - start.Y
    //返回角度 /Math.atan()返回数字的反正切值
    return 360 * Math.atan(_Y / _X) / (2 * Math.PI);
  },

  // 删除
  delAdd(e) {
    let id = e.currentTarget.dataset.id;
    console.log('id--->', id);
    wx.request({
      url: 'http://127.0.0.1:8081/wx/delUserAddress',
      method: 'get',
      data: {
        openid: app.globalData.openid,
        addressid: id
      },
      success: (res) => {
        wx.hideLoading()
        wx.showToast({
          title: '删除成功！',
          duration: 2000
        })
        this.onShow()
      }
    })
  },

  // 编辑
  editAdd(e) {
    let id = e.currentTarget.dataset.id;
    let receiver_name = e.currentTarget.dataset.receiver_name;
    let receiver_phone = e.currentTarget.dataset.receiver_phone;
    let province = e.currentTarget.dataset.province;
    let city = e.currentTarget.dataset.city;
    let area = e.currentTarget.dataset.area;
    let detail_address = e.currentTarget.dataset.detail_address;
    let is_default = e.currentTarget.dataset.is_default;
    wx.navigateTo({
      url: '/pages/address/create-new-address/create-new-address?flag=1&id='+id+'&receiver_name='+receiver_name+'&receiver_phone='+receiver_phone+'&province='+province+'&city='+city+'&area='+area+'&detail_address='+detail_address+'&is_default='+is_default
    })
  },

  // 添加
  createAdd(e) {
    wx.navigateTo({
      url: '/pages/address/create-new-address/create-new-address?flag=0',
    })
  },

  // 选择地址
  selectAdd: function(e){
    console.log(this.data.AddressList[e.currentTarget.dataset.index])
    // 将数据传递到上一个页面
    let address = this.data.AddressList[e.currentTarget.dataset.index]
    let pages = getCurrentPages();//当前页面
    let prevPage = pages[pages.length-2];//上一页面
    prevPage.setData({//直接给上移页面赋值
      selAddress: address
    });
    wx.navigateBack({//返回
      delta:1
    })
  } 
})
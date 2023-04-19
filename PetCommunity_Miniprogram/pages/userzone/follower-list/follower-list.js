// pages/userzone/follower-list/follower-list.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    user_openid: null,
    followerList:[],
    page:1,
    limit:20
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // 接收数组
    // let string = decodeURIComponent(options.followerlist)
    // let followerList = JSON.parse(string)
    this.setData({
      user_openid: options.user_openid,
    })
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.setData({
      followerList:[],
      page:1,
      limit:20
    })
    // 分页获取用户粉丝列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getFollowerListByOpenid',
      data: {
        user_openid: this.data.user_openid,
        limit: this.data.limit,
        page: this.data.page
      },
      success: (res) => {
        wx.hideLoading()
        this.setData({
          followerList: res.data
        })
        console.log(this.data.followerList)
      }
    })
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    this.setData({
      followerList:[],
      page:1,
      limit:20
    })
    // 分页获取用户粉丝列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getFollowerListByOpenid',
      data: {
        user_openid: this.data.user_openid,
        limit: this.data.limit,
        page: this.data.page
      },
      success: (res) => {
        wx.hideLoading()
        this.setData({
          followerList: res.data
        })
        console.log(this.data.followerList)
      }
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    this.setData({
      page: this.data.page+1
    })
    // 分页获取用户粉丝列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getFollowerListByOpenid',
      data: {
        user_openid: this.data.user_openid,
        limit: this.data.limit,
        page: this.data.page
      },
      success: (res) => {
        wx.hideLoading()
        let followerList = this.data.followerList
        let resdata = res.data
        for(var i=0;i<resdata.length;i++){
          followerList.push(resdata[i])
        }
        this.setData({
          followerList: followerList
        })
        console.log(this.data.followerList)
      }
    })
  },

  gotoUserZone(e){
    wx.navigateTo({
      url: '/pages/userzone/userzone?user_openid=' + e.currentTarget.dataset.openid,
    })
  }
})
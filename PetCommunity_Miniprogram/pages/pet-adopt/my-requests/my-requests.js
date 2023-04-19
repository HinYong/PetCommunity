// pages/pet-adopt/my-requests/my-requests.js

const app = getApp()
const util = require('../../../utils/util')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    notprocess_List: [], // 未审核
    preagree_List:[], // 初审通过
    finish_List:[], // 领养完成
    refuse_List:[], // 已拒绝
    cancel_List:[], // 已取消
    notprocess_page:1, 
    preagree_page:1, 
    finish_page:1, 
    refuse_page:1, 
    cancel_page:1,
    limit:20,
    navbar: ['未审核', '初审通过', '已完成', '已拒绝', '已取消'],
    currentTab: 0,
  },

  onLoad() {
    // 数据初始化
    this.setData({
      notprocess_List: [], // 未审核
      preagree_List:[], // 初审通过
      finish_List:[], // 领养完成
      refuse_List:[], // 已拒绝
      cancel_List:[], // 已取消
      notprocess_page:1, 
      preagree_page:1, 
      finish_page:1, 
      refuse_page:1, 
      cancel_page:1,
      limit:20,
    })
    // 分页获取未审核领养申请列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
      data: {
        page: this.data.notprocess_page,
        limit: this.data.limit,
        status: 0,
        user_openid: app.globalData.openid
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].adopt.publish_time1 = util.formatDate(list[i].adopt.publish_time , 'yyyy/MM/dd')
        }
        this.setData({
          notprocess_List: list
        })
        console.log(this.data.notprocess_List)
      }
    })
    // 分页获取通过初审的领养申请列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
      data: {
        page: this.data.preagree_page,
        limit: this.data.limit,
        status: 1,
        user_openid: app.globalData.openid
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].adopt.publish_time1 = util.formatDate(list[i].adopt.publish_time , 'yyyy/MM/dd')
        }
        this.setData({
          preagree_List: list
        })
        console.log(this.data.preagree_List)
      }
    })
    // 分页获取已完成的领养申请列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
      data: {
        page: this.data.finish_page,
        limit: this.data.limit,
        status: 2,
        user_openid: app.globalData.openid
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].adopt.publish_time1 = util.formatDate(list[i].adopt.publish_time , 'yyyy/MM/dd')
        }
        this.setData({
          finish_List: list
        })
        console.log(this.data.finish_List)
      }
    })
    // 分页获取已拒绝的领养申请列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
      data: {
        page: this.data.refuse_page,
        limit: this.data.limit,
        status: 3,
        user_openid: app.globalData.openid
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].adopt.publish_time1 = util.formatDate(list[i].adopt.publish_time , 'yyyy/MM/dd')
        }
        this.setData({
          refuse_List: list
        })
        console.log(this.data.refuse_List)
      }
    })
    // 分页获取已取消的领养申请列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
      data: {
        page: this.data.cancel_page,
        limit: this.data.limit,
        status: 4,
        user_openid: app.globalData.openid
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].adopt.publish_time1 = util.formatDate(list[i].adopt.publish_time , 'yyyy/MM/dd')
        }
        this.setData({
          cancel_List: list
        })
        console.log(this.data.cancel_List)
      }
    })
  },

  onShow: function () {
    // 获取上一页传过来的is_publish对象，true代表发布成功，false代表没有发布
    let pages = getCurrentPages();
    let currPage = pages[pages.length-1];
    let is_cancel_adopt_request = currPage.data.is_cancel_adopt_request;
    let cancel_help_id = currPage.data.cancel_help_id;
    // 取消申请，重新加载未审核以及已取消的领养申请列表
    if(is_cancel_adopt_request!=null){
      // 分页获取未审核领养申请列表
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
        data: {
          page: 1,
          limit: this.data.limit,
          status: 0,
          user_openid: app.globalData.openid
        },
        success: (res) => {
          wx.hideLoading()
          let list = res.data
          for(var i=0;i<list.length;i++){
            list[i].adopt.publish_time1 = util.formatDate(list[i].adopt.publish_time , 'yyyy/MM/dd')
          }
          this.setData({
            notprocess_List: list
          })
          console.log(this.data.notprocess_List)
        }
      })
      // 分页获取已取消的领养申请列表
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
        data: {
          page: 1,
          limit: this.data.limit,
          status: 4,
          user_openid: app.globalData.openid
        },
        success: (res) => {
          wx.hideLoading()
          let list = res.data
          for(var i=0;i<list.length;i++){
            list[i].adopt.publish_time1 = util.formatDate(list[i].adopt.publish_time , 'yyyy/MM/dd')
          }
          this.setData({
            cancel_List: list
          })
          console.log(this.data.cancel_List)
        }
      })
    }
  },

  onPullDownRefresh(){
    // 数据初始化
    this.setData({
      notprocess_List: [], // 未审核
      preagree_List:[], // 初审通过
      finish_List:[], // 领养完成
      refuse_List:[], // 已拒绝
      cancel_List:[], // 已取消
      notprocess_page:1, 
      preagree_page:1, 
      finish_page:1, 
      refuse_page:1, 
      cancel_page:1,
      limit:20,
    })
    // 分页获取未审核领养申请列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
      data: {
        page: this.data.notprocess_page,
        limit: this.data.limit,
        status: 0,
        user_openid: app.globalData.openid
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].adopt.publish_time1 = util.formatDate(list[i].adopt.publish_time , 'yyyy/MM/dd')
        }
        this.setData({
          notprocess_List: list
        })
        console.log(this.data.notprocess_List)
      }
    })
    // 分页获取通过初审的领养申请列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
      data: {
        page: this.data.preagree_page,
        limit: this.data.limit,
        status: 1,
        user_openid: app.globalData.openid
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].adopt.publish_time1 = util.formatDate(list[i].adopt.publish_time , 'yyyy/MM/dd')
        }
        this.setData({
          preagree_List: list
        })
        console.log(this.data.preagree_List)
      }
    })
    // 分页获取已完成的领养申请列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
      data: {
        page: this.data.finish_page,
        limit: this.data.limit,
        status: 2,
        user_openid: app.globalData.openid
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].adopt.publish_time1 = util.formatDate(list[i].adopt.publish_time , 'yyyy/MM/dd')
        }
        this.setData({
          finish_List: list
        })
        console.log(this.data.finish_List)
      }
    })
    // 分页获取已拒绝的领养申请列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
      data: {
        page: this.data.refuse_page,
        limit: this.data.limit,
        status: 3,
        user_openid: app.globalData.openid
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].adopt.publish_time1 = util.formatDate(list[i].adopt.publish_time , 'yyyy/MM/dd')
        }
        this.setData({
          refuse_List: list
        })
        console.log(this.data.refuse_List)
      }
    })
    // 分页获取已取消的领养申请列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
      data: {
        page: this.data.cancel_page,
        limit: this.data.limit,
        status: 4,
        user_openid: app.globalData.openid
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].adopt.publish_time1 = util.formatDate(list[i].adopt.publish_time , 'yyyy/MM/dd')
        }
        this.setData({
          cancel_List: list
        })
        console.log(this.data.cancel_List)
      }
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  loadMore: function () {
    // 用户处于未审核界面
    if(this.data.currentTab==0){
      this.setData({
        notprocess_page: this.data.notprocess_page+1
      })
      console.log("notprocess_page"+this.data.notprocess_page)
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
        data: {
          page: this.data.notprocess_page,
          limit: this.data.limit,
          status: 0,
          user_openid: app.globalData.openid
        },
        success: (res) => {
          wx.hideLoading()
          let list = this.data.notprocess_List
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].adopt.publish_time1 = util.formatDate(resdata[i].adopt.publish_time, 'yyyy/MM/dd')
            list.push(resdata[i])
          }
          this.setData({
            notprocess_List: list
          })
          console.log(this.data.notprocess_List)
        }
      })
    }
    // 用户处于通过初审界面
    else if(this.data.currentTab==1){
      this.setData({
        preagree_page: this.data.preagree_page+1
      })
      console.log("preagree_page"+this.data.preagree_page)
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
        data: {
          page: this.data.preagree_page,
          limit: this.data.limit,
          status: 1,
          user_openid: app.globalData.openid
        },
        success: (res) => {
          wx.hideLoading()
          let list = this.data.preagree_List
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].adopt.publish_time1 = util.formatDate(resdata[i].adopt.publish_time, 'yyyy/MM/dd')
            list.push(resdata[i])
          }
          this.setData({
            preagree_List: list
          })
          console.log(this.data.preagree_List)
        }
      })
    }
    // 用户处于已完成界面
    else if(this.data.currentTab==2){
      this.setData({
        finish_page: this.data.finish_page+1
      })
      console.log("finish_page"+this.data.finish_page)
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
        data: {
          page: this.data.finish_page,
          limit: this.data.limit,
          status: 2,
          user_openid: app.globalData.openid
        },
        success: (res) => {
          wx.hideLoading()
          let list = this.data.finish_List
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].adopt.publish_time1 = util.formatDate(resdata[i].adopt.publish_time, 'yyyy/MM/dd')
            list.push(resdata[i])
          }
          this.setData({
            finish_List: list
          })
          console.log(this.data.finish_List)
        }
      })
    }
    // 用户处于已拒绝界面
    else if(this.data.currentTab==3){
      this.setData({
        refuse_page: this.data.refuse_page+1
      })
      console.log("refuse_page"+this.data.refuse_page)
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
        data: {
          page: this.data.refuse_page,
          limit: this.data.limit,
          status: 3,
          user_openid: app.globalData.openid
        },
        success: (res) => {
          wx.hideLoading()
          let list = this.data.refuse_List
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].adopt.publish_time1 = util.formatDate(resdata[i].adopt.publish_time, 'yyyy/MM/dd')
            list.push(resdata[i])
          }
          this.setData({
            refuse_List: list
          })
          console.log(this.data.refuse_List)
        }
      })
    }
    // 用户处于已取消界面
    else if(this.data.currentTab==4){
      this.setData({
        cancel_page: this.data.cancel_page+1
      })
      console.log("cancel_page"+this.data.cancel_page)
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getPublishAdoptRequestsByUserOpenIdAndStatus',
        data: {
          page: this.data.cancel_page,
          limit: this.data.limit,
          status: 4,
          user_openid: app.globalData.openid
        },
        success: (res) => {
          wx.hideLoading()
          let list = this.data.cancel_List
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].adopt.publish_time1 = util.formatDate(resdata[i].adopt.publish_time, 'yyyy/MM/dd')
            list.push(resdata[i])
          }
          this.setData({
            cancel_List: list
          })
          console.log(this.data.cancel_List)
        }
      })
    }
  },

  // 切换顶部bar
  navbarTap: function(e){
    this.setData({
      currentTab: e.currentTarget.dataset.idx
    })
  },

  // 跳转到领养申请详情页
  gotoAdoptRequestDetail(e){
    // 多层嵌套必须必须encode
    let string = JSON.stringify(e.currentTarget.dataset.adopt_request)
    string = encodeURIComponent(string)
    wx.navigateTo({
      url: '/pages/pet-adopt/my-requests/request-detail/request-detail?adopt_request=' + string,
    })
  }

})
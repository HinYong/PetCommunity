// pages/pet-help/pet-help.js

const util = require('../../utils/util')
const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    navbar: ['未处理', '处理中', '已处理', '已取消'],
    currentTab: 0,
    notprocess_helps:[],  // 未处理
    processing_helps:[],  // 处理中
    finish_helps:[],  // 已完成
    cancel_helps:[],  // 已取消
    notprocess_page:1,
    processing_page:1,
    finish_page:1,
    cancel_page:1,
    limit:20
  },

  onLoad: function(option){
    // 数据初始化
    this.setData({
      notprocess_helps:[],  // 未处理
      processing_helps:[],  // 处理中
      finish_helps:[],  // 已完成
      cancel_helps:[],  // 已取消
      notprocess_page:1,
      processing_page:1,
      finish_page:1,
      cancel_page:1,
      limit:20
    })
    // 分页获取未处理的求助请求
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishHelpsByUserOpenIdAndStatus',
      data: {
        user_openid: app.globalData.openid,
        status: 0,
        limit: this.data.limit,
        page: this.data.notprocess_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time1 = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
          list[i].publish_time = util.formatTime(list[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
        }
        this.setData({
          notprocess_helps: list
        })
        console.log(this.data.notprocess_helps)
      }
    })
    // 分页获取处理中的求助请求
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishHelpsByUserOpenIdAndStatus',
      data: {
        user_openid: app.globalData.openid,
        status: 1,
        limit: this.data.limit,
        page: this.data.processing_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time1 = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
          list[i].publish_time = util.formatTime(list[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
          list[i].process_time = util.formatTime(list[i].process_time , 'yyyy/MM/dd/HH/mm/ss')
        }
        this.setData({
          processing_helps: list
        })
        console.log(this.data.processing_helps)
      }
    })
    // 分页获取已完成的求助请求
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishHelpsByUserOpenIdAndStatus',
      data: {
        user_openid: app.globalData.openid,
        status: 2,
        limit: this.data.limit,
        page: this.data.finish_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time1 = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
          list[i].publish_time = util.formatTime(list[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
          list[i].process_time = util.formatTime(list[i].process_time , 'yyyy/MM/dd/HH/mm/ss')
          list[i].finish_time = util.formatTime(list[i].finish_time , 'yyyy/MM/dd/HH/mm/ss')
        }
        this.setData({
          finish_helps: list
        })
        console.log(this.data.finish_helps)
      }
    })
    // 分页获取已取消的求助请求
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishHelpsByUserOpenIdAndStatus',
      data: {
        user_openid: app.globalData.openid,
        status: 3,
        limit: this.data.limit,
        page: this.data.cancel_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time1 = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
          list[i].publish_time = util.formatTime(list[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
          list[i].cancel_time = util.formatTime(list[i].cancel_time , 'yyyy/MM/dd/HH/mm/ss')
        }
        this.setData({
          cancel_helps: list
        })
        console.log(this.data.cancel_helps)
      }
    })
  },

  /**
   * 生命周期函数--监听页面展示
   */
  onShow: function () {
    // 获取上一页传过来的is_publish对象，true代表发布成功，false代表没有发布
    let pages = getCurrentPages();
    let currPage = pages[pages.length-1];
    let is_publish_help = currPage.data.is_publish_help;
    let cancel_help_id = currPage.data.cancel_help_id;
    // 请求发布成功，重新加载未处理的请求列表
    if(is_publish_help!=null){
      // 分页获取未处理的求助请求
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getPublishHelpsByUserOpenIdAndStatus',
        data: {
          user_openid: app.globalData.openid,
          status: 0,
          limit: this.data.limit,
          page: 1
        },
        success: (res) => {
          wx.hideLoading()
          let list = res.data
          for(var i=0;i<list.length;i++){
            list[i].publish_time1 = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
            list[i].publish_time = util.formatTime(list[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
          }
          this.setData({
            notprocess_helps: list
          })
        }
      })
    }
    // 请求取消成功，将请求从未处理的请求列表中删除，并且重新加载已取消的请求列表
    if(cancel_help_id!=null){
      let notprocess_helps = this.data.notprocess_helps
      // 将请求从未处理的请求列表中删除
      for(var i=0;i<notprocess_helps.length;i++){
        if(notprocess_helps[i].help_id == cancel_help_id){
          notprocess_helps.splice(i,1)
          break
        }
      }
      this.setData({
        notprocess_helps: notprocess_helps
      })
      // 分页获取已取消的求助请求
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getPublishHelpsByUserOpenIdAndStatus',
        data: {
          user_openid: app.globalData.openid,
          status: 3,
          limit: this.data.limit,
          page: 1
        },
        success: (res) => {
          wx.hideLoading()
          let list = res.data
          for(var i=0;i<list.length;i++){
            list[i].publish_time1 = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
            list[i].publish_time = util.formatTime(list[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
            list[i].cancel_time = util.formatTime(list[i].cancel_time , 'yyyy/MM/dd/HH/mm/ss')
          }
          this.setData({
            cancel_helps: list
          })
        }
      })
    }
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    // 数据初始化
    this.setData({
      notprocess_helps:[],  // 未处理
      processing_helps:[],  // 处理中
      finish_helps:[],  // 已完成
      cancel_helps:[],  // 已取消
      notprocess_page:1,
      processing_page:1,
      finish_page:1,
      cancel_page:1,
      limit:20
    })
    // 分页获取未处理的求助请求
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishHelpsByUserOpenIdAndStatus',
      data: {
        user_openid: app.globalData.openid,
        status: 0,
        limit: this.data.limit,
        page: this.data.notprocess_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time1 = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
          list[i].publish_time = util.formatTime(list[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
        }
        this.setData({
          notprocess_helps: list
        })
        console.log(this.data.notprocess_helps)
      }
    })
    // 分页获取处理中的求助请求
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishHelpsByUserOpenIdAndStatus',
      data: {
        user_openid: app.globalData.openid,
        status: 1,
        limit: this.data.limit,
        page: this.data.processing_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time1 = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
          list[i].publish_time = util.formatTime(list[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
          list[i].process_time = util.formatTime(list[i].process_time , 'yyyy/MM/dd/HH/mm/ss')
        }
        this.setData({
          processing_helps: list
        })
        console.log(this.data.processing_helps)
      }
    })
    // 分页获取已完成的求助请求
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishHelpsByUserOpenIdAndStatus',
      data: {
        user_openid: app.globalData.openid,
        status: 2,
        limit: this.data.limit,
        page: this.data.finish_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time1 = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
          list[i].publish_time = util.formatTime(list[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
          list[i].process_time = util.formatTime(list[i].process_time , 'yyyy/MM/dd/HH/mm/ss')
          list[i].finish_time = util.formatTime(list[i].finish_time , 'yyyy/MM/dd/HH/mm/ss')
        }
        this.setData({
          finish_helps: list
        })
        console.log(this.data.finish_helps)
      }
    })
    // 分页获取已取消的求助请求
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishHelpsByUserOpenIdAndStatus',
      data: {
        user_openid: app.globalData.openid,
        status: 3,
        limit: this.data.limit,
        page: this.data.cancel_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time1 = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
          list[i].publish_time = util.formatTime(list[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
          list[i].cancel_time = util.formatTime(list[i].cancel_time , 'yyyy/MM/dd/HH/mm/ss')
        }
        this.setData({
          cancel_helps: list
        })
        console.log(this.data.cancel_helps)
      }
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  loadMore: function () {
    // 用户处于未处理请求界面，则向后端请求加载更多的notprocess_helps并且加入数组
    if(this.data.currentTab==0){
      this.setData({
        notprocess_page: this.data.notprocess_page+1
      })
      console.log("notprocess"+this.data.notprocess_page)
      // 分页获取未处理的请求
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getPublishHelpsByUserOpenIdAndStatus',
        data: {
          user_openid: app.globalData.openid,
          status: 0,
          limit: this.data.limit,
          page: this.data.notprocess_page
        },
        success: (res) => {
          wx.hideLoading()
          let notprocess_helps = this.data.notprocess_helps
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].publish_time1 = util.formatDate(resdata[i].publish_time , 'yyyy/MM/dd')
            resdata[i].publish_time = util.formatTime(resdata[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
            notprocess_helps.push(resdata[i])
          }
          this.setData({
            notprocess_helps: notprocess_helps
          })
          console.log(this.data.notprocess_helps)
        }
      })
    }
    // 用户处于处理中的请求界面，则向后端请求加载更多的processing_helps并且加入数组
    else if(this.data.currentTab==1){
      this.setData({
        processing_page: this.data.processing_page+1
      })
      console.log("processing"+this.data.processing_page)
      // 分页获取处理中的请求
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getPublishHelpsByUserOpenIdAndStatus',
        data: {
          user_openid: app.globalData.openid,
          status: 1,
          limit: this.data.limit,
          page: this.data.processing_page
        },
        success: (res) => {
          wx.hideLoading()
          let processing_helps = this.data.processing_helps
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].publish_time1 = util.formatDate(resdata[i].publish_time , 'yyyy/MM/dd')
            resdata[i].publish_time = util.formatTime(resdata[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
            resdata[i].process_time = util.formatTime(resdata[i].process_time , 'yyyy/MM/dd/HH/mm/ss')
            processing_helps.push(resdata[i])
          }
          this.setData({
            processing_helps: processing_helps
          })
          console.log(this.data.processing_helps)
        }
      })
    }
    // 用户处于已处理请求界面，则向后端请求加载更多的finish_helps并且加入数组
    else if(this.data.currentTab==2){
      this.setData({
        finish_page: this.data.finish_page+1
      })
      console.log("finish"+this.data.finish_page)
      // 分页获取已完成的请求
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getPublishHelpsByUserOpenIdAndStatus',
        data: {
          user_openid: app.globalData.openid,
          status: 2,
          limit: this.data.limit,
          page: this.data.finish_page
        },
        success: (res) => {
          wx.hideLoading()
          let finish_helps = this.data.finish_helps
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].publish_time1 = util.formatDate(resdata[i].publish_time , 'yyyy/MM/dd')
            resdata[i].publish_time = util.formatTime(resdata[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
            resdata[i].process_time = util.formatTime(resdata[i].process_time , 'yyyy/MM/dd/HH/mm/ss')
            resdata[i].finish_time = util.formatTime(resdata[i].finish_time , 'yyyy/MM/dd/HH/mm/ss')
            finish_helps.push(resdata[i])
          }
          this.setData({
            finish_helps: finish_helps
          })
          console.log(this.data.finish_helps)
        }
      })
    }
    // 用户处于已取消请求界面，则向后端请求加载更多的cancel_helps并且加入数组
    else if(this.data.currentTab==2){
      this.setData({
        cancel_page: this.data.cancel_page+1
      })
      console.log("cancel"+this.data.cancel_page)
      // 分页获取已取消的请求
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getPublishHelpsByUserOpenIdAndStatus',
        data: {
          user_openid: app.globalData.openid,
          status: 3,
          limit: this.data.limit,
          page: this.data.cancel_page
        },
        success: (res) => {
          wx.hideLoading()
          let cancel_helps = this.data.cancel_helps
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].publish_time1 = util.formatDate(resdata[i].publish_time , 'yyyy/MM/dd')
            resdata[i].publish_time = util.formatTime(resdata[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
            resdata[i].cancel_time = util.formatTime(resdata[i].cancel_time , 'yyyy/MM/dd/HH/mm/ss')
            cancel_helps.push(resdata[i])
          }
          this.setData({
            cancel_helps: cancel_helps
          })
          console.log(this.data.cancel_helps)
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

  // 跳转到求助发布页
  gotoPublishHelp(e){
    wx.navigateTo({
      url: '/pages/pet-help/publish-help/publish-help'
    })
  },

  // 跳转到求助详情页
  gotoHelpDetail(e){
    if(this.is_preview_image!=true){
      // 多层嵌套必须必须encode
      let string = JSON.stringify(e.currentTarget.dataset.help)
      string = encodeURIComponent(string)
      wx.navigateTo({
        url: '/pages/pet-help/help-detail/help-detail?help=' + string,
      })
    }
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
})
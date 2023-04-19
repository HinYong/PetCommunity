// pages/discovery/discovery.js

const app = getApp()
const util = require('../../utils/util')

Page({
  /**
   * 页面的初始数据
   */
  data: {
    recomList:[],  // 热门博客列表
    page:1,
    limit:20,
    searchContent: null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    // 数据初始化
    this.setData({
      recomList:[],  // 热门博客列表
      page:1,
      limit:20
    })
    // 分页获取点赞最多的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllBlogsByLikes',
      data: {
        limit: this.data.limit,
        page: this.data.page,
        searchContent: this.data.searchContent
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time = util.formatDate(list[i].publish_time, 'yyyy/MM/dd')
        }
        this.setData({
          recomList: list
        })
        console.log(this.data.recomList)
      }
    })
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow(){
    // 获取上一页传过来的blog对象
    let pages = getCurrentPages();
    let currPage = pages[pages.length-1];
    let blog = currPage.data.blog;
    let is_deleted = currPage.data.is_deleted
    console.log(blog)
    // 不是初次加载页面，而是从博客详情页面跳转过来的，则将对应的博客对象进行替换
    if(blog!=null){
      let recomList = this.data.recomList
      for(var i=0;i<recomList.length;i++){
        if(recomList[i].blog_id==blog.blog_id){
          // 删除
          if(is_deleted==true){
            recomList.splice(i,1)
          }
          // 更新对象
          else{
            recomList[i] = blog
          }
          break
        }
      }
      this.setData({
        recomList: recomList
      })
    }
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    // 数据初始化
    this.setData({
      recomList:[],  // 热门博客列表
      page:1,
      limit:20
    })
    // 分页获取点赞最多的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllBlogsByLikes',
      data: {
        limit: this.data.limit,
        page: this.data.page,
        searchContent: this.data.searchContent
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time = util.formatDate(list[i].publish_time, 'yyyy/MM/dd')
        }
        this.setData({
          recomList: list
        })
        console.log(this.data.recomList)
      }
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  loadMore: function () {
    this.setData({
      page: this.data.page+1
    })
    // 分页获取最多点赞的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllBlogsByLikes',
      data: {
        limit: this.data.limit,
        page: this.data.page,
        searchContent: this.data.searchContent
      },
      success: (res) => {
        wx.hideLoading()
        let recomList = this.data.recomList
        let resdata = res.data
        for(var i=0;i<resdata.length;i++){
          resdata[i].publish_time = util.formatDate(resdata[i].publish_time , 'yyyy/MM/dd')
          recomList.push(resdata[i])
        }
        this.setData({
          recomList: recomList
        })
        console.log(this.data.recomList)
      }
    })
  },

  // 跳转到百科页面
  gotoEncyclopedia(){
    wx.navigateTo({
      url: '/pages/encyclopedia/encyclopedia',
    })
  },

  // 跳转到社区页面
  gotoCommunity(){
    if (app.globalData.openid == null) {
      wx.hideLoading()
      wx.showModal({
        title: '提示',
        content: '请先登录',
        success(res) {
          if (res.confirm) {
            wx.switchTab({
              url: '../user/user',
            })
          }
        }
      })
    }
    else{
      wx.navigateTo({
        url: '/pages/community/community',
      })
    }
  },

  // 跳转到求助页面
  gotoHelp(){
    if (app.globalData.openid == null) {
      wx.hideLoading()
      wx.showModal({
        title: '提示',
        content: '请先登录',
        success(res) {
          if (res.confirm) {
            wx.switchTab({
              url: '../user/user',
            })
          }
        }
      })
    }
    else{
      wx.navigateTo({
        url: '/pages/pet-help/pet-help',
      })
    }
  },

  // 跳转到领养页面
  gotoAdopt(){
    if (app.globalData.openid == null) {
      wx.hideLoading()
      wx.showModal({
        title: '提示',
        content: '请先登录',
        success(res) {
          if (res.confirm) {
            wx.switchTab({
              url: '../user/user',
            })
          }
        }
      })
    }
    else{
      wx.navigateTo({
        url: '/pages/pet-adopt/pet-adopt'
      })
    }
  },

  // 跳转到博客详情页
  gotoBlogDetail(e){
    // 多层嵌套必须必须encode
    let string = JSON.stringify(e.currentTarget.dataset.blog)
    string = encodeURIComponent(string)
    wx.navigateTo({
      url: '/pages/blog-detail/blog-detail?blog=' + string,
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

  // 点击头像跳转到用户空间
  gotoUserZone(e){
    wx.navigateTo({
      url: '/pages/userzone/userzone?user_openid=' + e.currentTarget.dataset.user_openid,
    })
  },
})
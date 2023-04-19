// pages/community/community.js

const app = getApp()
const util = require('../../utils/util')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    navbar: ['最多点赞', '最多收藏', '最新发布', '关注的人'],
    currentTab: 0,
    list_orderby_like:[],  // 最多点赞博客列表
    list_orderby_star:[],  // 最多收藏博客列表
    list_orderby_time:[],  // 最新发布博客列表
    list_orderby_following:[],  // 关注的人的博客列表
    time_page:1,
    like_page:1,
    star_page:1,
    following_page:1,
    limit:20,
    searchContent: null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    console.log(this.data.searchContent)
    // 数据初始化
    this.setData({
      list_orderby_like:[],  // 最多点赞博客列表
      list_orderby_star:[],  // 最多收藏博客列表
      list_orderby_time:[],  // 最新发布博客列表
      list_orderby_following:[],  // 关注的人的博客列表
      time_page:1,
      like_page:1,
      star_page:1,
      following_page:1,
      limit:20,
    })
    // 分页获取点赞最多的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllBlogsByLikes',
      data: {
        limit: this.data.limit,
        page: this.data.like_page,
        searchContent: this.data.searchContent
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time = util.formatDate(list[i].publish_time, 'yyyy/MM/dd')
        }
        this.setData({
          list_orderby_like: list
        })
        console.log(this.data.list_orderby_like)
      }
    })
    // 分页获取收藏最多的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllBlogsByStars',
      data: {
        limit: this.data.limit,
        page: this.data.star_page,
        searchContent: this.data.searchContent
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time = util.formatDate(list[i].publish_time, 'yyyy/MM/dd')
        }
        this.setData({
          list_orderby_star: list
        })
        console.log(this.data.list_orderby_star)
      }
    })
    // 分页获取最新发布的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllBlogsByTime',
      data: {
        limit: this.data.limit,
        page: this.data.time_page,
        searchContent: this.data.searchContent
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time = util.formatDate(list[i].publish_time, 'yyyy/MM/dd')
        }
        this.setData({
          list_orderby_time: list
        })
        console.log(this.data.list_orderby_time)
      }
    })
    // 分页获取关注的人发布的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllBlogsByFollowing',
      data: {
        limit: this.data.limit,
        page: this.data.following_page,
        user_openid: app.globalData.openid,
        searchContent: this.data.searchContent
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time = util.formatDate(list[i].publish_time, 'yyyy/MM/dd')
        }
        this.setData({
          list_orderby_following: list
        })
        console.log(this.data.list_orderby_following)
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
      let list_orderby_like = this.data.list_orderby_like
      for(var i=0;i<list_orderby_like.length;i++){
        if(list_orderby_like[i].blog_id==blog.blog_id){
          // 删除
          if(is_deleted==true){
            list_orderby_like.splice(i,1)
          }
          // 更新对象
          else{
            list_orderby_like[i] = blog
          }
          break
        }
      }
      let list_orderby_star = this.data.list_orderby_star
      for(var i=0;i<list_orderby_star.length;i++){
        if(list_orderby_star[i].blog_id==blog.blog_id){
          // 删除
          if(is_deleted==true){
            list_orderby_star.splice(i,1)
          }
          // 更新对象
          else{
            list_orderby_star[i] = blog
          }
          break
        }
      }
      let list_orderby_time = this.data.list_orderby_time
      for(var i=0;i<list_orderby_time.length;i++){
        if(list_orderby_time[i].blog_id==blog.blog_id){
          // 删除
          if(is_deleted==true){
            list_orderby_time.splice(i,1)
          }
          // 更新对象
          else{
            list_orderby_time[i] = blog
          }
          break
        }
      }
      let list_orderby_following = this.data.list_orderby_following
      for(var i=0;i<list_orderby_following.length;i++){
        if(list_orderby_following[i].blog_id==blog.blog_id){
          // 删除
          if(is_deleted==true){
            list_orderby_following.splice(i,1)
          }
          // 更新对象
          else{
            list_orderby_following[i] = blog
          }
          break
        }
      }
      this.setData({
        list_orderby_like: list_orderby_like,
        list_orderby_star: list_orderby_star,
        list_orderby_time: list_orderby_time,
        list_orderby_following: list_orderby_following
      })
    }
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    // 数据初始化
    this.setData({
      list_orderby_like:[],  // 最多点赞博客列表
      list_orderby_star:[],  // 最多收藏博客列表
      list_orderby_time:[],  // 最新发布博客列表
      list_orderby_following:[],  // 关注的人的博客列表
      time_page:1,
      like_page:1,
      star_page:1,
      following_page:1,
      limit:20
    })
    // 分页获取点赞最多的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllBlogsByLikes',
      data: {
        limit: this.data.limit,
        page: this.data.like_page,
        searchContent: this.data.searchContent
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time = util.formatDate(list[i].publish_time, 'yyyy/MM/dd')
        }
        this.setData({
          list_orderby_like: list
        })
        console.log(this.data.list_orderby_like)
      }
    })
    // 分页获取收藏最多的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllBlogsByStars',
      data: {
        limit: this.data.limit,
        page: this.data.star_page,
        searchContent: this.data.searchContent
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time = util.formatDate(list[i].publish_time, 'yyyy/MM/dd')
        }
        this.setData({
          list_orderby_star: list
        })
        console.log(this.data.list_orderby_star)
      }
    })
    // 分页获取最新发布的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllBlogsByTime',
      data: {
        limit: this.data.limit,
        page: this.data.time_page,
        searchContent: this.data.searchContent
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time = util.formatDate(list[i].publish_time, 'yyyy/MM/dd')
        }
        this.setData({
          list_orderby_time: list
        })
        console.log(this.data.list_orderby_time)
      }
    })
    // 分页获取关注的人发布的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllBlogsByFollowing',
      data: {
        limit: this.data.limit,
        page: this.data.following_page,
        user_openid: app.globalData.openid,
        searchContent: this.data.searchContent
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time = util.formatDate(list[i].publish_time, 'yyyy/MM/dd')
        }
        this.setData({
          list_orderby_following: list
        })
        console.log(this.data.list_orderby_following)
      }
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  loadMore: function () {
    // 用户处于最多点赞界面
    if(this.data.currentTab==0){
      this.setData({
        like_page: this.data.like_page+1
      })
      // 分页获取最多点赞的博客
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getAllBlogsByLikes',
        data: {
          limit: this.data.limit,
          page: this.data.like_page,
          searchContent: this.data.searchContent
        },
        success: (res) => {
          wx.hideLoading()
          let list_orderby_like = this.data.list_orderby_like
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].publish_time = util.formatDate(resdata[i].publish_time , 'yyyy/MM/dd')
            list_orderby_like.push(resdata[i])
          }
          this.setData({
            list_orderby_like: list_orderby_like
          })
          console.log(this.data.list_orderby_like)
        }
      })
    }
    // 用户处于最多收藏界面
    else if(this.data.currentTab==1){
      this.setData({
        star_page: this.data.star_page+1
      })
      // 分页获取最多收藏的博客
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getAllBlogsByStars',
        data: {
          limit: this.data.limit,
          page: this.data.star_page,
          searchContent: this.data.searchContent
        },
        success: (res) => {
          wx.hideLoading()
          let list_orderby_star = this.data.list_orderby_star
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].publish_time = util.formatDate(resdata[i].publish_time , 'yyyy/MM/dd')
            list_orderby_star.push(resdata[i])
          }
          this.setData({
            list_orderby_star: list_orderby_star
          })
          console.log(this.data.list_orderby_star)
        }
      })
    }
    // 用户处于最新发布界面
    else if(this.data.currentTab==2){
      this.setData({
        time_page: this.data.time_page+1
      })
      // 分页获取最新发布的博客
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getAllBlogsByTime',
        data: {
          limit: this.data.limit,
          page: this.data.time_page,
          searchContent: this.data.searchContent
        },
        success: (res) => {
          wx.hideLoading()
          let list_orderby_time = this.data.list_orderby_time
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].publish_time = util.formatDate(resdata[i].publish_time , 'yyyy/MM/dd')
            list_orderby_time.push(resdata[i])
          }
          this.setData({
            list_orderby_time: list_orderby_time
          })
          console.log(this.data.list_orderby_time)
        }
      })
    }
    // 用户处于关注用户界面
    else if(this.data.currentTab==3){
      this.setData({
        following_page: this.data.following_page+1
      })
      // 分页获取最新发布的博客
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getAllBlogsByFollowing',
        data: {
          limit: this.data.limit,
          page: this.data.following_page,
          user_openid: app.globalData.openid,
          searchContent: this.data.searchContent
        },
        success: (res) => {
          wx.hideLoading()
          let list_orderby_following = this.data.list_orderby_following
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].publish_time = util.formatDate(resdata[i].publish_time , 'yyyy/MM/dd')
            list_orderby_following.push(resdata[i])
          }
          this.setData({
            list_orderby_following: list_orderby_following
          })
          console.log(this.data.list_orderby_following)
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

  // 跳转到博客详情页
  gotoBlogDetail(e){
    // 多层嵌套必须必须encode
    let string = JSON.stringify(e.currentTarget.dataset.blog)
    string = encodeURIComponent(string)
    wx.navigateTo({
      url: '/pages/blog-detail/blog-detail?blog=' + string,
    })
  },

  // 输入框输入事件绑定
  searchInput(e) {
    this.setData({
      searchContent: e.detail.value
    })
  },

  // 搜索按钮
  search(e) {
    console.log(this.searchContent)
    this.onLoad()
  },

  // 发帖
  gotoPublishBlog(){
    wx.navigateTo({
      url: '/pages/community/publish-blog/publish-blog',
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
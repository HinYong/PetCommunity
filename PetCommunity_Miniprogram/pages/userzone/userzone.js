// pages/userzone/userzone.js

const util = require('../../utils/util')
const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    is_me: null,  // 判断是用户访问自己主页还是访问他人主页
    user_openid: null,  // 当前主页用户的id
    navbar: ['笔记', '收藏', '赞过'],
    currentTab: 0,
    user:[],  // 用户信息
    publish_blogs:[],  // 发布博客
    star_blogs:[],  // 收藏博客
    like_blogs:[],  // 点赞博客
    publish_page:1,
    star_page:1,
    like_page:1,
    limit:20
  },

  onLoad: function(option){
    this.setData({
      user_openid: option.user_openid
    })
    // 访问自己的主页
    if(this.data.user_openid==app.globalData.openid){
      this.setData({
        is_me: true
      })
    }
    // 访问他人的主页
    else{
      this.setData({
        is_me: false
      })
      wx.request({
        url: 'http://127.0.0.1:8081/wx/checkIsFollowing',
        data: {
          myopenid: app.globalData.openid,  // 当前用户id
          othersopenid: this.data.user_openid  // 访问的用户id
        },
        success: (res) => {
          wx.hideLoading()
          this.setData({
            is_following: res.data
          })
          console.log("isfollowing:"+this.data.is_following)
        }
      })
    }
    // console.log("isme:"+this.data.is_me)
    // 数据初始化
    this.setData({
      user:[],  // 用户信息
      publish_blogs:[],  // 发布博客
      star_blogs:[],  // 收藏博客
      like_blogs:[],  // 点赞博客
      publish_page:1,
      star_page:1,
      like_page:1,
      limit:20
    })
    // 获取用户顶部信息栏，包括关注列表以及粉丝列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getUserInfoByOpenid',
      data: {
        user_openid: this.data.user_openid
      },
      success: (res) => {
        wx.hideLoading()
        this.setData({
          user: res.data
        })
        console.log(this.data.user)
      }
    })
    // 分页获取用户发的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishBlogsByUserOpenId',
      data: {
        user_openid: this.data.user_openid,
        limit: this.data.limit,
        page: this.data.publish_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
        }
        this.setData({
          publish_blogs: list
        })
        console.log(this.data.publish_blogs)
      }
    })
    // 分页获取用户收藏的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getStarsBlogsByUserOpenId',
      data: {
        user_openid: this.data.user_openid,
        limit: this.data.limit,
        page: this.data.star_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
        }
        this.setData({
          star_blogs: list
        })
        console.log(this.data.star_blogs)
      }
    })
    // 分页获取用户点赞的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getLikesBlogsByUserOpenId',
      data: {
        user_openid: this.data.user_openid,
        limit: this.data.limit,
        page: this.data.like_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
        }
        this.setData({
          like_blogs: list
        })
        console.log(this.data.like_blogs)
      }
    })
  },

  /**
   * 生命周期函数--监听页面展示
   */
  onShow: function () {
    // 获取上一页传过来的blog对象
    let pages = getCurrentPages();
    let currPage = pages[pages.length-1];
    let blog = currPage.data.blog;
    console.log(blog)
    // 不是初次加载页面，而是从博客详情页面跳转过来的，则将对应的博客对象进行替换
    if(blog!=null){
      let publish_blogs = this.data.publish_blogs
      let like_blogs = this.data.like_blogs
      let star_blogs = this.data.star_blogs
      // 自己的空间，如果有点赞、取消点赞；收藏、取消收藏；删除博客的话，需要将相应的博客在前端列表删除
      if(this.data.is_me==true){
        // 删除
        if(currPage.data.is_deleted==true){
          for(var i=0;i<publish_blogs.length;i++){
            if(publish_blogs[i].blog_id==blog.blog_id){
              publish_blogs.splice(i,1)
              break
            }
          }
        }
        // 点赞
        var flag = false
        for(var i=0;i<like_blogs.length;i++){
          if(like_blogs[i].blog_id==blog.blog_id){
            flag = true
            break
          }
        }
        // 该博客本来就在点赞列表里
        if(flag==true){
          // 如果没有取消点赞，则只需要更新点赞列表中相应的博客对象
          if(currPage.data.is_liked==true){
            like_blogs[i] = blog
          }
          // 若取消点赞，则从前端的点赞列表中删除该对象
          else{
            like_blogs.splice(i,1)
          }
        }
        // 该博客本来不在点赞列表里
        else{
          // 用户点赞了，则加入点赞列表
          if(currPage.data.is_liked==true){
            // 点赞列表一开始为空的
            if(like_blogs.length==0){
              like_blogs = new Array()
            }
            like_blogs.push(blog)
          }
        }
        // 收藏
        flag = false
        for(var i=0;i<star_blogs.length;i++){
          if(star_blogs[i].blog_id==blog.blog_id){
            flag = true
            break
          }
        }
        // 该博客本来就在收藏列表里
        if(flag==true){
          // 如果没有取消收藏，则只需要更新收藏列表中相应的博客对象
          if(currPage.data.is_stared==true){
            star_blogs[i] = blog
          }
          // 若取消收藏，则从前端的收藏列表中删除该对象
          else{
            star_blogs.splice(i,1)
          }
        }
        // 该博客本来不在收藏列表里
        else{
          // 用户收藏了，则加入收藏列表
          if(currPage.data.is_stared==true){
            // 点赞列表一开始为空的
            if(star_blogs.length==0){
              star_blogs = new Array()
            }
            star_blogs.push(blog)
          }
        }
        this.setData({
          publish_blogs: publish_blogs,
          like_blogs: like_blogs,
          star_blogs: star_blogs,
        })
      }
      // 别人的空间，只需要改变点赞，收藏，评论数量在列表页的显示即可
      else{
        // 更新发布过的列表
        let publish_blogs = this.data.publish_blogs
        for(var i=0;i<publish_blogs.length;i++){
          if(publish_blogs[i].blog_id==blog.blog_id){
            publish_blogs[i] = blog
            break
          }
        }
        // 更新点赞列表
        let like_blogs = this.data.like_blogs
        for(var i=0;i<like_blogs.length;i++){
          if(like_blogs[i].blog_id==blog.blog_id){
            like_blogs[i] = blog
            break
          }
        }
        // 更新收藏列表
        let star_blogs = this.data.star_blogs
        for(var i=0;i<star_blogs.length;i++){
          if(star_blogs[i].blog_id==blog.blog_id){
            star_blogs[i] = blog
            break
          }
        }
        this.setData({
          publish_blogs: publish_blogs,
          like_blogs: like_blogs,
          star_blogs: star_blogs,
        })
      }
      
    }
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    // 数据初始化
    this.setData({
      user:[],  // 用户信息
      publish_blogs:[],  // 发布博客
      star_blogs:[],  // 收藏博客
      like_blogs:[],  // 点赞博客
      publish_page:1,
      star_page:1,
      like_page:1,
      limit:20
    })
    // 获取用户顶部信息栏，包括关注列表以及粉丝列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getUserInfoByOpenid',
      data: {
        user_openid: this.data.user_openid
      },
      success: (res) => {
        wx.hideLoading()
        this.setData({
          user: res.data
        })
        console.log(this.data.user)
      }
    })
    // 分页获取用户发的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getPublishBlogsByUserOpenId',
      data: {
        user_openid: this.data.user_openid,
        limit: this.data.limit,
        page: this.data.publish_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
        }
        this.setData({
          publish_blogs: list
        })
        console.log(this.data.publish_blogs)
      }
    })
    // 分页获取用户收藏的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getStarsBlogsByUserOpenId',
      data: {
        user_openid: this.data.user_openid,
        limit: this.data.limit,
        page: this.data.star_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
        }
        this.setData({
          star_blogs: list
        })
        console.log(this.data.star_blogs)
      }
    })
    // 分页获取用户点赞的博客
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getLikesBlogsByUserOpenId',
      data: {
        user_openid: this.data.user_openid,
        limit: this.data.limit,
        page: this.data.like_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
        }
        this.setData({
          like_blogs: list
        })
        console.log(this.data.like_blogs)
      }
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  loadMore: function () {
    // 用户处于发布博客界面，则向后端请求加载更多的publishblog并且加入数组
    if(this.data.currentTab==0){
      this.setData({
        publish_page: this.data.publish_page+1
      })
      console.log("publish"+this.data.publish_page)
      // 分页获取用户发的博客
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getPublishBlogsByUserOpenId',
        data: {
          user_openid: this.data.user_openid,
          limit: this.data.limit,
          page: this.data.publish_page
        },
        success: (res) => {
          wx.hideLoading()
          let publish_blogs = this.data.publish_blogs
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].publish_time = util.formatDate(resdata[i].publish_time , 'yyyy/MM/dd')
            publish_blogs.push(resdata[i])
          }
          this.setData({
            publish_blogs: publish_blogs
          })
          console.log(this.data.publish_blogs)
        }
      })
    }
    // 用户处于收藏博客界面，则向后端请求加载更多的starblog并且加入数组
    else if(this.data.currentTab==1){
      this.setData({
        star_page: this.data.star_page+1
      })
      console.log("star"+this.data.star_page)
      // 分页获取用户收藏的博客
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getStarsBlogsByUserOpenId',
        data: {
          user_openid: this.data.user_openid,
          limit: this.data.limit,
          page: this.data.star_page
        },
        success: (res) => {
          wx.hideLoading()
          let star_blogs = this.data.star_blogs
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].publish_time = util.formatDate(resdata[i].publish_time , 'yyyy/MM/dd')
            star_blogs.push(resdata[i])
          }
          this.setData({
            star_blogs: star_blogs
          })
          console.log(this.data.star_blogs)
        }
      })
    }
    // 用户处于喜欢博客界面，则向后端请求加载更多的likeblog并且加入数组
    else if(this.data.currentTab==2){
      this.setData({
        like_page: this.data.like_page+1
      })
      console.log("like"+this.data.like_page)
      // 分页获取用户赞过的博客
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getLikesBlogsByUserOpenId',
        data: {
          user_openid: this.data.user_openid,
          limit: this.data.limit,
          page: this.data.like_page
        },
        success: (res) => {
          wx.hideLoading()
          let like_blogs = this.data.like_blogs
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].publish_time = util.formatDate(resdata[i].publish_time , 'yyyy/MM/dd')
            like_blogs.push(resdata[i])
          }
          this.setData({
            like_blogs: like_blogs
          })
          console.log(this.data.like_blogs)
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
    if(this.is_preview_image!=true){
      // 多层嵌套必须必须encode
      let string = JSON.stringify(e.currentTarget.dataset.blog)
      string = encodeURIComponent(string)
      wx.navigateTo({
        url: '/pages/blog-detail/blog-detail?blog=' + string,
      })
    }
  },

  // 获取关注列表
  gotoFollowingList(){
    wx.navigateTo({
      url: '/pages/userzone/following-list/following-list?user_openid='+ this.data.user_openid,
    })
  },

  // 获取粉丝列表
  gotoFollowerList(){
    wx.navigateTo({
      url: '/pages/userzone/follower-list/follower-list?user_openid=' + this.data.user_openid,
    })
  },

  // 关注他人
  Follow(){
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
      wx.request({
        url: 'http://127.0.0.1:8081/wx/Following',
        data: {
          myopenid: app.globalData.openid,
          othersopenid: this.data.user_openid,
        },
        success: (res) => {
          wx.hideLoading()
          this.setData({
            is_following: true
          })
          wx.showToast({
            title: '关注成功！',
            duration: 2000
          })
        }
      })
    }
  },

  // 取消关注
  cancelFollow(){
    wx.showModal({
      title: '提示',
      content: '确认要取消关注吗？',
      success:(res)=> {
        if (res.confirm) {
          wx.request({
            url: 'http://127.0.0.1:8081/wx/cancelFollowing',
            data: {
              myopenid: app.globalData.openid,
              othersopenid: this.data.user_openid,
            },
            success: (res) => {
              wx.hideLoading()
              this.setData({
                is_following: false
              })
              wx.showToast({
                title: '取关成功！',
                duration: 2000
              })
            }
          })
        }
      }
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
    this.setData({
      is_preview_image:true
    })
  },
  
  // 点击头像跳转到用户空间
  gotoUserZone(e){
    wx.navigateTo({
      url: '/pages/userzone/userzone?user_openid=' + e.currentTarget.dataset.user_openid,
    })
  },
})
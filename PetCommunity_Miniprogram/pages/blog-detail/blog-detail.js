// pages/community/blog-detail/blog-detail.js

const util = require('../../utils/util')
const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    blog: null,  // 博客内容
    blogCommentList: null,  // 博客所有一级评论
    is_me: null,  // 判断当前博客是否为自己发布的
    is_liked: null,  // 判断当前博客用户是否点赞了
    is_stared: null,  // 判断当前博客用户是否收藏了
    publish_comment_grade: null,  // 将要发表的评论等级，这决定调用哪个接口
    father_comment_id: null, // 所属一级评论id
    reply_target_id: null, // 回复评论的id
    reply_target_userid: null // 回复评论所属用户的openid
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // decode
    let string = decodeURIComponent(options.blog)
    let blog = JSON.parse(string)
    this.setData({
      blog: blog,
      my_openid: app.globalData.openid
    })
    if(blog.user_openid==app.globalData.openid){
      this.setData({
        is_me: true
      })
    }
    else{
      this.setData({
        is_me: false
      })
    }
    // 数据初始化
    this.setData({
      blogCommentList:null,  // 一级评论列表
      comment_page:1,
      limit:10
    })
    // 若不是自己博客，则判断当前博客是否是自己的收藏
    if(this.data.is_me==false){
      wx.request({
        url: 'http://127.0.0.1:8081/wx/isStared',
        data: {
          blog_id: this.data.blog.blog_id,
          user_openid: app.globalData.openid
        },
        success: (res) => {
          wx.hideLoading()
          this.setData({
            is_stared: res.data
          })
          // 是否收藏过的赋值
          var pages = getCurrentPages(); 
          var prevPage = pages[pages.length - 2]; // 上一页
          prevPage.setData({
            is_stared: res.data,
            is_deleted: false  // 是否删除，赋初值为false
          })
        }
      })
    }
    // 若不是自己的博客，则判断当前博客是否已经点赞过
    if(this.data.is_me==false){
      wx.request({
        url: 'http://127.0.0.1:8081/wx/isLiked',
        data: {
          blog_id: this.data.blog.blog_id,
          user_openid: app.globalData.openid
        },
        success: (res) => {
          wx.hideLoading()
          this.setData({
            is_liked: res.data
          })
          // 是否点赞过的赋值
          var pages = getCurrentPages(); 
          var prevPage = pages[pages.length - 2]; // 上一页
          prevPage.setData({
            is_liked: res.data
          })
        }
      })
    }
    // 分页获取评论
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllFatherBlogCommentsByBlogId',
      data: {
        user_openid: app.globalData.openid,
        blog_id: this.data.blog.blog_id,
        limit: this.data.limit,
        page: this.data.comment_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          // 一级评论时间格式化
          list[i].comment_time = util.formatDate(list[i].comment_time, 'yyyy/MM/dd')
          // 所属二级评论时间格式化
          if(list[i].sub_commentList!=null){
            for(var j=0;j<list[i].sub_commentList.length;j++){
              list[i].sub_commentList[j].comment_time = util.formatDate(list[i].sub_commentList[j].comment_time, 'yyyy/MM/dd')
            }
          }
        }
        this.setData({
          blogCommentList: list
        })
        console.log(this.data.blogCommentList)
      }
    })
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    // 数据初始化
    this.setData({
      blogCommentList:null,  // 一级评论列表
      comment_page:1,
      limit:10
    })
    // 若不是自己的博客，则判断当前博客是否是自己的收藏
    if(this.data.is_me==false){
      wx.request({
        url: 'http://127.0.0.1:8081/wx/isStared',
        data: {
          blog_id: this.data.blog.blog_id,
          user_openid: app.globalData.openid
        },
        success: (res) => {
          wx.hideLoading()
          this.setData({
            is_stared: res.data
          })
          // 是否收藏过的赋值
          var pages = getCurrentPages(); 
          var prevPage = pages[pages.length - 2]; // 上一页
          prevPage.setData({
            is_stared: res.data,
            is_deleted: false  // 是否删除，赋初值为false
          })
        }
      })
    }
    // 若不是自己的博客，则判断当前博客是否已经点赞过
    if(this.data.is_me==false){
      wx.request({
        url: 'http://127.0.0.1:8081/wx/isLiked',
        data: {
          blog_id: this.data.blog.blog_id,
          user_openid: app.globalData.openid
        },
        success: (res) => {
          wx.hideLoading()
          this.setData({
            is_liked: res.data
          })
          // 是否点赞过的赋值
          var pages = getCurrentPages(); 
          var prevPage = pages[pages.length - 2]; // 上一页
          prevPage.setData({
            is_liked: res.data
          })
        }
      })
    }
    // 分页获取所有评论
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllFatherBlogCommentsByBlogId',
      data: {
        user_openid: app.globalData.openid,
        blog_id: this.data.blog.blog_id,
        limit: this.data.limit,
        page: this.data.comment_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          // 一级评论时间格式化
          list[i].comment_time = util.formatDate(list[i].comment_time, 'yyyy/MM/dd')
          // 所属二级评论时间格式化
          if(list[i].sub_commentList!=null){
            for(var j=0;j<list[i].sub_commentList.length;j++){
              list[i].sub_commentList[j].comment_time = util.formatDate(list[i].sub_commentList[j].comment_time, 'yyyy/MM/dd')
            }
          }
        }
        this.setData({
          blogCommentList: list
        })
        console.log(this.data.blogCommentList)
      }
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    // 数据初始化
    this.setData({
      comment_page: this.data.comment_page+1,
    })
    // 分页获取博客评论
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllFatherBlogCommentsByBlogId',
      data: {
        user_openid: app.globalData.openid,
        blog_id: this.data.blog.blog_id,
        limit: this.data.limit,
        page: this.data.comment_page
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        let blogCommentList = this.data.blogCommentList
        for(var i=0;i<list.length;i++){
          // 一级评论时间格式化
          list[i].comment_time = util.formatDate(list[i].comment_time, 'yyyy/MM/dd')
          // 所属二级评论时间格式化
          if(list[i].sub_commentList!=null){
            for(var j=0;j<list[i].sub_commentList.length;j++){
              list[i].sub_commentList[j].comment_time = util.formatDate(list[i].sub_commentList[j].comment_time, 'yyyy/MM/dd')
            }
          }
          blogCommentList.push(list[i])
        }
        this.setData({
          blogCommentList: blogCommentList
        })
        // console.log(this.data.blogCommentList)
      }
    })
  },

  // 点击头像跳转到用户空间
  gotoUserZone(e){
    wx.navigateTo({
      url: '/pages/userzone/userzone?user_openid=' + e.currentTarget.dataset.user_openid,
    })
  },

  // 删除博客
  deleteBlog(){
    var that = this
    wx.showModal({
      title: '提示',
      content: '确认要删除这篇博客吗？',
      success:(res)=> {
        if (res.confirm) {
          wx.request({
            url: 'http://127.0.0.1:8081/wx/delBlog',
            data: {
              blog_id: this.data.blog.blog_id,
            },
            success: (res) => {
              wx.hideLoading()
              wx.showToast({
                title: '删除成功！',
              })
              // 跳转到上一界面携带的blog对象，将上一页的列表中相应的blog进行替换，列表页就不需要重新全部加载
              var pages = getCurrentPages(); 
              var prevPage = pages[pages.length - 2]; // 上一页
              prevPage.setData({
                blog: that.data.blog,
                is_deleted: true
              })
              setTimeout(() => {
                wx.navigateBack()
              }, 1500)
            }
          })
        }
      }
	  })
  },

  // 点击博客点赞按钮
  likeBlog(){
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
      if(this.data.is_me==true){
        wx.showToast({
          title: '不能给自己点赞',
          icon: 'none',
          duration: 1500
        })
      }
      // 别人的博客
      else{
        // 已经点赞了，再点击就取消
        if(this.data.is_liked==true){
          wx.request({
            url: 'http://127.0.0.1:8081/wx/cancelLikeBlog',
            data: {
              user_openid: app.globalData.openid,
              blog_id: this.data.blog.blog_id,
            },
            success: (res) => {
              wx.hideLoading()
              let blog = this.data.blog
              blog.likes -= 1
              this.setData({
                is_liked: false,
                blog: blog
              })
              // 跳转到上一界面携带的blog对象，将上一页的列表中相应的blog进行替换，列表页就不需要重新全部加载
              var pages = getCurrentPages(); 
              var prevPage = pages[pages.length - 2]; // 上一页
              prevPage.setData({
                blog: this.data.blog,
                is_liked: false
              })
            }
          })
        }
        // 没点过赞
        else{
          wx.request({
            url: 'http://127.0.0.1:8081/wx/likeBlog',
            data: {
              user_openid: app.globalData.openid,
              blog_id: this.data.blog.blog_id,
            },
            success: (res) => {
              wx.hideLoading()
              let blog = this.data.blog
              blog.likes += 1
              this.setData({
                is_liked: true,
                blog: blog
              })
              // 跳转到上一界面携带的blog对象，将上一页的列表中相应的blog进行替换，列表页就不需要重新全部加载
              var pages = getCurrentPages(); 
              var prevPage = pages[pages.length - 2]; // 上一页
              prevPage.setData({
                blog: this.data.blog,
                is_liked: true
              })
              // console.log(prevPage.data.blog)
            }
          })
        }
      }
    }
  },
  
  // 收藏博客
  starBlog(){
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
      if(this.data.is_me==true){
        wx.showToast({
          title: '不能给自己收藏',
          icon: 'none',
          duration: 1500
        })
      }
      // 别人的博客
      else{
        // 已经收藏了，再点击就取消
        if(this.data.is_stared==true){
          wx.request({
            url: 'http://127.0.0.1:8081/wx/cancelStarBlog',
            data: {
              user_openid: app.globalData.openid,
              blog_id: this.data.blog.blog_id,
            },
            success: (res) => {
              wx.hideLoading()
              let blog = this.data.blog
              blog.stars -= 1
              this.setData({
                is_stared: false,
                blog: blog
              })
              // 跳转到上一界面携带的blog对象，将上一页的列表中相应的blog进行替换，列表页就不需要重新全部加载
              var pages = getCurrentPages(); 
              var prevPage = pages[pages.length - 2]; // 上一页
              prevPage.setData({
                blog: this.data.blog,
                is_stared: false
              })
            }
          })
        }
        // 没有收藏
        else{
          wx.request({
            url: 'http://127.0.0.1:8081/wx/starBlog',
            data: {
              user_openid: app.globalData.openid,
              blog_id: this.data.blog.blog_id,
            },
            success: (res) => {
              wx.hideLoading()
              let blog = this.data.blog
              blog.stars += 1
              this.setData({
                is_stared: true,
                blog: blog
              })
              // 跳转到上一界面携带的blog对象，将上一页的列表中相应的blog进行替换，列表页就不需要重新全部加载
              var pages = getCurrentPages(); 
              var prevPage = pages[pages.length - 2]; // 上一页
              prevPage.setData({
                blog: this.data.blog,
                is_stared: true
              })
            }
          })
        }
      }
    }
  },

  // 点赞评论
  likeComment(e){
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
      let currentComment = e.currentTarget.dataset.blog_comment
      if(currentComment.user_openid==app.globalData.openid){
        wx.showToast({
          title: '不能给自己点赞',
          icon: 'none',
          duration: 1500
        })
      }
      // 别人的评论
      else{
        // 已经点赞了，再点击就取消
        if(currentComment.liked==true){
          wx.request({
            url: 'http://127.0.0.1:8081/wx/cancelLikeBlogComment',
            data: {
              user_openid: app.globalData.openid,
              blog_comment_id: currentComment.blog_comment_id,
            },
            success: (res) => {
              wx.hideLoading()
              // 前端展示修改，寻找取消点赞的那条评论并且改点赞数和点赞状态
              let blogCommentList = this.data.blogCommentList
              // 一级评论，那只需要在一级评论里面比较即可
              if(currentComment.grade==1){
                for(var i=0;i<blogCommentList.length;i++){
                  if(blogCommentList[i].blog_comment_id==currentComment.blog_comment_id){
                    blogCommentList[i].liked = false
                    blogCommentList[i].likes -= 1
                  }
                }
              }
              // 二级评论，那需要到一级评论下的二级评论里面找
              else if(currentComment.grade==2){
                for(var i=0;i<blogCommentList.length;i++){
                  if(blogCommentList[i].sub_commentList!=null){
                    for(var j=0;j<blogCommentList[i].sub_commentList.length;j++){
                      if(blogCommentList[i].sub_commentList[j].blog_comment_id==currentComment.blog_comment_id){
                        blogCommentList[i].sub_commentList[j].liked = false
                        blogCommentList[i].sub_commentList[j].likes -= 1
                      }
                    }
                  }
                }
              }
              this.setData({
                blogCommentList:blogCommentList
              })
              console.log(this.data.blogCommentList)
            }
          })
        }
        // 没有点过赞
        else{
          wx.request({
            url: 'http://127.0.0.1:8081/wx/likeBlogComment',
            data: {
              user_openid: app.globalData.openid,
              blog_comment_id: currentComment.blog_comment_id,
            },
            success: (res) => {
              wx.hideLoading()
              // 前端展示修改，寻找点赞的那条评论并且改点赞数和点赞状态
              let blogCommentList = this.data.blogCommentList
              // 一级评论，那只需要在一级评论里面比较即可
              if(currentComment.grade==1){
                for(var i=0;i<blogCommentList.length;i++){
                  if(blogCommentList[i].blog_comment_id==currentComment.blog_comment_id){
                    blogCommentList[i].liked = true
                    blogCommentList[i].likes += 1
                  }
                }
              }
              // 二级评论，那需要到一级评论下的二级评论里面找
              else if(currentComment.grade==2){
                for(var i=0;i<blogCommentList.length;i++){
                  if(blogCommentList[i].sub_commentList!=null){
                    for(var j=0;j<blogCommentList[i].sub_commentList.length;j++){
                      if(blogCommentList[i].sub_commentList[j].blog_comment_id==currentComment.blog_comment_id){
                        blogCommentList[i].sub_commentList[j].liked = true
                        blogCommentList[i].sub_commentList[j].likes += 1
                      }
                    }
                  }
                }
              }
              this.setData({
                blogCommentList:blogCommentList
              })
              console.log(this.data.blogCommentList)
            }
          })
        }
      }
    }
  },

  // 删除评论
  deleteComment(e){
    let currentComment = e.currentTarget.dataset.blog_comment
    wx.showModal({
      title: '提示',
      content: '确认要删除这条评论吗？',
      success:(res)=> {
        if (res.confirm) {
          wx.request({
            url: 'http://127.0.0.1:8081/wx/delBlogComment',
            data: {
              blog_id: this.data.blog.blog_id,
              blog_comment_id: currentComment.blog_comment_id,
              grade: currentComment.grade
            },
            success: (res) => {
              wx.hideLoading()
              // 前端展示修改
              let blogCommentList = this.data.blogCommentList
              // 删除的是一级评论
              if(currentComment.grade==1){
                for(var i=0;i<blogCommentList.length;i++){
                  if(blogCommentList[i].blog_comment_id==currentComment.blog_comment_id){
                    // 修改前端评论条目数量
                    let blog = this.data.blog
                    // 总评论数减1
                    blog.blog_comment_count -= 1
                    // 若有二级评论列表则还要减去二级评论列表长度
                    if(blogCommentList[i].sub_commentList!=null){
                      blog.blog_comment_count -= blogCommentList[i].sub_commentList.length
                    }
                    this.setData({
                      blog: blog
                    })
                    // 跳转到上一界面携带的blog对象，将上一页的列表中相应的blog进行替换，列表页就不需要重新全部加载
                    var pages = getCurrentPages(); 
                    var prevPage = pages[pages.length - 2]; // 上一页
                    prevPage.setData({
                      blog: this.data.blog
                    })
                    blogCommentList.splice(i, 1)
                  }
                }
              }
              // 删除的是二级评论
              else if(currentComment.grade==2){
                for(var i=0;i<blogCommentList.length;i++){
                  if(blogCommentList[i].sub_commentList!=null){
                    for(var j=0;j<blogCommentList[i].sub_commentList.length;j++){
                      if(blogCommentList[i].sub_commentList[j].blog_comment_id== currentComment.blog_comment_id){
                        // 修改前端评论条目数量
                        let blog = this.data.blog
                        blog.blog_comment_count-=1
                        this.setData({
                          blog: blog
                        })
                        // 跳转到上一界面携带的blog对象，将上一页的列表中相应的blog进行替换，列表页就不需要重新全部加载
                        var pages = getCurrentPages(); 
                        var prevPage = pages[pages.length - 2]; // 上一页
                        prevPage.setData({
                          blog: this.data.blog
                        })
                        blogCommentList[i].sub_commentList.splice(j, 1)
                      }
                    }
                  }
                }
              }
              this.setData({
                blogCommentList: blogCommentList
              })
              wx.showToast({
                title: '删除成功！',
              })
            }
          })
        }
      }
	  })
  },

  // 点击底部评论按钮，发表一级评论，调用输入框
  showFatherCommentModel(){
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
      this.setData({
        publish_comment_grade:1,  // 设置将要发表评论的等级
      })
      this.showModal();
    }
  },

  // 点击一级评论内容，评论的是一级评论，没有回复对象
  showSubCommentModel(e){
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
      let father_comment_id = e.currentTarget.dataset.father_comment_id
      this.setData({
        publish_comment_grade:2,  // 设置将要发表评论的等级
        father_comment_id: father_comment_id, // 所属一级评论id
      })
      this.showModal();
    }
  },

  // 点击二级评论内容，回复他人评论，发表的是二级评论
  showReplySubCommentModel(e){
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
      let father_comment_id = e.currentTarget.dataset.father_comment_id
      let reply_target_id = e.currentTarget.dataset.reply_target_id
      let reply_target_userid = e.currentTarget.dataset.reply_target_userid
      if(reply_target_userid==app.globalData.openid){
        wx.showToast({
          title: '不能回复自己',
          icon: 'none',
          duration: 1500
        })
      }
      else{
        this.setData({
          publish_comment_grade:2,  // 设置将要发表评论的等级
          father_comment_id: father_comment_id, // 所属一级评论id
          reply_target_id: reply_target_id, // 回复评论的id
          reply_target_userid: reply_target_userid // 回复评论所属用户的openid
        })
        this.showModal();
      }
    }
  },

  // 获取评论区域输入内容
  onInput(event) {
    this.setData({
      commentValue: event.detail.value.trim()
    })
  },

  // 发表评论
  publishComment(event) {
    let content = this.data.commentValue;
    if (!content) return
    wx.showLoading({
      title: '正在发表评论'
    })
    // 发表一级评论
    if(this.data.publish_comment_grade==1){
      wx.request({
        url: 'http://127.0.0.1:8081/wx/publishFatherComment',
        data: {
          user_openid: app.globalData.openid,
          blog_id: this.data.blog.blog_id,
          comment_content: content
        },
        success: (res) => {
          wx.hideLoading()
          wx.showToast({
            title: '发表评论成功！'
          })
          console.log(res.data)
          // 将发表的评论在前端展示，加入前端的commentList
          let blogCommentList = this.data.blogCommentList
          res.data.comment_time = util.formatDate(res.data.comment_time, 'yyyy/MM/dd')
          // 博客没有一级评论的话需要创建数组
          if(blogCommentList.length==0){
            blogCommentList = new Array()
          }
          blogCommentList.push(res.data)
          let blog = this.data.blog
          blog.blog_comment_count+=1
          this.setData({
            blogCommentList: blogCommentList,
            blog: blog
          })
          // 跳转到上一界面携带的blog对象，将上一页的列表中相应的blog进行替换，列表页就不需要重新全部加载
          var pages = getCurrentPages(); 
          var prevPage = pages[pages.length - 2]; // 上一页
          prevPage.setData({
            blog: this.data.blog
          })
          this.hideModal()
        }
      })
    }
    // 发表二级评论
    else if(this.data.publish_comment_grade==2){
      // 回复一级评论
      if(this.data.reply_target_id==null){
        wx.request({
          url: 'http://127.0.0.1:8081/wx/publishSubComment',
          data: {
            user_openid: app.globalData.openid,
            blog_id: this.data.blog.blog_id,
            comment_content: content,
            father_comment_id: this.data.father_comment_id
          },
          success: (res) => {
            wx.hideLoading()
            wx.showToast({
              title: '发表评论成功！'
            })
            console.log(res.data)
            // 将发表的评论在前端展示，加入前端的commentList
            let blogCommentList = this.data.blogCommentList
            for(var i=0;i<blogCommentList.length;i++){
              if(blogCommentList[i].blog_comment_id==this.data.father_comment_id){
                res.data.comment_time = util.formatDate(res.data.comment_time, 'yyyy/MM/dd')
                // 若该一级评论原来没有子评论数组，则创建
                if(blogCommentList[i].sub_commentList==null){
                  blogCommentList[i].sub_commentList = [res.data]
                }
                // 若该一级评论原来就有子评论数组，直接插入
                else{
                  blogCommentList[i].sub_commentList.push(res.data)
                }
              }
            }
            let blog = this.data.blog
            blog.blog_comment_count+=1
            this.setData({
              blogCommentList: blogCommentList,
              blog: blog,
              father_comment_id: null,
            })
            // 跳转到上一界面携带的blog对象，将上一页的列表中相应的blog进行替换，列表页就不需要重新全部加载
            var pages = getCurrentPages(); 
            var prevPage = pages[pages.length - 2]; // 上一页
            prevPage.setData({
              blog: this.data.blog
            })
            this.hideModal()
          }
        })
      }
      // 回复二级评论
      else{
        wx.request({
          url: 'http://127.0.0.1:8081/wx/replySubComment',
          data: {
            user_openid: app.globalData.openid,
            blog_id: this.data.blog.blog_id,
            comment_content: content,
            father_comment_id: this.data.father_comment_id,
            reply_target_id: this.data.reply_target_id,
            reply_target_userid: this.data.reply_target_userid
          },
          success: (res) => {
            wx.hideLoading()
            wx.showToast({
              title: '发表评论成功！'
            })
            console.log(res.data)
            // 将发表的评论在前端展示，加入前端的commentList
            let blogCommentList = this.data.blogCommentList
            for(var i=0;i<blogCommentList.length;i++){
              if(blogCommentList[i].blog_comment_id==this.data.father_comment_id){
                res.data.comment_time = util.formatDate(res.data.comment_time, 'yyyy/MM/dd')
                // 若该一级评论原来没有子评论数组，则创建
                if(blogCommentList[i].sub_commentList==null){
                  blogCommentList[i].sub_commentList = [res.data]
                }
                // 若该一级评论原来就有子评论数组，直接插入
                else{
                  blogCommentList[i].sub_commentList.push(res.data)
                }
              }
            }
            let blog = this.data.blog
            blog.blog_comment_count+=1
            this.setData({
              blogCommentList: blogCommentList,
              blog: blog,
              father_comment_id: null,
              reply_target_id: null,
              reply_target_userid: null
            })
            // 跳转到上一界面携带的blog对象，将上一页的列表中相应的blog进行替换，列表页就不需要重新全部加载
            var pages = getCurrentPages(); 
            var prevPage = pages[pages.length - 2]; // 上一页
            prevPage.setData({
              blog: this.data.blog
            })
            this.hideModal()
          }
        })
      }
    }
    console.log(this.data.blogCommentList)
  },

  //显示对话框
  showModal: function () {
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
      showModalStatus: true,
      count: 1
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export()
      })
    }.bind(this), 200)
  },

  //隐藏对话框
  hideModal: function () {
    // 隐藏遮罩层
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
      count: 1,
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export(),
        showModalStatus: false,
        father_comment_id: null,
        reply_target_id: null,
        reply_target_userid: null
      })
    }.bind(this), 200)
  },

  // 预览图片
  showImage:function(e){
    let index = e.currentTarget.dataset.imageid
    let list = this.data.blog.images_List
    wx.previewImage({
      urls: list,
      current: list[index]
    })
  },
})
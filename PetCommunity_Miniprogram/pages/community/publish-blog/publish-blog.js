// pages/community/publish-blog/publish-blog.js
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    content:"",  // 上传文本内容
    location: "",  // 发布博客定位
    imageList: [],  // 上传图片临时路径列表
    images: ""  // 上传图片在服务器上的虚拟路径字符串拼接
  },

  // 监听文本框的输入内容并且赋值
  bindContent: function(e) {
    this.setData({
      content: e.detail.value
    })
  },

  // 向list中添加图片
  addNewImage(imagePath){
    var list = this.data.imageList
    list = list.concat(imagePath)
    this.setData({
      imageList: list
    })
  },

  // 选择与上传图片
  chooseImage: function(e){
    if(this.data.imageList.length==9){
      wx.showToast({
        title: '最多上传9张图片！',
      })
    }
    else{
      var that = this
      let surplus = 9 - this.data.imageList.length
      wx.chooseImage({
        count: surplus,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: function(res){
          that.addNewImage(res.tempFilePaths)
        }
      })
    }
  },

  // 预览图片
  showImage:function(e){
    let index = e.currentTarget.dataset.imageid
    let list = this.data.imageList
    wx.previewImage({
      urls: list,
      current: list[index]
    })
  },

  // 删除图片
  deleteImage: function(e){
    let index = e.currentTarget.dataset.imageid
    let list = this.data.imageList
    list.splice(index, 1)
    this.setData({
      imageList: list
    })
  },

  // 获取地理位置
  getCityName(address) {
    let city = undefined
    if (address) {
      let index0 = address.indexOf('省')
      let index1 = address.indexOf('市')
      if (index0 > 0 && index1 > 0 && index1 > index0 ) {
        city = address.substring(index0+1, index1+1)
      } else if (address.includes('北京市')) {
        city = '北京市'
      } else if (addr.includes('上海市')) {
        city = '上海市'
      } else if (addr.includes('天津市')) {
        city = '天津市'
      } else if (addr.includes('重庆市')) {
        city = '重庆市'
      }
    }
    return city
  },

  // 选择定位
  chooseLocation:function(e){
    wx.showLoading({
      title: '正在加载',
    })
    wx.chooseLocation({
      success:(res)=>{
        wx.hideLoading({
          success: (res) => {},
        })
        let address = ''
        let locName = res.name
        let city = this.getCityName(res.address)
        if(city){
          address = city + '·' + locName
        }else{
          address = locName
        }
        this.setData({
          location: address
        })
      },
      fail:(res)=>{
        wx.hideLoading({
          success: (res) => {},
        })
      }
    })
  },

  // 删除定位
  deleteLocation:function(e){
    this.setData({
      location: ''
    })
  },

  // 用户发布博客
  publish(){
    if(this.data.content==""&&this.data.imageList.length==0){
      wx.showToast({
        icon: "none",
        title: '发布内容不能为空',
      })
    }
    else{
      // 有图片，则递归上传图片
      if(this.data.imageList.length!=0){
        let imageList = this.data.imageList
        let images = this.data.images
        this.upload(imageList,0,images)
      }
      // 无图片，直接调用后端接口上传
      else{
        wx.showLoading({
          title: '发表中...',
        })
        // 请求后端接口
        wx.request({
          url: 'http://127.0.0.1:8081/wx/publishBlog',
          data: {
            user_openid: app.globalData.openid,
            location: this.data.location,
            blog_content: this.data.content,
            blog_images: this.data.images
          },
          success:function(res){
            wx.hideLoading()
            wx.showToast({
              title: '发表成功！'
            })
            setTimeout(() => {
              wx.navigateBack()
            }, 1500)
          }
        })
      }
    }
  },

  // 将图片递归上传到后端服务器
  upload:function(imageList,i,images){
    var that = this
    wx.showLoading({
      title: '发表中...',
    })
    wx.uploadFile({
      // 后端接口
      url: "http://127.0.0.1:8081/wxuser_upload",
      // 获取图片路径
      filePath: imageList[i],
      // 请求头
      header: {
      'content-type': 'multipart/form-data'
      },
      // 后台获取图片的key
      name: 'file',
      // 额外的参数formData
      formData: {
        'user_openid': app.globalData.openid
      },
      success: function(res) {
        let result = JSON.parse(res.data)
        images = images + result.msg + ","
        i++
        // 已经将所有图片上传到服务器，并且拿到了虚拟路径列表，则发布博客
        if(i==imageList.length){
          images = images.substr(0, images.length - 1)
          console.log(images)
          // 请求后端接口
          wx.request({
            url: 'http://127.0.0.1:8081/wx/publishBlog',
            data: {
              user_openid: app.globalData.openid,
              location: that.data.location,
              blog_content: that.data.content,
              blog_images: images
            },
            success:function(res){
              wx.hideLoading()
              wx.showToast({
                title: '发表成功！'
              })
              setTimeout(() => {
                wx.navigateBack()
              }, 1500)
            }
          })
        }
        // 未上传完，继续递归调用
        else{
          that.upload(imageList,i,images)
        }
      },
    })
  },

  // 取消发布博客
  cancel(){
    wx.navigateBack({
      delta: 1,
    })
  }

})
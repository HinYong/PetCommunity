// pages/pet-help/publish-help/publish-help.js
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    contact_name: "",  // 联系人姓名
    contact_phone: "",  // 联系电话
    province: "",  // 省份
    city: "",  // 城市
    detail_address: "",  // 详细地址
    location: "",  // 完整地址
    content:"",  // 上传文本内容
    imageList: [],  // 上传图片临时路径列表
    images: ""  // 上传图片在服务器上的虚拟路径字符串拼接
  },

  // 监听文本框的输入内容并且赋值
  bindContent: function(e) {
    this.setData({
      content: e.detail.value
    })
  },

  // 监听联系人姓名的输入内容并且赋值
  bindName: function(e) {
    this.setData({
      contact_name: e.detail.value
    })
  },

  // 监听联系人电话的输入内容并且赋值
  bindPhone: function(e) {
    this.setData({
      contact_phone: e.detail.value
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

  // 处理地理位置
  processAddress(res) {
    let city = undefined
    let province = undefined
    let detail_address = undefined
    let address = res.address
    let locName = res.name
    if (address) {
      let index0 = address.indexOf('省')
      let index1 = address.indexOf('市')
      if (index0 > 0 && index1 > 0 && index1 > index0 ) {
        province = address.substring(0, index0+1)
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
      detail_address = address.substring(index1+1) + locName
      // console.log(province)
      // console.log(city)
      // console.log(detail_address)
      this.setData({
        province:province,
        city:city,
        detail_address:detail_address,
        location:province+city+detail_address
      })
    }
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
        this.processAddress(res)
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

  // 用户发布求助内容
  publish(){
    if(this.data.content==""){
      wx.showToast({
        icon: "none",
        title: '描述内容不能为空',
      })
    }
    else if(this.data.imageList.length==0){
      wx.showToast({
        icon: "none",
        title: '上传图片不能为空',
      })
    }
    else if(this.data.contact_name==""){
      wx.showToast({
        icon: "none",
        title: '联系人姓名不能为空',
      })
    }
    else if(this.data.contact_phone==""){
      wx.showToast({
        icon: "none",
        title: '联系人电话不能为空',
      })
    }
    else if(this.data.location==""){
      wx.showToast({
        icon: "none",
        title: '地址不能为空',
      })
    }
    else{
      let that = this
      wx.request({
        url: 'http://127.0.0.1:8081/wx/findAgencyByCity',
        data: {
          city: this.data.city,
        },
        success:function(res){
          wx.hideLoading()
          if(res.data==200){
            // 递归上传图片与描述内容
            let imageList = that.data.imageList
            let images = that.data.images
            that.upload(imageList,0,images)
          }
          else{
            wx.showModal({
              title: '提示',
              content: '当前城市暂时没有流浪动物救助机构入驻平台，我们会尽快完善信息！',
            })
          }
        }
      })
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
            url: 'http://127.0.0.1:8081/wx/publishHelp',
            data: {
              user_openid: app.globalData.openid,
              contact_name: that.data.contact_name,
              contact_phone: that.data.contact_phone,
              province: that.data.province,
              city: that.data.city,
              detail_address: that.data.detail_address,
              help_content: that.data.content,
              help_images: images
            },
            success:function(res){
              // 发表成功，返回上一页面需要重新加载“未处理”的请求列表
              var pages = getCurrentPages(); 
              var prevPage = pages[pages.length - 2]; // 上一页
              prevPage.setData({
                is_publish_help: true
              })
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
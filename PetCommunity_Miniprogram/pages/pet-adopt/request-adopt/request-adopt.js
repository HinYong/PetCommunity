// pages/pet-adopt/request-adopt/request-adopt.js
var app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    adopt: [],
    contact_name: "",  // 联系人姓名
    contact_phone: "",  // 联系电话
    contact_address: "",  // 联系人所在地
    content: "",  // 上传文本内容
    imageList: [],  // 上传图片临时路径列表
    images: ""  // 上传图片在服务器上的虚拟路径字符串拼接
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // decode
    let string = decodeURIComponent(options.adopt)
    let adopt = JSON.parse(string)
    this.setData({
      adopt: adopt,
    })
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
    let address = res.address
    let locName = res.name
    if (address) {
      this.setData({
        contact_address: address+locName
      })
      console.log(this.data.contact_address)
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
      contact_address: ''
    })
  },

  // 用户提交申请
  requestAdopt(){
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
    else if(this.data.contact_address==""){
      wx.showToast({
        icon: "none",
        title: '联系人地址不能为空',
      })
    }
    else{
      // 递归上传图片与描述内容
      let imageList = this.data.imageList
      let images = this.data.images
      this.upload(imageList,0,images)
    }
  },

  // 将图片递归上传到后端服务器
  upload:function(imageList,i,images){
    var that = this
    wx.showLoading({
      title: '提交申请中...',
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
            url: 'http://127.0.0.1:8081/wx/publishAdoptRequest',
            data: {
              adopt_id: that.data.adopt.adopt_id,
              agency_id: that.data.adopt.agency_id,
              user_openid: app.globalData.openid,
              contact_name: that.data.contact_name,
              contact_phone: that.data.contact_phone,
              contact_address: that.data.contact_address,
              request_content: that.data.content,
              request_images: images
            },
            success:function(res){
              wx.hideLoading()
              wx.showToast({
                title: '申请提交成功！'
              })
              setTimeout(() => {
                wx.navigateBack({
                  delta: 2,
                })
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
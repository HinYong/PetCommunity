// pages/pet-adopt/pet-adopt.js

const app = getApp()
const util = require('../../utils/util')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    dogList: [], // 狗狗领养需求列表
    catList:[], // 猫咪领养需求列表
    dog_page:1,
    cat_page:1,
    limit:20,
    navbar: ['狗狗', '猫咪'],
    currentTab: 0,
    searchContent: null
  },

  onLoad() {
    // 数据初始化
    this.setData({
      dogList: [], // 狗狗领养需求列表
      catList:[], // 猫咪领养需求列表
      dog_page:1,
      cat_page:1,
      limit:20,
    })
    // 分页获取狗狗领养请求列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllPublishAdoptsByPetType',
      data: {
        page: this.data.dog_page,
        limit: this.data.limit,
        pet_type_id: 1,
        searchContent: this.data.searchContent
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time1 = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
          list[i].publish_time = util.formatTime(list[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
        }
        this.setData({
          dogList: list
        })
        console.log(this.data.dogList)
      }
    })
    // 分页获取猫咪领养请求列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllPublishAdoptsByPetType',
      data: {
        page: this.data.cat_page,
        limit: this.data.limit,
        pet_type_id: 2,
        searchContent: this.data.searchContent
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time1 = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
          list[i].publish_time = util.formatTime(list[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
        }
        this.setData({
          catList: list
        })
        console.log(this.data.catList)
      }
    })
  },

  onPullDownRefresh(){
    // 数据初始化
    this.setData({
      dogList: [], // 狗狗领养需求列表
      catList:[], // 猫咪领养需求列表
      dog_page:1,
      cat_page:1,
      limit:20,
    })
    // 分页获取狗狗领养请求列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllPublishAdoptsByPetType',
      data: {
        page: this.data.dog_page,
        limit: this.data.limit,
        pet_type_id: 1,
        searchContent: this.data.searchContent
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time1 = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
          list[i].publish_time = util.formatTime(list[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
        }
        this.setData({
          dogList: list
        })
        console.log(this.data.dogList)
      }
    })
    // 分页获取猫咪领养请求列表
    wx.request({
      url: 'http://127.0.0.1:8081/wx/getAllPublishAdoptsByPetType',
      data: {
        page: this.data.cat_page,
        limit: this.data.limit,
        pet_type_id: 2,
        searchContent: this.data.searchContent
      },
      success: (res) => {
        wx.hideLoading()
        let list = res.data
        for(var i=0;i<list.length;i++){
          list[i].publish_time1 = util.formatDate(list[i].publish_time , 'yyyy/MM/dd')
          list[i].publish_time = util.formatTime(list[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
        }
        this.setData({
          catList: list
        })
        console.log(this.data.catList)
      }
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  loadMore: function () {
    // 用户处于狗狗领养需求界面，则向后端请求加载更多的狗狗领养需求并且加入数组
    if(this.data.currentTab==0){
      this.setData({
        dog_page: this.data.dog_page+1
      })
      console.log("dog_page"+this.data.dog_page)
      // 分页获取狗狗领养需求
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getAllPublishAdoptsByPetType',
        data: {
          page: this.data.dog_page,
          limit: this.data.limit,
          pet_type_id: 1,
          searchContent: this.data.searchContent
        },
        success: (res) => {
          wx.hideLoading()
          let dogList = this.data.dogList
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].publish_time1 = util.formatDate(resdata[i].publish_time , 'yyyy/MM/dd')
            resdata[i].publish_time = util.formatTime(resdata[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
            dogList.push(resdata[i])
          }
          this.setData({
            dogList: dogList
          })
          console.log(this.data.dogList)
        }
      })
    }
    // 用户处于猫咪领养需求界面，则向后端请求加载更多的猫咪领养需求并且加入数组
    else if(this.data.currentTab==1){
      this.setData({
        cat_page: this.data.cat_page+1
      })
      console.log("cat_page"+this.data.cat_page)
      // 分页获取猫咪领养需求
      wx.request({
        url: 'http://127.0.0.1:8081/wx/getAllPublishAdoptsByPetType',
        data: {
          page: this.data.cat_page,
          limit: this.data.limit,
          pet_type_id: 2,
          searchContent: this.data.searchContent
        },
        success: (res) => {
          wx.hideLoading()
          let catList = this.data.catList
          let resdata = res.data
          for(var i=0;i<resdata.length;i++){
            resdata[i].publish_time1 = util.formatDate(resdata[i].publish_time , 'yyyy/MM/dd')
            resdata[i].publish_time = util.formatTime(resdata[i].publish_time , 'yyyy/MM/dd/HH/mm/ss')
            catList.push(resdata[i])
          }
          this.setData({
            catList: catList
          })
          console.log(this.data.catList)
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

  // 跳转到领养需求详情页
  gotoAdoptDetail(e){
    // 多层嵌套必须必须encode
    let string = JSON.stringify(e.currentTarget.dataset.adopt)
    string = encodeURIComponent(string)
    wx.navigateTo({
      url: '/pages/pet-adopt/adopt-detail/adopt-detail?adopt=' + string,
    })
  },
  
  // 跳转到我的申请页面
  gotoMyRequests(){
    wx.navigateTo({
      url: '/pages/pet-adopt/my-requests/my-requests'
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

})
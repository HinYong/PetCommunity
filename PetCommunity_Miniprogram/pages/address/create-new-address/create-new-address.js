// pages/address/createnewaddress/create-new-address.js

const app = getApp()
const util = require('../../../utils/util')
const formMethods = require('../../../components/form/utils/formMethods.js')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    is_checked: false,
    is_default: 0,
    form: [
      { label: '收件人', submitName: 'receiver_name', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入收件人', rule: { required: true }, message: { required: '请输入收件人' }},
      { label: '联系电话', submitName: 'receiver_phone', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入联系电话', rule: { required: true }, message: { required: '请输入联系电话' }},
      { label: '省', submitName: 'province', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入省份', rule: { required: true }, message: { required: '请输入省份' }},
      { label: '市', submitName: 'city', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入市', rule: { required: true }, message: { required: '请输入市' }},
      { label: '区(县)', submitName: 'area', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入区(县)', rule: { required: true }, message: { required: '请输入区(县)'}},
      { label: '详细地址', submitName: 'detail_address', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入详细地址', rule: { required: true }, message: { required: '请输入详细地址' }},
      { label: '是否设为默认', submitName: 'is_default', inputType: 'checkbox', value: ''},
    ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // input验证
    formMethods.initValidate(this.data.form, this);
    // 是添加新地址
    if (options.flag==0){
      this.setData({
        flag: options.flag,
        is_checked: false,
        openid: app.globalData.openid,
      })
    }
    // 是编辑地址 
    else{
      let id=options.id;
      let receiver_name= options.receiver_name;
      let receiver_phone= options.receiver_phone;
      let province=options.province;
      let city=options.city;
      let area=options.area;
      let detail_address=options.detail_address;
      let is_default= options.is_default;
      if (is_default==0){
        this.setData({
          is_checked: false
        })
      }
      else{
        this.setData({
          is_checked: true
        })
      }
      this.setData({
        id: id,
        flag: options.flag,
        is_default: is_default,
        openid: app.globalData.openid,
        form: [
          { label: '收件人', submitName: 'receiver_name', type: 'input', inputType: 'text', value: receiver_name, requied: true, placeholder: '请输入收件人', rule: { required: true }, message: { required: '请输入收件人' }},
          { label: '联系电话', submitName: 'receiver_phone', type: 'input', inputType: 'text', value: receiver_phone, requied: true, placeholder: '请输入联系电话', rule: { required: true }, message: { required: '请输入联系电话' }},
          { label: '省', submitName: 'province', type: 'input', inputType: 'text', value: province, requied: true, placeholder: '请输入省份', rule: { required: true }, message: { required: '请输入省份' }},
          { label: '市', submitName: 'city', type: 'input', inputType: 'text', value: city, requied: true, placeholder: '请输入市', rule: { required: true }, message: { required: '请输入市' }},
          { label: '区(县)', submitName: 'area', type: 'input', inputType: 'text', value: area, requied: true, placeholder: '请输入区(县)', rule: { required: true }, message: { required: '请输入区(县)' }},
          { label: '详细地址', submitName: 'detail_address', type: 'input', inputType: 'text', value: detail_address, requied: true, placeholder: '请输入详细地址', rule: { required: true }, message: { required: '请输入详细地址' }},
          { label: '是否设为默认', submitName: 'is_default', inputType: 'checkbox', value: is_default},
        ]
      })
    }
  },

  changeSwitch: function(e){
    console.log(e.detail.value)
    if(e.detail.value==true){
      this.setData({
        is_default: 1
      })
    }
    else{
      this.setData({
        is_default: 0
      })
    }
    console.log(this.data.is_default)
  },

  formSubmit: function (e) {
    // 数据校验
    const self = this;
    const { form } = this.data;
    const address_id = this.data.id
    const formValidate = {};
    form.forEach(item => { formValidate[item.submitName] = item.value});
    /**
     * 传入表单数据，调用验证方法
     * ！！！
     * requied为true时，会默认校验是否必填；当自己增加校验规则时，会覆盖该默认方法
     * */ 
    if (!self.WxValidate.checkForm(formValidate)) {
      const error = self.WxValidate.errorList[0];
      wx.showToast({ title: error.msg, icon: 'none' });
      return false
    };
    // 数据校验结束
    // 新增地址
    if (this.data.flag==0){
      console.log("新增")
      wx.request({
        url: 'http://127.0.0.1:8081/wx/createUserAddress',
        data:{
          openid: app.globalData.openid,
          receiver_name: form[0].value,
          receiver_phone: form[1].value,
          province: form[2].value,
          city: form[3].value,
          area: form[4].value,
          detail_address: form[5].value,
          is_default: this.data.is_default
        },
        success: (res) => {
          wx.hideLoading()
          wx.showToast({
            title: '添加成功！'
          })
          setTimeout(() => {
            wx.navigateBack()
          }, 1500)
        }
      })
    }
    // 编辑地址
    else{
      console.log("编辑")
      wx.request({
        url: 'http://127.0.0.1:8081/wx/editUserAddress',
        data:{
          openid: app.globalData.openid,
          id: address_id,
          receiver_name: form[0].value,
          receiver_phone: form[1].value,
          province: form[2].value,
          city: form[3].value,
          area: form[4].value,
          detail_address: form[5].value,
          is_default: this.data.is_default
        },
        success: (res) => {
          wx.hideLoading()
          wx.showToast({
            title: '编辑成功！'
          })
          setTimeout(() => {
            wx.navigateBack()
          }, 1500)
        }
      })
    }
  },
  formReset: function () {
    this.setData({
      is_checked: false,
      is_default: 0,
      form: [
        { label: '收件人', submitName: 'receiver_name', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入收件人', rule: { required: true }, message: { required: '请输入收件人' }},
        { label: '收件人电话', submitName: 'receiver_phone', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入收件人电话', rule: { required: true }, message: { required: '请输入收件人电话' }},
        { label: '省', submitName: 'province', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入省份', rule: { required: true }, message: { required: '请输入省份' }},
        { label: '市', submitName: 'city', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入市', rule: { required: true }, message: { required: '请输入市' }},
        { label: '区(县)', submitName: 'area', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入区(县)', rule: { required: true }, message: { required: '请输入区(县)'}},
        { label: '详细地址', submitName: 'detail_address', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入详细地址', rule: { required: true }, message: { required: '请输入详细地址' }},
        { label: '是否设为默认', submitName: 'is_default', inputType: 'checkbox', value: ''},
      ]
    })
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

})
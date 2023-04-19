// pages/order/order-return/order-return.js
const app = getApp()
const util = require('../../../utils/util')
const formMethods = require('../../../components/form/utils/formMethods.js')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    form: [
      { label: '退款原因', submitName: 'return_reason', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入退款原因', rule: { required: true }, message: { required: '请输入退款原因' }},
      { label: '退款件数', submitName: 'return_count', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入退款件数', rule: { required: true }, message: { required: '请输入退款件数' }},
      { label: '快递单号', submitName: 'return_delivery_id', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入快递单号', rule: { required: true }, message: { required: '请输入快递单号' }},
      { label: '快递公司', submitName: 'return_delivery_company', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入快递公司', rule: { required: true }, message: { required: '请输入快递公司' }},
      { label: '联系电话', submitName: 'contact_phone', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入联系电话', rule: { required: true }, message: { required: '请输入联系电话' }}
    ],
    Address:{
      receiver_name: "猫狗小站官方售后",
      receiver_phone: "88888888",
      province: "广东省",
      city: "深圳市",
      area: "福田区",
      detail_address: "皇庭广场A座"
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let string = decodeURIComponent(options.orderitem)
    let orderitem = JSON.parse(string)
    this.setData({
      orderitem: orderitem
    })
    console.log(this.data.orderitem)
    // input验证
    formMethods.initValidate(this.data.form, this);
  },

  formSubmit: function (e) {
    // 数据校验
    const self = this;
    const { form } = this.data;
    const formValidate = {};
    form.forEach(item => { formValidate[item.submitName] = item.value});
    // console.log(this.data.orderitem)
    if (!self.WxValidate.checkForm(formValidate)) {
      const error = self.WxValidate.errorList[0];
      wx.showToast({ title: error.msg, icon: 'none' })
      return false
    }
    else if(form[1].value>this.data.orderitem.count){
      wx.showToast({
        title: "退货数量超额！",
        icon: 'none' 
      })
      return false
    }
    // 数据校验结束
    else{
      let orderItem = this.data.orderitem
      let return_totalprice = util.formatPrice(orderItem.goods.price*form[1].value)
      wx.showModal({
        title: '提示',
        content: '确认提交退货申请？',
        success:(res)=> { 
          if (res.confirm) {
            wx.request({
              url: 'http://127.0.0.1:8081/wx/applyOrderItemReturn',
              data:{
                user_openid: app.globalData.openid,
                order_id: orderItem.order_id,
                item_id: orderItem.item_id,
                goods_id: orderItem.goods_id,
                return_totalprice: return_totalprice,
                return_reason: form[0].value,
                return_count: form[1].value,
                return_delivery_id: form[2].value,
                return_delivery_company: form[3].value,
                contact_phone: form[4].value,
              },
              success: (res) => {
                wx.hideLoading()
                wx.showToast({
                  title: '提交成功！'
                })
                setTimeout(() => {
                  wx.navigateBack()
                }, 1500)
              }
            })
          }
        }
      })
    }
  },

  formReset: function () {
    this.setData({
      form: [
        { label: '退款原因', submitName: 'return_reason', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入退款原因', rule: { required: true }, message: { required: '请输入退款原因' }},
        { label: '退款件数', submitName: 'return_count', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入退款件数', rule: { required: true }, message: { required: '请输入退款件数' }},
        { label: '快递单号', submitName: 'return_delivery_id', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入快递单号', rule: { required: true }, message: { required: '请输入快递单号' }},
        { label: '快递公司', submitName: 'return_delivery_company', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入快递公司', rule: { required: true }, message: { required: '请输入快递公司' }},
        { label: '联系电话', submitName: 'contact_phone', type: 'input', inputType: 'text', value: '', requied: true, placeholder: '请输入联系电话', rule: { required: true }, message: { required: '请输入联系电话' }}
      ]
    })
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    
  },
})
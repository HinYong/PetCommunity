# coding=utf-8
# -*- encoding = utf-8 -*-
# @Time: 2022/4/21 18:12
# @Author: HinYong
# @File: GoodsCrawler.py
# 宠物用品爬取

import os
from mysql.util import util
from mysql.tasks import tasks
import sys
import time
import requests

reload(sys)
sys.setdefaultencoding('utf-8')
util = util()
tasks = tasks()
count = 0

requests.packages.urllib3.disable_warnings()

#  在这先准备好请求头，需要爬的URL，表单参数生成函数，以及建立会话
header = {
    "Accept": "text/html, application/xhtml+xml, image/jxr, */*",
    "Accept-Language": "zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3",
    "Content-Type": "application/x-www-form-urlencoded",
    "Accept-Encoding": "gzip, deflate",
    "Connection": "Keep-Alive",
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) \
 AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36",
    "Accept-Encoding": "gzip, deflate",
    "Upgrade-Insecure-Requests": "1",
    # Cookie由Session管理，这里不用传递过去
}


def task(success, fail):
    """
    :param success: 爬取成功回调
    :param fail: 爬取失败回调
    :return:
    """

    def parserDetail(item_url, name, price, image, type_id, type_name, counter):
        """
        详情页解析并将结果保存到数据库
        :param item_url: 页面url
        :param name: 商品名称
        :param price: 商品价格
        :param image: 主图片
        :param type_id: 商品类型编号
        :param type_name: 商品类型名
        :param counter: 计数器
        :return:
        """
        global count

        # 创建服务器上图片的存储文件夹路径
        dirpath = "../../../后端/PetCommunity_Backend/src/main/resources/static/crawler_goods/" + type_name + "/" + str(counter) + "/"
        # 因为路径有中文，所以需要解码
        dirpath = dirpath.decode('utf-8')
        if not os.path.exists(dirpath):
            # 如果不存在则创建目录
            os.makedirs(dirpath)

        # 主图片下载，取的是列表页面的主图片，避免访问商品详情页获取不到商品图片，导致小程序中的商品没有主图片以及轮播图
        # main_image主图片存储的路径
        main_image = dirpath + "image1.jpg"
        # 获取图片链接
        r = requests.get(image)
        # 写入main_image指向的路径
        with open(main_image, 'wb') as f:
            f.write(r.content)
        # 主图片的虚拟路径
        main_image = "http://localhost:8081/crawler_goods/" + type_name + "/" + str(counter) + "/" + "image1.jpg"
        print main_image

        # 请求商品详情页面
        try:
            soup = util.getPageForHTML(item_url)
            if soup == 404:
                print "详情页已失效，跳过"
        except Exception, e:
            errDetail = "获取详情页HTML失败，URL：%s，错误信息：\n%s" % (item_url, str(e))
            fail(util.defaultFailCallback, taskName, errDetail)

        # 轮播图下载
        content_selector = 'div.w-max.ct.mb20 > div.goods-md-bg > div.fl.rw400.prelative.gdms > div.picroll.mb > div > ul > li > a > img'
        imageList = soup.select(content_selector)
        if len(imageList) == 0 or imageList is None:
            content_selector = 'div.w-max.ct.mb20 > div.xq-first.bgwhite > div.xq-con.clearfix > div.fl.xq-img > div.Dimg > div.spec-scroll.fl > div > ul > li > img'
            imageList = soup.select(content_selector)
        a = 0  # 文件名
        swiper_images = ''  # 轮播图存储路径
        for i in imageList:
            # 下载图片
            a += 1
            # 详情页轮播图的第一张图片与列表的商品主图片是一样的，将主图片作为轮播图第一张即可，不需要重复下载
            if a == 1:
                swiper_images = swiper_images + main_image + ","
            # 从第二张轮播图开始下载
            else:
                # fileurl是下载图片保存的位置及名称
                fileurl = dirpath + "image" + str(a) + ".jpg"
                # 因为路径有中文，所以需要解码
                fileurl = fileurl.decode("utf-8")
                # 轮播图虚拟路径拼接
                swiper_images = swiper_images + "http://localhost:8081/crawler_goods/"+type_name+"/"+str(counter)+"/image"+str(a)+".jpg,"
                # 获取图片链接i['src']并下载
                downloadUrl = i['src0']
                downloadUrl = downloadUrl[:downloadUrl.find("@")]
                r = requests.get(downloadUrl)
                # 写入对应位置
                with open(fileurl, 'wb') as f:
                    f.write(r.content)
                time.sleep(1)

        if swiper_images.endswith(','):
            swiper_images = swiper_images[:-1]
        print swiper_images

        # 插入数据库
        util.insert(image=main_image, name=name, price=price, type_id=type_id, status=1, swiper_images=swiper_images,
                    sourceURL=item_url)
        count += 1

    """
    1.查询列表页，解析列表元素
    2.遍历元素，请求详情页并解析
    3.保存到数据库
    """

    # 爬取函数
    def crawler(urlKey, num, type_id, type_name):
        counter = 0
        old_list = None
        isloop = True
        # 查询列表页
        pageNo = 1  # 页码
        while isloop:
            pageUrl = "https://list.epet.com/" + urlKey + str(pageNo) + ".html?for_app_set_properties="
            selector = "div.lis_box.glist1 > div"
            list = util.getListForHTML(pageUrl, selector)
            # print(list)
            # 校验是否成功获取到列表元素
            if len(list) < 1:
                # 获取失败，打印失败信息
                errDetail = "第%d页获取元素失败" % pageNo
                fail(util.defaultFailCallback, taskName, errDetail)
                break
            if str(list) == old_list:
                break
            old_list = str(list)
            print "开始抓取第%d页" % pageNo
            pageNo += 1
            for item in list:
                # 最后一项没有内容，表明这一页列表结束了
                if item.select_one("span.title-subject") is None:
                    break
                # 商品名称
                name = item.select_one("span.title-subject").text.strip()
                name = name.replace(name[name.find("【"): name.find("】") + 1], "")
                print name
                # 商品价格
                price = item.select_one("p.gprice > span.cred.ft14.price").text.strip().strip("￥")
                print price
                # 主图片路径
                image = item.select_one("img.gd-photoimg")['src0']
                # 详情页路径
                item_url = item.select_one("a")['href']
                try:
                    parserDetail(item_url, name, price, image, type_id=type_id, type_name=type_name, counter=counter)
                    counter = counter + 1
                    if counter >= num:
                        isloop = False
                        break
                except Exception, e:
                    errDetail = '获取详情页失败，URL：%s，错误信息：\n%s' % (item_url, str(e))
                    fail(util.defaultFailCallback, taskName, errDetail)
                time.sleep(5)

    # 猫咪部分
    taskName = '猫咪主粮爬取'
    print taskName
    crawler(urlKey="722b1f", num=60, type_id=1, type_name="cats_staple")

    taskName = '猫咪零食爬取'
    print taskName
    crawler(urlKey="3580b1f", num=60, type_id=2, type_name="cats_snack")

    taskName = '猫咪清洁用品爬取'
    print taskName
    crawler(urlKey="4336b1f", num=60, type_id=3, type_name="cats_cleaning")

    taskName = '猫咪日用品爬取'
    print taskName
    crawler(urlKey="936b1f", num=60, type_id=4, type_name="cats_daily")

    taskName = '猫咪保健用品爬取'
    print taskName
    crawler(urlKey="3627b1f", num=60, type_id=5, type_name="cats_health")

    taskName = '猫咪护理用品爬取'
    print taskName
    crawler(urlKey="3620b1f", num=60, type_id=6, type_name="cats_care")

    taskName = '猫咪玩具爬取'
    print taskName
    crawler(urlKey="958b1f", num=60, type_id=7, type_name="cats_toy")

    taskName = '猫咪洗浴用品爬取'
    print taskName
    crawler(urlKey="4209b1f", num=60, type_id=8, type_name="cats_shampoo")

    taskName = '猫咪装扮用品爬取'
    print taskName
    crawler(urlKey="4198b1f", num=60, type_id=9, type_name="cats_dress")

    taskName = '猫咪出行用品爬取'
    print taskName
    crawler(urlKey="4196b1f", num=60, type_id=10, type_name="cats_outdoor")

    # 狗狗部分
    taskName = '狗狗主粮爬取'
    print taskName
    crawler(urlKey="7b1f", num=60, type_id=11, type_name="dogs_staple")

    taskName = '狗狗零食爬取'
    print taskName
    crawler(urlKey="6b1f", num=60, type_id=12, type_name="dogs_snack")

    taskName = '狗狗清洁用品爬取'
    print taskName
    crawler(urlKey="4315b1f", num=60, type_id=13, type_name="dogs_cleaning")

    taskName = '狗狗日用品爬取'
    print taskName
    crawler(urlKey="54b1f", num=60, type_id=14, type_name="dogs_daily")

    taskName = '狗狗保健用品爬取'
    print taskName
    crawler(urlKey="48b1f", num=60, type_id=15, type_name="dogs_health")

    taskName = '狗狗护理用品爬取'
    print taskName
    crawler(urlKey="49b1f", num=60, type_id=16, type_name="dogs_care")

    taskName = '狗狗玩具爬取'
    print taskName
    crawler(urlKey="53b1f", num=60, type_id=17, type_name="dogs_toy")

    taskName = '狗狗洗浴用品爬取'
    print taskName
    crawler(urlKey="4334b1f", num=60, type_id=18, type_name="dogs_shampoo")

    taskName = '狗狗装扮用品爬取'
    print taskName
    crawler(urlKey="55b1f", num=60, type_id=19, type_name="dogs_dress")

    taskName = '狗狗出行用品爬取'
    print taskName
    crawler(urlKey="2652b1f", num=60, type_id=20, type_name="dogs_outdoor")

    print "共爬取了%d条数据" % count


# 开始执行任务
tasks.run(task)

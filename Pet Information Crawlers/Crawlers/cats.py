# coding=utf-8
# -*- encoding = utf-8 -*-
# @Time: 2022/4/5 18:12
# @Author: HinYong
# @File: cats.py
# 猫

import os
from mysql.util import util
from mysql.tasks import tasks
import sys
import time
import requests
from w3lib import html

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

    def parserDetail(item, name, image, item_url):
        """
        详情页解析并将结果保存到数据库
        :param item: 元素
        :param name: 宠物名称
        :param pageUrl: 页面url
        :return:
        """
        global count

        try:
            soup = util.getPageForHTML(item_url)
            if soup == 404:
                print "详情页已失效，跳过"
        except Exception, e:
            errDetail = "获取详情页HTML失败，URL：%s，错误信息：\n%s" % (item_url, str(e))
            fail(util.defaultFailCallback, taskName, errDetail)

        # 英文名
        English_name = None
        # 性格
        character = None
        # 祖籍
        hometown = None
        # 易患病
        illness = None
        # 寿命
        lifetime = None
        # 市场价
        common_price = None

        content_selector = '#content > div > div.entry_content.mt15 > div.entry_tit > dl > dd'
        find = soup.select(content_selector)
        if len(find) != 0:
            for i in find:
                if i.select_one("span").text.find("英") != -1:
                    English_name = i.select_one("em").text.strip()
                elif i.select_one("span").text.find("性") != -1:
                    character = i.select_one("em").text.strip()
                elif i.select_one("span").text.find("祖") != -1:
                    hometown = i.select_one("em").text.strip()
                elif i.select_one("span").text.find("易") != -1:
                    illness = i.select_one("em").text.strip()
                elif i.select_one("span").text.find("寿") != -1:
                    lifetime = i.select_one("em").text.strip()
                elif i.select_one("span").text.find("价") != -1:
                    common_price = i.select_one("em").text.strip()

        print English_name
        print character
        print hometown
        print illness
        print lifetime
        print common_price

        # 简介
        content_selector = '#content > div > div.entry_content.mt15 > div.entry_c > div > div.entry_c_l > div:nth-child(2) > p'
        find = soup.select(content_selector)
        # 分段方式是分为多个p标签，而不是一个模块在一个p标签中，所以需要将文字部分的p标签提取出来并进行拼接
        # 每个段落是一个p标签，则会有超过2个的p标签
        # 即使是只有一个段落，另一个情况下也会选择第二个p标签，而不会选择到图片
        if len(find) > 2:
            introduce = ''
            for i in find:
                if i.select_one("img") is not None:
                    continue
                if i.text is None:
                    continue
                introduce = introduce + str(i) + "\n"
            if introduce.endswith('\n'):
                introduce = introduce[:-1]
            # 标签替换方法，传入的必须为字符串
            introduce = html.remove_tags(introduce, which_ones=('span', 'a', 'p', 'br', 'strong'))
        # 多个段落在一个p标签中
        else:
            content_selector = '#content > div > div.entry_content.mt15 > div.entry_c > div > div.entry_c_l > div:nth-child(2) > p:nth-child(2)'
            if soup.select_one(content_selector) is None:
                content_selector = '#content > div > div.entry_content.mt15 > div.entry_c > div > div.entry_c_l > div:nth-child(2) > p:nth-child(1)'
            # 标签替换方法，只保留br/标签，传入的必须为字符串
            introduce = html.remove_tags(str(soup.select_one(content_selector)),
                                         which_ones=('strong', 'a', 'p', 'span'))
            # 将换行符进行替换
            introduce = introduce.replace('<br/>', '\n')
        print introduce

        # 形态特征
        content_selector = '#content > div > div.entry_content.mt15 > div.entry_c > div > div.entry_c_l > div:nth-child(6) > p'
        find = soup.select(content_selector)
        # 分段方式是分为多个p标签，而不是一个模块在一个p标签中，所以需要将文字部分的p标签提取出来并进行拼接
        # 每个段落是一个p标签，则会有超过2个的p标签
        # 即使是只有一个段落，另一个情况下也会选择第二个p标签，而不会选择到图片
        if len(find) > 2:
            appearance = ''
            for i in find:
                if i.select_one("img") is not None:
                    continue
                if i.text is None:
                    continue
                appearance = appearance + str(i) + "\n"
            if appearance.endswith('\n'):
                appearance = appearance[:-1]
            # 标签替换方法，传入的必须为字符串
            appearance = html.remove_tags(appearance, which_ones=('span', 'a', 'p', 'br', 'strong'))
        # 多个段落在一个p标签中
        else:
            content_selector = '#content > div > div.entry_content.mt15 > div.entry_c > div > div.entry_c_l > div:nth-child(6) > p:nth-child(2)'
            if soup.select_one(content_selector) is None:
                content_selector = '#content > div > div.entry_content.mt15 > div.entry_c > div > div.entry_c_l > div:nth-child(6) > p:nth-child(1)'
            # 标签替换方法，只保留br/标签，传入的必须为字符串
            appearance = html.remove_tags(str(soup.select_one(content_selector)),
                                          which_ones=('strong', 'a', 'p', 'span'))
            # 将换行符进行替换
            appearance = appearance.replace('<br/>', '\n')
        print appearance

        # 性格特征
        content_selector = '#content > div > div.entry_content.mt15 > div.entry_c > div > div.entry_c_l > div:nth-child(8) > p'
        find = soup.select(content_selector)
        # 分段方式是分为多个p标签，而不是一个模块在一个p标签中，所以需要将文字部分的p标签提取出来并进行拼接
        # 每个段落是一个p标签，则会有超过2个的p标签
        # 即使是只有一个段落，另一个情况下也会选择第二个p标签，而不会选择到图片
        if len(find) > 2:
            character_detail = ''
            for i in find:
                if i.select_one("img") is not None:
                    continue
                if i.text is None:
                    continue
                character_detail = character_detail + str(i) + "\n"
            if character_detail.endswith('\n'):
                character_detail = character_detail[:-1]
            # 标签替换方法，传入的必须为字符串
            character_detail = html.remove_tags(character_detail, which_ones=('span', 'a', 'p', 'br', 'strong'))
        # 多个段落在一个p标签中
        else:
            content_selector = '#content > div > div.entry_content.mt15 > div.entry_c > div > div.entry_c_l > div:nth-child(8) > p:nth-child(2)'
            if soup.select_one(content_selector) is None:
                content_selector = '#content > div > div.entry_content.mt15 > div.entry_c > div > div.entry_c_l > div:nth-child(8) > p:nth-child(1)'
            # 标签替换方法，只保留br/标签，传入的必须为字符串
            character_detail = html.remove_tags(str(soup.select_one(content_selector)),
                                                which_ones=('strong', 'a', 'p', 'span'))
            # 将换行符进行替换
            character_detail = character_detail.replace('<br/>', '\n')
        print character_detail

        # 饲养建议
        content_selector = '#content > div > div.entry_content.mt15 > div.entry_c > div > div.entry_c_l > div:nth-child(10) > p'
        find = soup.select(content_selector)
        # 分段方式是分为多个p标签，而不是一个模块在一个p标签中，所以需要将文字部分的p标签提取出来并进行拼接
        # 每个段落是一个p标签，则会有超过2个的p标签
        # 即使是只有一个段落，另一个情况下也会选择第二个p标签，而不会选择到图片
        if len(find) > 2:
            feed_knowledge = ''
            for i in find:
                if i.select_one("img") is not None:
                    continue
                if i.text is None:
                    continue
                feed_knowledge = feed_knowledge + str(i) + "\n"
            if feed_knowledge.endswith('\n'):
                feed_knowledge = feed_knowledge[:-1]
            # 标签替换方法，传入的必须为字符串
            feed_knowledge = html.remove_tags(feed_knowledge, which_ones=('span', 'a', 'p', 'br', 'strong'))
        # 多个段落在一个p标签中
        else:
            content_selector = '#content > div > div.entry_content.mt15 > div.entry_c > div > div.entry_c_l > div:nth-child(10) > p:nth-child(2)'
            if soup.select_one(content_selector) is None:
                content_selector = '#content > div > div.entry_content.mt15 > div.entry_c > div > div.entry_c_l > div:nth-child(10) > p:nth-child(1)'
            # 标签替换方法，只保留br/标签，传入的必须为字符串
            feed_knowledge = html.remove_tags(str(soup.select_one(content_selector)),
                                              which_ones=('strong', 'a', 'p', 'span'))
            # 将换行符进行替换
            feed_knowledge = feed_knowledge.replace('<br/>', '\n')
        print feed_knowledge

        # 图片爬取
        if English_name is not None:
            # 去空格，防止图片路径读取出错
            English_name_nospace = English_name.replace(" ", "_").replace("|", "_")
        # 没有英文名的情况
        else:
            English_name_nospace = "cat" + str(count)
        # 服务器上的存储路径
        dirpath = "../../../后端/PetCommunity_Backend/src/main/resources/static/crawler_images/cats/" + English_name_nospace + "/"
        # 因为路径有中文，所以需要解码
        dirpath = dirpath.decode('utf-8')
        if not os.path.exists(dirpath):
            # 如果不存在则创建目录
            os.makedirs(dirpath)

        # 主图片下载
        # main_image主图片存储的路径
        main_image = dirpath + English_name_nospace + ".png"
        # 获取图片链接
        r = requests.get(image)
        # 写入main_image指向的路径
        with open(main_image, 'wb') as f:
            f.write(r.content)
        main_image = "http://localhost:8081/crawler_images/cats/" + English_name_nospace + "/" + English_name_nospace + ".png"
        print main_image
        time.sleep(5)

        # 轮播图下载
        # 获取详情页的动物图册界面的html代码
        content_selector = 'div.entry_pic_more.right > a'
        imageUrl = soup.select_one(content_selector)['href']
        try:
            soup2 = util.getPageForHTML(imageUrl)
            if soup2 == 404:
                print "详情页已失效，跳过"
        except Exception, e:
            errDetail = "获取详情页轮播图失败，URL：%s，错误信息：\n%s" % (imageUrl, str(e))
            fail(util.defaultFailCallback, taskName, errDetail)
        # 选择出所有词条图片的元素
        image_selector = '#content > div > div.article.mt5 > div > dl > dt > a > img'
        imageList = soup2.select(image_selector)
        a = 0  # 文件名
        swiper_images = ''  # 轮播图存储路径
        for i in imageList:
            # 下载图片
            a += 1
            # fileurl是下载图片保存的位置及名称
            fileurl = dirpath + English_name_nospace + str(a) + ".png"
            # 因为路径有中文，所以需要解码
            fileurl = fileurl.decode("utf-8")
            # 轮播图路径拼接，由于是发布在服务器上的，所以这个路径必须为虚拟路径
            swiper_images = swiper_images + "http://localhost:8081/crawler_images/cats/" + English_name_nospace + "/" \
                            + English_name_nospace + str(a) + ".png,"
            # 获取图片链接i['src']并下载
            r = requests.get(i['src'])
            # 写入对应位置
            with open(fileurl, 'wb') as f:
                f.write(r.content)
            time.sleep(2)

        if swiper_images.endswith(','):
            swiper_images = swiper_images[:-1]
        print swiper_images

        # 插入数据库
        util.insert(pet_type_id=1, name=name, English_name=English_name, character=character,
                    hometown=hometown, illness=illness, lifetime=lifetime, common_price=common_price,
                    image=main_image, swiper_images=swiper_images, introduce=introduce, appearance=appearance,
                    character_detail=character_detail, feed_knowledge=feed_knowledge, sourceURL=item_url)
        count += 1

    """
    1.查询列表页，解析列表元素
    2.遍历元素，请求详情页并解析
    3.保存到数据库
    """

    def cats_crawler():
        isLoop = True
        old_list = None
        # 查询列表页
        pageNo = 1  # 页码
        while isLoop:
            pageUrl = "http://www.boqii.com/pet-all/cat/"
            if pageNo > 1:
                pageUrl = "http://www.boqii.com/pet-all/cat/?p=%d" % pageNo
            selector = "div.hot_pet > div.hot_pet_cont > dl"
            list = util.getListForHTML(pageUrl, selector)
            print(list)
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
                name = item.select_one("dd").text.strip()
                print name
                # 主图片路径
                image = item.select_one("dt > a > img")['src']
                item_url = item.select_one("a")['href']
                item_url = util.urlJoin(pageUrl, item_url)
                try:
                    parserDetail(item, name, image, item_url)
                except Exception, e:
                    errDetail = '获取"%s"详情页失败，URL：%s，错误信息：\n%s' % (name, item_url, str(e))
                    fail(util.defaultFailCallback, taskName, errDetail)
                time.sleep(30)

    taskName = '猫咪品种爬取'
    print taskName
    cats_crawler()

    print "共爬取了%d条数据" % count


# 开始执行任务
tasks.run(task)

# -*- coding: UTF-8 -*-
from mysql import mysql
import MySQLdb
from urlparse import urljoin
import smtplib
from email.mime.text import MIMEText
from email.header import Header
from bs4 import BeautifulSoup
import requests
import urllib3

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

session = requests.Session()
session.keep_alive = False
urllib3.disable_warnings()

class util:
    """数据库操作类"""
    __mysql = mysql()
    __cursor = __mysql.cursor()

    # 数据库字段

    def print_version(self):
        self.__cursor.execute("SELECT VERSION()")
        print "Database version : %s " % self.__cursor.fetchone()

    def insert(self, image=None, name=None, price=None, type_id=None, status=None, swiper_images=None, sourceURL=None):
        insert_sql = 'insert goods(_id,'
        if image is not None:
            insert_sql += 'image,'
        if name is not None:
            insert_sql += '`name`,'
        if price is not None:
            insert_sql += 'price,'
        if type_id is not None:
            insert_sql += 'type_id,'
        if status is not None:
            insert_sql += 'status,'
        if swiper_images is not None:
            insert_sql += 'swiper_images,'
        if sourceURL is not None:
            insert_sql += 'sourceURL,'

        if insert_sql.endswith(','):
            insert_sql = insert_sql[:-1]
        insert_sql += ') '
        insert_sql += 'values(null,'

        if image is not None:
            insert_sql += '"%s",' % MySQLdb.escape_string(str(image))
        if name is not None:
            insert_sql += '"%s",' % MySQLdb.escape_string(name)
        if price is not None:
            insert_sql += '"%s",' % MySQLdb.escape_string(price)
        if type_id is not None:
            insert_sql += '%d,' % type_id
        if status is not None:
            insert_sql += '%d,' % status
        if swiper_images is not None:
            insert_sql += '"%s",' % MySQLdb.escape_string(str(swiper_images))
        if sourceURL is not None:
            insert_sql += '"%s",' % MySQLdb.escape_string(sourceURL)

        if insert_sql.endswith(','):
            insert_sql = insert_sql[:-1]
        insert_sql += ')'

        print insert_sql
        try:
            # 执行sql语句
            self.__cursor.execute(insert_sql)
            # 提交到数据库执行
            self.__mysql.commit()
        except Exception, e:
            print e
            # Rollback in case there is any error
            self.__mysql.rollback()

    def urlJoin(self, base, url, allow_fragments=True):
        """
        连接URL
        :param base: 当前页面完整链接，例：http://www.xxx.com/
        :param url: 目标页面链接，绝对地址直接返回该参数，相对地址连接后返回
        :param allow_fragments:
        :return:
        """
        return urljoin(base, url, allow_fragments)

    def sendMail(self, receivers, From, To, Subject, message):
        """ 发送邮件 """
        mailHost = "smtp.163.com"
        mailPort = 994
        mail_user = "cjwdnxx@163.com"  # 用户名
        mail_pass = "IJWECBMDEMKPCIYQ"  # 口令

        sender = 'cjwdnxx@163.com'

        message = MIMEText(message, 'plain', 'utf-8')
        message['From'] = Header(From, 'utf-8')
        message['To'] = Header(To, 'utf-8')

        # subject = 'Python SMTP 邮件测试'
        message['Subject'] = Header(Subject, 'utf-8')

        try:
            smtpObj = smtplib.SMTP_SSL()
            smtpObj.connect(mailHost, mailPort)
            print "连接邮箱服务器"
            smtpObj.login(mail_user, mail_pass)
            print "登录邮箱服务器"
            smtpObj.sendmail(sender, receivers, message.as_string())
            print "邮件发送成功"
        except smtplib.SMTPException as ex:
            print ex
            print "Error: 无法发送邮件"

    def defaultFailCallback(self, taskName, errDetail=""):
        """
        失败回调默认实现
        :param taskName:
        :param errDetail:
        :return:
        """
        message = "任务 %s 执行失败。" % taskName
        content = message if len(errDetail) < 1 else message + "详细信息：" + errDetail
        print content
        self.sendMail(
            ['1174788253@qq.com'],
            "HinYong",
            "HinYong",
            "宠物用品数据爬取任务执行失败",
            content
        )

    def getPageForHTML(self, url, features="html.parser", custom_header=None):
        headers = header
        if custom_header is not None:
            headers = custom_header
        f = requests.get(url, headers=headers, verify=False)
        if f.status_code == 404:
            return 404
        soup = BeautifulSoup(f.content, features)
        return soup

    def getListForHTML(self, url, selector, features="html.parser", custom_header=None):
        """
        解析Html获取列表元素
        :param custom_header:
        :param url: 列表地址
        :param selector: 元素选择条件
        :param features: 解析器，默认为"html.parser"
        :return: 元素元组
        """
        headers = header
        if custom_header is not None:
            headers = custom_header
        f = requests.get(url, headers=headers, verify=False)
        # print f.content
        soup = BeautifulSoup(f.content, features)
        find = soup.select(selector)
        return find

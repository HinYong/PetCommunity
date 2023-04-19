# -*- coding: UTF-8 -*-
import MySQLdb


def Singleton(cls):
    _instance = {}

    def _singleton(*args, **kargs):
        if cls not in _instance:
            _instance[cls] = cls(*args, **kargs)
        return _instance[cls]

    return _singleton


@Singleton
class mysql:

    def __init__(self):
        self.__db = MySQLdb.connect('127.0.0.1', 'root', 'root', 'petcommunity', charset='utf8')

    def cursor(self):
        return self.__db.cursor()

    def commit(self):
        self.__db.commit()

    def rollback(self):
        self.__db.rollback()

# -*- coding: UTF-8 -*-

"""任务执行控制器"""


class tasks:

    def run(self, task):
        task(self.success, self.fail)

    def success(self, callback, *args):
        callback(*args)

    def fail(self, callback, *args):
        callback(*args)


if __name__ == '__main__':
    '''Test'''
    tasks = tasks()
    tasks.run(lambda success, fail: success(lambda: ""))

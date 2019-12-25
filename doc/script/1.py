import time

import pymysql

dev = {
    'host': '192.168.88.201',
    'port': 3306,
    'user': 'devuser',
    'password': 'mypassword',
    'database': 'order_api',
    'charset': 'utf8mb4',
    'cursorclass': pymysql.cursors.DictCursor,
}

test = {
    'host': '192.168.88.203',
    'port': 3306,
    'user': 'admin',
    'password': 'admin123',
    'database': 'order_api',
    'charset': 'utf8mb4',
    'cursorclass': pymysql.cursors.DictCursor,
}

pro = {
    'host': 'xxx',
    'port': 3306,
    'user': 'xxx',
    'password': 'xxx',
    'database': 'xxx',
    'charset': 'utf8mb4',
    'cursorclass': pymysql.cursors.DictCursor,
}


class Test():

    def __init__(self, config):
        self.db = pymysql.connect(**config)
        self.cursor = self.db.cursor()

    def __del__(self):
        self.db.close()

    def insert(self, table, data):
        keys = ', '.join(data.keys())
        values = ', '.join(['%s'] * len(data))
        sql = 'insert into %s (%s) values (%s)' % (table, keys, values)
        self.cursor.execute(sql, tuple(data.values()))
        self.db.commit()

    def get_today_datetime(self):
        '''获取现在时间datetime类型（yyyy-MM-dd HH:mm:ss）'''
        return str(time.strftime("%Y-%m-%d %H:%M:%S"))

    def get_user_order(self):
        sql = 'select * from user_order'
        self.cursor.execute(sql)
        return self.cursor.fetchall()

    def start(self):
        orders = self.get_user_order()
        for order in orders:
            item = {}
            item['order_id'] = order['id']
            item['check_reason'] = order['failure_cause']
            item['check_status'] = 3
            if order['check_time'] == None:
                item['create_time'] = self.get_today_datetime()
            else:
                item['create_time'] = order['check_time']
            print('保存订单审核', item)
            self.insert('user_order_check', item)


if __name__ == '__main__':
    tset = Test(pro)
    tset.start()
    print('脚本结束了...  end ')

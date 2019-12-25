import time
from decimal import Decimal

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

    def get_user_pay(self):
        sql = 'select * from user_pay'
        self.cursor.execute(sql)
        return self.cursor.fetchall()

    def start(self):
        orders = self.get_user_order()
        pays = self.get_user_pay()

        pay_list = []
        for pay in pays:
            pay_list.append(pay['order_id'])

        print('当前订单 order 有{}条数据'.format(len(orders)))
        print('当前订单 pay 有{}条数据'.format(len(pays)))
        print('待同步数据 --> {}条'.format(len(orders) - len(pays)))

        count = 1
        for order in orders:
            if order['id'] in pay_list:
                continue

            item = {}
            item['pay_no'] = 'P-{}'.format(int(round(time.time() * 1000)))
            item['order_id'] = order['id']
            item['money'] = float(Decimal(str(order['order_num'])) * Decimal(str(order['order_price'])))
            item['pay_status'] = 3
            item['create_time'] = self.get_today_datetime()

            self.insert('user_pay', item)
            print('{} -> 保存支付信息'.format(count), item)
            count = count + 1


if __name__ == '__main__':
    tset = Test(test)
    tset.start()
    print('脚本结束了...  end ')

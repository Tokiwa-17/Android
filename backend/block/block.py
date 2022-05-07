from flask import Blueprint, request, jsonify
from .models import Block
from ..db import db


block = Blueprint('block', __name__)

@block.route('/block/test', methods=['GET', 'POST']) # 前端接受后端字符串数据
def test_url():
    return "block success!", 200

@block.route('/block/test2', methods=['GET', 'POST']) # 前端向后端数据库发送数据
def test_url2():
    new_block = Block()
    new_block.blocked_user_id = str(123)
    new_block.user_id = str(456)
    db.session.add(new_block)
    db.session.commit()
    return "OK", 200

@block.route('/block/test/post', methods=['POST']) # 测试post发送登录数据
def test_post():
    data = request.form
    print(f"data:{data}")
    account = data.get('account')
    password = data.get('password')
    print(f"account: {account}, password: {password}")
    return "OK", 200


#coding=utf-8
from flask import Blueprint, request, jsonify

from ..user.models import User
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
#    print(f"data:{data}")
    account = data.get('account')
    password = data.get('password')
#    print(f"account: {account}, password: {password}")
    return "OK", 200

@block.route('/api/block/get_blocklist',  methods=['GET'])
def get_block_list():
    id = request.args.get('user_id')
#    print(f'id: {id}')
    block_list = []
    block_query = Block.query.filter(Block.user_id == id)
    if block_query != None:
        for block in block_query:
            user_id = block.blocked_user_id
#            print(f'user_id: {user_id}')
            user_query = User.query.filter(User.id == user_id)
            for user in user_query:
                block_list.append({'id': user.id, 'name':user.nickname, "url":user.avatar, 'introduction':user.introduction})
    return {'blocklist': block_list}, 200

@block.route("/api/block/delete_from_block", methods=['GET'])
def delete_from_block():
    user_id = request.args.get('user_id')
    block_id = request.args.get('block_id')
    block_query = Block.query.filter(Block.user_id == user_id, Block.blocked_user_id == block_id).first()
#    print(f'{block_query.user_id}, {block_query.blocked_user_id}')
    if block_query != None:
        db.session.delete(block_query)
        db.session.commit()
    return "OK", 200

@block.route("/api/block/add_to_block", methods=['GET'])
def add_to_block():
    user_id = request.args.get('user_id')
    block_id = request.args.get('block_id')
#    print(f'{user_id}, {block_id}')
    new_block = Block()
    new_block.user_id = user_id
    new_block.blocked_user_id = block_id
    db.session.add(new_block)
    db.session.commit()
    return "OK", 200


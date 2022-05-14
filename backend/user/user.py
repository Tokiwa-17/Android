from flask import Blueprint, request, jsonify
from .models import User
from ..db import db


user = Blueprint('user', __name__)

@user.route('/api/user_test', methods=['GET', 'POST']) # 前端接受后端字符串数据
def test_user():
    new_user = User()
    # new_test.id = str(new_id)
    new_user.account = '1'
    new_user.password =  '1'
    new_user.avatar = '1'
    new_user.nickname = '1'
    new_user.introduction = '1'
    db.session.add(new_user)
    db.session.commit()
    return "user_test success!", 200

@user.route('/api/user/login', methods=['GET', 'POST'])
def user_login():
    data = request.form
    print(f"data:{data}")
    account = data.get('account')
    password = data.get('password')
    print(f"account: {account}, password: {password}")
    user = User.query.filter(User.account == account).first()
    print(f'user: {user}')
    print(f'json:{user.to_json()}')
    if user != None and user.password == password:
        return user.to_json(), 200
    return "登录失败", 404


@user.route('/api/user/logon', methods=['GET', 'POST'])
def user_logon():
    data = request.form
    print(f"data:{data}")
    account = data.get('account')
    password = data.get('password')
    new_user = User()
    new_user.account = account
    new_user.password = password
    new_user.nickname = "user_" + new_user.id[0:10]
    db.session.add(new_user)
    db.session.commit()
    print(f"account: {account}, password: {password}")
    return "OK", 200

@user.route('/api/user/update_user_info', methods=['POST'])
def user_update_info():
    data = request.form
    id = data.get('id')
    nickname = data.get('nickname')
    password = data.get('password')
    intro = data.get('introduction')
    user = User.query.filter_by(id=id).first()
    user.nickname = nickname
    user.password = password
    user.introduction = intro
    print(f'nickname: {nickname}, password: {password}, intro: {intro}')
    db.session().commit()
    return {"nickname": nickname, "password": password, "introduction": intro}, 200

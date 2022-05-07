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
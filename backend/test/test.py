from flask import Blueprint, request, jsonify
from .models import Test
from ..db import db


test = Blueprint('test', __name__)

@test.route('/test', methods=['GET', 'POST']) # 前端接受后端字符串数据
def test_url():
    return "test success!", 200

@test.route('/test2', methods=['GET', 'POST']) # 前端向后端数据库发送数据
def test_url2():
    new_id = request.args.get("id")
    print("new_id: {id}", new_id)
    new_test = Test()
    # new_test.id = str(new_id)
    db.session.add(new_test)
    db.session.commit()
    return "OK", 200

from flask import Blueprint, request, jsonify
from .models import Notice
from ..db import db


notice = Blueprint('notice', __name__)

@notice.route('/notice/test', methods=['GET', 'POST']) # 前端接受后端字符串数据
def test_url():
    return "notice success!", 200

@notice.route('/notice/test2', methods=['GET', 'POST']) # 前端向后端数据库发送数据
def test_url2():
    new_notice = Notice()
    new_notice.type = 0
    new_notice.text = "qweqwe"
    new_notice.user_id = str(123)
    db.session.add(new_notice)
    db.session.commit()
    return "OK", 200


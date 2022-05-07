from flask import Blueprint, request, jsonify
from .models import Like
from ..db import db


like = Blueprint('like', __name__)

@like.route('/like/test', methods=['GET', 'POST']) # 前端接受后端字符串数据
def test_url():
    return "like success!", 200

@like.route('/like/test2', methods=['GET', 'POST']) # 前端向后端数据库发送数据
def test_url2():
    new_like = Like()
    new_like.post_id = str(1233)
    new_like.user_id = str(4566)
    db.session.add(new_like)
    db.session.commit()
    return "OK", 200


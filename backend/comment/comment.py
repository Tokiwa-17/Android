from flask import Blueprint, request, jsonify
from .models import Comment
from ..db import db


comment = Blueprint('comment', __name__)

@comment.route('/comment/test', methods=['GET', 'POST']) # 前端接受后端字符串数据
def test_url():
    return "comment success!", 200

@comment.route('/comment/test2', methods=['GET', 'POST']) # 前端向后端数据库发送数据
def test_url2():
    new_comment = Comment()
    new_comment.post_id = str(123)
    new_comment.user_id = str(456)
    new_comment.text = "测试评论"
    db.session.add(new_comment)
    db.session.commit()
    return "OK", 200

@comment.route('/comment/test/post', methods=['POST']) # 测试post发送登录数据
def test_post():
    data = request.form
    print(f"data:{data}")
    account = data.get('account')
    password = data.get('password')
    print(f"account: {account}, password: {password}")
    return "OK", 200


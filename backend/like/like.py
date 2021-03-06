#coding=utf-8
from flask import Blueprint, request, jsonify
from .models import Like
from ..user.models import User
from ..db import db
import json


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

@like.route('/api/like/get_upvote', methods=['GET', 'POST'])
def get_upvote():
    id_list = request.args.get('post_id_list').strip()
    upvote_list = []
    try:
        id_list = id_list.split(' ')
        for id in id_list:
            like_item = Like.query.filter(Like.post_id == id)

            for user in like_item:
                user_name = User.query.filter(User.id == user.user_id).first().nickname
                upvote_list.append({"post_id": id, "user_name": user_name})
    except:
        pass
#    print(upvote_list)
    return {"upvote_list" : upvote_list}, 200


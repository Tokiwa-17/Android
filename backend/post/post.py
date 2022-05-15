from flask import Blueprint, request, jsonify
from .models import Post
from ..db import db

from datetime import datetime

post = Blueprint('post', __name__)

@post.route('/api/post_test', methods=['GET', 'POST']) # 前端接受后端字符串数据
def post_test():
    new_post = Post()
    new_post.user_id = '1'
    new_post.title = '1'
    new_post.text = '1'
    new_post.image_url = '1'
    new_post.video_url = '1'
    new_post.audio_url = '1'
    new_post.type = '1'
    new_post.time = datetime.now()
    new_post.like_num = 10
    db.session.add(new_post)
    db.session.commit()
    return "post_test success!", 200

@post.route('/api/post/get_mypost', methods=['GET', 'POST']) # 前端向后端数据库发送数据
def get_mypost():
    id = request.args.get('user_id')
    print(f'id: {id}')
    post_list = []
    post_query = Post.query.filter(Post.user_id == id)
    if post_query != None:
        for post in post_query:
            title = post.title
            text = post.text
            print(f'text: {text}')
            post_list.append({'title':title, 'text':text})
    return {'post_list': post_list}, 200



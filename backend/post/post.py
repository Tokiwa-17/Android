from flask import Blueprint, request, jsonify
from sqlalchemy import or_

from ..user.models import User
from .models import Post
from ..db import db

from datetime import datetime

post = Blueprint('post', __name__)


@post.route('/api/post_test', methods=['GET', 'POST'])  # 前端接受后端字符串数据
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


@post.route('/api/post/get_mypost', methods=['GET', 'POST'])  # 前端向后端数据库发送数据
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
            post_list.append({'title': title, 'text': text})
    return {'post_list': post_list}, 200


@post.route('/api/post/get_allpost', methods=['GET', 'POST'])  # 前端向后端数据库发送数据
def get_allpost():
    num = request.args.get('num')
    print(f'num: {num}')
    post_list = []
    post_query = Post.query.order_by(Post.time).all()
    if post_query != None:
        i = 0
        for post in post_query:
            title = post.title
            text = post.text
            user_id = post.user_id
            user = User.query.filter(User.id == user_id).first()
            name = user.nickname
            avatar_url = user.avatar
            print(f'text: {text}')
            post_list.append({'name': name, 'avatar_url': avatar_url, 'title': title, 'text': text})
    return {'all_post_list': post_list}, 200


@post.route('/api/post/get_query', methods=['GET', 'POST'])  # 前端向后端数据库发送数据
def get_query():
    query = request.args.get('query')
    print(f'query: {query}')
    post_list = []
    post_query = Post.query.filter(or_(Post.title.like("%{}%".format(query)), Post.text.like("%{}%".format(query))))
    if post_query != None:
        i = 0
        for post in post_query:
            title = post.title
            text = post.text
            user_id = post.user_id
            user = User.query.filter(User.id == user_id).first()
            name = user.nickname
            avatar_url = user.avatar
            print(f'text: {text}')
            post_list.append({'name': name, 'avatar_url': avatar_url, 'title': title, 'text': text})
    return {'result_list': post_list}, 200

@post.route('/api/post/get_post_list_id', methods=['GET', 'POST'])
def get_post_list_id():
    num = request.args.get('num')
    print(f'num: {num}')
    post_list = ""
    post_query = Post.query.order_by(Post.time).all()
    if post_query != None:
        i = 0
        for post in post_query:
            id = post.post_id
            # print(f'text: {text}')
            post_list = post_list + id + " "
    return {'all_post_id_list': post_list}, 200

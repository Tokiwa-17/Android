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
    type = request.args.get('type')
    print(f'query: {query}')
    print(f'type: {type}')
    if type == "动态名称":
        post_list = []
        post_query = Post.query.filter(Post.title.like("%{}%".format(query)))
        if post_query != None:
            i = 0
            for post in post_query:
                title = post.title
                text = post.text
                user_id = post.user_id
                user = User.query.filter(User.id == user_id).first()
                name = user.nickname
                avatar_url = user.avatar
                post_list.append({'name': name, 'avatar_url': avatar_url, 'title': title, 'text': text})
        return {'result_list': post_list}, 200

    elif type == "动态内容":
        post_list = []
        post_query = Post.query.filter(Post.text.like("%{}%".format(query)))
        if post_query != None:
            i = 0
            for post in post_query:
                title = post.title
                text = post.text
                user_id = post.user_id
                user = User.query.filter(User.id == user_id).first()
                name = user.nickname
                avatar_url = user.avatar
                post_list.append({'name': name, 'avatar_url': avatar_url, 'title': title, 'text': text})
        return {'result_list': post_list}, 200

    elif type == "用户名称":
        user_list = []
        user_query = User.query.filter(User.nickname.like("%{}%".format(query)))
        if user_query!=None:
            i = 0
            for user in user_query:
                user_list.append({'id': user.id, 'name': user.nickname, "url": user.avatar, 'introduction': user.introduction})

        return {'result_list': user_list}, 200

    elif type == "作品类型":
        post_list = []
        post_query = Post.query.filter(Post.text.like("%{}%".format(query)))
        if post_query != None:
            i = 0
            for post in post_query:
                title = post.title
                text = post.text
                user_id = post.user_id
                user = User.query.filter(User.id == user_id).first()
                name = user.nickname
                avatar_url = user.avatar
                post_list.append({'name': name, 'avatar_url': avatar_url, 'title': title, 'text': text})
        return {'result_list': post_list}, 200
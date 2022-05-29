from flask import Blueprint, request, jsonify
from sqlalchemy import or_

from ..block.models import Block
from ..follow.models import Follow
from ..user.models import User
from ..like.models import Like
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
            user_id = post.user_id
            user = User.query.filter(User.id == user_id).first()
            name = user.nickname
            avatar_url = user.avatar
            time = post.time.strftime('%Y-%m-%d %H:%M:%S')
            like = post.like_num
            post_list.append(
                {'postId': post.post_id, 'userId': user_id, 'name': name, 'avatar_url': avatar_url, 'title': title,
                 'text': text, 'like': like, 'time': time})
    return {'post_list': post_list}, 200


@post.route('/api/post/get_allpost', methods=['GET', 'POST'])  # 前端向后端数据库发送数据
def get_allpost():
    id = request.args.get('id')
    block_list = []
    block_query = Block.query.filter(Block.user_id == id)
    if block_query != None:
        for block in block_query:
            user_id = block.blocked_user_id
            block_list.append(user_id)

    num = request.args.get('num')
    print(f'num: {num}')
    post_list = []

    post_query = Post.query.order_by(Post.time).all()
    if post_query != None:
        i = 0
        for post in post_query:
            if post.user_id not in block_list:
                title = post.title
                text = post.text
                user_id = post.user_id
                user = User.query.filter(User.id == user_id).first()
                name = user.nickname
                avatar_url = user.avatar
                time = post.time.strftime('%Y-%m-%d %H:%M:%S')
                like = post.like_num
                print(f'text: {text}')
                post_list.append(
                    {'postId': post.post_id, 'userId': user_id, 'name': name, 'avatar_url': avatar_url, 'title': title,
                     'text': text, 'like': like, 'time': time})
    return {'all_post_list': post_list}, 200


@post.route('/api/post/get_watchpost', methods=['GET', 'POST'])  # 前端向后端数据库发送数据
def get_watchpost():
    id = request.args.get('id')
    followed_list = []
    follow_query = Follow.query.filter(Follow.followed_user_id == id)
    if follow_query != None:
        for follower in follow_query:
            user_id = follower.user_id
            followed_list.append(user_id)

    block_list = []
    block_query = Block.query.filter(Block.user_id == id)
    if block_query != None:
        for block in block_query:
            user_id = block.blocked_user_id
            block_list.append(user_id)

    post_list = []
    post_query = Post.query.order_by(Post.time).all()
    if post_query != None:
        i = 0
        for post in post_query:
            if post.user_id in followed_list and post.user_id not in block_list:
                title = post.title
                text = post.text
                user_id = post.user_id
                user = User.query.filter(User.id == user_id).first()
                name = user.nickname
                avatar_url = user.avatar
                time = post.time.strftime('%Y-%m-%d %H:%M:%S')
                like = post.like_num
                print(f'text: {text}')
                post_list.append(
                    {'postId': post.post_id, 'userId': user_id, 'name': name, 'avatar_url': avatar_url, 'title': title,
                     'text': text, 'like': like, 'time': time})
        return {'watch_post_list': post_list}, 200

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
        if user_query != None:
            i = 0
            for user in user_query:
                user_list.append(
                    {'id': user.id, 'name': user.nickname, "url": user.avatar, 'introduction': user.introduction})

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

@post.route('/api/post/get_post_list_id', methods=['GET', 'POST'])
def get_post_list_id():
    num = request.args.get('num')
    print(f'num: {num}')
    post_list = ""
    post_query = Post.query.order_by(Post.time).all()
    upvote_list = []
    if post_query != None:
        i = 0
        for post in post_query:
            id = post.post_id
            post_list = post_list + id + " "
    try:
        id_list = post_list.strip()
        id_list = id_list.split(' ')
        for id in id_list:
            like_item = Like.query.filter(Like.post_id == id)

            for user in like_item:
                user_name = User.query.filter(User.id == user.user_id).first().nickname
                upvote_list.append({"post_id": id, "user_name": user_name})
    except:
        pass
    print(upvote_list)
    return {"upvote_list": upvote_list}, 200

from flask import Blueprint, request, jsonify
from .models import Follow

from ..user.models import User
from ..db import db

from datetime import datetime

follow = Blueprint('follow', __name__)

@follow.route('/api/follow_test', methods=['GET', 'POST']) # 前端接受后端字符串数据
def follow_test():
    new_follow = Follow()
    new_follow.user_id = '1'
    new_follow.followed_user_id = '2'
    db.session.add(new_follow)
    db.session.commit()
    return "follow test success!", 200

@follow.route('/api/follow/get_follow_list', methods=['GET'])
def get_follow_list():
    id = request.args.get('user_id')
    print(f'id: {id}')
    follow_list = []
    follow_query = Follow.query.filter(Follow.user_id == id)
    if follow_query != None:
        for follower in follow_query:
            user_id = follower.followed_user_id
            print(f'user_id: {user_id}')
            user_query = User.query.filter(User.id == user_id)
            for user in user_query:
                follow_list.append({'nickname':user.nickname, "avatar":user.avatar, 'introduction':user.introduction})
    return {'follow_list': follow_list}, 200

@follow.route('/api/follow/get_followed_list', methods=['GET'])
def get_followed_list():
    id = request.args.get('user_id')
    print(f'id: {id}')
    followed_list = []
    follow_query = Follow.query.filter(Follow.followed_user_id == id)
    if follow_query != None:
        for follower in follow_query:
            user_id = follower.user_id
            print(f'user_id: {user_id}')
            user_query = User.query.filter(User.id == user_id)
            for user in user_query:
                followed_list.append({'nickname':user.nickname, "avatar":user.avatar, 'introduction':user.introduction})
    return {'followed_list': followed_list}, 200

@follow.route('/api/follow/get_watchlist',  methods=['GET'])
def get_watch_list():
    id = request.args.get('user_id')
    print(f'id: {id}')
    followed_list = []
    follow_query = Follow.query.filter(Follow.followed_user_id == id)
    if follow_query != None:
        for follower in follow_query:
            user_id = follower.user_id
            print(f'user_id: {user_id}')
            user_query = User.query.filter(User.id == user_id)
            for user in user_query:
                followed_list.append({'id': user.id, 'name':user.nickname, "url":user.avatar, 'introduction':user.introduction})
    return {'watchlist': followed_list}, 200

@follow.route('/api/follow/get_fanlist',  methods=['GET'])
def get_fan_list():
    id = request.args.get('user_id')
    print(f'id: {id}')
    followed_list = []
    follow_query = Follow.query.filter(Follow.user_id == id)
    if follow_query != None:
        for follower in follow_query:
            user_id = follower.followed_user_id
            print(f'user_id: {user_id}')
            user_query = User.query.filter(User.id == user_id)
            for user in user_query:
                followed_list.append({'id': user.id, 'name':user.nickname, "url":user.avatar, 'introduction':user.introduction})
    return {'fanlist': followed_list}, 200

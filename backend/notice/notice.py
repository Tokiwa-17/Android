#coding=utf-8
from flask import Blueprint, request, jsonify
from .models import Notice
from ..db import db

notice = Blueprint('notice', __name__)


@notice.route('/notice/test', methods=['GET', 'POST'])  # 前端接受后端字符串数据
def test_url():
    return "notice success!", 200


@notice.route('/notice/test2', methods=['GET', 'POST'])  # 前端向后端数据库发送数据
def test_url2():
    new_notice = Notice()
    new_notice.type = 0
    new_notice.text = "qweqwe"
    new_notice.user_id = str(123)
    db.session.add(new_notice)
    db.session.commit()
    return "OK", 200


@notice.route('/api/notice/get_notice_list', methods=['GET', 'POST'])  # 前端向后端数据库发送数据
def get_notice_list():
    id = request.args.get('user_id')
#    print(f'id: {id}')
    notice_list = []
    notice_query = Notice.query.filter(Notice.user_id == id)
    if notice_query != None:
        for notice in notice_query:
            if notice.type == 0:
                type = "新点赞"
            elif notice.type == 1:
                type = "新评论"
            else:
                type = "关注作者动态"

            text = notice.text
            read = notice.read
            postId = notice.post_id
            notice_list.append({'type': type, 'text': text, 'postId': postId, 'read': read})
            if not read:
                notice.read = 1
                db.session.commit()
    return {'notice_list': notice_list}, 200

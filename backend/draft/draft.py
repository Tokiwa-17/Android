#coding=utf-8
from flask import Blueprint, request, jsonify

from ..user.models import User
from .models import Draft
from ..db import db

from datetime import datetime

draft = Blueprint('draft', __name__)

@draft.route('/api/draft_test', methods=['GET', 'POST']) # 前端接受后端字符串数据
def draft_test():
    new_draft = Draft()
    new_draft.user_id = '1'
    new_draft.title = 'tt'
    new_draft.text = 'tt'
    new_draft.image_url = 'tt'
    new_draft.video_url = 't'
    new_draft.audio_url = 't'
    db.session.add(new_draft)
    db.session.commit()
    return "post_test success!", 200

@draft.route('/api/draft/get_draft', methods=['GET', 'POST']) # 前端向后端数据库发送数据
def get_draft():
    id = request.args.get('user_id')
#    print(f'id: {id}')
    draft_list = []
    draft_query = Draft.query.filter(Draft.user_id == id)
    if draft_query != None:
        for draft in draft_query:
            title = draft.title
            text = draft.text
            user_id = draft.user_id
            user = User.query.filter(User.id == user_id).first()
            name = user.nickname
            time = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
            avatar_url = user.avatar
#            print(f'text: {text}')
            draft_list.append(
                {'postId': draft.draft_id, 'userId': user_id, 'name': name, 'avatar_url': avatar_url, 'title': title,
                 'text': text, 'like': 0, 'time': time})
    return {'draft_list': draft_list}, 200



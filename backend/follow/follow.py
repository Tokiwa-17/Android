from flask import Blueprint, request, jsonify
from .models import Follow
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



from flask import Blueprint, request, jsonify
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



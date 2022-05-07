import uuid

from ..db import db

class Post(db.Model):
    __tablename__ = 'post'

    post_id = db.Column(db.String(32), primary_key=True)
    user_id = db.Column(db.String(32))
    title = db.Column(db.String(32))
    text = db.Column(db.String(128));
    image_url = db.Column(db.String(128))
    video_url = db.Column(db.String(128))
    audio_url = db.Column(db.String(128))
    type = db.Column(db.String(8))
    time = db.Column(db.DateTime)
    like_num = db.Column(db.Integer, default=0)

    def __repr__(self):
        return '<Post %r>' % self.post_id

    def __init__(self):
        self.post_id = str(uuid.uuid4()).replace("-", "")


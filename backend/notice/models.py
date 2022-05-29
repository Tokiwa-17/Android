import uuid

from ..db import db

class Notice(db.Model):
    __tablename__ = 'notice'

    notice_id = db.Column(db.String(32), primary_key=True)
    type = db.Column(db.Integer)
    user_id = db.Column(db.String(32))
    text = db.Column(db.String(128))
    post_id = db.Column(db.String(32))
    read = db.Column(db.Boolean)

    def __repr__(self):
        return '<notice %r>' % self.notice_id

    def __init__(self):
        self.notice_id = str(uuid.uuid4()).replace("-", "")


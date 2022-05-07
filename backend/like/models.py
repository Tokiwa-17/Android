import uuid

from ..db import db

class Comment(db.Model):
    __tablename__ = 'comment'

    comment_id = db.Column(db.String(32), primary_key=True)
    post_id = db.Column(db.String(32))
    user_id = db.Column(db.String(32))
    text = db.Column(db.String(128))

    def __repr__(self):
        return '<comment %r>' % self.id

    def __init__(self):
        self.comment_id = str(uuid.uuid4()).replace("-", "")


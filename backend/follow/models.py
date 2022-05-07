import uuid

from ..db import db

class Follow(db.Model):
    __tablename__ = 'follow'

    followed_user_id = db.Column(db.String(32), primary_key=True)
    user_id = db.Column(db.String(32), primary_key=True)

    def __repr__(self):
        return '<Follow %r>' % self.user_id

    def __init__(self):
        pass
        # self.post_id = str(uuid.uuid4()).replace("-", "")


import uuid

from ..db import db

class Like(db.Model):
    __tablename__ = 'like'

    post_id = db.Column(db.String(32), primary_key=True)
    user_id = db.Column(db.String(32), primary_key=True)

    def __repr__(self):
        return '<like %r>' % self.post_id


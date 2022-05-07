import uuid

from ..db import db

class Block(db.Model):
    __tablename__ = 'block'

    blocked_user_id = db.Column(db.String(32), primary_key=True)
    user_id = db.Column(db.String(32), primary_key=True)

    def __repr__(self):
        return '<Block %r>' % self.blocked_user_id



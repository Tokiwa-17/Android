import uuid

from ..db import db

class User(db.Model):
    __tablename__ = 'user'

    id = db.Column(db.String(32), primary_key=True)
    account = db.Column(db.String(50))
    password = db.Column(db.String(50))
    avatar = db.Column(db.String(128))
    nickname = db.Column(db.String(32))
    introduction = db.Column(db.String(128))

    def __repr__(self):
        return '<User %r>' % self.nickname

    def __init__(self):
        self.id = str(uuid.uuid4()).replace("-", "")


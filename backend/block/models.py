import uuid

from ..db import db

class Test(db.Model):
    __tablename__ = 'test'

    id = db.Column(db.String(32), primary_key=True)

    def __repr__(self):
        return '<Test %r>' % self.id

    def __init__(self):
        self.id = str(uuid.uuid4()).replace("-", "")


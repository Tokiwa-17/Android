import uuid

from ..db import db

class Draft(db.Model):

    __tablename__ = 'draft'

    draft_id = db.Column(db.String(32), primary_key=True)
    user_id = db.Column(db.String(32))
    title = db.Column(db.String(50))
    text = db.Column(db.String(128))
    image_url = db.Column(db.String(128))
    video_url = db.Column(db.String(128))
    audio_url = db.Column(db.String(128))

    def __repr__(self):
        return '<Draft %r>' % self.draft_id

    def __init__(self):
        self.draft_id = str(uuid.uuid4()).replace("-", "")


from flask import Flask, url_for
from . import test, user, post, draft, follow, db, comment, block, notice, like


def create_app(config_name='default'):
    app = Flask(__name__)
    app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:123456@43.138.70.51:3306/android'
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True

    db.init_app(app)
    test.init_app(app)
    block.init_app(app)
    comment.init_app(app)
    notice.init_app(app)
    like.init_app(app)
    user.init_app(app)
    post.init_app(app)
    draft.init_app(app)
    follow.init_app(app)


    return app

from flask import Flask, url_for
from . import test, user, post, db


def create_app(config_name='default'):
    app = Flask(__name__)
    app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:123456@43.138.70.51:3306/android'
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True

    db.init_app(app)
    test.init_app(app)
    user.init_app(app)
    post.init_app(app)

    return app

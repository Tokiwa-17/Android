from .post import post

def init_app(app):
    app.register_blueprint(post)
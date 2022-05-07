from .comment import comment

def init_app(app):
    app.register_blueprint(comment)
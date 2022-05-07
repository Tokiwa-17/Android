from .notice import notice

def init_app(app):
    app.register_blueprint(notice)
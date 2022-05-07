from .like import like

def init_app(app):
    app.register_blueprint(like)
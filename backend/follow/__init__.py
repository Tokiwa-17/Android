from .follow import follow

def init_app(app):
    app.register_blueprint(follow)
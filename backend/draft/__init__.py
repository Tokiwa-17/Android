from .draft import draft

def init_app(app):
    app.register_blueprint(draft)
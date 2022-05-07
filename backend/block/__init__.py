from .block import block

def init_app(app):
    app.register_blueprint(block)